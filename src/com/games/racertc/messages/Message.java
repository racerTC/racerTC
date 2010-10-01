package com.games.racertc.messages;

/**
 * Klasa reprezentujaca wiadomosc przesylana pomiedzy watkami gry.
 */
public class Message {

	public final static int FLAG_NONE = 0; //0000
	public final static int FLAG_DOWN = 1; //0001
	public final static int FLAG_UP = 2;   //0010
	public final static int FLAG_LEFT = 4; //0100
	public final static int FLAG_RIGHT = 8;//1000
	
	public final static int FLAG_START = 0; //00000
	public final static int FLAG_STOP = 16; //10000
	
	public final static int MASK_MOVEMENT = (~( 31 ) << 1) >>> 1; //...1100000
	
	/**
	 * Zestaw flag okreslajacych parametry wiadomosci.
	 */
	private int flags;
	
	private int mask;
	
	/**
	 * Identyfikator wlasciciela wiadomosci, czyli samochodu, ktorego ta wiadomosc
	 * dotyczy.
	 */
	private int owner;
	public float xAxisUsage;
	public float yAxisUsage;
	
	/**
	 * Konstruuje nowa wiadomosc. Powinno byc wolane przez wszystkie klasy potomne.
	 */
	public Message() {
		setOwner( 0 );
		setFlags( 0, 0f, 0f );
	}
	
	/**
	 * Konstruuje nowa wiadomosc. Powinno byc wolane przez wszystkie klasy potomne.
	 * @param owner Identyfikator samochodu, ktorego dotyczy ta wiadomosc.
	 * @param yAxisUsage 
	 * @param xAxisUsage 
	 */
	public Message( int owner, int flags, float xAxisUsage, float yAxisUsage ) {
		setOwner( owner );
		setFlags( flags, xAxisUsage, yAxisUsage );
	}
	
	/**
	 * Pozwala ustawic wlasciciela wiadomosci.
	 * @param owner Nowy wlasciciel wiadomosci.
	 */
	public void setOwner( int owner ) {
		this.owner = owner;
	}
	
	/**
	 * Pozwala pobrac identyfikator wlasciciela wiadomosci.
	 * @return Identyfikator samochodu, ktorego dotyczy wiadomosc.
	 */
	public int getOwner() {
		return owner;
	}
	
	public void setFlags( int flags, float xAxisUsage, float yAxisUsage ) {
		this.flags = flags;
		this.xAxisUsage = xAxisUsage;
		this.yAxisUsage = yAxisUsage;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setMask( int mask ) {
		this.mask = mask;
	}
	
	public int getMask() {
		return mask;
	}	
	
}
