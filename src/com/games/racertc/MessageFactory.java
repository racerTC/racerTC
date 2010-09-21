package com.games.racertc;

import com.games.racertc.messages.Message;

/* Klasa produkujaca wiadomosci przesylane miedzy watkami gry. W szczegolnosci
 * sa to wiadomosci informujace o sterowaniu dokonywanym przez gracza. */
public class MessageFactory {

/*--------------------------------------------*/
/*-                  Stale:                  -*/
/*--------------------------------------------*/		
	
/** Flaga oznaczajaca poczatek ruchu. */
public final static int BEGIN = 0;

/** Flaga oznaczajaca zakonczenie ruchu. */
public final static int END = 1;

/*--------------------------------------------*/
/*-   Implementacja tworzenia wiadomosci:    -*/
/*--------------------------------------------*/		
	
	/**
	 * Tworzy wiadomosc sterujaca ruchem pojazdu gracza w przod.
	 * @param flag flaga definiujaca podtyp wiadomisci. W zaleznosci od flagi
	 * okresla koniecznosc zakocnzenia lub rozpoczecia ruchu.
	 * @return Zamowiona wiadomosci.
	 */
	public Message createMessageForward( int flag ) {
		if( flag == BEGIN )
			return null; //zwraca "przyspieszaj do przodu"
		else return null; //zwraca "przestan przyspieszac do przodu"
	}
	
	//itd
	
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
