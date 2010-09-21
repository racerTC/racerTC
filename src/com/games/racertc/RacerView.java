package com.games.racertc;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Klasa przechowujaca View aplikacji i obslugujaca wejscie uzytkownika.
 */
public class RacerView extends SurfaceView implements GameStateChangeListener {

	//private Context context;
	
	/** Przechowuje referencje do MessageQueue. */ 
	private MessageQueue messageQueue;
	
	/** Przechowuje referencje do MessageFactory. */
	private MessageFactory messageFactory;
	
	public RacerView( Context context, AttributeSet attrs ) {
		super( context, attrs );
		
		//SurfaceHolder surface_holder = getHolder();
		
		//buforuje sobie MessageQueue i messageFactory
		messageQueue = MessageQueue.getInstance();
		messageFactory = MessageFactory.getInstance();
		
		//rejestruje sie jako obserwator zmian stanu gry:
		StateMachine.getInstance().addListener( this );		
		
		//ponoc pomaga odbierac zdarzenia klawiszy! :D
		setFocusable( true );
	}

/*-----------------------------------------*/
/*-           Obsluga wejscia:            -*/
/*-----------------------------------------*/		
	
	/* Moze sie okazac, ze nie wszystkie sa potrzebne. */
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		//TODO: obsluga nacisniecia guzika
		return true;
	}
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ) {
		//TODO: obsluga puszczenia guzika
		return true;
	}
	
	public boolean onTouchEvent( MotionEvent event ) {
		Message m = null;
		
		//TODO: obsluga dotkniecia ekranu
		//np. gracz kliknal tak, ze rozpoczynamy jechac do przodu. 
		//robimy MessageFactory.createMessageForward( MessageFactory.BEGIN )
		
		messageQueue.push( m );
		
		return true;
	}
	
/*-----------------------------------------*/
/*-      Implementacja zdarzen okna:      -*/
/*-----------------------------------------*/		
	
	/**
	 * Wolane, gdy aplikacja znajdzie sie w tle albo powroci
	 * na pierwszy plan.
	 */
	@Override
	public void onWindowFocusChanged( boolean hasWindowFocus ) {
		//TODO: obsluga zmiany focusu (pauza).
	}

/*-----------------------------------------*/
/*-Implementacja GameStateChangeListener: -*/
/*-----------------------------------------*/			
				
	private int gameState;
				
	@Override
	public void onGameStateChange( int gameState ) {
		this.gameState = gameState;
		//TODO: reakcja na zmiany stanu.
	}		
	
}
