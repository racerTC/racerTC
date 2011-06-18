package com.games.racertc.gameplay.graphics;

import java.util.ListIterator;

import com.games.racertc.objects.Car;
import com.games.racertc.objects.GameObject;
import com.games.racertc.tracks.Track;
import com.games.racertc.ui.UIManager;
import com.games.racertc.utility.Vec2D;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Klasa odpowiadajaca za rysowanie gry.
 * @author Piotr Balut
 */
public class Presentation {

	/** Uchwyt do SurfaceHolder. */
	protected final SurfaceHolder surfaceHolder;
	
	/** Uchwyt do zasobow. */
	protected final Resources resources;
	
	protected UIManager uiManager;
	
	Vec2D camPos = new Vec2D();
	
	/** Szerokosc ekranu. */
	protected int width = 1024;
	/** Wysokosc ekranu. */
	protected int height = 768;
	
	/** Polowa szerokosci ekranu (zaokraglana w dol). */
	protected int halfWidth;
	/** Polowa wysokosci ekranu (zaokraglana w dol). */
	protected int halfHeight;
	
	/** Aktualnie aktywna trasa. */
	protected Track track;
	
	//private Bitmap trackBitmap;
	
	public Presentation( SurfaceHolder surfaceHolder, Resources resources ) {
		this.surfaceHolder = surfaceHolder;
		this.resources = resources;
	}
	
	/**
	 * Przygotowuje Presentation do pracy. Powinno byc wolane kazdorazowo przed rozpoczeciem
	 * nowej rozgrywki.
	 * @param track Trasa na ktorej odbywac sie bedzie rozgrywka.
	 */
	public void initialise( Track track ) {
		this.track = track;
	}

	/**
	 * Ustawia menadzera interface'u uzytkownika wykorzystywanego do rysowania UI.
	 * @param uiManager Menadzer UI, ktory bedzie od teraz uzywany.
	 */
	public void setUIManager( UIManager uiManager ) {
		this.uiManager = uiManager;
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}
	
	public void setResolution( int width, int height ) {
		this.width = width;
		this.height = height;
		//oblicza polowy wysokosci i szerokosci - aby zaoszczedzic
		//obliczen pozniej
		halfWidth = ( width >> 1 );
		halfHeight = ( height >> 1 );
	}	
	
/*-----------------------------------------*/
/*-             Obliczenia:               -*/
/*-----------------------------------------*/		
	
	/**
	 * Metoda pozwalajaca obliczyc pozycje kamery zawieszonej nad zadanym obiektem. Jezeli
	 * obiekt znajduje sie na krawedzi trasy, zwrocone zostanie taka wartosc pozycji kamery,
	 * aby nie wysuwala sie ona poza trase.
	 * @param stickToObject Obiekt do ktorego "przyczepiona" jest kamera.
	 * @return Wspolrzedne piksela trasy nad ktorym zawieszona jest kamera.
	 */
	protected void calculateCameraPosition( GameObject stickToObject ) {
		camPos = stickToObject.getPosition().toPx();
		int x = (int) camPos.getX();
		int y = (int) camPos.getY();
		
		//jesli kamera zbyt blisko lewej krawedzi
		if( x < halfWidth )
			camPos.setX( (float) halfWidth );
		
		//jesli kamera zbyt blisko prawej krawedzi
		else if( x > track.getWidth() - halfWidth )
			camPos.setX( (float) (track.getWidth() - halfWidth) );
		
		//jesli kamera zbyt blisko gornej krawedzi
		if( y < halfHeight )
			camPos.setY( (float) halfHeight );
		
		//jesli kamera zbyt blisko dolnej krawedzi
		else if( y > track.getHeight() - halfHeight )
			camPos.setY( (float) (track.getHeight() - halfHeight) );
		
	}
	
	/**
	 * Podaje pozycje srodka obiektu na wyswietlaczu.<br>
	 * <br>
	 * <strong>UWAGA!</strong> Nie sprawdza, czy obiekt
	 * faktycznie jest widoczny - ta metoda sluzy _tylko_ do obliczenia wspolrzednych,
	 * ktore to moga byc mniejsze od zera, lub przekraczac rozdzielczosc ekranu (co
	 * moze tez oznaczac, ze obiekt widoczny jest czesciowo). Testowanie widocznosci
	 * mozna przeprowadzic za pomoca funkcji isOnScreen.
	 * @param obj Obiekt, ktorego wspolrzedne zostana obliczone.
	 * @param camPos Punkt trasy, ktory obserwuje kamera.
	 * @return Wspolrzedne srodka obiektu na wyswietlaczu.
	 */
	protected Vec2D getOnScreenPosition( GameObject obj, Vec2D camPos ) {
		//pobiera pozycje obiektu:
		Vec2D obj_pos = obj.getPosition().toPx();
		//oblicza przesuniecie obiektu od srodka ekranu:
		obj_pos.set(
				obj_pos.getX() - camPos.getX(),
				obj_pos.getY() - camPos.getY()
		); //TODO: a jakby tak zrobic Vec2I.substract( Vec2I )?
		//ale tak naprawde teraz mamy przesuniecie wzgledem punktu (0,0)
		//totez przesuwamy obiekt tak, aby faktycznie znalazl sie tam
		//gdzie trzeba na ekranie
		obj_pos.add( halfWidth, halfHeight );
		return obj_pos;
	}
	
