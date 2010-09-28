package com.games.racertc.objects;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.games.racertc.Presentation;
import com.games.racertc.messages.Message;
import com.games.racertc.other.Vec2D;

public class Car extends GameObject {

	/**	Przechowuje numer pojazdu gracza na liscie pojazdow. */
	public final static int CAR_PLAYER = 0;
	
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
	
	///
	
	private float velocity;
	
	public float getVelocity() { return velocity; };
	
	public void setVelocity( float velocity ) { this.velocity = velocity; };
	
	/**
	 * Flagi okreslajace aktualne zachowanie samochodu.
	 */
	private int behaviourFlags;

	public void updateBehaviour( Message m ) {
		behaviourFlags &= m.getMask(); //usuwa stare flagi
		behaviourFlags |= m.getFlags(); //aplikuje nowe flagi
	}
	
	public int getBehaviourFlags() {
		return behaviourFlags;
	}
	
}
