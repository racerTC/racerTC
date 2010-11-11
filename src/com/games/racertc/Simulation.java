package com.games.racertc;

import java.util.Iterator;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;
import com.games.racertc.messages.MessageFactory;
import com.games.racertc.objects.Car;
import com.games.racertc.other.Vec2D;
import com.games.racertc.tracks.Track;

/**
 * Klasa odpowiadajaca za symulacje w grze.
 */
public class Simulation {

	/** Aktualnie aktywna trasa. */
	private Track track;
	
	/** Przechowuje referencje do MessageQueue. */ 
	private MessageQueue messageQueue;
	
	/** Przechowuje referencje do MessageFactory. */
	private MessageFactory messageFactory;	
	
	public Simulation() {
		//buforuje sobie MessageQueue i messageFactory
		messageQueue = MessageQueue.getInstance();
		messageFactory = MessageFactory.getInstance();
	}
	
	/**
	 * Przygotowuje Simulation do pracy. Powinno byc wolane kazdorazowo przed rozpoczeciem
	 * nowej rozgrywki.
	 * @param track Trasa na ktorej odbywac sie bedzie rozgrywka.
	 */
	public void initialise( Track track ) {
		this.track = track;
	}	
	
	/**
	 * Odbiera wiadomosci sterowania pojazdami
	 */
	private void receiveInputMessages() {
		Message m;
		while( (m = messageQueue.pop()) != null ) {
			//interpretuje wiadomosc i steruje odpowiednio samochodem gracza/kogos innego
			Car c = track.getCar( m.getOwner() );
			c.updateBehaviour( m );
			messageFactory.disposeMessage( m );
		}
	}
	
	private void simulate( Car c, long dt ) {
		/* Sterowanie samochodu */
		int bhv = c.getBehaviourFlags();
		
		/* Wartosc przyspieszenia dzialajacego na samochod: */
		float acceleration = 0f;
		
		/* Od tej pory obliczamy parametry fizyczne niezalezne od czasu: */
		
		float friction_force = track.getFrictionForce( c, c.getPosition() );
		
		if( (bhv & ( Message.FLAG_UP | bhv & Message.FLAG_DOWN )) != 0 ) {
			
			if( (bhv & Message.FLAG_UP) != 0 ) {
				
				if( c.velocityMagnitude >= 0f ) {
					//jedziemy do przodu i przyspieszamy
					float driving_force = (c.requestedDrivingForce * c.maxDrivingForce);
					float force = driving_force - friction_force;			
					if( force > 0f )
						acceleration = force / c.mass;
				} else {
					//jedziemy do tylu i hamujemy
					float braking_force = (c.requestedDrivingForce * c.maxBrakingForce);
					float force = braking_force + friction_force;
					acceleration = force / c.mass;					
				}
			}
				
			if( (bhv & Message.FLAG_DOWN) != 0 ) {
				
				if( c.velocityMagnitude <= 0f ) {
					//jedziemy do tylu i przyspieszamy
					float driving_force = (c.requestedDrivingForce * c.maxDrivingForce);
					float force = - driving_force + friction_force;
					//teraz dziala jak wsteczny:
					if( force < 0f )
						acceleration = force / c.mass;
				} else {
					//jedziemy do przodu i hamujemy
					float braking_force = (c.requestedDrivingForce * c.maxBrakingForce);
					float force = - braking_force + friction_force;
					acceleration = force / c.mass;	
				}
			}
		} else { /* Koniec obliczania przyspieszenia w ruchu w przod/tyl; teraz ssprawdzamy opor gdy nie ma przyspieszen: */
			if( c.velocityMagnitude > 0f )
				acceleration = -friction_force / c.mass;
			else if( c.velocityMagnitude < 0f )
				acceleration = friction_force / c.mass;
		}
		
		/* Uwzgledniamy obliczone parametry w kontekscie zmiany czasu od ostatniej klatki: */
		
		/* Czas, ktory uplynal od poprzedniego kroku symulacji w sekundach. */
		float dtf = 0.001f * (float) dt;

		if( (bhv & (Message.FLAG_RIGHT | Message.FLAG_LEFT) ) == 0 ) {
			//nie ma skrecania - po prostu zwiekszamy predkosc:
			//TODO: jakis odpalacz poslizgu bazowany na acc
		} else {
			Vec2D vn = null;
			
			if(
					( c.velocityMagnitude > 0f && (bhv & Message.FLAG_RIGHT) != 0 ) ||
					( c.velocityMagnitude < 0f && (bhv & Message.FLAG_LEFT) != 0 )
			) {
				vn = c.velocity.instantiateRotatedVector( c.requestedTurningAngle * c.maxTurningAngle * dtf );
			} else if(
					( c.velocityMagnitude > 0f && (bhv & Message.FLAG_LEFT) != 0 ) ||
					( c.velocityMagnitude < 0f && (bhv & Message.FLAG_RIGHT) != 0 )
			) {
				vn = c.velocity.instantiateRotatedVector( - c.requestedTurningAngle * c.maxTurningAngle * dtf );
			} 
			
			//TODO: jakis odpalacz poslizgu bazowany na acc?
			
			if( vn != null ) {
				c.velocity = vn;
			}			
		} /* Koniec obslugi skrecania */

		//gdy mamy przyczepnosc:		
		
		float sign = Math.signum( c.velocityMagnitude );
		c.velocityMagnitude += acceleration * dtf * dtf * 0.5f;
		float sign2 = Math.signum( c.velocityMagnitude );
		//zabezpieczenie przed sytuacja, kiedy hamowanie powoduje natychmiastowe
		//rozpoczecie jazdy na wstecznym - lub odwrotnie:
		if( (sign != sign2) && (sign != 0f) ) {
			//nastapila zmiana przyspieszenie <-> hamulec
			c.velocityMagnitude = 0f;
		}
		
		if( c.velocityMagnitude > c.maxVelocity )
			c.velocityMagnitude = c.maxVelocity;
		else if( c.velocityMagnitude < c.maxReversedVelocity )
			c.velocityMagnitude = c.maxReversedVelocity;
		
		//zmienia pozycje samochodu:
		c.getPosition().add(
				c.velocity.getX() * c.velocityMagnitude,
				c.velocity.getY() * c.velocityMagnitude
		);
		
		//gdy nie mamy przyczepnosci:
		
		//sprawdza, czy spelnione sa warunki wejscia w poslizg poslizg:
		//oblicza sile dzialajaca na bok samochodu:
		
	}
	
	public void simulate( long dt ) {
		//krok I - odbiera i interpretuje zdarzenia wejscia
		receiveInputMessages();
		
		//krok II - symuluje swiat
		Iterator< Car > iter = track.getCarIterator();
		
		while( iter.hasNext() ) {
			Car c = iter.next();
			
			simulate( c, dt );
			

			
		}
		
	}

}
