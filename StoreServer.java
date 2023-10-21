import java.io.IOException;
import java.lang.reflect.Proxy;
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
    private LinkedList<ProxyClient> clients;

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
        if (connection.isActive()) {
            if (message.startsWith("DATABASEREQUESTED")) {
                databaseRequest(connection);

            } else if (message.startsWith("SIGNUP")) {
                String client = message.substring("SIGNUP".length());
                clientSignUp(connection, client);

            } else if (message.startsWith("SIGNIN")) {
                String client = message.substring("SIGNIN".length());
                clientSignIn(connection, client);

            } else if (message.startsWith("DISCONNECT")) {
                toDisconnect(connection);

            } else if (message.startsWith("INVALID")) {
                error(connection, message.toString());
            }
        }
    }    
    

    private void databaseRequest(Connection connection) {
        writeMessage("Database requested by port: %d", port);
        try{
            Iterator<Product> iterator = database.iterator();
            connection.sendDatabase(store, iterator);
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

    private void clientSignIn(Connection connection, String client) {
        String u = client.substring(client.indexOf("Username: ") + "Username: ".length(), client.indexOf("Password"));
        String p = client.substring(client.indexOf("Password: ") + "Password: ".length(), client.indexOf("Phone number"));
        for (ProxyClient c : clients)
            if (c.getUsername().equals(u) && c.getPassword() == p.hashCode()) {
                country(connection, c.getCountry());
                return;
            }
        
        try {
            connection.sendMessage("INVALID".concat("You have not signed up yet.\n"));
        } catch (IOException ioe) {}
    }

    private void clientSignUp(Connection connection, String client) {
        String u = client.substring(client.indexOf("Username: ") + "Username: ".length(), client.indexOf("Password"));
        String p = client.substring(client.indexOf("Password: ") + "Password: ".length(), client.indexOf("Phone number"));
        for (ProxyClient c : clients)
            if (c.getUsername().equals(u) && c.getPassword() == p.hashCode()) {
                try {
                    connection.sendMessage("SIGNUP".concat("You've already signed up; " +
                                        "creating the store according to your country anyway...\n"));
                } catch (IOException ioe) {}
                country(connection, c.getCountry());
                databaseRequest(connection);
                return;
            }

        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), client.indexOf("Username:"));
        String pN = client.substring(client.indexOf("Phone number: ") + "Phone number: ".length(), client.indexOf("Address"));
        String a = client.substring(client.indexOf("Address: ") + "Address: ".length(), client.indexOf("Bank"));
        String b = client.substring(client.indexOf("Bank Account: ") + "Bank Account: ".length(), client.indexOf("Country"));
        String c = client.substring(client.indexOf("Country: ") + "Country: ".length(), client.indexOf("Money"));
        String m = client.substring(client.indexOf("Money: ") + "Money: ".length());

        ProxyClient newClient = new ProxyClient(new Client(u, p, n, Long.parseLong(pN), a, 
                                                Long.parseLong(b), c, Double.parseDouble(m)));
        clients.add(newClient);
        country(connection, c);
        store.sayHi();
        databaseRequest(connection);
    }

    private void country(Connection connection, String country) {
        switch(country) {
            case "México" :
                store = new MexicoVirtualStore(database.iterator());
                break;
            case "España" : 
                store = new SpainVirtualStore(database.iterator());
                break;
            case "United States" : 
                store = new USAVirtualStore(database.iterator());
                break;
            default : 
                try {
                    connection.sendMessage("Invalid name for a country.\n");
                } catch (IOException ioe) {
                    System.out.println("Error while trying to notify the client that " +
                                            "its country is not valid.\n");
                }
        }
    }

    private void storeManager(String country) {

    }

    public VirtualStore getVirtualStore() {
        return store;
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