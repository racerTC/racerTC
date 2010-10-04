package com.games.racertc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RacerTrackMenu extends Activity implements OnClickListener {
	
	/** Wybrany samochód */
	private int carId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_menu);
        
        // Pobiera informacjê o wybranym samochodzie
        Intent i = getIntent();
        carId = i.getIntExtra("_car", 0);
        
        // Instancje buttonów
        View prevButton = findViewById(R.id.prev);
        prevButton.setOnClickListener(this);
        View track01Button = findViewById(R.id.track_01);
        track01Button.setOnClickListener(this);
        View track02Button = findViewById(R.id.track_02);
        track02Button.setOnClickListener(this);
        View track03Button = findViewById(R.id.track_03);
        track03Button.setOnClickListener(this);
        View track04Button = findViewById(R.id.track_04);
        track04Button.setOnClickListener(this);        
    }
	
	/** Przesy³a informacjê do RacerGame o wybranym samochodzie i trasie */
	public void onClick(View v) {
		Intent i;
		
		switch(v.getId()) {
		case R.id.prev:
			finish();
			break;
    	case R.id.track_01:
    		i = new Intent(this, RacerGame.class);
    		i.putExtra("_car", carId);
    		i.putExtra("_track", v.getId());
    		startActivity(i);
    		break;
    	case R.id.track_02:
    		i = new Intent(this, RacerGame.class);
    		i.putExtra("_car", carId);
    		i.putExtra("_track", v.getId());
    		startActivity(i);
    		break;
    	case R.id.track_03:
    		i = new Intent(this, RacerGame.class);
    		i.putExtra("_car", carId);
    		i.putExtra("_track", v.getId());
    		startActivity(i);
    		break;
    	case R.id.track_04:
    		i = new Intent(this, RacerGame.class);
    		i.putExtra("_car", carId);
    		i.putExtra("_track", v.getId());
    		startActivity(i);
    		break;
    	}
    }
}