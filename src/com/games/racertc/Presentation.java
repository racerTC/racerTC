package com.games.racertc;

import java.util.ListIterator;

import com.games.racertc.objects.Car;
import com.games.racertc.objects.GameObject;
import com.games.racertc.other.Vec2D;
import com.games.racertc.tracks.Track;
import com.games.racertc.ui.UIManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Klasa odpowiadajaca za rysowanie gry.
 */
public class Presentation {

	/** Uchwyt do SurfaceHolder. */
	private final SurfaceHolder surfaceHolder;
	
	/** Uchwyt do zasobow. */
	private final Resources resources;
	
	private UIManager uiManager;
	
	/** Szerokosc ekranu. */
	int width = 1024;
	/** Wysokosc ekranu. */
	int height = 768;
	
	/** Polowa szerokosci ekranu (zaokraglana w dol). */
	int halfWidth;
	/** Polowa wysokosci ekranu (zaokraglana w dol). */
	int halfHeight;
	
	/** Aktualnie aktywna trasa. */
	private Track track;
	
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
		//trackBitmap = track.getTrackGraphics();
	}

	/**
	 * Ustawia menadzera interface'u uzytkownika wykorzystywanego do rysowania UI.
	 * @param uiManager Menadzer UI, ktory bedzie od teraz uzywany.
	 */
	public void setUIManager( UIManager uiManager ) {
		this.uiManager = uiManager;
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
	private Vec2D getCameraPosition( GameObject stickToObject ) {
		Vec2D pos = stickToObject.getPosition().toPx();
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		
		//jesli kamera zbyt blisko lewej krawedzi
		if( x < halfWidth )
			pos.setX( (float) halfWidth );
		
		//jesli kamera zbyt blisko prawej krawedzi
		else if( x > track.getWidth() - halfWidth )
			pos.setX( (float) (track.getWidth() - halfWidth) );
		
		//jesli kamera zbyt blisko gornej krawedzi
		if( y < halfHeight )
			pos.setY( (float) halfHeight );
		
		//jesli kamera zbyt blisko dolnej krawedzi
		else if( y > track.getHeight() - halfHeight )
			pos.setY( (float) (track.getHeight() - halfHeight) );
		
		//zwracamy obliczone koordynaty
		return pos;
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
	private Vec2D getOnScreenPosition( GameObject obj, Vec2D camPos ) {
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
	private boolean isOnScreen( GameObject obj, Vec2D onScreenPosition ) {
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
/*-              Rysowanie:               -*/
/*-----------------------------------------*/
	
	public void drawGame() {
		Canvas canvas = surfaceHolder.lockCanvas();
		
		ListIterator< GameObject > iter = track.getGameObjectIterator();
		
		//pobieramy pojazd gracza
		GameObject obj = iter.next();
		
		//obliczamy nad jakim punktem mapy bedzie znajdowac sie kamera:
		Vec2D cam_pos = getCameraPosition( obj );
		
		//rysujemy wycinek mapy (tlo):
		drawTrackFragment( canvas, cam_pos );
		
		//rysujemy samochod gracza:
		obj.acceptPresentation( this, canvas, getOnScreenPosition( obj, cam_pos ) );
		
		//rysujemy pozostale obiekty:
		while( iter.hasNext() ) {
			obj = iter.next();
			Vec2D screen_pos = getOnScreenPosition( obj, cam_pos );
			if( isOnScreen( obj, screen_pos ) ) //czy jest na ekranie?
				obj.acceptPresentation( this, canvas, screen_pos ); //jezeli tak, to rysujemy
		}
		
		//na koniec rysuje UI:
		uiManager.drawUI( canvas );
		
		surfaceHolder.unlockCanvasAndPost( canvas );
	}
	
	/**
	 * Wypelnia ekran fragmentem trasy obserwowanym przez kamere znajdujaca sie
	 * nad zadanymi wspolrzednymi trasy.
	 * @param canvas Obiekt Canvas na ktorym bedzie wykonywane rysowanie
	 * @param camPos Wspolrzedne trasy, nad ktorymi zawieszona jest kamera
	 */
	private void drawTrackFragment( Canvas canvas, Vec2D camPos ) {
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
			//krok II: obraca samochod
		canvas.rotate( car.getRotation() );
			//krok I: przesuwa samochod na wlasciwa pozycje na ekranie
		canvas.translate( screenPos.getX(), screenPos.getY() );
		
		//rysuje samochod
		car.getDrawable().draw( canvas );
		
		//przywraca macierz przekrztalcen
		canvas.restore();
		
	}
	
}
