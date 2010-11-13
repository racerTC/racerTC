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
	
/*-----------------------------------------*/
/*-             Stan pojazdu:             -*/
/*-----------------------------------------*/		
	

	
/*-----------------------------------------*/
/*-          Parametry jazdy:             -*/
/*-----------------------------------------*/		
	
	/**
	 * Wektor jednostkowy predkosci pojazdu. Gdy nie ma poslizgu, okresla rowniez przod pojazdu.
	 */
	public Vec2D velocity = new Vec2D( 0f );
	
	/**
	 * Dlugosc wektora predkosci.
	 */
	public float velocityMagnitude = 0f;
	
	public final float maxVelocity = 1f;
	
	public final float maxReversedVelocity = - 0.2f * maxVelocity;
	
	/**
	 * Okresla mase pojazdu w kg.
	 */
	public final float mass = 1280f;
	
	/**
	 * Okresla maksymalna sile napedzajaca pojazd w N
	 */
	public final float maxDrivingForce = 15000f;
	
	/** Maksyma szybkosc samochodu. */
	public final float maxSpeed = 1f;	
	
	/** Maksyma szybkosc samochodu. */
	public final float maxReversedSpeed = - 0.3f * maxSpeed;		
	
	/**
	 * Zadana predkosc pojazdu wzgledem predkosci maksymalnej, w zakresie [0..1]
	 */
	public float requestedSpeed = 0f;
	
	/**
	 * 
	 */
	public final float maxBrakingForce = 50000f;
	
	/** Maksymalny kat skretu samochodu - w radianach na sekunde. */
	public float maxTurningAngle = (float) Math.toRadians( 120f );
	
	/** Ile stopni w kazdej klatce samochod bedzie skrecal bardziej niz w poprzedniej. */
	//public float turningAngleStep = (float) Math.toRadians( 0.4f );
	
	/** Obecny kat skretu samochodu. */
	//public float currentTurningAngle = 0f;
	
	public float requestedTurningAngle = 0f;
	
	/** Przyczepnosc. */
	//public float adhesion = 1f;
	
	//public float adhesionSide = 1f;
	

	
	/**
	 * Flagi okreslajace aktualne zachowanie samochodu.
	 */
	private int behaviourFlags;

	public void updateBehaviour( Message m ) {
		behaviourFlags &= m.getMask(); //usuwa stare flagi
		behaviourFlags |= m.getFlags(); //aplikuje nowe flagi
		this.requestedSpeed = m.yAxisUsage;
		this.requestedTurningAngle = m.xAxisUsage;
	}
	
	public int getBehaviourFlags() {
		return behaviourFlags;
	}
	
	public float getRotation() {
		return velocity.getAngleForUnitVector();
	}
	
}
