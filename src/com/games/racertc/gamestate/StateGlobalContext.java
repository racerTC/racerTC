package com.games.racertc.gamestate;

import com.games.racertc.RacerGameView;

import android.content.Context;
import android.content.res.Resources;
import android.view.SurfaceHolder;

/**
 * Reprezentuje globalny kontekst dzialania aplikacji dostepny dla wszystkich stanow.
 * @author Piotr Balut
 */
public final class StateGlobalContext {

	public RacerGameView racerView;
	public SurfaceHolder surfaceHolder;
	public Context context;
	public Resources resources;

	/** Rozdzielczosc ekranu. */
	public int surfaceWidth = 1024, surfaceHeight = 768;
	
}
