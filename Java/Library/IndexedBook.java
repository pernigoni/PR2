public interface IndexedBook extends Book 
{
    public int getIndex(); // restituisce l'intero che corrisponde al codice identificativo del libro
    
    public String getGenre(); // restituisce la stringa che corrisponde al genere del libro
    
    public boolean sameGenre(IndexedBook b); // restituisce true se l'oggetto b appartiene allo stesso genere di this
}