public interface Book 
{
    public String getAuthor(); // restituisce la stringa che corrisponde al nome dell'autore del libro

    public String getTitle(); // restituisce la stringa che corrisponde al titolo del libro
    
    public String getPublisher(); // restituisce la stringa che corrisponde all'editore del libro
    
    public int getYear(); // restituisce l'intero che corrisponde all'anno di pubblicazione del libro

    public boolean sameAuthor(Book b); // restituisce true se l'oggetto b ha lo stesso autore di this
    
    public boolean sameTitle(Book b); // restituisce true se l'oggetto b ha lo stesso titolo di this
    
    public boolean samePublisher(Book b); // restituisce true se l'oggetto b ha lo stesso editore di this
    
    public boolean sameYear(Book b); // restituisce true se l'oggetto b ha lo stesso anno di pubblicazione di this
    
    public boolean sameWork(Book b); // restituisce true se l'oggetto b è la stessa opera di this

    public boolean equals(Book b); // restituisce true se l'oggetto b è lo stesso libro di this
}