package com.games.racertc.gameplay;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Debug;
import android.view.View;

import com.games.racertc.Globals;
import com.games.racertc.R;
import com.games.racertc.gameplay.graphics.Presentation;
import com.games.racertc.gamestate.GameState;
import com.games.racertc.objects.Car;
import com.games.racertc.tracks.Track;
import com.games.racertc.ui.TwoSlidersSingleTouchUI;
import com.games.racertc.ui.UIManager;
import com.games.racertc.utility.Vec2D;

/**
 * Reprezentuje stan w ktorym znajduje sie aplikacja podczas rozgrywki.
 * @author Piotr Balut
 */
public class StateIngame extends GameState {

	/** Przechowuje obiekt prowadzacy symulacje rozgrywki. */
	Simulation simulation = null;
	
	/** Przechowuje obiekt rysujacy rozgrywke. */
	Presentation presentation = null;
	
	UIManager uiMan;
	
	int carId, trackId;
	
	public StateIngame( int carId, int trackId /* TODO: GameSettings */) {
		super();
		this.carId = carId;
		this.trackId = trackId;
	}
	
/*-----------------------------------------*/
/*-    Logika dzialania stanu Ingame:     -*/
/*-----------------------------------------*/		
	
	@Override
	public void process( long dt ) {
		//TODO: time_accumultor i dalsze kwantowania np. po 5 lub 10 ms
		simulation.simulate( dt );
	}

	@Override
	public void render( Canvas canvas ) {
		presentation.drawGame( canvas );
	}
	
/*-----------------------------------------*/
/*- Implementacja zdarzen on...() stanu:  -*/
/*-----------------------------------------*/	
	
	@Override
	protected void onTouchListenerNeeded( View v ) {
		uiMan = new TwoSlidersSingleTouchUI( Globals.resources );
		v.setOnTouchListener( uiMan );
	}
	
	@Override
	public void onEnter( GameState oldState, boolean isEdgeState ) {
		//TODO: loadscreen!
		if( presentation == null ) {
			presentation = new Presentation( Globals.surfaceHolder, Globals.resources );
			simulation = new Simulation();
			presentation.setUIManager( uiMan );
	
			setupGame( carId, trackId );
		}
		onResolutionChanged( Globals.surfaceWidth, Globals.surfaceHeight );
	}
	
/*
 *  Do przeniesienia do innego stanu: ladowanie rozgrywki:	
 */
	
/**
 * Przygotowuje gre do uruchomienia wlasciwej rozgrywki.
 * 
 * <ul>TODO: klasa GameSettings sluzaca do komunikacja UI z ta metoda, w szczegolnosci:
 * <li> przechowujaca nazwe wybranej trasy
 * <li> przechowujaca ustawienia samochodu gracza (innych samochodow)
 * <li> inne ustawienia: pogoda, poziom trudnosci, etc
 * </ul>
 * 
 * setupGame nastepnie laduje wszystkie potrzebne zasoby i odpowiednio konfiguruje presentation
 * i simulation.
 * @author Pawegio & Piotr Balut
 */
public void setupGame(int carId, int trackId) {
	//////////////////////////////////////
	// Przygotowuje przykladowa gre     //
	//////////////////////////////////////
	//samochod:
	int chosenCar;
	
	Resources resources = Globals.resources;
	
	switch(carId) {
	case R.id.car_01:
		chosenCar = resources.getIdentifier("car_01", "drawable", "com.games.racertc");
		break;
	case R.id.car_02:
		chosenCar = resources.getIdentifier("car_02", "drawable", "com.games.racertc");
		break;
	case R.id.car_03:
		chosenCar = resources.getIdentifier("car_03", "drawable", "com.games.racertc");
		break;
	case R.id.car_04:
		chosenCar = resources.getIdentifier("car_04", "drawable", "com.games.racertc");
		break;
	default:
		chosenCar = resources.getIdentifier("car_01", "drawable", "com.games.racertc");
		break;
	}
	
	Car c = new Car( 2.0f, resources.getDrawable( chosenCar ));
	c.setPosition( new Vec2D( 25f,25f ) );
	//trasa:
	int chosenTrack;
	
	switch(trackId) {
	case R.id.track_01:
		chosenTrack = resources.getIdentifier("track_01", "drawable", "com.games.racertc");
		break;
	case R.id.track_02:
		chosenTrack = resources.getIdentifier("track_02", "drawable", "com.games.racertc");
		break;
	case R.id.track_03:
		chosenTrack = resources.getIdentifier("track_03", "drawable", "com.games.racertc");
		break;
	case R.id.track_04:
		chosenTrack = resources.getIdentifier("track_04", "drawable", "com.games.racertc");
		break;
	default:
		chosenTrack = resources.getIdentifier("track_01", "drawable", "com.games.racertc");
		break;
	}
	Track t = null;
	Debug.MemoryInfo dbmi1 = new Debug.MemoryInfo();
	Debug.MemoryInfo dbmi2 = new Debug.MemoryInfo();
	Debug.getMemoryInfo( dbmi1 );
	int bg = dbmi1.otherSharedDirty;
	t = new Track( BitmapFactory.decodeResource(resources, chosenTrack) );
	Debug.getMemoryInfo( dbmi2 );
	int en = dbmi2.otherSharedDirty;
	int imgs = en - bg;
	//dodaje samochod do trasy:
	t.addCar( c );
	//inicjalizuje obiekty symulujace i rysujace
	presentation.initialise( t );
	simulation.initialise( t );
}	
	
/*-----------------------------------------*/
/*-            Rozdzielczosc:             -*/
/*-----------------------------------------*/	
		
	/**
	 * Wolane po zmianie rozdzieczosci Surface'u.
	 */
	@Override
	protected void onResolutionChanged( int width, int height ) {
		if(uiMan != null) uiMan.setResolution(width, height);
		if(presentation != null) presentation.setResolution(width, height);
	}

}
