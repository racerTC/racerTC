package com.games.racertc.objects;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.games.racertc.Presentation;
import com.games.racertc.other.Vec2D;

/**
 * Ogolna klasa reprezentujaca dowolny obiekt znajdujacy sie na torze.
 */
public abstract class GameObject {

	protected Vec2D velocity;

	private final float radius;
	
	private final float imgRadius;
	
	private final Drawable drawable;
	
	public GameObject( float radius, Drawable drawable ) {
		//zapisuje zmienne:
		this.radius = radius;
		this.drawable = drawable;
		this.imgRadius = radius * Vec2D.PIXELS_PER_METER;
		//ustawia BoundingBox drawabla
		//XXX: sposob rysowania wymaga jeszcze glebokiego przemyslenia
		float intrW = (float) drawable.getIntrinsicWidth();
		float intrH = (float) drawable.getIntrinsicHeight();
		drawable.setBounds(
				(int) Math.floor( -0.5f*intrW ),//left 
				(int) Math.floor( -0.5f*intrH ),//top
				(int) Math.ceil( 0.5f*intrW ),//right
				(int) Math.ceil( 0.5f*intrH )//bottom
		);
	}
	
	/** Pozycja obiektu (jego srodek) na planszy. */
	protected Vec2D position;
	
	public Vec2D getPosition() {
		return position;
	}

	public void setPosition( Vec2D position ) {
		this.position = position;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getImageRadius() {
		return imgRadius;
	}
	
	/**
	 * Pozwala pobrac Drawable obiektu.
	 * @return Zwraca Drawable obiektu.
	 */
	public Drawable getDrawable() {
		return drawable;
	}
	
	/**
	 * Pozwala sprawdzic kat obrotu pojazdu.
	 * @return Kat pod jakim obrocony jest obiekt.
	 */
	public float getRotation() {
		return 0f; //TODO: Obliczanie kata obrotu pojazdu
	}
	
	/**
	 * Rysuje obiekt na ekranie.
	 * @param presentation Uzyty do rysowania obiekt Presentation.
	 * @param canvas Canvas na ktorym obiekt zostanie namalowany.
	 * @param screenPos Wspolrzedne piksela, w ktorym znajduje sie srodek obiektu.
	 */
	public abstract void acceptPresentation( Presentation presentation, Canvas canvas, Vec2D screenPos );
	
	/**
	 * Dokonuje prostego testu kolizji opartego na przecinaniu sie okregow utworzonych wokol
	 * srodkow testowanych obiektow.
	 * @param anotherObject Obiekt, ktory bedzie testowany z obecnym obiektem.
	 * @return Zwraca <strong>true</strong>, jezeli kolizja zachodzi, lub <strong>false</strong>
	 * w przeciwnym wypadku.
	 */
	public boolean intersects( GameObject anotherObject ) {
		//TODO: napsiac
		return false;
	}
	
}
