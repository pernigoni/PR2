public interface CoinsCollection
{
    /*  OVERVIEW: una CoinsCollection è una collezione modificabile di monete del valore di 50 centesimi, 1 o 2 euro. 
        TYPICAL ELEMENT: [v1 * 50C, v2 * 1E, v3 * 2E] dove v1, v2, v3 sono valori interi maggiori di zero.  */

    /*  MODIFIES: this
        EFFECTS: inizializza this a una CoinsCollection vuota.  */
    public void createCC();
    
    /*  MODIFIES: this
        EFFECTS: inserisce la moneta di valore coin nella collezione, se coin ∈ {50, 100, 200}. 
            Se coin = null oppure ha valore diverso, lancia una IllegalArgumentException, unchecked.    */
    public void addCoin(Integer coin) throws IllegalArgumentException;
    
    /*  EFFECTS: restituisce il valore totale della collezione in centesimi di Euro.    */
    public int balance();
    
    /*  MODIFIES: this
        EFFECTS: restituisce, se possibile, una CoinsCollection con un valore totale uguale a amount, 
            togliendo le monete corrispondenti da this. 
            Se amount < 0 || amount > balance() || amount % 50 != 0 lancia una IllegalArgumentException. 
            Se 0 < amount < balance() ma il cambio non è possibile (es: this contiene solo 
            una moneta da 1E e amount = 50) lancia una NoChangeException, checked.  */
    public CoinsCollection  makeChange(Integer amount) throws IllegalArgumentException, NoChangeException;
}

class NoChangeException extends Exception
{
    public NoChangeException()
    {
	    super();
    }

    public NoChangeException(String s)
    {
	    super(s);
    }
}