public class LimitedTwoListsSSMap<K extends Comparable<K>, V> extends TwoListsSSMap<K, V> 
{
    /*  OVERVIEW: tipo modificabile delle funzioni parziali con dominio un ordine totale K
            e codominio V, definite solo su un sottoinsieme finito e limitato di K.

        FA = <lim, dim, keys, values> -> <dim, keys, values>
        IR = come sopra e inoltre dim <= lim.   */

    private int lim;
    
    /*  REQUIRES: limit > 0
        THROWS: se limit <= 0 lancia una IllegalArgumentException, unchecked.
        EFFECTS: pone la dimensione massima dell'insieme delle chiavi a limit.  */
    public LimitedTwoListsSSMap(int limit) throws IllegalArgumentException 
    {
        super();

        if(limit <= 0) 
            throw new IllegalArgumentException();
        
        lim = limit;
    }

    /*  REQUIRES: key != null && value != null && size() < lim
        THROWS: se lim = size lancia una IllegalStateException, unchecked.
            Se key = null || value = null lancia una NullPointerException, unchecked.
        MODIFIES: dim, keys, values
        EFFECTS: dopo l'invocazione assegna value a values(i) se key.equals(keys.get(i)),
            e se prima dell'invocazione values.get(i) vale v, restituisce il valore v;
            se key non compare in keys, esegue keys.add(key) e values.add(value) e restituisce null.    */
    public V put(K key, V value) throws IllegalStateException, NullPointerException 
    {
        if(super.size() == lim) 
            throw new IllegalStateException();
        
        return super.put(key, value);
    }
    /*  Se non è stato raggiunto il limite massimo di chiavi definite, associa il valore value alla chiave 
        key, restituisce il valore precedentemente associato a key se questo esisteva, null altrimenti. 
        ---
        Questa implementazione della put è molto restrittiva perché se voglio inserire un elemento
        già presente me lo impedisce se size = lim, invece di sovrascriverlo. Per una implementazione
        più flessibile avrei dovuto mettere la lista delle chiavi keys protected invece che private.    */

    /*  Notare che per essere una sottoclasse il costruttore e il metodo put devono lanciare 
        eccezioni unchecked, e non checked. Detto questo, è un sottotipo o meno?    */
}
