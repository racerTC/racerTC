package com.games.racertc.ui;

import com.games.racertc.R;
import com.games.racertc.messages.Message;
import com.games.racertc.objects.Car;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Podstawowe UI nie wykorzystujace technologii multitouch (jedyne UI dla API level < 5).
 */
public class SingleTouchUI extends UIManager {

	/** Tajemnicze obiekty graficzne. */
	final Bitmap galka;
	final Bitmap kolko;
	
	/** Tajemnicze parametry tajemniczych obiektow graficznych. */
	float xoffset = 0f;
	float yoffset = 0f;
	
	public SingleTouchUI( Resources resources ) {
		super();
		this.galka = BitmapFactory.decodeResource(resources, R.drawable.left_right_point_selection);
		this.kolko = BitmapFactory.decodeResource(resources, R.drawable.gear_lever);
	}
	
/*-----------------------------------------*/
/*-           Obsluga wejscia:            -*/
/*-----------------------------------------*/	
	
	/** Przechowuje ostatni stan flag ruchu. */
	private int movement_flags = Message.FLAG_NONE;
	
	private boolean allow_input = false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		//Analiza dotkniecia:
		switch( event.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
			if( !((x > 10f) && ( y > 10f ) && ( x < 70f ) && ( y < 70f )) )
				return true;
			else allow_input = true;
		case MotionEvent.ACTION_MOVE:
			if( !allow_input ) return true;
			
			int flags = Message.FLAG_NONE;
			
			if( x < 30f ) { //skrecamy w lewo
				flags |= Message.FLAG_LEFT;
			} else if( x > 50f ) { //skrecamy w prawo
				flags |= Message.FLAG_RIGHT;
			} else {} //jedziemy prosto
			
			if( y < 35f ) { //jedziemy do przodu
				flags |= Message.FLAG_UP;
			} else if( y > 45f ) { //jedziemy do tylu
				flags |= Message.FLAG_DOWN;
			}
			
			if( movement_flags != flags ) {
				messageQueue.push( messageFactory.createMovementMessage( Car.CAR_PLAYER, flags ) );
				movement_flags = flags;
			}
			
			//ograniczenie wychylenia galki:
			if( x < 10f ) x = 10f;
			if( y < 10f ) y = 10f;
			if( x > 70f ) x = 70f;
			if( y > 70f ) y = 70f;
			
			xoffset = x - 40f; yoffset = y - 40f;
			
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			xoffset = 0f; yoffset = 0f;
			allow_input = false;
			movement_flags = Message.FLAG_NONE;
			messageQueue.push(
					messageFactory.createMovementMessage( Car.CAR_PLAYER, Message.FLAG_NONE )
			);
			break;
		}
		
		return true;
	}

/*-----------------------------------------*/
/*-            Rysowanie UI:              -*/
/*-----------------------------------------*/

	@Override
	public void drawUI(Canvas canvas) {
		int gpx = 40;
		int gpy = 40;
		synchronized( this ) {
			gpx += xoffset;
			gpy += yoffset;
		}
		canvas.drawBitmap( galka, gpx, gpy, null );
	}

}
