package com.games.racertc.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.games.racertc.Globals;
import com.games.racertc.R;
import com.games.racertc.messages.Message;
import com.games.racertc.utility.Vec2D;

public class SteeringWheelUI extends UIManager {	
	
	private SteeringWheel steeringWheel;
	private VerticalSlider verticalSlider;
	
	// Wartosc wychylenia kontrolera predkosci
	protected float storedUD = 0f;
	
	// Flagi ustawione przez kontroler predkosci
	protected int storedUDFlag = Message.FLAG_NONE;
	
	
	public SteeringWheelUI(Resources resources){
		super();
		
		steeringWheel=new SteeringWheel(resources, this);
		verticalSlider=new VerticalSlider(resources, this);
	}
	@Override
	public void setResolution(int width, int height) {
		super.setResolution(width, height);
		steeringWheel.onResolutionChanged(width, height);
		verticalSlider.onResolutionChanged(width, height);
	}
	
	@Override
	public void drawUI(Canvas canvas){
		steeringWheel.draw(canvas);
		verticalSlider.draw(canvas);
		
		if(Globals.MEASURE_FPS)
			super.drawUI(canvas);
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		PointF p=new PointF(event.getX(), event.getY());
		
		/*
		 * Sprawdzamy, czy zdarzenie nie powinno zostac przekazane kierownicy.
		 * Kiedy kierownica jest aktualnie obracana (isActive()==true), przekazujemy jej wszystkie zdarzenia, aby ulatwic sterowanie
		 */
		if(steeringWheel.isActive() || event.getAction()==MotionEvent.ACTION_DOWN && steeringWheel.contains(p))
			steeringWheel.handleEvent(event);
			
		
		// Sprawdzamy, cza zdarzenie powinno zostac przekazane sliderowi
		else if(verticalSlider.contains(p))
			verticalSlider.handleEvent(event);
			

		SystemClock.sleep(50);
		
		return true;
	}

}




/*
 * Klasa reprezentujaca kierownice
 */

class SteeringWheel{

	private SteeringWheelUI parent;
	
	private Bitmap bitmap;
	
	private final float maxRotation=300;
	
	// tutaj przechowujemy aktualny kat obrotu kierownicy
	private float rotation=0;		
	
	/* Wektor pomiedzy srodkiem kierownicy a miejscem, w ktorym uzytkownik dotyka aktualnie ekranu
	 * Bedzie potrzebny przy obliczania kata obrotu kierownicy 
	 */
	private Vec2D currentRotationVector; 
	
	// Promien kierownicy (moze byc dowolnie zmieniany)
	private float radius=70;
	
	private PointF onScreenPosition;

	
	
	SteeringWheel(Resources resources, SteeringWheelUI parent){
		this.parent=parent;
		
		onScreenPosition=new PointF(parent.width-radius-40, parent.height-radius-30);
		
		bitmap=BitmapFactory.decodeResource(resources, R.drawable.ui_wheel);
		
		// Skalujemy bitmape w zaleznosci od podanego promienia
		bitmap=Bitmap.createScaledBitmap(bitmap, (int)radius*2, (int)radius*2, true);
	}
	
	public void onResolutionChanged(int width, int height){
		this.onScreenPosition=new PointF(width-radius-40, height-radius-30);
	}
	
	public void draw(Canvas canvas){
		/*
		Matrix mtx=new Matrix();
		mtx.postRotate(-rotation);
		Bitmap tmpBitmap=Bitmap.createBitmap(bitmap, 0, 0, (int)radius*2, (int)radius*2, mtx, true);
		
		canvas.drawBitmap(tmpBitmap, onScreenPosition.x-tmpBitmap.getWidth()/2.0f, onScreenPosition.y-tmpBitmap.getHeight()/2.0f, paint);
		*/
		Paint paint=new Paint();
		paint.setFilterBitmap(true);
		if(!active)
			paint.setAlpha(150);
		
		canvas.save();
		canvas.translate(onScreenPosition.x, onScreenPosition.y);
		canvas.rotate(-rotation);
		canvas.drawBitmap(bitmap, -radius, -radius, paint);
		canvas.restore();
		
	}
	
	private boolean active=false;
	
	public void handleEvent(MotionEvent event){
		
		int flags=Message.FLAG_NONE;
		// Ustawiamy flagi kontrolera predkosci, zeby samochod utrzymywal ustalona predkosc.
		flags|=parent.storedUDFlag;
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			active=true;
			currentRotationVector=new Vec2D(event.getX()-onScreenPosition.x, onScreenPosition.y-event.getY());
		break;
		
		case MotionEvent.ACTION_MOVE:
			if(active){
				performRotation( new Vec2D(event.getX()-onScreenPosition.x, onScreenPosition.y-event.getY()) );
				flags|=(rotation<0 ? Message.FLAG_RIGHT : Message.FLAG_LEFT);
				
				parent.sendMessage(flags, Math.abs(rotation/maxRotation), parent.storedUD);
			}
		break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			active=false;
			rotation=0;
			parent.sendMessage(flags, 0f, parent.storedUD);
			
		}
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void setOnScreenPosition(PointF pos){
		onScreenPosition=pos;
	}
	
