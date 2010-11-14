package com.games.racertc.messages;

import java.util.Vector;


/* Klasa produkujaca wiadomosci przesylane miedzy watkami gry. W szczegolnosci
 * sa to wiadomosci informujace o sterowaniu dokonywanym przez gracza. */
public class MessageFactory {

/*--------------------------------------------*/
/*-                  Stale:                  -*/
/*--------------------------------------------*/		
	
/*--------------------------------------------*/
/*-       Bufor wiadomosci nieuzytych:       -*/
/*--------------------------------------------*/	

	private final Vector<Message> messageBuffer;
	
	private int mAvailable = 0;
	
/*--------------------------------------------*/
/*-   Implementacja tworzenia wiadomosci:    -*/
/*--------------------------------------------*/		
	
	/**
	 * Tworzy wiadomosc sterujaca ruchem pojazdu gracza w przod.
	 * @param owner 
	 * @param flags Znaczniki definiujace podtyp wiadomisci. W zaleznosci od znacznikow
	 * okreslana jest koniecznosc zakonczenia lub rozpoczecia ruchu.
	 * @param xAxisUsage 
	 * @param yAxisUsage
	 * @return Zamowiona wiadomosci.
	 */
	public Message createMovementMessage( int owner, int flags, float xAxisUsage, float yAxisUsage ) {
		
		if( mAvailable == 0 ) {
			return new Message( owner, flags, xAxisUsage, yAxisUsage );
		} else {
			mAvailable--;
			Message m = messageBuffer.get( mAvailable );
			m.setFlags( owner, flags, xAxisUsage, yAxisUsage );
			return m;
		}
	}
	
/*--------------------------------------------*/
/*-    Implementacja usuwania wiadomosci:    -*/
/*--------------------------------------------*/		
	
	public void disposeMessage( Message m ) {
		//recykling m:
		messageBuffer.add( m );
		mAvailable++;
	}
	
/*--------------------------------------------*/
/*- Implementacja singletonu MessageFactory: -*/
/*--------------------------------------------*/		
	
	private MessageFactory() {
		 messageBuffer = new Vector<Message>( 5 );
	}
	
	/* Klasa jest singletonem i to jest jej jedyna dozwolona instancja. */
	private static MessageFactory instance;
	
	/**
	 * Pobiera instancje MessageFactory.
	 * @return Instancja MessageFactory
	 */
	public static MessageFactory getInstance() {
		if( instance == null )
			instance = new MessageFactory();
		return instance;
	}
	
	
}
