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
	
	/**
	 * Tworzy nowy wektor o wartosci (0,0).
	 */
	public Vec2D() {
		set( 0f, 0f );
	}
	
	/**
	 * Tworzy nowy wektor o wartosci (x,y). 
	 * @param x Dlugosc x wektora.
	 * @param y Dlugosc y wektora.
	 */
	public Vec2D( float x, float y ) {
		set( x, y );
	}
	
	/**
	 * Tworzy nowy wektor jenostkowy obrocony o kat a.
	 * @param f Kat obrotu wektora (w radianach).
	 */
	public Vec2D( float a ) {
		x = 1f;
		y = 0f;
		rotate( a );
	}

	/**
	 * Tworzy nowy wektor obrocony o zadany kat wzgledem obecnego wektora.
	 * @param a Kat obrotu nowego wektora.
	 * @return Nowy wektor obrocony o zadany kat wzgledem macierzystego wektora.
	 */
	public Vec2D instantiateRotatedVector( float a ) {
		Vec2D v = new Vec2D( x, y );
		v.rotate( a );
		return v;
	}
	
	/**
	 * Obraca aktualny wektor o kat a.
	 * @param a Kat obrotu wektora (w radianach).
	 */
	public void rotate( float a ) {
		float old_x = x;
		float old_y = y;
		x = (float) (old_x * Math.cos( a ) - old_y * Math.sin( a ));
		y = (float) (old_x * Math.sin( a ) + old_y * Math.cos( a ));
	}
	
	public void set( float x, float y ) {
		this.setX(x);
		this.setY(y);
	}

	public void set( Vec2D velocity ) {
		this.x = velocity.x;
		this.y = velocity.y;
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

	public float magnitude() {
		return (float) Math.sqrt( x * x + y * y );
	}
	
	/**
	 * Pozwala zbadac nachylenia wektora do osi OX.
	 * @return Kat nachylenia wektora do osi OX.
	 */
	public float getAngle() {
		float angle = (float) Math.acos( x / magnitude() );
		if( x > 0 ) {
			//return angle;
			if( y >= 0 ) {
				return angle;
			} else {
				return 6.28f - angle;
			}
		} else if( x < 0 ) {
			if( y >= 0 ) {
				return angle;
			} else {
				return 6.28f - angle;
			}
		} else {
			if( y >= 0 ) return 0.5f * 3.14f;
				else return 0.75f * 6.28f;
		}
	}

	/**
	 * Pozwala zbadac nachylenia wektora do osi OX. Funkcja zoptymalizowana do pracy z
	 * wektorami jednostkowymi.
	 * @return Kat nachylenia wektora do osi OX.
	 */
	public float getAngleForUnitVector() {
		float angle = (float) Math.acos( x );
		if( x > 0 ) {
			//return angle;
			if( y >= 0 ) {
				return angle;
			} else {
				return 6.28f - angle;
			}
		} else if( x < 0 ) {
			if( y >= 0 ) {
				return angle;
			} else {
				return 6.28f - angle;
			}
		} else {
			if( y >= 0 ) return 0.5f * 3.14f;
				else return 0.75f * 6.28f;
		}
	}
	
}
