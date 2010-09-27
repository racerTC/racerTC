package com.games.racertc;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;
import com.games.racertc.tracks.Track;

/**
 * Klasa odpowiadajaca za symulacje w grze.
 */
public class Simulation {

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
	 * Odbiera wiadomosci sterowania pojazdami
	 */
	private void receiveInputMessages() {
		Message m;
		while( (m = messageQueue.pop()) != null ) {
			//interpretuje wiadomosc i steruje odpowiednio samochodem gracza/kogos innego
		}
	}
	
	public void simulate( long dt ) {
		//krok I - odbiera i interpretuje zdarzenia wejscia
		receiveInputMessages();
		
		//krok II - symuluje swiat
		
		
	}

	public void initialise(Track t) {
		// TODO Auto-generated method stub
		
	}
	
}
