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
 * Do sterowania wykorzystywany jest joystick, ktorego okreslenie okresla przyspieszenie
 * i skrety pojazdu.
 */
public class JoystickSingleTouchUI extends UIManager {

	/** Tajemnicze obiekty graficzne. */
	final Bitmap galka;
	final Bitmap joystickIdle;
	final Bitmap joystickFwd;
	final Bitmap joystickRev;
	
	/** Odleglosc joysticka od krawedzi ekranu. */
	final float treshold = 10f;
		
	/** Tajemnicze parametry tajemniczych obiektow graficznych. */
	float xoffset = 0f;
	float yoffset = 0f;
	
	public JoystickSingleTouchUI( Resources resources ) {
		super();
		this.galka = BitmapFactory.decodeResource( resources, R.drawable.ui_point_selection );
		this.joystickIdle = BitmapFactory.decodeResource( resources, R.drawable.ui_joystick_idle );
		this.joystickFwd = BitmapFactory.decodeResource( resources, R.drawable.ui_joystick_fwd );
		this.joystickRev = BitmapFactory.decodeResource( resources, R.drawable.ui_joystick_rev );
		
	}
	
/*-----------------------------------------*/
/*-           Obsluga wejscia:            -*/
/*-----------------------------------------*/	
	
	/** Przechowuje ostatni stan flag ruchu. */
	private int movement_flags = Message.FLAG_NONE;
	
	private float fwdUsage = 0f;
	
	private float sideUsage = 0f;
	
	private boolean allow_input = false;
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) {
		float x = event.getX();
		float y = event.getY();
		
		final float LeftUpX = width - treshold - 113f;
		final float LeftUpY = height - treshold - 113f;
		final float RightDownX = width - treshold;
		final float RightDownY = height - treshold;
		
		//Analiza dotkniecia:
		switch( event.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
			if( !(
					( x > LeftUpX ) &&
					( y > LeftUpY ) &&
					( x < RightDownX ) && 
					( y < RightDownY )
				) ) return true;
			else allow_input = true;
		case MotionEvent.ACTION_MOVE:
			if( !allow_input ) return true;
			
			//ograniczenie wychylenia galki:
			if( x < LeftUpX ) x = LeftUpX;
			if( y < LeftUpY ) y = LeftUpY;
			if( x > RightDownX ) x = RightDownX;
			if( y > RightDownY ) y = RightDownY;
			
			//zerujemy flagi:
			int flags = Message.FLAG_NONE;
			float fwdUsage = 0f;
			float sideUsage = 0f;
			
			if( x < LeftUpX + 50f ) { //skrecamy w lewo
				flags |= Message.FLAG_LEFT;
				sideUsage = ( ((LeftUpX + 50f) - x) * 0.02f * 0.6f ) + (0.4f * 50f * 0.02f );
			} else if( x > LeftUpX + 13f + 50f ) { //skrecamy w prawo
				flags |= Message.FLAG_RIGHT;
				sideUsage = ( (x - (LeftUpX + 13f + 50f)) * 0.02f * 0.6f ) + (0.4f * 50f * 0.02f );
			} else {} //jedziemy prosto
			 	
			if( y < LeftUpY + 50f ) { //jedziemy do przodu
				flags |= Message.FLAG_UP;
				fwdUsage = ( ((LeftUpY + 50f) - y) * 0.02f * 0.75f ) + (0.25f * 50f * 0.02f );
			} else if( y > LeftUpY + 50f + 13f ) { //jedziemy do tylu
				flags |= Message.FLAG_DOWN;
				fwdUsage = ( (y - (LeftUpY + 50f + 13f)) * 0.02f * 0.75f ) + (0.25f * 50f * 0.02f );
			}
			
			if(
					(movement_flags != flags) ||
					(fwdUsage != this.fwdUsage) ||
					(sideUsage != this.sideUsage)
			) {
				messageQueue.push( messageFactory.createMovementMessage( Car.CAR_PLAYER, flags, sideUsage, fwdUsage ) );
				movement_flags = flags;
				this.sideUsage = sideUsage;
				this.fwdUsage = fwdUsage;
			}
			
			synchronized( this ) {
				xoffset = x - 57f - LeftUpX;
				yoffset = y - 57f - LeftUpY;
			}
			
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			xoffset = 0f; yoffset = 0f;
			allow_input = false;
			movement_flags = Message.FLAG_NONE;
			messageQueue.push(
					messageFactory.createMovementMessage( Car.CAR_PLAYER, Message.FLAG_NONE, 0f, 0f )
			);
			break;
		}
		
		return true;
	}

/*-----------------------------------------*/
/*-            Rysowanie UI:              -*/
/*-----------------------------------------*/

	@Override
	public void drawUI(Canvas canvas) { //113
		float gpx = width - treshold - 57f - 5f;
		float gpy = height - treshold - 57f - 5f;
		synchronized( this ) {
			gpx += xoffset;
			gpy += yoffset;
		}
		
		if( !allow_input ) {
			canvas.drawBitmap( joystickIdle , width - treshold - 113f, height - treshold - 113f, null );
		} else {
			canvas.drawBitmap( joystickFwd , width - treshold - 113f, height - treshold - 113f, null );
		}
		canvas.drawBitmap( galka, gpx, gpy, null );
	}

}
