package com.games.racertc;

import com.games.racertc.gamestate.GameStateChangeListener;
import com.games.racertc.gamestate.StateMachine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

/**
 * Klasa odpowiadajaca za rysowanie gry.
 */
public class Presentation {

	SurfaceHolder surfaceHolder;
	
	Resources resources;
	
	/* Szerokosc ekranu. */
	int width = 1024;
	/* Wysokosc ekranu. */
	int height = 768;
	
	public void setResulution( int width, int height ) {
		this.width = width;
		this.height = height;
	}
	
	public Presentation( SurfaceHolder surfaceHolder, Resources resources ) {
		this.surfaceHolder = surfaceHolder;
		this.resources = resources;
	}
	
	public void drawGame() {
		Canvas canvas = surfaceHolder.lockCanvas( null );
		
		Bitmap _scratch = BitmapFactory.decodeResource(resources, R.drawable.trasa_01);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(_scratch, 0, 0, null);
		Bitmap _scratch2 = BitmapFactory.decodeResource(resources, R.drawable.car_red);
		canvas.drawBitmap(_scratch2, 150, 180, null);
		Bitmap _scratch3 = BitmapFactory.decodeResource(resources, R.drawable.car_white);
		canvas.drawBitmap(_scratch3, 150, 190, null);
		Bitmap _scratch4 = BitmapFactory.decodeResource(resources, R.drawable.left);
		canvas.drawBitmap(_scratch4, 10, 410, null);
		Bitmap _scratch5 = BitmapFactory.decodeResource(resources, R.drawable.right);
		canvas.drawBitmap(_scratch5, 120, 410, null);
		Bitmap _scratch6 = BitmapFactory.decodeResource(resources, R.drawable.up);
		canvas.drawBitmap(_scratch6, 270, 290, null);
		Bitmap _scratch7 = BitmapFactory.decodeResource(resources, R.drawable.down);
		canvas.drawBitmap(_scratch7, 270, 380, null);
		
		surfaceHolder.unlockCanvasAndPost( canvas );
	}
	
}
