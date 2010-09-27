package com.games.racertc;

import com.games.racertc.gamestate.GameState;
import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.objects.Car;
import com.games.racertc.other.Vec2D;
import com.games.racertc.tracks.Track;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.view.SurfaceHolder;

/**
 * Klasa obslugujaca glowny watek gry. Przechowuje klasy Simulation i Presentation odpowiadajace odpowiednio
 * za symulacje i rysowanie w grze, a takze posredniczy w komunikacji z nimi.
 */
public class RacerThread extends Thread implements GameStateChangeListener {
	
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
	 * Pozwala pobrac referencje podsystemu rysujacego.<br>
	 * <br>
	 * Nie lubie tej czesci. Sluzy bezposrednio wymianie danych miedzy watkiem
	 * UI a Presentation w celu ustalenia pozycji interaktywnych elementow. Byc
	 * moze lepiej bylo by zrobic jakiegos listenera?
	 * @return Aktywny obiekt Presentation.
	 */
	public Presentation getPresentation() {
		return presentation;
	}
	
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
	public void setupGame(/* GameSettings gs */) {
		//////////////////////////////////////
		// Przygotowuje przykladowa gre     //
		//////////////////////////////////////
		//samochod:
		Car c = new Car( 2.0f, resources.getDrawable( R.drawable.new2_car_red ) );
		c.setPosition( new Vec2D( 25f,25f ) );
		//trasa:
		Track t = null;
		Debug.MemoryInfo dbmi1 = new Debug.MemoryInfo();
		Debug.MemoryInfo dbmi2 = new Debug.MemoryInfo();
		Debug.getMemoryInfo( dbmi1 );
		int bg = dbmi1.otherSharedDirty;
		try {
			t = new Track( BitmapFactory.decodeResource(resources, R.drawable.tor_big) );
		} catch( Exception e ) {
			e.printStackTrace();
		}
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
