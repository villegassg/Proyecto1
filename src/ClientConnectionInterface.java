//Interfaz de la conexión del cliente
public interface ClientConnectionInterface {

    //El metodo purchase va a recibir un parametro de conexión Connection llamado connection
    public void purchase(Connection connection);
 //El metodo connect va a recibir un parametro de conexión Connection llamado connection
    public void connect(Connection connection);
 //El metodo purchaseMode va hacer las acciones del modo de compra que desee el cliente
    public void purchaseMode();
 //El metodo bankAccount1 va a recibir un parametro de conexión Connection llamado connection
    public void bankAccount1(Connection connection);
    
 //El metodo bankAccount2 va a recibir un parametro de conexión Connection llamado connection y un parametro product del tipo String
    public void bankAccount2(Connection connection, String product);
}
