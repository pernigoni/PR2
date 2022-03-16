import java.util.List;
import java.util.ArrayList;

public class ArrayGame<V extends Comparable<V>> implements Game<V> 
{
    /*  FA = f: [1, ..., ress.length] -> V tale che f(i) = res[i - 1] se res[i - 1] != null, 
            indefinito altrimenti.
        IR = ress != null && 
            1 <= firstIndex <= ress.length &&
            (forall i. 0 <= i < ress.length ⇒ ress[i] == null || ress[firstIndex - 1] >= ress[i]) &&
            (forall i. 0 <= i < firstIndex - 1 ⇒ ress[i] == null || ress[firstIndex - 1] > ress[i])     */

    protected V[] ress; // array che memorizza i risultati
    private int firstIndex; // atleta in testa in questo momento
    
    public ArrayGame(int len) throws IllegalArgumentException 
    {
        if(len <= 0)
            throw new IllegalArgumentException();

        ress = (V[]) new Object[len]; // Type safety: Unchecked cast from Object[] to V[]
        firstIndex = -1;
    }

    private int searchMax(int ex) 
    {
        int tmp = ex;
        for(int i = ress.length - 1; i > 0; i--)
            if((ress[i] != null) && (ress[i].compareTo(ress[tmp]) >= 0))
                tmp = i;
        return tmp;
    }

    /*  REQUIRES: 0 < num <= ress.length && res != null
        THROWS: se num <= 0 || num > ress.length lancia una IllegalArgumentException, unchecked;
            se res = null lancia una NullPointerException, unchecked.
        MODIFIES: ress ed eventualemente firstIndex.
        EFFECTS: ress[num - 1] = res ed eventualmente si aggiorna firstIndex.   */
    public void setResult(int num, V res) throws IllegalArgumentException, NullPointerException 
    {
        if((num <= 0) || (num > ress.length)) 
            throw new IllegalArgumentException();
        if(res == null) 
            throw new NullPointerException();
        
        if((firstIndex == -1) || (res.compareTo(ress[firstIndex]) > 0)) 
        {
            ress[num - 1] = res;
            firstIndex = num - 1;
        }
        else
        {
            ress[num - 1] = res;
            firstIndex = searchMax(num - 1);
        }
    }

    /*  REQUIRES: 0 < num <= ress.length
        THROWS: se num <= 0 || num > ress.length lancia una IllegalArgumentException, unchecked.    */
    public V getResult(int num) throws IllegalArgumentException 
    {
        if((num <= 0) || (num > ress.length)) 
            throw new IllegalArgumentException();

        return ress[num - 1];
    }
    
    /*  EFFECTS: restituisce i tale che
            (for all j. 0 <= j < ress.length ⇒ ress[j] = null || ress[j].compareTo(ress[i - 1]) <= 0)
        THROWS: se non esiste tale i lancia una NoAthleteException, checked.    */
    public int first() throws NoAthleteException
    {
        if(firstIndex == -1) 
            throw new NoAthleteException();

        return firstIndex + 1;
    }
    
    /*  EFFECTS: restituisce una lista tmp tale che tmp.get(i) = ress[i] (tmp è una shallow copy di ress).
            Per preservare l'ordine la lista include anche null.    */
    public List<V> results() 
    {
        List<V> tmp = new ArrayList<V>();
        for(V elem : ress)
            tmp.add(elem);
        return tmp; 
        /*  Sto esponendo la rappresentazione, gli elementi di tmp sono degli alias. Se V è un tipo immutable
            non ci sono problemi. 
            Con i generici è un casino implementare cloneable. */
    }
}