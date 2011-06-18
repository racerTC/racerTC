package com.games.racertc.commands;

import com.games.racertc.utility.FastList;

/**
 * Jaka komenda jest, kazdy widzi.
 * @author Piotr Balut
 */
public abstract class Command extends FastList.Element {

	public Command() {
		super();
	}
	
	/**
	 * Wykonuje komende.<br><br>
	 * <b>Uwaga!</b> Biorac pod uwage zewnerzna nierozroznialnosc komend, komenda po
	 * wykonnaiu ma obowiazek samodzielnie 'odlozyc sie' do odpowiedniej puli obiektow. W przeciwnym
	 * razie zostanie ona usunieta przez GC.
	 */
	public abstract void execute();
	
}
