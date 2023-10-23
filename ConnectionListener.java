@FunctionalInterface
    //Interfaz ConnectionListener
public interface ConnectionListener {
    //metodo receivedMessage recibe como parametro dos objetos Connection y String
    public void receivedMessage(Connection connection, String message);
}
