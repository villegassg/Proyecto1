@FunctionalInterface
public interface ConnectionListener {
    
    public void receivedMessage(Connection connection, String message);
}
