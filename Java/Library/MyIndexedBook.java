public class MyIndexedBook extends MyBook implements IndexedBook 
{
    private int id;
    private String genre;

    public MyIndexedBook(Book b, int id) 
    {
        this(b, id, "undefined");
    }

    public MyIndexedBook(Book b, int id, String genre) 
    {
        this(b.getAuthor(), b.getTitle(), b.getPublisher(), b.getYear(), id, genre);
    }

    public MyIndexedBook(String author, String title, String publisher, int year, int id, String genre) 
    {
        super(author, title, publisher, year);

        if(genre == null)
            throw new NullPointerException();
        
        this.id = id;
        this.genre = genre;
    }

    public int getIndex() 
    {
        return this.id;
    }

    public String getGenre() 
    {
        return this.genre;
    }
    
    public boolean sameGenre(IndexedBook b) 
    {
        if(b == null) 
            throw new NullPointerException();
    
        return this.genre.equals(b.getGenre());
    }

    public String toString() 
    {
        return this.id + " - " + super.toString() + " - " + this.genre;
    }
    
    public boolean equals(IndexedBook b) 
    {
        return (this.id == b.getIndex()) && super.equals(b);
    }
}