	/**
	 * Sprawdza, czy zadany obiekt jest przynajmniej czesciowo widoczny na ekranie.
	 * @param obj Sprawdzany obiekt.
	 * @param onScreenPosition wspolrzedne obiektu na ekranie.
	 * @return <strong>true</strong>, jezeli obiekt jest widoczny; <strong>false</strong>
	 * w przeciwnym wypadku.
	 */
	protected boolean isOnScreen( GameObject obj, Vec2D onScreenPosition ) {
		final float obj_radius = obj.getImageRadius();
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
	
/*-----------------------------------------*/
/*       Rysowanie - f. publiczne:        -*/
/*-----------------------------------------*/	
	
	/**
	 * Rysuje rozgrywke oraz UI.
	 */
	public void drawGame( Canvas canvas ) {
		//rysuje rozgrywke
		internalDrawGame( canvas );
		
		//na koniec rysuje UI:
		uiManager.drawUI( canvas );
		
	}
	
/*-----------------------------------------*/
/*-              Rysowanie:               -*/
/*-----------------------------------------*/
	
	/**
	 * Rysuje rozgrywke.
	 */
	protected void internalDrawGame( Canvas canvas ) {
		ListIterator< GameObject > iter = track.getGameObjectIterator();
		
		//pobieramy pojazd gracza
		GameObject obj = iter.next();
		
		//obliczamy nad jakim punktem mapy bedzie znajdowac sie kamera:
		calculateCameraPosition( obj );
		
		//rysujemy wycinek mapy (tlo):
		drawTrackFragment( canvas, camPos );
		
		//rysujemy samochod gracza:
		obj.acceptPresentation( this, canvas, getOnScreenPosition( obj, camPos ) );
		
		//rysujemy pozostale obiekty:
		while( iter.hasNext() ) {
			obj = iter.next();
			Vec2D screen_pos = getOnScreenPosition( obj, camPos );
			if( isOnScreen( obj, screen_pos ) ) //czy jest na ekranie?
				obj.acceptPresentation( this, canvas, screen_pos ); //jezeli tak, to rysujemy
		}
	}
	
	/**
	 * Wypelnia ekran fragmentem trasy obserwowanym przez kamere znajdujaca sie
	 * nad zadanymi wspolrzednymi trasy.
	 * @param canvas Obiekt Canvas na ktorym bedzie wykonywane rysowanie
	 * @param camPos Wspolrzedne trasy, nad ktorymi zawieszona jest kamera
	 */
	protected void drawTrackFragment( Canvas canvas, Vec2D camPos ) {
		//XXX: co to znaczy "immutable bitmap"?! Prawdopodobnie kod nizej jest wysoce nieefektywny
		//poniewaz tworzone sa kopie bitmapy. Jezeli tak jest w istocie, trzeba by napisac funkcje
		//blit() kopiujaca bezposrednio piksele z bitmapy planszy na canvas (albo cos podobnego i wydajnego).
		//Bitmap fragment = Bitmap.createBitmap(
		//	track.getTrackGraphics(),
		//	200,200,//((int) camPos.getX()) - halfWidth,
			//((int) camPos.getY()) - halfHeight,
		//	250,250//width,
			//height
		//);
		Rect r = new Rect(
				((int) camPos.getX()) - halfWidth,
				((int) camPos.getY()) - halfHeight,
				((int) camPos.getX()) - halfWidth + width,
				((int) camPos.getY()) - halfHeight + height
			);
		//canvas.drawBitmap( fragment, 0, 0, null );
		try {
		canvas.drawBitmap(track.getTrackGraphics(), r, new Rect(0,0,width,height), null);
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Rysowanie obiektow - wersja przeciazana dla typu Car.<br><br>
	 * Oprocz rysowania samochodu dokonywana jest takze jego animacja.
	 * @param car Rysowany obiekt samochodu.
	 * @param canvas Canvas na ktorym rysowany jest obiekt.
	 * @param screenPos Wspolrzedne piksela w ktorych znajduje sie srodek samochodu.
	 */
	public void drawGameObject( Car car, Canvas canvas, Vec2D screenPos ) {
		//zapisuje macierz przekrztalcen
		canvas.save();
		//przygotowuje Canvas do rysowania samochodu:
			//krok II: przesuwa samochod na wlasciwa pozycje na ekranie
		canvas.translate( screenPos.getX(), screenPos.getY() );
			//krok I: obraca samochod
		canvas.rotate( (float) Math.toDegrees( car.getRotation() ) );
		
		//rysuje samochod
		car.getDrawable().draw( canvas );
		
		//przywraca macierz przekrztalcen
		canvas.restore();
		
	}
	
}
