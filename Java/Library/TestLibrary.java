public class TestLibrary 
{
    public static void main (String[] args) 
    {
        Book myBk1 = new MyBook("autore1", "titolo1", "editore1", 1900);
        Book myBk2 = new MyBook("autore2", "titolo2", "editore1", 2000);
        Book myBk3 = new MyBook("autore3", "titolo3");

        Library myL = new MyLibrary();
        myL.insert(myBk1);
        myL.insert(myBk2);
        System.out.printf("myL: %s\n", myL.toString());
        try 
        {
            myL.remove(myBk2);
            myL.remove(myBk2);
        }
        catch(Exception exc) 
        {
            System.out.println(exc); 
        }
        System.out.printf("myL: %s\n", myL.toString());

        Book[] bkArr = {myBk1, myBk2, myBk3};
        Library myL2 = new MyLibrary(bkArr);
        myL2.insert(myBk3);
        System.out.printf("myL2: %s\n", myL2.toString());

        assert ((myL.getAuthorByPublisher("editore1"))[0]).equals("autore2");
        // la correttezza dell'assert dipende dalla specifica implementazione dell'estrazione
    }
}