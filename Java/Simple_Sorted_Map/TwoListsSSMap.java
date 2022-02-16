import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class TwoListsSSMap<K extends Comparable<K>, V> implements SimpleSortedMap<K, V> 
{
    /*  OVERVIEW: tipo modificabile delle funzioni parziali con dominio un ordine totale K
            e codominio V, definite solo su un sottoinsieme finito di K.    

        FA = f: K -> V tale che f(keys.get(i)) = values.get(i) per 0 <= i < dim, indefinito altrimenti.
        IR = keys != null && values != null && keys.size() = values.size() = dim
            && for all i. 0 <= i < dim ⇒ (keys.get(i) != null && values.get(i) != null)
            && for all i != j. 0 <= i, j < dim ⇒ (keys.get(i) != keys.get(j))   */

    private int dim;
    private List<K> keys;
    private List<V> values;
    
    public TwoListsSSMap()
    {
        this(new ArrayList<K>(), new ArrayList<V>()); // entrambi gli ArrayList sono sicuramente diversi da null in quanto appena creati
    }

    /*  REQUIRES: collK != null && collV != null && collK.size() = collV.size()
        THROWS: se collK = null || collV = null lancia una NullPointerException, unchecked.
        MODIFIES: dim, keys, values
        EFFECTS: inizializza keys e values con i valori di lstK e lstV, rispettivamente.    */
    public TwoListsSSMap(List<? extends K> lstK, List<? extends V> lstV) 
    {   // !!!!! problema duplicati in lstK
        if((lstK == null) || (lstV == null)) 
            throw new NullPointerException();
        if(lstK.size() != lstV.size()) 
            throw new IllegalArgumentException();
        
        dim = lstK.size();
        keys = new ArrayList<K>();
        for(K elem : lstK)
            keys.add(elem);
        values = new ArrayList<V>();
        for(V elem : lstV)
            values.add(elem);
    }

    /*  REQUIRES: key != null && value != null
        THROWS: se key = null || value = null lancia una NullPointerException, unchecked.
        MODIFIES: dim, keys, values
        EFFECTS: dopo l'invocazione assegna value a values(i) se key.equals(keys.get(i)),
            e se prima dell'invocazione values.get(i) vale v, restituisce il valore v;
            se key non compare in keys, esegue keys.add(key) e values.add(value) e restituisce null.    */
    public V put(K key, V value) throws NullPointerException 
    {
        if((key == null) || (value == null)) 
            throw new NullPointerException();
        
        V former = null;
        int loc = keys.indexOf(key);
        if(loc != -1)
        {   
            former = values.get(loc);
            values.set(loc, value);
        } 
        else 
        {
            keys.add(key);
            values.add(value);
            dim++;
        }
        return former;
    }

    /*  REQUIRES: key != null
        THROWS: se key = null lancia una NullPointerException, unchecked.
        EFFECTS: restituisce values.get(i) se key.equals(keys.get(i));
            se key non compare in keys restituisce null.    */
    public V get(K key) throws NullPointerException 
    {
        if(key == null) 
            throw new NullPointerException();

        V former = null;
        int loc = keys.indexOf(key);
        if(loc != -1)
            former = values.get(loc);
        return former;
    }
    
    /*  EFFECTS: restituisce un insieme tmp tale che keys.get(i) == tmp.get(i)
            (ovvero, restituisce una shallow copy di keys: la deep copy avrebbe richiesto l'esistenza 
            di un metodo clone() per K; si noti come un problema analogo si ritrovi in put e get).  */
    public Set<K> keySet() 
    {
        Set<K> tmp = new TreeSet<K>();
        for(int i = 0; i < dim; i++)
            tmp.add(keys.get(i));
        return tmp;
    }
 
    /*  REQUIRES: keys != null
        THROWS: se keys = null lancia una EmptyListException, checked.
        EFFECTS: restituisce l'elemento più grande contenuto in keys.   */
    public K lastKey() throws EmptyListException
    {
        if(dim == 0) 
            throw new EmptyListException();

        K tmp = keys.get(0);
        for(K elem : keys)
            if(tmp.compareTo(elem) < 0)
                tmp = elem;
        return tmp;
    }

    /*  EFFECTS: restituisce il valore di dim (che corrisponde a keys.size()).  */
    protected int size() 
    {
        return dim;
    }
    // restituisce il numero di elementi di K per i quali la funzione è definita
}

/*
    3) Si dimostri la correttezza dell'implementazione proposta.
        Bisogna verificare che ogni metodo preserva il rep invariant.
*/
