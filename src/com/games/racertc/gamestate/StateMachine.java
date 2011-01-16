package com.games.racertc.gamestate;

import java.util.Stack;
import java.util.Vector;

import com.games.racertc.gameplay.Simulation;
import com.games.racertc.gameplay.graphics.Presentation;

/**
 * Maszyna stanow gry. Pozwala zazadac zmiany stanu gry. Po nastapieniu zadania
 * StateMachine informuje wszystkie zarejestrowane klasy o wystapieniu zmiany
 * stanu gry.
 * 
 * Stan gry jest definiowany poprzez pare symulacji oraz prezentacji, jakie w danym
 * momencie sa aktywne i uzywane w glownej petli gry.
 * @author Piotr Balut
 */
public class StateMachine {
	
/*-----------------------------------------*/
/*-     Implementacja maszyny stanow:     -*/
/*-----------------------------------------*/		
	
	/** Przechowuje stos stanow gry. */
	private Stack<GameState> stateStack = new Stack<GameState>();
	
	/** Obserwatorzy zmiany stanu. */
	private Vector< GameStateChangeListener > listeners;
	
	/**
	 * Ustawia stan gry i informuje o tym obserwatorow. Obserwatorzy informowani sa po przejsciu maszyny
	 * do nowego stanu.
	 * @param gameState nowy stan gry
	 */
	public void enterGameState( GameState gameState ) {
		gameState.injectGlobalContext( sgc );
		gameState.onStateMachineInsertion();
		if( stateStack.size() != 0 ) {
			stateStack.peek().leave();
		}
		stateStack.push( gameState );
		gameState.enter();
		for( int i = 0; i != listeners.size(); i++ ) {
			listeners.get( i ).onGameStateChange( gameState );
		}
	}
	
	/**
	 * Opuszcza aktualny stan gry i przechodzi do poprzedniego. Obserwatorzy informowanu sa o przejsciu
	 * po przejsciu maszyny do poprzedniego stanu.
	 */
	public void leaveGameState() {
		GameState old_state = stateStack.pop();
		old_state.leave();
		GameState new_state = stateStack.peek();
		if( new_state != null )
			new_state.enter();
		for( int i = 0; i != listeners.size(); i++ ) {
			listeners.get( i ).onGameStateChange( new_state );
		old_state.onStateMachineExtraction();
		}
	}
	
	/** Pozwala sprawdzic aktualny stan gry. Jednak na ogol klasy, ktore
	 * sa zainteresowane zmianami stanu gry powinny zarejestrowac sie
	 * jako nasluchujace tych zmian. */
	public GameState getGameState() {
		return stateStack.peek();
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
/*-        Komunikacja ze stanami:        -*/
/*-----------------------------------------*/	
	
	public void postResolutionChange( int width, int height ) {
		for( GameState gs : stateStack ) {
			gs.onResolutionChanged(width, height);
		}
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

/*-----------------------------------------*/
/*-      Obsluga danych globalnych:       -*/
/*-----------------------------------------*/	
	
	StateGlobalContext sgc = null;
	
	public void setGlobalContext( StateGlobalContext sgc ) {
		this.sgc = sgc;
		for( GameState gs : stateStack ) {
			gs.injectGlobalContext( sgc );
		}
	}	
		
}
