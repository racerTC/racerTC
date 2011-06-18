package com.games.racertc.messages;

import java.util.LinkedList;
import java.util.List;


/**
 * Synchronizowana kolejka FIFO sluzaca wymianie wiadomosci miedzy watkami.
 * 
 * @author Piotr Balut
 */
public final class MessageQueue {

	public int size() {
		return messageQueue.size();
	}
	
/*-----------------------------------------*/
/*-       Implementacja singletonu:       -*/
/*-----------------------------------------*/		
	
	/** Prywatny konstruktor. */
	private MessageQueue() {
		messageQueue = new LinkedList< Message >();
	}
	
	/** MessageQueue jest singletonem, ktorego jedyna dozwolona instancje okresla
	 * ta zmienna. */
	private final static MessageQueue instance = new MessageQueue();
	
	/**
	 * Pozwala na dostep do jedynej dozwolonej instancji MessageQueue.
	 * @return instancja MessageQueue
	 */
	public static MessageQueue getInstance() {
		return instance;
	}

/*-----------------------------------------*/
/*-        Implementacja kolejki:         -*/
/*-----------------------------------------*/	
	
	/** Kolejka wiadomosci. */
	private List< Message > messageQueue;
	
	/**
	 * Wstawia wiadomosc na koniec kolejki.
	 * @param m wiadomosc wstawiana do kolejki
	 */
	public synchronized void push( Message m ) {
		messageQueue.add( m );
	}
	
	/**
	 * Zwraca wiadomosc znajdujaca sie na poczatku kolejki. Wiadmosc zostaje
	 * usunieta z kolejki (kolejka jest kolejka typu FIFO).
	 * @return wiadomosc znajdujaca sie na pcozatku kolejki. Jezeli kolejka jest pusta, zwraca null.
	 */
	public synchronized Message pop() {
		if( messageQueue.isEmpty() ) {
			return null;
		} else {
			Message m = messageQueue.get( 0 );
			messageQueue.remove( 0 );
			return m;
		}
	}
	
}
