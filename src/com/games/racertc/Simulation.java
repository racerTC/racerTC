package com.games.racertc;

import java.util.Iterator;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;
import com.games.racertc.messages.MessageFactory;
import com.games.racertc.objects.Car;
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
	
	public void simulate( long dt ) {
		//krok I - odbiera i interpretuje zdarzenia wejscia
		receiveInputMessages();
		
		//krok II - symuluje swiat
		Iterator< Car > iter = track.getCarIterator();
		
		while( iter.hasNext() ) {
			Car c = iter.next();
			int bhv = c.getBehaviourFlags();
			
			if( (bhv & Message.FLAG_UP) != 0 )
				c.getPosition().add(0f, -0.5f);
			
			if( (bhv & Message.FLAG_DOWN) != 0 )
				c.getPosition().add(0f, 0.5f);
			
			if( (bhv & Message.FLAG_LEFT) != 0 )
				c.getPosition().add(-0.5f, 0f);
			
			if( (bhv & Message.FLAG_RIGHT) != 0 )
				c.getPosition().add(0.5f, 0f);	
			
		}
		
	}

}
