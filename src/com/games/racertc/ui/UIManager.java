package com.games.racertc.ui;

import com.games.racertc.MessageQueue;
import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;
import com.games.racertc.messages.Message;
import com.games.racertc.messages.MessageFactory;
import com.games.racertc.objects.Car;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.OnTouchListener;

/**
 * Klasa bazowa dla klas zarzadzajacych UI. Do ich zadan nalezy odbior
 * i analiza wejscia oraz rysowanie interface'u uzytkownika. UIManager
 * udostepnia klasom potomnym klasy MessageQueue i MessageFactory, a
 * takze nasluchuje zmian stanu gry i rowniez informacje o stanie gry
 * udostepnia klasom potomnym.
 */
public abstract class UIManager implements OnTouchListener {

	
	protected float
		lastYUsage = 0f,
		lastXUsage = 0f;
		
	protected int lastFlags = Message.FLAG_NONE;
	
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
	public void drawUI( Canvas canvas ) {
		canvas.drawText( "FPS: " + fps, width - 50f, 20f, new Paint() );
	}
	
	//TODO: wszystkie elementy UI wskakuja tutaj.	
	
/*-----------------------------------------*/
/*-         Funkcje pomocnicze:           -*/
/*-----------------------------------------*/	
	
	/**
	 * Sprawdza, czy zadana wartosc miesci sie w podanym przedziale. Sprawdzane
	 * sa nierownosci slabe.
	 * @param val Testowana wartosc.
	 * @param min Dolna granica przedzialu.
	 * @param max Gorna granica przedzialu.
	 * @return Zwraca <strong>true</strong> jezeli wartosc miesci sie w przedziale
	 * [min,max], albo <strong>false</strong> w przeciwnym przypadku.
	 */
	public static boolean isInRange( float val, float min, float max ) {
		if( (val >= min) && (val <= max) )
			return true;
		else return false;
	}

	/**
	 * Wysyla do glownego watku gry wiadomosc zawierajaca zadane sterowanie. Metoda dba o to, aby
	 * nie wysylac kilkakrotnie wiadomosci powielajacych wiadomosci wyslane uprzednio.
	 * @param flags
	 * @param sideUsage
	 * @param fwdUsage
	 */
	public void sendMessage( int flags, float sideUsage, float fwdUsage ) {
		/* Zaokraglanie liczb: */
		sideUsage = (float) Math.round( sideUsage * 100f ) * 0.01f;
		fwdUsage = (float) Math.round( fwdUsage * 100f ) * 0.01f;
		/* Jezeli trzeba, wysyla wiadomosc. */
		if( !(flags == lastFlags && lastXUsage == sideUsage && lastYUsage == fwdUsage) ) {
			lastFlags = flags;
			lastXUsage = sideUsage;
			lastYUsage = fwdUsage;
			messageQueue.push(
					messageFactory.createMovementMessage(
							Car.CAR_PLAYER,
							flags,
							sideUsage,
							fwdUsage
					)
			);
		}
	}
	
/*-----------------------------------------*/
/*-           Pomiar FPS'ow:              -*/
/*-----------------------------------------*/		
	
	long fps;
	
	public void setFPS( long fps ) {
		this.fps = fps;
	}
	
}
