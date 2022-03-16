public class FullGame<V extends Comparable<V>> extends ArrayGame<V> 
{
    /*  FA = come la superclasse.
        IR = come la superclasse.   */
    
    public FullGame(int len) throws IllegalArgumentException 
    {
        super(len);
    }
    
    /*  REQUIRES: 0 < num <= ress.length && res != null
        THROWS: se num <= 0 || num > ress.length lancia una IllegalArgumentException, unchecked;
            se res = null lancia una NullPointerException, unchecked.
        MODIFIES: ress ed eventualmente firstIndex.
        EFFECTS: res >= ress[num - 1] ⇒ ress[num - 1] = res ed eventualmente firstIndex = num - 1.  */
    public void setResult(int num, V res) throws IllegalArgumentException, NullPointerException 
    {
        if(res == null) 
            throw new NullPointerException();
        if((num <= 0) || (num > ress.length)) 
            throw new IllegalArgumentException();

        V tmp = getResult(num);
        if((tmp == null) || (tmp.compareTo(res) < 0))
            super.setResult(num, res);
    }
    /*  Precondizione di setResult in ArrayGame e FullGame è la stessa.

        A = il risultato è migliore.
        B = modifica dell'oggetto corrente.

        Postcondizione di setResult in ArrayGame:   B
        Postcondizione di setResult in FullGame:    A ⇒ B

        ((A ⇒ B) ⇒ B) è una tautologia? No, quindi FullGame non verifica il principio di sostituzione.  
        
        Informalmente: FullGame non verifica il principio di sostituzione perché setResult memorizza il 
        risultato in modo condizionato, mentre in ArrayGame memorizza sempre.   */
}