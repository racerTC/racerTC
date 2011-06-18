package com.games.racertc;


import android.content.Context;
import android.content.res.Resources;
import android.view.SurfaceHolder;

/**
 * Reprezentuje globalny kontekst dzialania aplikacji dostepny dla wszystkich stanow.
 * @author Piotr Balut
 */
public final class Globals {

	public static RacerGameView racerView;
	public static SurfaceHolder surfaceHolder;
	public static Context context;
	public static Resources resources;

	/** Rozdzielczosc ekranu. */
	public static int surfaceWidth = 1024, surfaceHeight = 768;

	public final static boolean MEASURE_FPS = true;
	public static int fps;
	
}
