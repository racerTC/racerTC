package com.games.racertc.utility;

/**
 * Szybka lista dwukierunkowa, nie alokujaca pamieci. Lista przechowuje elementy typu 
 * FastList::Element. Lista nie jest bezpieczna ani w kontekscie uzycia wielowatkowego..
 * 
 * @type <Type> Typ obiektow, jakie zwracane beda przez liste (musza dziedziczyc po FastList.Element).
 * 
 * @author Piotr Balut
 */
@SuppressWarnings("unchecked")
public class FastList<Type> {
	
/*-----------------------------------------*/
/*-          Reprezntacja listy:          -*/
/*-----------------------------------------*/	
	
	/**
	 * Klasa bazowa dla elementow, ktore przechowywac bedzie lista.
	 */
	static public abstract class Element {
		
		/**
		 * Wskazanie na poprzedni element listy.
		 */
		public Element previousElement;
		
		/**
		 * Wskazanie na nastepny element listy.
		 */
		public Element nextElement;
		
		public Element() {
			previousElement = null;
			nextElement = null;
		}
		
		public Element( Element previous, Element next ) {
			previousElement = previous;
			nextElement = next;
		}
				
	} // class Element
	
	private Element first = null;
	
	private Element last = null;
	
/*-----------------------------------------*/
/*-            Iterface listy:            -*/
/*-----------------------------------------*/	
	
	/**
	 * Usuwa zadany element z listy. UWAGA! Metoda nie jest bezpieczna w kontekscie
	 * sprawdzania przynaleznosci elementu do listy. Podanie do metody obiektu nie
	 * nalezacego do listy skutkuje nieokreslonym zachowaniem.
	 * @param element Element do usuniecia.
	 */
	public void remove( Element element ) {
		if( element == first ) popFront(); else
		if( element == last ) popBack(); else
		{
			element.nextElement.previousElement = element.previousElement;
			element.previousElement.nextElement = element.nextElement;
			element.nextElement = element.previousElement = null;
		}
	}
	
	/**
	 * Dodaje element na poczatek listy.
	 * @param element Element, ktory zostanie dodany do listy. Element musi miec
	 * ustawione na null pola nextElement oraz previousElement.
	 */
	public void pushFront( Element element ) {
		element.nextElement = first;
		if( first != null )
			first.previousElement = element;
		first = element;
	}
	
	/**
	 * Dodaje element na koncu listy.
	 * @param element Element, ktory zostanie dodany do listy. Element musi miec
	 * ustawione na null pola nextElement oraz previousElement.
	 */
	public void pushBack( Element element ) {
		if( first != null ) {
			// lista jest niepusta element
			last.nextElement = element;
			element.previousElement = last;
			last = element;
		} else {
			// lista jest pusta
			first = last = element;
		}
	}
	
	/**
	 * Usuwa element z poczatku listy.
	 * @return Usuwany element. Pola nextElement i previousElement elementu zostaja
	 * ustawione na wartosc null. Jezeli lista byla pusta, zwracana jest wartosc null.
	 */
	public Type popFront() {
		if( first != last ) {
			// lista ma wiecej niz jeden element.
			Element e = first;
			first = e.nextElement;
			if( first != null )
				first.previousElement = null;
			e.nextElement = null;
			return (Type) e;
		} else if( first == last ) {
			// lista ma tylko jeden element;
			Element e = first;
			first = last = null;
			return (Type) e;
		} else
			return null;
	}
	
	/**
	 * Usuwa element z konca listy.
	 * @return Usuwany element. Pola nextElement i previousElement elementu zostaja
	 * ustawione na wartosc null. Jezeli lista byla pusta, zwracana jest wartosc null.
	 */
	public Type popBack() {
		if( first != last ) {
			// lista ma wiecej niz jeden element.
			Element e = last;
			last = e.previousElement;
			last.nextElement = null;
			last.previousElement = null;
			return (Type) e;
		} else if( first == last ) {
			Element e = last;
			first = last = null;
			return (Type) e;
		} else
			return null;
	}
	
	/**
	 * Przejmuje wlasnosc nad wszystkimi obiektami z zadanej listy i wstawia je na koncu aktualnej listy.
	 * @param list Lista, z ktorej elementy zostana przeniesione. Po zakonczeniu operacji lista list bedzie
	 * pusta.
	 */
	public void append( FastList<Type> list ) {
		if( last != null )
			last.nextElement = list.first;
		if( list.first != null ) {
			list.first.previousElement = last;
			last = list.last;
		}
		if( first == null )
			first = list.first;
		list.first = list.last = null;
	}
	
	/**
	 * Pozwala podejrzec pierwszy element na liscie.
	 * @return Pierwszy element na liscie. Element pozostaje wlasnoscia listy.
	 */
	public final Type peekFront() {
		return (Type) first;
	}
	
	/**
	 * Pozwala podejrzec ostatni element na liscie.
	 * @return Ostatni element listy. Element pozostaje wlasnoscia listy.
	 */
	public final Type peekBack() {
		return (Type) last;
	}

	public final boolean isEmpty() {
		return (first == null);
	}
}