	public boolean isActive(){
		return active;
	}
	
	/*
	 * Funkcja performRotation jako argument przyjmuje wektor utworzony przez srodek kierownicy i punkt,
	 * w ktorym uzytkownik aktualnie dotyka ekranu.
	 * Kat miedzy tym wektorem a wektorem currentRotationVector to kat, o ktory zostala obrocona kierownica. 
	 */
	public void performRotation(Vec2D newRotationVector){
		double angle=countAngle(currentRotationVector, newRotationVector);
		
		// jezeli nie zostala przekroczona maksymalna wartosc obrotu, obracamy kierownice
		if(Math.abs(rotation+angle)<maxRotation && Math.abs(rotation+angle)>-maxRotation)
			rotation+=angle;
		
		currentRotationVector=newRotationVector;
		
	}
	
	// Funkcja sprawdza, czy dany punkt lezy w obrebie kierownicy
	public boolean contains(PointF p){
		return Math.pow(p.x-onScreenPosition.x, 2)+Math.pow(p.y-onScreenPosition.y, 2)<=Math.pow(radius, 2);
	}	
	
	
	/*
	 * Funkcja oblicza kat skierowany pomiedzy dwoma wektorami
	 * Kat ma wartosc dodatnia dla obrotu CW i ujemna dla obrotu CCW
	 */
	
	public static double countAngle(Vec2D v1, Vec2D v2)
	{
		float x1=v1.getX();
		float y1=v1.getY();
		float x2=v2.getX();
		float y2=v2.getY();
		//jezeli ktorys z wektorow jest zerowy zwracamy 0
		if( (x1==0 && y1==0) || (x2==0 && y2==0) )
			return 0;
		
		
		double angle1=Math.atan2(y1, x1);
		double angle2=Math.atan2(y2, x2);
		double angle=angle2-angle1;
		
		// Przy przekraczaniu 180st. nalezy odpowiednio dodac lub odjac od kata wartosc 2pi
		if(x1<0 && x2<0){
			if(y1<=0 && y2>0)
				angle-=2*Math.PI;
			else if(y1>0 && y2<=0)
				angle+=2*Math.PI;
			
		}

		return Math.toDegrees(angle);
		
	}
}



/*
 * Klasa reprezentujaca kontroler predkosci, wiekszosc skopiowana z klasy TwoSliderSingleTouchUI
 */
class VerticalSlider{
	private final float deadZone = 20f;
	private final float udWidth = 53f;
	private final float udHeight = 227f;

	private final Bitmap upDown;
	private final Bitmap galka;
	
	private final float udSensitiveZone = (udHeight - 1f) * 0.5f - deadZone;
	
	private float udPosY;
	private float udPosX;	
	

	private float udCenterX;
	private float udCenterY;
	
	private float udGalkaY;
	
	
	private SteeringWheelUI parent;
	
	public VerticalSlider(Resources resources, SteeringWheelUI parent){
		this.galka = BitmapFactory.decodeResource( resources, R.drawable.ui_point_selection );
		this.upDown = BitmapFactory.decodeResource( resources, R.drawable.ui_up_down );
		this.parent=parent;
		

		udPosX=10f;
		udPosY=parent.height-udHeight-10f;
		
		udCenterX = udPosX + (udWidth * 0.5f);
		udCenterY = udPosY + (udHeight * 0.5f);
		
		udGalkaY = udCenterY;
		
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap( upDown, udPosX, udPosY, null );
		canvas.drawBitmap( galka, udCenterX - 4.5f, udGalkaY - 5f, null );
	}
	
	public void onResolutionChanged(int width, int height){
		udPosX=10f;
		udPosY=height-udHeight-10f;
		
		udCenterX = udPosX + (udWidth * 0.5f);
		udCenterY = udPosY + (udHeight * 0.5f);
		
		udGalkaY = udCenterY;
	}
	
	public boolean contains(PointF p){
		RectF rect=new RectF(udPosX, udPosY, udPosX+udWidth, udPosY+udHeight);
		return rect.contains(p.x, p.y);
	}
	
	public void handleEvent(MotionEvent event){
		
		
		float fwdUsage = parent.storedUD;
		int flags = Message.FLAG_NONE;
		
		
		switch( event.getAction() ) {
		
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			
			float touch_y = event.getY();
			
			float off = touch_y - udCenterY;
			if( off < - deadZone ) {
				parent.storedUDFlag = Message.FLAG_UP;
				flags |= parent.storedUDFlag;
				fwdUsage = - (off + deadZone) / udSensitiveZone;
				} else if( off > deadZone ) {
					parent.storedUDFlag = Message.FLAG_DOWN;
					flags |= parent.storedUDFlag;
					fwdUsage = (off - deadZone) / udSensitiveZone;
				} else {
					fwdUsage = 0f;
					parent.storedUDFlag = Message.FLAG_NONE;
				}

				udGalkaY = udCenterY + off;
		
				parent.sendMessage( flags, 0.0f, fwdUsage );
				parent.storedUD = fwdUsage;
			
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			flags = parent.storedUDFlag;
			parent.sendMessage( flags, 0f, parent.storedUD );
			break;
		}
	}
}