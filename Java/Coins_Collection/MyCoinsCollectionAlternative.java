import java.util.*;

public class  MyCoinsCollectionAlternative implements CoinsCollection
{
	/*  OVERVIEW: una CoinsCollection è una collezione modificabile di monete del valore di 50 centesimi, 1 o 2 euro. 
        TYPICAL ELEMENT: [v1 * 50C, v2 * 1E, v3 * 2E] dove v1, v2, v3 sono valori interi maggiori di zero. 
		
		FA = 
			a(c) = [v1 * 50C, v2 * 1E, v3 * 2E] dove
	 		v1 = coins.get(0)
	 		v2 = coins.get(1)
	 		v3 = coins.get(2)
	 		inoltre se vi = 0, il corrispondente termine non compare in a(c) per i = 1, 2, 3.
		IR =
			I(c) = (coins != null) && (coins.size() = 3) && (forall x. x ∈ [0, coins.size()) ⇒ coins.get(x) >= 0)	*/

    private List<Integer> coins; // lista di tre elementi contenenti il numero di monete da 50, 100, 200 centesimi

    public MyCoinsCollectionAlternative()
	{	}

	/*	MODIFIES: this
       	EFFECTS: inizializza this a una CoinsCollection vuota.	*/
    public void createCC()
	{
		coins = new ArrayList<Integer>();
		coins.add(0);	
		coins.add(0);	
		coins.add(0);
    }

	/*	MODIFIES: this
       	EFFECTS: inserisce la moneta di valore coin nella collezione se coin ∈ {50, 100, 200}. 
			Se coin = null o ha valore diverso, lancia una IllegalArgumentException.	*/
    public void addCoin(Integer coin) throws IllegalArgumentException
	{
		if(coin == null) 
			throw new IllegalArgumentException("adding " + coin + " to CoinsCollection");

		int c = coin;
		if(c == 50) 
			coins.set(0, 1 + coins.get(0)); 
		else if(c == 100) 
			coins.set(1, 1 + coins.get(1));
		else if(c == 200) 
			coins.set(2, 1 + coins.get(2));
		else
			throw new IllegalArgumentException("adding " + coin + " to CoinsCollection");
    }

	/*	EFFECTS: restituisce il valore totale della collezione in centesimi di Euro.	*/
    public int balance()
	{
		return coins.get(0) * 50 + coins.get(1) * 100 + coins.get(2) * 200;
    }
	
	/*	MODIFIES: this
       	EFFECTS: restituisce, se possibile, una CoinsCollection con un valore totale uguale a amount, 
			togliendo le monete corrispondenti da this. 
			Se amount < 0 || amount > balance() || amount % 50 != 0 lancia una IllegalArgumentException. 
       		Se 0 < amount < balance() ma il cambio non è possibile lancia una NoChangeException.

       	IR: assumendo che this lo soddisfi all'invocazione di makeChange, lo soddisferà anche alla fine, 
			poichè su coins si invoca il metodo set che non modifica la lunghezza della lista e sempre 
			con valori non minori di zero.
       		Anche la collezione coll soddisfa l'invariante, per costruzione.	*/
    public CoinsCollection  makeChange(Integer amount) throws IllegalArgumentException, NoChangeException
	{
		if (amount == null || amount < 0 || amount > balance() || amount % 50 != 0)
			throw new IllegalArgumentException();

		MyCoinsCollectionAlternative coll = new MyCoinsCollectionAlternative();
		coll.createCC();
		int tot = amount;
		int m = Math.min(tot / 200, coins.get(2));
		tot = tot - m * 200;
		coins.set(2, coins.get(2) - m);
		coll.coins.set(2, m);
		m = Math.min(tot / 100, coins.get(1));
		tot = tot - m * 100;
		coll.coins.set(1, m);
		coins.set(1, coins.get(1) - m);
		m = Math.min(tot / 50, coins.get(0));
		tot = tot - m * 50;
		coll.coins.set(0, m);
		coins.set(0, coins.get(0) - m);
		
		if(tot == 0) 
			return coll;
		// cambio non possibile, rimetto le monete a posto
		coins.set(0, coins.get(0) + coll.coins.get(0));
		coll.coins.set(0, 0);
		coins.set(1, coins.get(1) + coll.coins.get(1));
		coll.coins.set(1, 0);
		coins.set(2, coins.get(2) + coll.coins.get(2));
		coll.coins.set(2, 0);
		throw new NoChangeException("failed to change " + amount);
    }
}