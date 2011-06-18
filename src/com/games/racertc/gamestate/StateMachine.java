package com.games.racertc.gamestate;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

import com.games.racertc.commands.CommandQueue;

/**
 * Maszyna stanow gry pozwala zarzadzac przejsciami pomiedzy stanami gry.
 * @author Piotr Balut
 */
public class StateMachine {

/*-----------------------------------------*/
/*-     Implementacja maszyny stanow:     -*/
/*-----------------------------------------*/			
	
	/**
	 * Stos stanow gry.
	 */
	private Stack<GameState> stateStack = new Stack<GameState>();

	/**
	 * Aktywuje zadany stan. Dla dotychczasowego, opuszczanego stanu wolana jest metoda onLeave, nastepnie dla nowego stanu
	 * wolane jest onEneter. Po zmianie stanu sluchacze zmian stanu informowani sa o przejsciu maszyny do nowego stanu.<br><br>
	 * Przejscie pomiedzy stanami nie zostanie zostanie wykonane natychmiastowo - wykonanie przejscia zostanie odlozone do
	 * czasu zakonczenia aktualnego przejscia glownej petli gry.
	 * @param state
	 */
	public static void enterState( GameState state ) {
		StateTransitionCommand stc = new StateTransitionCommand();
		stc.action = StateTransitionCommand.ENTER;
		stc.state = state;
		CommandQueue.GetInstance().push( stc );
	}
	
	protected void immediateEnterState( GameState state ) {
		GameState prev;
		try {
			 prev = stateStack.peek();
		} catch( EmptyStackException e ) {
			prev = null;
		}
		if( prev != null )
			prev.leave( state, false );
		stateStack.push( state );
		state.enter( prev, true );
		notifyListeners( state );
		
	}
	
	/**
	 * 
	 */
	public static void leaveState() {
		
	}
	
	protected void immediateLeaveState() {
		GameState prev = stateStack.pop();
		GameState state = stateStack.peek();
		prev.leave( state, true );
		state.enter( prev, false );
		notifyListeners( state );
		
	}
	
/*-----------------------------------------*/
/*-       Implementacja singletonu:       -*/
/*-----------------------------------------*/		
	
	private static final StateMachine instance = new StateMachine();
	
	private StateMachine() {}
	
	public static StateMachine GetInstance() {
		return instance;
	}

/*-----------------------------------------*/
/*-      Implementacja obserwatora:       -*/
/*-----------------------------------------*/	
	
	/**
	 * Interface, ktory pozwala zarejestrowac sie klasie jako sluchacz zmian stanow gry.
	 */
	public static interface ChangeListener {
		
		/**
		 * Wolane przez StateMachine dla kazdego zarejestrowanego sluchacza po zmianie stanu.
		 * Maszyna stanow gwarantuje, iz stany beda zmieniane pomiedzy przejsciami glownej
		 * petli gry, tak wiec notifyStateChanged nie zostanie nigdy wywolane w trakcie przetwarzania
		 * aktualnie aktywnego stanu.
		 * @param newState Nowy stan.
		 */
		public void notifyStateChanged( GameState newState );
		
	} // interface ChangeListener
	
	/**
	 * Lista przechowujaca wszystkich sluchaczy zmian stanu maszyny.
	 */
	private LinkedList<ChangeListener> changeListeners = new LinkedList<ChangeListener>();
	
	/**
	 * Rejestruje nowego sluchacza zmiany stanow. Zostaje on natychmiast poinformowany o
	 * aktualnie aktywnym stanie.
	 * @param listener
	 */
	public void addListener( ChangeListener listener ) {
		try {
			listener.notifyStateChanged( stateStack.peek() );
		} catch( EmptyStackException e ) {
			listener.notifyStateChanged( null );
		}
		changeListeners.add( listener );
	}
	
	public void removeListener( ChangeListener listener ) {
		changeListeners.remove( listener );
	}
	
	private void notifyListeners( GameState state ) {
		for( ChangeListener cl : changeListeners ) {
			cl.notifyStateChanged( state );
		}
	}

	public void updateResolution(int width, int height) {
		for( GameState gs : stateStack ) {
			gs.onResolutionChanged( width, height );
		}
	}
	
}
