package com.games.racertc;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Klasa przechowujaca View aplikacji i obslugujaca wejscie uzytkownika.
 */
public class RacerGameView extends SurfaceView implements GameStateChangeListener {

	//private Context context;
	
	/** Przechowuje referencje do MessageQueue. */ 
	private MessageQueue messageQueue;
	
	/** Przechowuje referencje do MessageFactory. */
	private MessageFactory messageFactory;
	
	private Presentation presentation;
	
	public RacerGameView( Context context, AttributeSet attrs ) {
		super( context, attrs );	
		
		//ponoc pomaga odbierac zdarzenia klawiszy! :D
		//setFocusable( true );
	}

	/**
	 * Metoda powinna zostac wywolana po odebraniu klasy przez findViewById. Przygotowuje
	 * RacerView do pracy.
	 * @param presentation  
	 */
	public void initialise( Presentation presentation ) {
		//SurfaceHolder surface_holder = getHolder();
		
		//buforuje sobie MessageQueue i messageFactory
		messageQueue = MessageQueue.getInstance();
		messageFactory = MessageFactory.getInstance();
		
		//rejestruje sie jako obserwator zmian stanu gry:
		StateMachine.getInstance().addListener( this );	
		
		//zapisuje referencje do presentation
		this.presentation = presentation;
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
		
		float x = event.getX();
		float y = event.getY();
					
		//
		// Kod ponizej to proteza: do zastapienia przez cos lepszego, oraz
		// obslugujacego wiele typow sterowania.
		//
		
		//Analiza dotkniecia:
		switch( event.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if( !((x > 10f) && ( y > 10f ) && ( x < 70f ) && ( y < 70f )) )
				return true;
			if( (x == 40f) && (y == 40f) ) return true;
			
			Message m = null;
			
			if( x < 30f ) { //skrecamy w lewo
				
			} else if( x < 50f ) { //jedziemy prosto
				
			} else { //skrecamy w prawo
				
			}
			
			if( y < 40f ) { //jedziemy do przodu
				
			} else { //jedziemy do tylu
				
			}
			
			presentation.injectOffsets(x - 40f, y - 40f);
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			presentation.injectOffsets(0f, 0f);
			break;
		}
		
		//Message m = null;
		
		//TODO: obsluga dotkniecia ekranu
		//np. gracz kliknal tak, ze rozpoczynamy jechac do przodu. 
		//robimy MessageFactory.createMessageForward( MessageFactory.BEGIN )
		
		//messageQueue.push( m );
		
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
