import java.util.*;

public class MyLibrary implements Library 
{
    protected BookNode root;

    public MyLibrary() 
    {
        root = null;
    }

    public MyLibrary(Book[] start) 
    {
        if(start == null) 
            throw new NullPointerException();
        
        /* for(int i = 0; i < start.length; i++)
            insert(clone(start[i])); // forse un overkill... */

        if(start.length == 0)
            throw new IllegalArgumentException();
        
        root = new BookNode(start[0]);
        BookNode tmp = root;
        for(int i = 1; i < start.length; i++)
        {
            tmp.setNext(new BookNode(start[i]));
            tmp = tmp.getNext();
        }
    }
    
    public String[] getByAuthor(String aut) 
    {
        if(aut == null)
            throw new NullPointerException();
        
        Vector tmpV = new Vector(); // controllare i warning
        /*  da stackoverflow: This comes up in Java 5 and later if you're using collections without type 
            specifiers (e.g., Arraylist() instead of ArrayList<String>()). It means that the compiler 
            can't check that you're using the collection in a type-safe way, using generics. */

        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.getAuthor().equals(aut))
                tmpV.add(bk);
        }
        return fromVecToArr(tmpV);
    }

    public String[] getByTitle(String tit) 
    {
        if(tit == null) 
            throw new NullPointerException();
        
        Vector tmpV = new Vector();
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.getTitle().equals(tit))
                tmpV.add(bk);
        }
        return fromVecToArr(tmpV);
    }

    public String[] getByPublisher(String pub) 
    {
        if(pub == null) 
            throw new NullPointerException();

        Vector tmpV = new Vector();
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.getPublisher().equals(pub))
                tmpV.add(bk);
        }
        return fromVecToArr(tmpV);
    }

    public String[] getByYear(int year) 
    {
        if(year < 0) 
            throw new IllegalArgumentException();
        
        Vector tmpV = new Vector();
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext())
        {
            Book bk = tmp.getBook();
            if(bk.getYear() == year)
                tmpV.add(bk);
        }
        return fromVecToArr(tmpV);
    }

    public String[] getAuthorByPublisher(String pub) 
    {
        if(pub == null) 
            throw new NullPointerException();
        
        Vector tmpV = new Vector();
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.getPublisher().equals(pub))
                if(tmpV.indexOf(bk.getAuthor()) == -1) // ogni autore è inserito una volta sola
                    tmpV.add(bk.getAuthor());
        }
        return fromVecToArr(tmpV);
    }

    public String[] getTitleByYear(int year) 
    {
        if(year < 0) 
            throw new IllegalArgumentException();
        
        Vector tmpV = new Vector();
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.getYear() == year)
                if(tmpV.indexOf(bk.getTitle()) == -1)
                    tmpV.add(bk.getTitle());
        }
        return fromVecToArr(tmpV);
    }

    public void remove(Book b) throws MissingBookException 
    {
        if(b == null) 
            throw new NullPointerException();

        boolean found = false;
        for(BookNode prev = null, tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            if(bk.equals(b)) // scelta implementativa: elimino tutti i libri duplicati
            {
                if(prev == null)
                    root = tmp.getNext();
                else
                    prev.setNext(tmp.getNext());
                found = true; 
            }
            else 
                prev = tmp;
        }
        if(!found) 
            throw new MissingBookException(b.toString());
    }

    public void insert(Book b) 
    {
        if(b == null) 
            throw new NullPointerException();
        
        root = new BookNode(b, root); // senza clonazione o check duplicazione...
    }

    public String toString()
    {   
        String s = "";
        for(BookNode tmp = root; tmp != null; tmp = tmp.getNext()) 
        {
            Book bk = tmp.getBook();
            s += bk.toString() + " --- ";
        }
        return s;
    }

    private String[] fromVecToArr(Vector vec) 
    {
        String[] tmp = new String[vec.size()];
        for(int i = 0; i < vec.size(); i++)
            tmp[i] = vec.get(i).toString();
        return tmp; // array di stringhe
    }
    
    private Book clone(Book b) 
    {
        return new MyBook(b.getAuthor(), b.getTitle(), b.getPublisher(), b.getYear());
    }
}