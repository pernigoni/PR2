import java.util.Set;

public interface SimpleSortedMap<K extends Comparable<K>, V> 
{
    /*  OVERVIEW: tipo modificabile delle funzioni parziali con dominio un ordine totale K
            e codominio V, definite solo su un sottoinsieme finito di K.
        TYPICAL ELEMENT: una funzione parziale f: K -> V per la quale {k | f(k) definito} è un insieme finito. */
    
    /*  REQUIRES: key != null && value != null
        THROWS: se key = null || value = null lancia una NullPointerException, unchecked.
        MODIFIES: f
        EFFECTS: dopo l'invocazione f(key) = value, se prima dell'invocazione f(key) era definito e valeva v
            restituisce v, null altrimenti. */
    public V put(K key, V value) throws NullPointerException; 
    // associa value a key e restituisce il value precedentemente associato a key se esisteva, null altrimenti
    
    /*  REQUIRES: key != null
        THROWS: se key = null lancia una NullPointerException, unchecked.
        EFFECTS: restituisce f(key) se è definito, null altrimenti. */
    public V get(K key) throws NullPointerException;
    // restituisce il value associato a key se questo esiste, null altrimenti

    /*  EFFECTS: restituisce l'insieme di tutte e sole le chiavi sulle quali f è definito.  */
    public Set<K> keySet();
    // restituisce l'insieme delle chiavi che hanno associato un valore
    
    /*  REQUIRES: l'insieme {k | f(k) definito} non è vuoto.
        THROWS: se {k | f(k) definito} è vuoto lancia una EmptyListException, checked.
        EFFECTS: restituisce la chiave più grande contenuta nell'insieme {k | f(k) definito}.   */
    public K lastKey() throws EmptyListException;
    // restituisce la chiave più grande che ha associato un valore
    
    class EmptyListException extends Exception 
    {
        public EmptyListException() 
        {
            super();
        }
        
        public EmptyListException(String s) 
        {
            super(s);
        }
    }
}