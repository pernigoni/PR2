public class MyIndexedLibrary extends MyLibrary implements IndexedLibrary 
{
    private static int id = 1; // una variabile statica è condivisa da tutte le istanze della classe
     
    public MyIndexedLibrary() 
    {
        super();
    }
    
    public MyIndexedLibrary(Book[] start) 
    {
        if(start == null) 
            throw new NullPointerException();

        for(int i = 0; i < start.length; i++)
            insert(new MyIndexedBook(start[i], id++));
    }
    
    public IndexedBook getByIndex(int index) 
    {
        if(index <= 0) 
            throw new IllegalArgumentException();
                    
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            IndexedBook bk = (IndexedBook) tmp.getBook(); // notare il cast...
            if(bk.getIndex() == index)
                return bk;
        }
        return null;
    }
    
    public void insert(Book b) 
    {
        if(b == null) 
            throw new NullPointerException();
        
        super.insert(new MyIndexedBook(b, id++));
    }

    public void insert(IndexedBook b) 
    {
        if(b == null) 
            throw new NullPointerException();
        
        super.insert(new MyIndexedBook(b, id++, b.getGenre()));
    }

    public void remove(int index) throws MissingBookException 
    {
        if(index <= 0) 
            throw new IllegalArgumentException();

        IndexedBook tmp = this.getByIndex(index);
        
        if(tmp == null) 
            throw new MissingIndexException("missing " + index);
        
        super.remove(tmp);
    }
}