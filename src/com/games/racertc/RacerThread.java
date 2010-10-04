package com.games.racertc;

import com.games.racertc.gamestate.GameState;
import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.objects.Car;
import com.games.racertc.other.Vec2D;
import com.games.racertc.tracks.Track;
import com.games.racertc.ui.UIManager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * Klasa obslugujaca glowny watek gry. Przechowuje klasy Simulation i Presentation odpowiadajace odpowiednio
 * za symulacje i rysowanie w grze, a takze posredniczy w komunikacji z nimi.
 */
public class RacerThread extends Thread implements GameStateChangeListener {
	
	/**
	 * Docelowy czas trwania pojedynczej klatki w milisekundach. Obecne
	 * ustawienie to 33 ms, co daje okolo 30 fps.
	 */
	public final static long TARGET_FRAME_DURATION = 33;
	
	/**
	 * Okresla, czy bedzie stosowane limitowanie liczby klatek na sekunde.
	 * Jezeli tak, docelowa dlugosc klatki jest okreslana przez stala
	 * TARGET_FRAME_DURATION.
	 */
	public final static boolean LIMIT_FRAMERATE = true;
	
	final SurfaceHolder surfaceHolder;
	
	final Context context;
	
	final Simulation simulation;
	
	final Presentation presentation;
	
	final Resources resources;
	
	public RacerThread( SurfaceHolder surfaceHolder, Context context, Resources resources ) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		this.resources = resources;
		
		presentation = new Presentation( surfaceHolder, resources );
		simulation = new Simulation();
		
		//rejestruje sie jako obserwator zmian stanu gry:
		StateMachine.getInstance().addListener( this );
	}

/*-----------------------------------------*/
/*-           Dostep do danych:           -*/
/*-----------------------------------------*/		
	
	/**
	 * Informuje watek gry o zmianie rozdzielczosci ekranu. Musi byc wolane po kazdej
	 * zmianie rozdzielczosci.
	 * @param width Nowa szerokosc ekranu.
	 * @param height Nowa wysokosc ekranu.
	 */
	public void setResolution( int width, int height ) {
		if( presentation != null ) {
			presentation.setResolution(width, height);
		} else {
			//To znaczy, ze zmiana powierzchni nastapila
			//przed utworzeniem presentation. Mam nadzieje,
			//ze tak nie jest. Jezeli jest, trzeba zbuforowac
			//rozdzielczosc w zmiennych w tej klasie i podac
			//ja presentation po jej utworzeniu.
		}
	}
	
	/**
	 * Wiaze glowny watek gry z wybranym menadzerem wejscia. Od tej pory bedzie on
	 * uzywany do rysowania UI.
	 * @param uiManager
	 */
	public void setUIManager(UIManager uiManager) {
		presentation.setUIManager( uiManager );
	}	
	
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
	 * i simulation. Odpowiednie pokierowanie stanami gry moze doprowadzic di uruchomienia setupGame
	 * nie w watku glownym, tylko w watku RacerThread, albo jeszcze innym - i posluzyc np. do wyswietlania
	 * paska postepu ladowania; albo wplecienie takiego rysujacego kodu w samo setupGame, co moze byc
	 * prostrze wrealizacji.
	 */
	public void setupGame(int carId, int trackId) {
		//////////////////////////////////////
		// Przygotowuje przykladowa gre     //
		//////////////////////////////////////
		//samochod:
		int chosenCar;
		
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
		//ustawia stan
		StateMachine.getInstance().setGameState( GameState.GAME_ACTIVE );
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
		
		long previous_time = SystemClock.uptimeMillis();
		//do mierzenia sredniej ilosci fpsow:
		//long ttm = 0, fct = 0;
		
		while( run ) {
			
			long current_time = SystemClock.uptimeMillis();
			long time_quantum = current_time - previous_time;
			previous_time = current_time;
			
			//Obliczanie FPS'ow:
			//ttm += time_quantum;
			//fct += 1;
			//float fps_avg = fct / ttm;
			
			switch( gameState ) {
			
			case GameState.INTRO:
				/* Odtwarzamy intro: */
				
				break;
				
			case GameState.GAME_ACTIVE:
				/* Gra aktywna: */
				
				
				//odpal symulacje
				if( time_quantum > 0 )
					//TODO: time_accumultor i dalsze kwantowania np. po 5 lub 10 ms
					simulation.simulate( time_quantum ); //dt - kwant czasu
				
				//odpal rysowanie
				presentation.drawGame();
				/* jezeli stan w menu, rysuj menu. */
				
				break;
			}
			
			if( LIMIT_FRAMERATE ) {
				//oblicza czas jaki watek bedzie spal
				long finished_time = SystemClock.uptimeMillis();
				long sleep_time = TARGET_FRAME_DURATION - ( finished_time - current_time );
				//jezeli zostal jeszcze czas wolny, watek odpoczywa
				if( sleep_time > 0 )
					SystemClock.sleep( sleep_time );
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
