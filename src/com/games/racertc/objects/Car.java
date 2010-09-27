package com.games.racertc.objects;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.games.racertc.Presentation;
import com.games.racertc.other.Vec2D;

public class Car extends GameObject {

	/**	Przechowuje numer pojazdu gracza na liscie pojazdow. */
	public final static int CAR_PLAYER = 1;
	
	public Car( float radius, Drawable drawable ) {
		super( radius, drawable );
		// TODO Auto-generated constructor stub
	}

	public void acceptPresentation( Presentation presentation, Canvas canvas, Vec2D screenPos ) {
		presentation.drawGameObject( this, canvas, screenPos );
	}	
	
	/** Predkosc maksymalna. */
	private float speed;
	
	private float acceleration;
	
	private float grip;
	
	private float turn;
	
}
