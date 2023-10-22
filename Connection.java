import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;

public class Connection {

    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private boolean isActive;
    private LinkedList<ConnectionListener> listeners;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.listeners = new LinkedList<>();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isActive = true;
    }

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

    public void sendDatabase(VirtualStore store, Iterator<Product> iterator) throws IOException {
        out.write(store.sayHi());
        out.newLine();
        out.flush();
        while(iterator.hasNext()) {
            out.write("DATABASEREQUESTED".concat(iterator.next().toString()));
            out.newLine();
            out.flush();
        }
    }

    public void disconect() {
        isActive = false;
        try {
            socket.close();
            System.out.println("Connection disconnected.\n");
        } catch (IOException ioe) {}
    }

    public boolean isActive() {
        return isActive;
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }
}