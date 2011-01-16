package com.games.racertc.gameplay.graphics;
import java.util.Iterator;
import java.util.ListIterator;

import com.games.racertc.R;
import com.games.racertc.R.drawable;
import com.games.racertc.objects.Car;
import com.games.racertc.objects.GameObject;
import com.games.racertc.other.Vec2D;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;

public class PresentationNighttime extends Presentation {
        
        protected final Paint multiplyPaint = new Paint();
        
        protected final Bitmap frontlights;
        
        protected Bitmap mapOfLights = null;
        
        protected final Canvas canvasOfLights = new Canvas();
        
        protected final int darknessColor = Color.rgb( 25, 25, 25 );
        
        /**
         * 
         * @param surfaceHolder
         * @param resources
         */
        public PresentationNighttime( SurfaceHolder surfaceHolder, Resources resources ) {
                super(surfaceHolder, resources);
                frontlights = BitmapFactory.decodeResource(
                                resources,
                                R.drawable.car_headlights
                );
                multiplyPaint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.MULTIPLY ) );
        }
        
    @Override
    public void setResolution( int width, int height ) {
            super.setResolution( width, height );
            if( mapOfLights != null ) mapOfLights.recycle();
            mapOfLights = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565 );
            canvasOfLights.setBitmap( mapOfLights );
    } 
        
        /**
         * Sprawdza, czy swiatla z reflektorow zadanego obiektu sa przynajmniej czesciowo widoczne
         * na ekranie.
         * @param obj Sprawdzany obiekt.
         * @param onScreenPosition wspolrzedne obiektu na ekranie.
         * @return <strong>true</strong>, jezeli swiatla obiektu jest widoczny; <strong>false</strong>
         * w przeciwnym wypadku.
         */
        protected boolean lightsOnScreen( GameObject obj, Vec2D onScreenPosition ) {
                final float obj_radius = 377f; //TODO: To chyba jest za malo;
                //sprawdzenie na osi X
                float tmp_var = onScreenPosition.getX();
                if( ((tmp_var + obj_radius) < 0) || ((tmp_var - obj_radius) > width) )
                        return false;
                //sprawdzenie na osi Y
                tmp_var = onScreenPosition.getY();
                if( ((tmp_var + obj_radius) < 0) || ((tmp_var - obj_radius) > height) )
                        return false;           
                return true;
        }
    
        /**
         * Rysuje rozgrywke oraz UI. Zarzadza dostepem do obiektu Canvas kontekstu
         * graficznego.
         */
        @Override
        public void drawGame( Canvas canvas ) {     
                //rysuje rozgrywke
                internalDrawGame( canvas );
                
                if( mapOfLights != null ) {
                
                        //przygotowuje mape swiatel:
                        mapOfLights.eraseColor( darknessColor );
                        
                        Iterator< Car > iter = track.getCarIterator();
                        Car obj;
                        while( iter.hasNext() ) {
                                obj = iter.next();
                                Vec2D screen_pos = getOnScreenPosition( obj, camPos );
                                if( lightsOnScreen( obj, screen_pos ) ) {//czy swiatla sa widoczne na ekranie?
                                        Matrix m = new Matrix();
                                                //krok II: przesuwa swiatla na wlasciwa pozycje na ekranie
                                        m.setTranslate( screen_pos.getX(), screen_pos.getY() );
                                                //krok I: obraca swiatlo tak, jak obrocony jest samochod
                                        m.preRotate( (float) Math.toDegrees( obj.getRotation() ) );
                                                //krok I: ustawia poczatek swiatla w srodku ukladu wspolrzednych
                                        m.preTranslate( -65f, -286f );
                                        canvasOfLights.drawBitmap( frontlights, m, null );
                                }
                        }
                        
                        //rysujemy mrok i swiatla:
                        canvas.drawBitmap( mapOfLights, 0f, 0f, multiplyPaint );
                
                }
                
                //na koniec rysuje UI:
                uiManager.drawUI( canvas );
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
