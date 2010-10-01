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
		//Najpierw sprawdz, jakie jest sterowanie samochodu
		int bhv = c.getBehaviourFlags();
		
		float acceleration = 0f;
		
		if( (bhv & Message.FLAG_UP) != 0 ) {
			if( c.speed < c.maxSpeed * c.requestedSpeed )
				acceleration += c.acceleration;
			else {
				int j = 50;
			}
		}
			
		if( (bhv & Message.FLAG_DOWN) != 0 ) {
			//TODO: rozroznienie hamulce/wsteczny
			if( c.speed > -( c.maxSpeed * c.requestedSpeed ) )
				acceleration -= c.acceleration;
		}

		if( (bhv & (Message.FLAG_RIGHT | Message.FLAG_LEFT) ) == 0 ) {
			c.getDirection().set( c.getVelocity() );
		} else {
		
			if( c.speed != 0f && (bhv & Message.FLAG_RIGHT) != 0 ) {
				if( c.currentTurningAngle < 0f ) c.currentTurningAngle = 0f;
				if( c.currentTurningAngle < c.maxTurningAngle * c.requestedTurningAngle )
					c.currentTurningAngle += c.turningAngleStep;
				else
					c.currentTurningAngle -= c.turningAngleStep;
				c.getDirection().set( c.getVelocity() );
				c.getVelocity().rotate( c.currentTurningAngle );
			}
			
			if( c.speed != 0f && (bhv & Message.FLAG_LEFT) != 0 ) {
				if( c.currentTurningAngle > 0f ) c.currentTurningAngle = 0f;
				if( c.currentTurningAngle > -(c.maxTurningAngle * c.requestedTurningAngle) )
					c.currentTurningAngle -= c.turningAngleStep;
				else
					c.currentTurningAngle += c.turningAngleStep;
				c.getDirection().set( c.getVelocity() );
				c.getVelocity().rotate( c.currentTurningAngle );
			}
		
		}
		
		//tarcie powierzchni:
		if( c.speed > 0 )
			acceleration -= track.getDecceleration( c.getPosition() );
		else if( c.speed < 0 )
			acceleration += track.getDecceleration( c.getPosition() );
		
		//dokonuje przyspieszen:
		float speed_sign = Math.signum( c.speed );
		c.speed += acceleration * (float) dt;
		if( speed_sign == -Math.signum(c.speed) )
			c.speed = 0;
		//obsoletowe:
		//if( c.speed > c.maxSpeed )
		//	c.speed = c.maxSpeed;
		//if( c.speed < -c.maxSpeed )
		//	c.speed = -c.maxSpeed;
		
		//gdy mamy przyczepnosc:
		
		//zmienia pozycje samochodu:
		c.getPosition().add(
				c.getVelocity().getX() * c.speed,
				c.getVelocity().getY() * c.speed
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
