package com.games.racertc.gamestate;

import com.games.racertc.commands.Command;

public final class StateTransitionCommand extends Command {

	public final static int ENTER = 0;
	
	public final static int LEAVE = 1;
	
	public GameState state = null;
	
	public int action = ENTER;
	
	StateTransitionCommand() {
		super();
	}

	@Override
	public void execute() {
		StateMachine sm = StateMachine.GetInstance();
		if( action == ENTER )
			sm.immediateEnterState( state );
		else 
			sm.immediateLeaveState();
	}

}
