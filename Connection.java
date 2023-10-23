import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
//Clase connection, vamos a definir la conexión que va obtener el cliente
public class Connection {

    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private boolean isActive;
    private LinkedList<ConnectionListener> listeners;
    
    //Este constructor va a crear una instancia de la clase Connection la cual va a representar la 
    //conexión a través de un socket, la cual va marcar la conexión activa.
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.listeners = new LinkedList<>();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isActive = true;
    }
// El metodo receiveMessages se va a encargar de recibir los mensajes de entrada y notificar a los
    //oyentes ¨ConnectionListener¨ sobre los mensajes que se han recibido.
    public void receiveMessages() {
        try {
            String read;
            while((read = in.readLine()) != null) {
                for (ConnectionListener listener : listeners) 
                    listener.receivedMessage(this, read);
            }
            isActive = false;
        } catch (SocketException se) {

        } catch (IOException ioe) {
            if (isActive) 
                for (ConnectionListener listener : listeners) 
                    listener.receivedMessage(this, "INVALID");
                System.out.printf("It's been tried to send an invalid message.\n");
            ioe.printStackTrace();
        } 
        for (ConnectionListener listener : listeners) 
            listener.receivedMessage(this, "DISCONNECT");
    }

    //El método sendMessage va a enviar los mensajes a través de una conexión
    public void sendMessage(String message) throws IOException {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            throw new IOException("It's been ocurred an error during the send of the " +
                                    "message \"" + message + "\"");
        }
    }
    
//EL método sendDatabase utiliza los datos de la tiendavirtual y sus productos a través de una conexión
    //de salida.
    public void sendDatabase(VirtualStore store, Iterator<Product> iterator) throws IOException {
        out.write(store.sayHi());
        out.newLine();
        out.flush();
        while(iterator.hasNext()) {
            Product p = iterator.next();
            if (p == null) return;
            else {
                out.write("DATABASEREQUESTED".concat(p.toString()));
                out.newLine();
                out.flush();
            }
        }
    }
//Desconecta la Conexión
    public void disconect() {
        isActive = false;
        try {
            socket.close();
            System.out.println("Connection disconnected.\n");
        } catch (IOException ioe) {}
    }

    //Regresa si la conexión esta activa
    public boolean isActive() {
        return isActive;
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }
}
