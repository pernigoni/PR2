public class MyBook implements Book
{
    private String author, title, publisher;
    private int year;

    public MyBook(String author, String title)
    {
        this(author, title, "sconosciuto", 9999);
    }

    public MyBook(String author, String title, String publisher, int year)
    {
        if(author == null || title == null || publisher == null)
            throw new NullPointerException(); // eccezioni unchecked
        if(year < 0)
            throw new IllegalArgumentException();
        
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
    }

    public String getAuthor()
    {
        return this.author;
        // possiamo restituire questi valori esponendo la rappresentazione perché le stringhe sono immutabili
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getPublisher()
    {
        return this.publisher;
    }

    public int getYear()
    {
        return this.year;
    }

    public boolean sameAuthor(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return this.author.equals(b.getAuthor());
    }

    public boolean sameTitle(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return this.title.equals(b.getTitle());
    }

    public boolean samePublisher(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return this.publisher.equals(b.getPublisher());
    }

    public boolean sameYear(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return this.year == b.getYear();
    }

    public boolean sameWork(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return this.sameAuthor(b) && this.sameTitle(b);
    }

    public boolean equals(Book b)
    {
        if(b == null)
            throw new NullPointerException();
        return sameWork(b) && samePublisher(b) && sameYear(b);
    }

    public String toString()
    {
        return this.author + " - " + this.title + " - " + this.publisher + " - " + this.year;
        // return this.toString().equals(b.toString());
    }
}