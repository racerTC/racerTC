package com.games.racertc.other;

/**
 * Klasa pozwalajaca na wygodne korzystanie z macierzy dwuwymiarowych.
 */
public class Matrix< T > {

	private final T[] array;
	
	private final int width;
	
	/**
	 * Tworzy nowa macierz dwuwymiarowa. Wszystkie jej elementy inicjalizowane sa jako null.
	 * @param width Szerokosc macierzy.
	 * @param height Wysokosc macierzy.
	 */
	@SuppressWarnings("unchecked")
	public Matrix( int width, int height ) {
		this.width = width;
		int size = width * height;
		array = (T[]) new Object[ size ];
		for( int i = 0; i < size; ++i ) {
			array[i] = null;
		}
	}
	
	/**
	 * Pobiera element z macierzy.
	 * @param x Wiersz pobieranego elementu, w zakresie [ 0, width ).
	 * @param y Kolumna pobieranego elementu, w zakresie [ 0, width ).
	 * @return Element z macierzy o wspolrzednych ( x, y ).
	 */
	public T get( int x, int y ) {
		return array[ ( width * y ) + x ];
	}
	
	/**
	 * Ustawia element w macierzy.
	 * @param x Wiersz ustawianego elementu, w zakresie [ 0, width ).
	 * @param y Kolumna ustawianego elementu, w zakresie [ 0, width ).
	 * @param value Nowa wartosc macierzy na ( x, y ).
	 */
	public void set( int x, int y, T value ) {
		array[ ( width * y ) + x ] = value;
	}
	
	/**
	 * Zwraca element z macierzy, jednoczesnie usuwajac go z niej (zostaje on zastapiany
	 * wartoscia null).
	 * @param x Wiersz elementu, w zakresie [ 0, width ).
	 * @param y Kolumna elementu, w zakresie [ 0, width ).
	 * @return Element z macierzy o wspolrzednych ( x, y ).
	 */
	public T reset( int x, int y ) {
		final int pos = ( width * y ) + x;
		T tmp = array[ pos ];
		array[ pos ] = null;
		return tmp;
	}
	
	/**
	 * Usuwa element z macierzy. Zostaje on zastapiony wartoscia null.
	 * @param x Wiersz usuwanego elementu, w zakresie [ 0, width ).
	 * @param y Kolumna usuwanego elementu, w zakresie [ 0, width ).
	 */
	public void erase( int x, int y ) {
		array[ ( width * y ) + x ] = null;
	}
	
	
	
}
