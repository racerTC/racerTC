package com.games.racertc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class games extends Activity {
		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new Panel(this));
		}

		class Panel extends View {
		public Panel(Context context) {
		super(context);
		}

		@Override
		public void onDraw(Canvas canvas) {
		Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.trasa_01);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(_scratch, 0, 0, null);
		Bitmap _scratch2 = BitmapFactory.decodeResource(getResources(), R.drawable.car_red);
		canvas.drawBitmap(_scratch2, 150, 180, null);
		Bitmap _scratch3 = BitmapFactory.decodeResource(getResources(), R.drawable.car_white);
		canvas.drawBitmap(_scratch3, 150, 190, null);
		Bitmap _scratch4 = BitmapFactory.decodeResource(getResources(), R.drawable.left);
		canvas.drawBitmap(_scratch4, 10, 410, null);
		Bitmap _scratch5 = BitmapFactory.decodeResource(getResources(), R.drawable.right);
		canvas.drawBitmap(_scratch5, 120, 410, null);
		Bitmap _scratch6 = BitmapFactory.decodeResource(getResources(), R.drawable.up);
		canvas.drawBitmap(_scratch6, 270, 290, null);
		Bitmap _scratch7 = BitmapFactory.decodeResource(getResources(), R.drawable.down);
		canvas.drawBitmap(_scratch7, 270, 380, null);
		}
		}
		}