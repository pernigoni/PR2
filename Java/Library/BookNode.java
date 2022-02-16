public class BookNode // lista concatenata di supporto 
{
    private Book bk;
    private BookNode next;
    /*  Richiede getBook e getNext per gestire i metodi di MyLibrary. Senza il modificatore private le variabili
        bk e next si sarebbero potute manipolare direttamente in MyLibrary. */
    
    public BookNode(Book bk) 
    {
        this(bk, null);
    }

    public BookNode(Book bk, BookNode next) 
    {
        this.bk = bk;
        this.next = next;
    }
    
    public Book getBook() 
    {
        return this.bk;
    }

    public BookNode getNext() 
    {
        return this.next;
    }

    public void setNext(BookNode next) 
    {
        this.next = next;
    }
}