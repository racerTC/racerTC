package com.games.racertc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Klasa przechowujaca View aplikacji i obslugujaca wejscie uzytkownika.
 */
public class RacerGameView extends SurfaceView {
	
	public RacerGameView( Context context, AttributeSet attrs ) {
		super( context, attrs );	
	}

	/**
	 * Metoda powinna zostac wywolana po odebraniu klasy przez findViewById. Przygotowuje
	 * RacerView do pracy.
	 * @param presentation  
	 */
	public void initialise() {
		//ponoc pomaga odbierac zdarzenia klawiszy! :D
		setFocusable( true );
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
	
}
