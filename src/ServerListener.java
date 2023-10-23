@FunctionalInterface
    //Interfaz de los oyentes¨ServerListener¨
public interface ServerListener {
//El método processMessage procesa los mensajes recibidos
    public void processMessage(String format, Object ... arguments);
    
}
