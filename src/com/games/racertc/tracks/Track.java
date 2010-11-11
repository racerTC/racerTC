package com.games.racertc.tracks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.games.racertc.objects.Car;
import com.games.racertc.objects.GameObject;
import com.games.racertc.other.Vec2D;

import android.graphics.Bitmap;

public class Track {

/*-----------------------------------------*/
/*-    Podstawowe parametry i metody:     -*/
/*-----------------------------------------*/		
	
	private final static int MAX_CARS = 8;
	
	private int cars_added = 0;
	
	/** Szerokosc trasy (w pikselach). */
	private final int width;
	
	/** Wysokosc trasy (w pikselach). */
	private final int height;
	
	/** Grafika tla. */
	private final Bitmap trackGraphics;
	
	public Track( Bitmap trackGraphics ) {
		this.width = trackGraphics.getWidth();
		this.height = trackGraphics.getHeight();
		this.trackGraphics = trackGraphics;
		//inicjalizacja kontenerow:
		cars = new Vector< Car >( MAX_CARS );
		objects = new LinkedList< GameObject >();
	}

/*-----------------------------------------*/
/*-          Gettery parametrow:          -*/
/*-----------------------------------------*/		
	
public int getWidth() {
	return width;
}
	
public int getHeight() {
	return height;
}

public Bitmap getTrackGraphics() {
	return trackGraphics;
}	
	
/*-----------------------------------------*/
/*-  Informacje o pojazdach i obiektach:  -*/
/*-----------------------------------------*/		
	
	/** Lista wszystkich obiektow na torze. */
	private final List< GameObject > objects;
	
	/**
	 * Lista samochodow - pozwala na szybki dostep
	 * bezposrednio do samochodow, na potrzeby symulacji.
	 */
	private final Vector< Car > cars;
	
	/**
	 * TODO: jezeli liczba obiektow bedzie duza, bedzie trzeba podzielic trase na
	 * sektory i zamiast wszystkich obiektow pobierac liste tylko tylko i wylacznie
	 * z potrzebnego sektora - aby nie tracic czasu na iterowanie po zbednych danych.<br>
	 * <br>
	 * Pozwala na dostep do obiektow znajdujacych sie na trasie. Gwarantowane jest przy
	 * tym, ze pierwszym obiektem na liscie jest samochod gracza.
	 * @return iterator do listy wszystkich obiektow znajdujacych sie na trasie.
	 */
	public ListIterator< GameObject > getGameObjectIterator() {
		objects.listIterator();
		return objects.listIterator();
	}
	
	/**
	 * Pozwala na dostep do samochodow znajdujacych sie na trasie.
	 * @return iterator do listy wszystkich samochodow znajdujacych sie na trasie.
	 */
	public Iterator< Car > getCarIterator() {
		return cars.iterator();
	}

	/**
	 * Dodaje samochod do trasy (pierwszy dodany samochod bedzie samochodem gracza).
	 * @param car - dodawany samochod
	 */
	public void addCar( Car car ) {
		cars.add( car );
		objects.add( car );
		cars_added++;
	}

	/**
	 * Pozwala uzyskac dostep do danych samochodu o podanym id.
	 * @param id Identyfikator samochodu.
	 * @return Referencja do obiektu opisujacego dane samochodu.
	 */
	public Car getCar( int id ) {
		return cars.get( id );
	}

	/**
	 * Pozwala sprawdzic sile tarcia w danym punkcie na mapie. Uwzgledniana takze opor powietrza dzialajacy na pojazd.
	 * @param objectsDrivingForce Sila dzialajaca na samochod, dla ktorego sprawdzana jest sila tarcia.
	 * @param coords Wspolrzedne punktu na mapie, dla ktorego sprawdzana jest sila tarcia.
	 * @return Wartosc sily tarcia w zadanym punkcie, w N. Zawsze zwracana jest wartosc nieujemna (chyba, ze powierzchnia odpycha
	 * pojazd zamiast zatrzymywac :P), tj. nie jest uwzgledniany zwrot sily.
	 */
	public float getFrictionForce( Car c, Vec2D coords ) {
		return
			1500f +												//tarcie
			(0.34f * c.velocityMagnitude * c.velocityMagnitude);//opor powietrza
	}
	
	
	
}
