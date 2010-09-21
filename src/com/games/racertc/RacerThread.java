package com.games.racertc;

import com.games.racertc.gamestate.GameState;
import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;

import android.content.Context;
import android.content.res.Resources;
import android.view.SurfaceHolder;

/**
 * Klasa obslugujaca glowny watek gry. Przechowuje klasy Simulation i Presentation odpowiadajace odpowiednio
 * za symulacje i rysowanie w grze, a takze posredniczy w komunikacji z nimi.
 */
public class RacerThread extends Thread implements GameStateChangeListener {
	
	SurfaceHolder surfaceHolder;
	Context context;
	
	Simulation simulation;
	
	Presentation presentation;
	
	public RacerThread( SurfaceHolder surfaceHolder, Context context, Resources resources ) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		
		presentation = new Presentation( surfaceHolder, resources );
		simulation = new Simulation();
		
		//rejestruje sie jako obserwator zmian stanu gry:
		StateMachine.getInstance().addListener( this );
	}

	public void setResolution( int width, int height ) {
		if( presentation != null ) {
			presentation.setResulution(width, height);
		} else {
			//To znaczy, ze zmiana powierzchni nastapila
			//przed utworzeniem presentation. Mam nadzieje,
			//ze tak nie jest. Jezeli jest, trzeba zbuforowac
			//rozdzielczosc w zmiennych w tej klasie i podac
			//ja presentation po jej utworzeniu.
		}
	}
	
/*-----------------------------------------*/
/*-           Glowna petla gry:           -*/
/*-----------------------------------------*/	
	
	private boolean run = true;
	
	/**
	 * Uruchamia i obsluguje glowna petle gry.
	 */
	@Override
	public void run() {
		
		while( run ) {
			
			
			
			switch( gameState ) {
			
			case GameState.INTRO:
				/* Odtwarzamy intro: */
				
				break;
				
			case GameState.MAIN_MENU:
				//sprawdza, czy przypadkiem uzytkownik nie nacisnal jakiegos guzika
				// (odbiera zdarzenia)
				//rozkazuje Presentation rysowac menu glowne
				break;	
				
			case GameState.GAME_ACTIVE:
				/* Gra aktywna: */
				
				
				//odpal symulacje
				//simulation.simulate( dt ); //dt - kwant czasu
				
				//odpal rysowanie
				presentation.drawGame();
				/* jezeli stan w menu, rysuj menu. */
				
				//spij ile trzeba
				break;
			}
			
			
			
		}
		
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
