package com.games.racertc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RacerMenu extends Activity implements OnClickListener{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View singlePlayerButton = findViewById(R.id.single_player);
        singlePlayerButton.setOnClickListener(this);
        View multiplayerButton = findViewById(R.id.multiplayer);
        multiplayerButton.setOnClickListener(this);
        View optionsButton = findViewById(R.id.options);
        optionsButton.setOnClickListener(this);
        View exitGameButton = findViewById(R.id.exit_game);
        exitGameButton.setOnClickListener(this);
    }
	
	public void onClick(View v) {
    	switch(v.getId()) {
    	case R.id.single_player:
    		Intent i = new Intent(this, RacerCarMenu.class);
    		startActivity(i);
    		break;
    	case R.id.exit_game:
    		finish();
    		break;
    	}
    }
}
