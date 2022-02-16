public interface IndexedLibrary extends Library 
{
    public IndexedBook getByIndex(int index); // restituisce il volume con il codice index

    public void remove(int index) throws Exception; // rimuove il libro con codice index, lancia un'eccezione se il libro non è presente
}