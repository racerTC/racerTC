package com.games.racertc.commands;

import com.games.racertc.utility.FastList;

/**
 * Synchronizowana kolejka FIFO sluzaca do wymiany komend miedzy watkami.
 * @author Piotr Balut
 */
public final class CommandQueue {

/*-----------------------------------------*/
/*-       Implementacja singletonu:       -*/
/*-----------------------------------------*/		

	private final static CommandQueue instance = new CommandQueue();
	
	private CommandQueue() {
		commandList = new FastList<Command>();
	}
	
	public static CommandQueue GetInstance() {
		return instance;
	}
	
/*-----------------------------------------*/
/*-        Implementacja kolejki:         -*/
/*-----------------------------------------*/
	
	FastList<Command> commandList;
	
/**
 * Wstawia wiadomosc na koniec kolejki.
 * @param m wiadomosc wstawiana do kolejki
 */
public synchronized void push( Command c ) {
	commandList.pushFront( c );
}

/**
 * Zwraca wiadomosc znajdujaca sie na poczatku kolejki. Wiadmosc zostaje
 * usunieta z kolejki (kolejka jest kolejka typu FIFO).
 * @return wiadomosc znajdujaca sie na pcozatku kolejki. Jezeli kolejka jest pusta, zwraca null.
 */
public synchronized Command pop() {
	if( commandList.isEmpty() ) {
		return null;
	} else {
		Command c = commandList.popFront();
		return c;
	}
}
	
}
