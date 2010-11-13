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

public class TwoSlidersSingleTouchUI extends UIManager {

	/** Tajemnicze obiekty graficzne. */
	final Bitmap galka;
	final Bitmap leftRight;
	final Bitmap upDown;
	
	final float lrWidth = 287f;
	final float lrHeight = 53f;

	final float udWidth = 53f;
	final float udHeight = 227f;	
	
	/* Polowa szerokosci 'martwego pola' strzalek. */
	final float deadZone = 20f;
	
	final float lrSensitiveZone = (lrWidth - 1f) * 0.5f - deadZone;
	final float udSensitiveZone = (udHeight - 1f) * 0.5f - deadZone;
	
	/** Polozenie kontrolera lewo-prawo: */
	float
		lrPosX = width - 287f - 10f,
		lrPosY = height - 53f - 10f;
	
	float
		lrCenterX = lrPosX + (lrWidth * 0.5f),
		lrCenterY = lrPosY + (lrHeight * 0.5f);	
	
	/** Polozenie kontrolera gora-dol: */
	final float
		udPosY = 10f,
		udPosX = 10f;	
	
	final float
		udCenterX = udPosX + (udWidth * 0.5f),
		udCenterY = udPosY + (udHeight * 0.5f);
	
	float
		lrGalkaX = 0f;
	
	float
		udGalkaY = udCenterY;
	
	float storedUD = 0f;
	int storedUDFlag = Message.FLAG_NONE;
	
	public TwoSlidersSingleTouchUI( Resources resources ) {
		super();
		this.galka = BitmapFactory.decodeResource( resources, R.drawable.ui_point_selection );
		this.leftRight = BitmapFactory.decodeResource( resources, R.drawable.ui_left_right );
		this.upDown = BitmapFactory.decodeResource( resources, R.drawable.ui_up_down );
	}
	
	@Override
	public void setResolution( int width, int height ) {
		super.setResolution( width, height );
		lrPosX = width - lrWidth - 10f;
		lrPosY = height - lrHeight - 10f;
		lrCenterX = lrPosX + (lrWidth * 0.5f);
		lrCenterY = lrPosY + (lrHeight * 0.5f);
		lrGalkaX = lrCenterX;
	}	
	
/*-----------------------------------------*/
/*-           Obsluga wejscia:            -*/
/*-----------------------------------------*/		
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) {

		//zerujemy flagi:
		int flags = Message.FLAG_NONE;
		float fwdUsage = storedUD;
		float sideUsage = 0f;
		
		switch( event.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			//najpierw sprawdza, gdzie nastapilo dotkniecie:
			float touch_x = event.getX();
			float touch_y = event.getY();
			boolean send_msg = false;
			/* Kontroler lewo - prawo: */
			if(
					isInRange( touch_x, lrPosX, lrPosX + lrWidth ) &&
					isInRange( touch_y, lrPosY, lrPosY + lrHeight )
			) {
				flags |= storedUDFlag;
				send_msg = true;
				//dotknieto skrecacza lewo-prawo:
				float off = touch_x - lrCenterX;
				if( off < - deadZone ) {
					flags |= Message.FLAG_LEFT;
					sideUsage = - (off + deadZone) / lrSensitiveZone;
				} else if( off > deadZone ) {
					flags |= Message.FLAG_RIGHT;
					sideUsage = (off - deadZone) / lrSensitiveZone;
				} else
					sideUsage = 0f;
				//zapisuje wychylenie galki:
				lrGalkaX = lrCenterX + off;
			} else
			/* Kontroler gora - dol: */
			if(
					isInRange( touch_x, udPosX, udPosX + udWidth ) &&
					isInRange( touch_y, udPosY, udPosY + udHeight )
			) {
				send_msg = true;
				//dotknieto przyspieszacza gora-dol:
				float off = touch_y - udCenterY;
				if( off < - deadZone ) {
					storedUDFlag = Message.FLAG_UP;
					flags |= storedUDFlag;
					fwdUsage = - (off + deadZone) / udSensitiveZone;
				} else if( off > deadZone ) {
					storedUDFlag = Message.FLAG_DOWN;
					flags |= storedUDFlag;
					fwdUsage = (off - deadZone) / udSensitiveZone;
				} else {
					fwdUsage = 0f;
					storedUDFlag = Message.FLAG_NONE;
				}
				//zapisuje wychylenie galki:
				udGalkaY = udCenterY + off;
			}
			/* Jesli trzeba wysyla stosowna wiadomosc: */
			if( send_msg ) {
				messageQueue.push(
						messageFactory.createMovementMessage(
								Car.CAR_PLAYER,
								flags,
								sideUsage,
								fwdUsage
						)
				);
				storedUD = fwdUsage;
			}
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			flags = storedUDFlag;
			lrGalkaX = lrCenterX;
			messageQueue.push(
					messageFactory.createMovementMessage( Car.CAR_PLAYER, flags, 0f, storedUD )
			);
			break;
		}
		
		return true;
	}

/*-----------------------------------------*/
/*-            Rysowanie UI:              -*/
/*-----------------------------------------*/	
	
	@Override
	public void drawUI( Canvas canvas ) {	
		/* Rysuje: */
		canvas.drawBitmap( leftRight, lrPosX, lrPosY, null );
		canvas.drawBitmap( upDown, udPosX, udPosY, null );
		canvas.drawBitmap( galka, lrGalkaX - 4.5f, lrCenterY - 6.5f, null );
		canvas.drawBitmap( galka, udCenterX - 4.5f, udGalkaY - 5f, null );
	}

}
