@FunctionalInterface
public interface ServerListener {

    public void processMessage(String format, Object ... arguments);
    
}