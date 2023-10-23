//Interfaz del ClientManager
public interface ClientManagerInterface {
    
    //El método logIn recibe un parametro country del tipo String
    public void logIn(String country);
   //El metodo Client recibe una firma signIn.
    public Client signIn();
//El metodo Client recibe una firma signUp de los parametros country del tipo String
    public Client signUp(String country);
}
