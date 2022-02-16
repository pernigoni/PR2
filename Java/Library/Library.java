public interface Library 
{
    public String[] getByAuthor(String aut); // restituisce l'elenco dei titoli con autore aut
    
    public String[] getByTitle(String tit); // restituisce l'elenco dei libri che hanno titolo tit
    
    public String[] getByPublisher(String pub); // restituisce l'elenco dei libri pubblicati da pub
    
    public String[] getByYear(int y); // restituisce l'elenco dei libri pubblicati nell'anno y

    public String[] getAuthorByPublisher(String pub); // restituisce l'elenco degli autori che hanno pubblicato per pub
    
    public String[] getTitleByYear(int year); // restituisce l'elenco dei titoli di libri pubblicati nell'anno year

    public void remove(Book b) throws Exception; // rimuove tutti i libri identici a b dalla libreria, lancia un'eccezione se il libro non è presente
    
    public void insert(Book b); // inserisce il libro b nella libreria
}