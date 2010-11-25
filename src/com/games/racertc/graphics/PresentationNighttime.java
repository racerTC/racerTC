package com.games.racertc.graphics;

import com.games.racertc.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;

public class PresentationNighttime extends Presentation {

	protected Bitmap canvasBuffer = null;
	
	/**
	 * Canvas udajace glowne canvas przed metodami rysujacymi Presentation.
	 */
	protected final Canvas fakeCanvas;
	
	/**
	 * 
	 * @param surfaceHolder
	 * @param resources
	 */
	public PresentationNighttime( SurfaceHolder surfaceHolder, Resources resources ) {
		super(surfaceHolder, resources);
		fakeCanvas = new Canvas();
		lnd = BitmapFactory.decodeResource( resources, R.drawable.swiatlo_i_mrok );
	}

	@Override
	public void setResolution( int width, int height ) {
		super.setResolution( width, height );
		if( canvasBuffer != null ) canvasBuffer.recycle();
		canvasBuffer = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
		fakeCanvas.setBitmap( canvasBuffer );
	}	
	
	final Bitmap lnd;
	
	/**
	 * Rysuje rozgrywke oraz UI. Zarzadza dostepem do obiektu Canvas kontekstu
	 * graficznego.
	 */
	@Override
	public void drawGame() {
		//blokujemy canvas
		Canvas canvas = surfaceHolder.lockCanvas();
		if( canvasBuffer != null ) {
			LightingColorFilter lcf;
			//fakeCanvas.( lcf );
			
			//rysuje rozgrywke
			internalDrawGame( canvas );
			
			//wklejamy mrok:
			Paint p = new Paint();
			p.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.MULTIPLY ) );
			canvas.drawBitmap( lnd, 0f, 0f, p );
			
			
			//dodajemy efekt nocy
			//applyDarkness( canvasBuffer );
			
			//wklejamy 'mroczny krajobraz' na wlasciwy canvas:
			//canvas.drawBitmap( canvasBuffer, 0f, 0f, null );
			
			//na koniec rysuje UI:
			uiManager.drawUI( canvas );
		} else
			super.internalDrawGame( canvas );
		
		//wysylamy zmiany na ekran:
		surfaceHolder.unlockCanvasAndPost( canvas );
		
	}	
	
	
	protected void applyDarkness( Bitmap bitmap ) {
		for( int i = 0; i<width; i++ )
			for( int j = 0; j<height; j++ ) {
				int color = bitmap.getPixel( i, j );
				int r = (int) ((float) Color.red( color ) * 0.1f);
				int g = (int) ((float) Color.green( color ) * 1f);
				int b = (int) ((float) Color.blue( color ) * 0.1f);
				bitmap.setPixel( i, j, Color.rgb( r, g, b ) );
			}
	}
	
}
