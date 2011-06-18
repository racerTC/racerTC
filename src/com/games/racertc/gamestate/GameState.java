package com.games.racertc.gamestate;

import com.games.racertc.Globals;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Klasa, ktora kazdy szanujacy sie stan gry powinien implementowac.
 * @author Piotr Balut
 */
public abstract class GameState implements OnTouchListener {

	/**
	 * Stan, ktory znajduje sie pod tym stanem na stosie stanow.
	 */
	GameState previousState = null;
	
	boolean paused = false;
	
/*-----------------------------------------*/
/*-               Zdarzenia:              -*/
/*-----------------------------------------*/	
	
	/**
	 * Wolane, kiedy stan staje sie aktywny, po tym, jak poprzedni stan przestal obowiazywac. Pole
	 * previousState ma poprawna wartosc w momencie uruchamiania tej metody. Gwarantowane jest wywolanie
	 * metody onEnter pomiedzy kolejnymi przejsciami glownej petli gry, tzn. pewne jest, iz w chwili
	 * wywolania nie jest przetwarzana zadna inna metoda tego stanu.<br><br>
	 * <b>UWAGA!</b> W trakcie wykonywania tej metody nie jest okreslone, jaki stan znajduje sie na
	 * szczycie stosu stanow.
	 * @param oldState Stan, ktory wlasnie przestanie byc aktywny.
	 * @param isEdgeState Przyjmuje wartosc <b>true</b>, jezeli stan jest nowym stanem w maszynie stanow
	 * (tzn. przejscie do niego nastapilo wskutek dodania nowego stanu do maszyny).
	 */
	public void onEnter( GameState oldState, boolean isEdgeState ) {}
	
	/**
	 * Wolane, kiedy stan przestaje byc aktywny, tuz zanim stan przestanie obowiazywac. Pole previousState 
	 * ma poprawna wartosc w momencie uruchamiania tej metody. Gwarantowane jest wywolanie metody onLeave
	 * pomiedzy kolejnymi przejsciami glownej petli gry, tzn. pewne jest, iz w chwili wywolania nie jest
	 * przetwarzana zadna inna metoda tego stanu.<br><br>
	 * <b>UWAGA!</b> W trakcie wykonywania tej metody nie jest rowniez okreslone, jaki stan znajduje sie na
	 * szczycie stosu stanow.
	 * @param newState Stan, ktory wkrotce bedzie aktywny.
	 * @param isEdgeState Przyjmuje wartosc <b>true</b>, jezeli stan po opuszczeniu zostanie usuniety z
	 * maszyny (tzn. jego opuszczenie nie wynika z dodania stanu nadrzednego).
	 */
	public void onLeave( GameState newState, boolean isEdgeState ) {}
	
	/**
	 * Wolane, gdy aplikacja zostanie zapauzowana lub odpauzowana. Metoda jest wolana _tylko_ dla aktualnie
	 * aktywnego stanu. W jego gestii lezy ocena, czy stany nizsze powinny zostac poinformowane o zmianie
	 * stanu aplikacji.<br><br>
	 * 
	 * Przekazanie informacji nizszemu stanowi jest zachowaniem domyslnym metody. Ponadto domyslnie znacznik
	 * paused jest ustawiony stosownie na <b>true</b> badz <b>false</b>
	 * @param isPaused
	 */
	public void onPause( boolean isPaused ) {
		if( previousState != null )
			previousState.onPause( isPaused );
		paused = isPaused;
	}	
	
	/**
	 * Po zmianie rozdzielczosci stan jest informowany o nowej szerokosci i wysokosci ekranu. Metoda wolana
	 * jest zawsze, niezaleznie od tego, czy stan znajduje sie na szczyscie stosu stanow, czy tez nie.
	 * @param width
	 * @param height
	 */
	protected void onResolutionChanged( int width, int height ) {}

	/**
	 * Metoda wolana jest zawsze wtedy, gdy stan staje sie stanem aktywnym i ma priorytet obslugi zdarzen
	 * wejscia. Metoda powinna zapewnic ustawienie wlasciwego TouchListenera (badz innych wedle potrzeby) dla
	 * zadanego View. Domyslnie <i>this</i> ustawiane jest jako TouchListener dla <i>v</i>.
	 * @param v View dla ktorego nalezy ustawic TouchListener'a.
	 */
	protected void onTouchListenerNeeded( View v ) {
		v.setOnTouchListener( this );
	}
	
/*-----------------------------------------*/
/*-     Komunikacja ze StateMachine:      -*/
/*-----------------------------------------*/		
	
	/**
	 * Wolane przez maszyne stanow podczas uruchomienia stanu, po uruchomieniu leave poprzedniego stanu.
	 * @param oldState Stan, ktory wlasnie przestanie byc aktywny.
	 * @param isEdgeState Przyjmuje wartosc <b>true</b>, jezeli stan jest nowym stanem w maszynie stanow
	 * (tzn. przejscie do niego nastapilo wskutek dodania nowego stanu do maszyny).
	 */
	final protected void enter( GameState oldState, boolean isEdgeState ) {
		previousState = oldState;
		onTouchListenerNeeded( Globals.racerView );
		onEnter( oldState, isEdgeState );
	}
	
	/**
	 * Wolane przez maszyne stanow podczas opuszczania stanu, przed uruchomieniem enter nowego stanu.
	 * @param newState Stan, ktory wkrotce bedzie aktywny.
	 * @param isEdgeState Przyjmuje wartosc <b>true</b>, jezeli stan po opuszczeniu zostanie usuniety z
	 * maszyny (tzn. jego opuszczenie nie wynika z dodania stanu nadrzednego).
	 */
	final protected void leave( GameState newState, boolean isEdgeState ) {
		onLeave( newState, isEdgeState );
	}

/*-----------------------------------------*/
/*-        Funkcjonalnosc stanow:         -*/
/*-----------------------------------------*/		
	
	/**
	 * Przetwarzanie stanu. Metoda jest wolana automatycznie przez glowna petle gry w kazdym przejsciu.
	 * @param dt Czas jaki uplynal od przetworzenia poprzednie klatki (w milisekundach).
	 */
	public abstract void process( long dt );

	/**
	 * Rysowanie stanu. Metoda jest wolana automatycznie przez glowna petle gry w kazdym przejsciu.
	 * Podczas wykonywania meotda ma zapewniony ekskluzywny dostep do puli obiektow RenderablePool.
	 * @param queue Kolejka do ktorej powinno nastepowac renderowanie.
	 */	
	public abstract void render( Canvas queue );
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) {
		return true;
	}

}
