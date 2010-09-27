package com.games.racertc.gamestate;

import java.util.Vector;

/**
 * Maszyna stanow gry. Pozwala zazadac zmiany stanu gry. Po nastapieniu zadania
 * StateMachine informuje wszystkie zarejestrowane klasy o wystapieniu zmiany
 * stanu gry.
 */
public class StateMachine {

/*-----------------------------------------*/
/*-     Implementacja maszyny stanow:     -*/
/*-----------------------------------------*/		
	
	/** Przechowuje aktualny stan gry. */
	private int state;
	
	/** Obserwatorzy zmiany stanu. */
	private Vector< GameStateChangeListener > listeners;
	
	/**
	 * Ustawia stan gry i informuje o tym obserwatorow.
	 * @param gameState nowy stan gry
	 */
	public void setGameState( int gameState ) {
		state = gameState;
		for( int i = 0; i != listeners.size(); i++ ) {
			listeners.get( i ).onGameStateChange( gameState );
		}
	}
	
	/** Pozwala sprawdzic aktualny stan gry. Jednak na ogol klasy, ktore
	 * sa zainteresowane zmianami stanu gry powinny zarejestrowac sie
	 * jako nasluchujace tych zmian. */
	public int getGameState() {
		return state;
	}
	
	/**
	 * Rejestruje obserwatora zmian stanu.
	 * @param l obserwator zmian stanu.
	 */
	public void addListener( GameStateChangeListener l ) {
		listeners.add( l );
	}
	
	/**
	 * Usuwa z listy obserwatorow zadanego obserwatora.
	 * @param l obserwator do usuniecia z listy.
	 */
	public void removeListener( GameStateChangeListener l ) {
		listeners.remove( l );
	}
	
/*-----------------------------------------*/
/*-       Implementacja singletonu:       -*/
/*-----------------------------------------*/	
	
	private StateMachine() {
		listeners = new Vector< GameStateChangeListener >();
	};
	
	private static StateMachine instance;

	/**
	 * Pozwala na dostep do jedynej dozwolonej instancji StateMachine.
	 * @return instancja StateMachine
	 */
	public static StateMachine getInstance() {
		if( instance == null )
			instance = new StateMachine();
		return instance;
	}
	
}
