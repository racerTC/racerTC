package com.games.racertc.gamestate;

import com.games.racertc.ui.UIManager;

import android.graphics.Canvas;

/**
 * Klasa, ktora kazdy szanujacy sie stan gry powinien implementowac.
 * @author Piotr Balut
 */
public abstract class GameState {

	/**
	 * Wolane tuz po dodaniu stanu do stosu stanow, jednak przed poinformowaniem
	 * obserwatorow zmian stanu o przejsciu.
	 */
	protected void onEnter() {}
	
	protected final void enter() {
		if( uiManager != null )
			if( sgc != null )
				sgc.racerView.setOnTouchListener( uiManager );
		onEnter();
	}
	
	/**
	 * Wolane tuz przed usunieciem stanu ze stosu stanow.
	 */
	protected void onLeave() {}
	
	protected final void leave() {
		if( uiManager != null )
			if( sgc != null )
				sgc.racerView.setOnTouchListener( null );
		onLeave();
	}
	
	/**
	 * Wolane jednorazowo tuz po wstawieniu stanu do maszyny stanow, tuz po wstrzyknieciu
	 * do stanu globalnego kontekstu, ale przed dowolnym innym zdarzeniem on...() i przed
	 * poinformowaniem sluchaczy zmian stanu o zmianie stanu.
	 */
	protected void onStateMachineInsertion() {}
	
	/**
	 * Wolane jednorazowo tuz przed usuniecie stanu z maszyny stanow. Metoda ta wywolywanajest po
	 * dowolnym innym zdarzeniu on...() i po poinformowaniu sluchaczy zmian stanu o zmianie stanu
	 * na nowy.
	 */
	protected void onStateMachineExtraction() {}
	
	/**
	 * Wolane po zmianie stanu paused Activity do ktorego nalezy stan, lub w innych okolicznosciach,
	 * w ktorych stan powinien zostac wstrzymany lub wznowiony.
	 * @param paused Okresla, czy stan zostal wlasnie wstrzymany (<strong>true</strong>), czy tez
	 * wznowiony (<strong>false</strong>).
	 */
	protected void onPause( boolean paused ) {
		
	}
	
	/**
	 * Okresla, czy stan jest obecnie wstrzymany.
	 */
	boolean isPaused = false;
	
	/**
	 * W zaleznosci od parametru <i>paused</i> wstrzymuje lub wznawia stan.
	 * @param paused Okresla, czy stan ma zostac wstrzymany (<strong>true</strong>), czy tez
	 * wznowiony (<strong>false</strong>).
	 */
	public final void pause( boolean paused ) {
		isPaused = paused;
		onPause( paused );
	}
	
	/**
	 * Wolane po zmianie rozdzieczosci Surface'u.
	 */
	protected void onResolutionChanged( int width, int height ) {}
	
	/**
	 * Metoda wolana cyklicznie przez glowna petle gry dla aktualnie aktywnego stanu. Metoda jest
	 * dedykowana przetwarzaniu logiki dzialania programu.
	 * @param dt Czas w milisekundach, jaki uplynal od przetworzenia ostatniej klatki.
	 */
	public abstract void process( long dt );
	
	/**
	 * Metoda wolana cyklicznie przez glowna petle gry dla aktualnie aktywnego stanu. Metoda jest
	 * dedykowana do rysowania grafiki w programie.
	 * @param canvas Obiekt canvas, na ktorym odbywa sie rysowanie.
	 */
	public abstract void draw( Canvas canvas );

	/**
	 * Okresla, czy bedzie stosowane limitowanie liczby klatek na sekunde w aktualnym
	 * stanie. Jezeli bedzie, docelowa dlugosc klatki jest okreslana przez stala
	 * RacerThread::TARGET_FRAME_DURATION.
	 * @return Zwraca <strong>true</strong>, jezeli stan pozwala na ograniczenie ilosci klatek na
	 * sekunde, lub <strong>false</strong>, jezeli ograniczenie ilosci klatek na sekunde nie powinno
	 * byc stosowane dla tego stanu. Domyslnie zwracana wartoscia jest <strong>true</strong>.
	 */
	public boolean limitFramerate() {
		return true;
	}

/*--------------------------------------*/
/*-    Wsparcie dla zarzadzania UI:    -*/
/*--------------------------------------*/
	
	/** UIManager. */
	protected UIManager uiManager;
	
	/**
	 * Przypisuje UIManager do tego stanu. Maszyna stanow zapewnia, iz menadzer UI bedzie
	 * otrzymywal dotkniecia tylko i wylacznie, gdy przypisany stan jest stanem aktualnym.
	 * TODO: a jak pauza, to co?
	 * @param uiManager
	 */
	public final void dispatchUIManager( UIManager uiManager ) {
		//if( uiManager != null ) ;?
		this.uiManager = uiManager;
		if( sgc != null )
			sgc.racerView.setOnTouchListener( uiManager );
	}	

	public UIManager getUIManager() {
		return uiManager;
	}
	
/*-----------------------------------------*/
/*-      Obsluga danych globalnych:       -*/
/*-----------------------------------------*/	
	
	protected StateGlobalContext sgc;
	
	/**
	 * Wolane jednorazowo po wstawieniu stanu do maszyny stanow w celu wstrzykniecia do niego
	 * globalnego kontekstu. Jest wywolywane przed dowolna inna metoda on...().
	 * @param sgc Globalny kontekst dostepny dla stanow.
	 */
	protected void injectGlobalContext( StateGlobalContext sgc ) {
		this.sgc = sgc;
	}
}
