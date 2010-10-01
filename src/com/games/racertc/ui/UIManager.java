package com.games.racertc.ui;

import com.games.racertc.MessageQueue;
import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.MessageFactory;

import android.graphics.Canvas;
import android.view.View.OnTouchListener;

/**
 * Klasa bazowa dla klas zarzadzajacych UI. Do ich zadan nalezy odbior
 * i analiza wejscia oraz rysowanie interface'u uzytkownika. UIManager
 * udostepnia klasom potomnym klasy MessageQueue i MessageFactory, a
 * takze nasluchuje zmian stanu gry i rowniez informacje o stanie gry
 * udostepnia klasom potomnym.
 */
public abstract class UIManager implements OnTouchListener, GameStateChangeListener {

	/** Przechowuje referencje do MessageQueue. */ 
	protected MessageQueue messageQueue;
	
	/** Przechowuje referencje do MessageFactory. */
	protected MessageFactory messageFactory;
	
	/** Szerokosc ekranu. */
	protected float width = 1024;
	
	/** Wysokosc ekranu. */
	protected float height = 768;
	
	/**
	 * Konstruktor klasy buforujacy obiekty MessageQueue i MessageFactory. Powinien
	 * byc wolany przez klasy nadrzedne.
	 */
	public UIManager() {
		//buforuje sobie MessageQueue i messageFactory
		messageQueue = MessageQueue.getInstance();
		messageFactory = MessageFactory.getInstance();
		//rejestruje sie jako obserwator zmian stanu gry:
		StateMachine.getInstance().addListener( this );	
	}

	/**
	 * Informuje UIManager o zmianie rozdzielczosci ekranu.
	 * @param width Nowa szerokosc ekranu.
	 * @param height Nowa wysokosc ekranu.
	 */
	public void setResolution( int width, int height ) {
		this.width = (float) width;
		this.height = (float) height;
	}
	
/*-----------------------------------------*/
/*-            Rysowanie UI:              -*/
/*-----------------------------------------*/
	
	/**
	 * Rysuje UI.
	 * @param canvas Canvas na ktorym narysowany zostanie interface uzytkownika.
	 */
	public abstract void drawUI( Canvas canvas );
	
	//TODO: wszystkie elementy UI wskakuja tutaj.
	
/*-----------------------------------------*/
/*-Implementacja GameStateChangeListener: -*/
/*-----------------------------------------*/			
					
	protected int gameState;
				
	@Override
	public void onGameStateChange( int gameState ) {
		this.gameState = gameState;
	}		
	
}
