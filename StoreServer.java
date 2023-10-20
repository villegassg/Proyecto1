import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class StoreServer {

    private Database database;
    private VirtualStore store;
    private ServerSocket serverSocket;
    private int port;
    private LinkedList<Connection> connections;
    private boolean isRunning;
    private LinkedList<ServerListener> listeners;
    private LinkedList<Client> clients;

    public StoreServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.connections = new LinkedList<>();
        this.isRunning = true;
        this.listeners = new LinkedList<>();
        this.database = createDatabase();
        clients = new LinkedList<>();
    }

    public void serve() {
        writeMessage("Listening from the port: %s.\n", port);
        while (isRunning) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                writeMessage("Connection received from: %s.\n", 
                                socket.getInetAddress().getCanonicalHostName());
                port = socket.getPort();
                connection.addListener((c, m) -> receivedMessage(c, m));
                synchronized(connections) {
                    connections.add(connection);
                }
                new Thread(() -> connection.receiveMessages()).start();
            } catch (IOException ioe) {
                writeMessage("Error while accepting a new connection.\n");
            }
        }
    }

    public void addListener(ServerListener listener) {
        listeners.add(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void receivedMessage(Connection connection, String message) {
        if(connection.isActive()) {
            switch(message) {
                case "DATABASEREQUESTED" : 
                    databaseRequest(connection);
                    break;
                case "NEWCLIENTREQUEST" :

                    break;
                case "DISCONNECT" :
                    toDisconnect(connection);
                    break;
                case "STOPSERVICE" : 
                    //stopService();
                    break;
                case "INVALID" :
                    error(connection, message.toString());
                    break;
                default: 
                    break;
            }
        }
    }    
    

    private void databaseRequest(Connection connection) {
        writeMessage("Database requested by port: %d", port);
        try{
            Iterator<Product> iterator = database.iterator();
            connection.sendDatabase(iterator);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void toDisconnect(Connection connection) {
        writeMessage("Disconnect request by port %d", port);
        disconnect(connection);
    }
    
    private void error(Connection connection, String message) {
        writeMessage(message);
        writeMessage("Disconnecting the connection from the port %d: Invalid message", 
                        port);
        disconnect(connection);
    }

    private void disconnect(Connection connection) {
        synchronized(connections) {
            connections.remove(connection);
        }
        connection.disconect();
        writeMessage("The connection %d has been disconnected.\n", port);
    }

    private void newClientRequest(String client) {

    }

    private void manageClient(String country) {

    }

    private void writeMessage(String format, Object ... arguments) {
        for (ServerListener listener : listeners) 
            listener.processMessage(format, arguments);
    }

    public Database createDatabase() {
        Database dataB = new Database();
        // Aquí llenamos la base de datos con todos los productos a vender :)
        Product a = new Product("a", 123, "A", 500);
        Product b = new Product("b", 456, "B", 200);
        dataB.addProduct(a);
        dataB.addProduct(b);
        return dataB;
    }
}