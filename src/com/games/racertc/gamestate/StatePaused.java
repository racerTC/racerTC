package com.games.racertc.gamestate;

import android.graphics.Canvas;

/**
 * GameState that completely pauses program. When StatePause becomes processed, execution of current
 * thread is ceased until onPause() with isPaused=false is called. StatePause exits immediately
 * if previous state was not paused. (note that StatePaused does affect UI thread, so - if needed
 * - care should be taken not to generate input events when game is paused. When game becomes
 * paused due to Android event, this should not be the case anyway.)
 * 
 * @author Piotr Balut
 */
public class StatePaused extends GameState {

	final Object lock = new Object();

	/**
	 * Pauses execution of current thread as long as paused is set to true; otherwise pops
	 * current state.
	 * @param dt param is ignored.
	 */
	@Override
	public void process(long dt) {
		while( paused ) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO: thread interrupted reaction?
			}
		}
		StateMachine.leaveState();
	}

	@Override
	public void render(Canvas queue) {
		
	}

	public void onPause( boolean isPaused ) {
		paused = isPaused;
		if( !isPaused )
			lock.notify();
		// TODO: else debug msg - this should _never_ happen!
		
		// we notify previousState about pause change, becouse it will be noted automatically
		// when it will gain priority.
	}
	
}
