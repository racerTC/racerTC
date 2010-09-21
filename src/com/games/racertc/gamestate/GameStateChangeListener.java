package com.games.racertc.gamestate;

/**
 * Interface pozwalajacy klasie byc informowana o zmianach stanu gry.
 */
public interface GameStateChangeListener {

	/**
	 * Wolane po zmianie stanu gry.
	 * @param gameState nowy stan gry.
	 */
	void onGameStateChange( int gameState );
	
}
