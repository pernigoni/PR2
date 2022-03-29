import java.util.*;

public class MyCoinsCollection implements CoinsCollection
{
	/*	OVERVIEW: una CoinsCollection è una collezione modificabile di monete del valore di 50 centesimi, 1 o 2 euro.
		TYPICAL ELEMENT: [v1 * 50C, v2 * 1E, v3 * 2E] dove v1, v2, v3 sono valori interi maggiori di zero.
		
		FA = 
			a(c) = [v1 * 50C, v2 * 1E, v3 * 2E] dove
			v1 = #{x | 0 <= x < coins.size() && coins.get(x) = 50}
	 		v2 = #{x | 0 <= x < coins.size() && coins.get(x) = 100}
	 		v3 = #{x | 0 <= x < coins.size() && coins.get(x) = 200}
	 		inoltre se vi = 0, il corrispondente termine non compare in a(c) per i = 1, 2, 3.
		IR = 
			I(c) = (coins != null) && (forall x. x ∈ [0, coins.size()) ⇒ coins.get(x) ∈ {50, 100, 200})	*/

	private List<Integer> coins; // lista delle monete presenti nella collezione

	public MyCoinsCollection()
	{	}

	/*	MODIFIES: this
		EFFECTS: inizializza this a una CoinsCollection vuota.	*/
	public void createCC()
	{
		coins = new ArrayList<Integer>();
	}

	/*	MODIFIES: this
		EFFECTS: inserisce la moneta di valore coin nella collezione se coin ∈ {50, 100, 200}. 
			Se coin = null o ha valore diverso, lancia una IllegalArgumentException.	*/
	public void addCoin(Integer coin) throws IllegalArgumentException
	{
		if(coin == null) 
			throw new IllegalArgumentException("adding " + coin + " to CoinsCollection");
		
		int c = coin;
		if(c == 50 || c == 100 || c == 200) 
			coins.add(coin);
		else
			throw new IllegalArgumentException("adding " + coin + " to CoinsCollection");
	}

	/*	EFFECTS: restituisce il valore totale della collezione in centesimi di Euro.	*/
	public int balance()
	{
		int res = 0;
		for(Integer i : coins) 
			res += i;
		return res;
	}

	/*	MODIFIES: this
		EFFECTS: restituisce, se possibile, una CoinsCollection con un valore totale uguale a amount, 
			togliendo le monete corrispondenti da this. 
			Se amount < 0 || amount > balance() || amount % 50 != 0 lancia una IllegalArgumentException. 
			Se 0 < amount < balance() ma il cambio non è possibile lancia una NoChangeException.

		IR: assumendo che this lo soddisfi all'invocazione di makeChange, lo soddisferà anche alla fine, 
			poichè su coins si invoca il metodo remove, che non può invalidare l'invariante e addCoin() 
			che assumiamo lo preservi.
			Inoltre la collezione coll lo soddisfa, perché inizializzata alla collezione vuota
			con createCC e poi modificata solo con addCoins e con remove.	*/
	public CoinsCollection makeChange(Integer amount) throws IllegalArgumentException, NoChangeException
	{
		if(amount == null || amount < 0 || amount > balance() || amount % 50 != 0)
			throw new IllegalArgumentException();
		
		MyCoinsCollection coll = new MyCoinsCollection();
		coll.createCC();
		int tot = amount;
		int i = coins.size() - 1;
		while(i >= 0 && tot >= 200)
		{
			if(coins.get(i) == 200) 
			{
				coll.addCoin(200);
				coins.remove(i);
				tot -= 200;
			}
			i--;
		}
		i = coins.size() - 1;
		while(i >= 0 && tot >= 100)
		{
			if(coins.get(i) == 100) 
			{
				coll.addCoin(100);
				coins.remove(i);
				tot -= 100;
			}
			i--;
		}
		i = coins.size() - 1;
		while (i >= 0 && tot >= 50)
		{
			if(coins.get(i) == 50) 
			{
				coll.addCoin(50);
				coins.remove(i);
				tot -= 50;
			}
			i--;
		}

		if(tot == 0) 
			return coll;
		// cambio non possibile, rimetto le monete a posto
		for(i = coll.coins.size() - 1; i >= 0; i--)
		{
			addCoin(coll.coins.get(i));
			coll.coins.remove(i);
		}
		throw new NoChangeException("failed to change " + amount);
	}
}