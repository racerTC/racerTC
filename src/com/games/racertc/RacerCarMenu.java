package com.games.racertc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RacerCarMenu extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_menu);
        
        // Instancje buttonów
        View prevButton = findViewById(R.id.prev);
        prevButton.setOnClickListener(this);
        View car01Button = findViewById(R.id.car_01);
        car01Button.setOnClickListener(this);
        View car02Button = findViewById(R.id.car_02);
        car02Button.setOnClickListener(this);
        View car03Button = findViewById(R.id.car_03);
        car03Button.setOnClickListener(this);
        View car04Button = findViewById(R.id.car_04);
        car04Button.setOnClickListener(this);        
    }
	
	/** Przesy³a informacjê do RacerTrackMenu o wybranym samochodzie */
	public void onClick(View v) {
		Intent i;
		
		switch(v.getId()) {
		case R.id.prev:
			finish();
			break;
    	case R.id.car_01:
    		i = new Intent(this, RacerTrackMenu.class);
    		i.putExtra("_car", getResources().getInteger(v.getId()));
    		startActivity(i);
    		break;
    	case R.id.car_02:
    		i = new Intent(this, RacerTrackMenu.class);
    		i.putExtra("_car", getResources().getInteger(v.getId()));
    		startActivity(i);
    		break;
		case R.id.car_03:
			i = new Intent(this, RacerTrackMenu.class);
    		i.putExtra("_car", getResources().getInteger(v.getId()));
			startActivity(i);
			break;
		case R.id.car_04:
			i = new Intent(this, RacerTrackMenu.class);
    		i.putExtra("_car", getResources().getInteger(v.getId()));
			startActivity(i);
			break;
		}
    }
}
