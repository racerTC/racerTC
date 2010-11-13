package com.games.racertc.messages;


/* Klasa produkujaca wiadomosci przesylane miedzy watkami gry. W szczegolnosci
 * sa to wiadomosci informujace o sterowaniu dokonywanym przez gracza. */
public class MessageFactory {

/*--------------------------------------------*/
/*-                  Stale:                  -*/
/*--------------------------------------------*/		
	


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
		return new Message( owner, flags, xAxisUsage, yAxisUsage );
	}
	
	//itd
	
/*--------------------------------------------*/
/*-    Implementacja usuwania wiadomosci:    -*/
/*--------------------------------------------*/		
	
	public void disposeMessage( Message m ) {
		//recykling m
	}
	
/*--------------------------------------------*/
/*- Implementacja singletonu MessageFactory: -*/
/*--------------------------------------------*/		
	
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
