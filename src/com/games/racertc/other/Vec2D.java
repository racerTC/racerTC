package com.games.racertc.other;

/**
 * Wektor dwuwymiarowy liczb zmiennoprzecinkowych.
 */
public class Vec2D {

	private float x;
	
	private float y;
	
	/** Wspolczynnik konwersji metrow na piksele. */
	public static final float PIXELS_PER_METER = 10.0f;
	
	/**
	 * Dokonuje konwersji wektora reprezentujacego odleglosc w metrach na wektor
	 * okreslajacy odleglosc w pikselach. Operacja wykonywana jest na kopii obiektu.
	 * @return Wektor reprezentujacy odleglosc w pikselach.
	 */
	public Vec2D toPx() {
		return new Vec2D(
				PIXELS_PER_METER * getX(),
				PIXELS_PER_METER * getY()
		);
	}	
	
	public Vec2D() {
		set( 0f, 0f );
	}
	
	public Vec2D( float x, float y ) {
		set( x, y );
	}
	
	public void set( float x, float y ) {
		this.setX(x);
		this.setY(y);
	}

	public void setX( float x ) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY( float y ) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void add( float x, float y ) {
		this.x += x;
		this.y += y;
	}
	
}
