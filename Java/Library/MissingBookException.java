public class MissingBookException extends Exception // eccezione checked
{
    public MissingBookException(String s) 
    {
        super(s); // chiamo il costruttore della superclasse (o classe padre)
    }
}