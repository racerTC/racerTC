package com.games.racertc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RacerTrackMenu extends Activity implements OnClickListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_menu);
        
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
	
	public void onClick(View v) {
		Intent i;
		
		switch(v.getId()) {
		case R.id.prev:
			finish();
			break;
    	case R.id.track_01:
    		i = new Intent(this, RacerGame.class);
    		startActivity(i);
    		break;
    	case R.id.track_02:
    		i = new Intent(this, RacerGame.class);
    		startActivity(i);
    		break;
    	case R.id.track_03:
    		i = new Intent(this, RacerGame.class);
    		startActivity(i);
    		break;
    	case R.id.track_04:
    		i = new Intent(this, RacerGame.class);
    		startActivity(i);
    		break;
    	}
    }
}