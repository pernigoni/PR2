import java.util.List;

public interface Game<V>
{
	/*	OVERVIEW: competizione con un numero fissato di atleti.
		TYPICAL ELEMENT: {(num_1, res_1), ..., (num_len, res_len)} 
			dove len è il numero di atleti, {num_1, ..., num_len} sono i numeri di pettorale degli atleti e
			{res_1, ..., res_len} sono i risultati di ogni atleta.	*/

	/* specifica */
	public void setResult(int num, V res); 
	// assegna all'atleta num il risultato res

	/* specifica */
	public int first() throws NoAthleteException; 
	// restituisce il num dell'atleta in testa in questo momento

	/* specifica */
	public List<V> results(); 
	// restituisce la lista dei res conseguiti fino a questo momento

	class NoAthleteException extends Exception
	{
		public NoAthleteException()
		{
			super();
		}
	}
}