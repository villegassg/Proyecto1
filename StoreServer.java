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

            } else if (message.startsWith("CATALOGUE")) {
                databaseRequest(connection);

            } else if (message.startsWith("PURCHASEMODE")) {
                purchaseMode(connection);     

            } else if (message.startsWith("CONNECT")) {
                connect(connection);

            } else if (message.startsWith("ADDTOCART")) {
                String client = message.substring("ADDTOCART".length(), message.indexOf("Product: "));
                String product = message.substring(message.indexOf("Product: ") + "Product: ".length());
                addToCart(connection, client, product);

            } else if (message.startsWith("REMOVEFROMSHOPPINGCART")) {
                String client = message.substring("REMOVEFROMSHOPPINGCART".length(), message.indexOf("Product: "));
                String product = message.substring(message.indexOf("Product: ") + "Product: ".length());
                removeFromCart(connection, client, product);

            } else if (message.startsWith("PRINTSHOPPINGCART")) {
                String client = message.substring("PRINTSHOPPINGCART".length());
                printShoppingCart(connection, client);

            } else if (message.startsWith("PURCHASESHOPPINGCART")){
                String client = message.substring("PURCHASESHOPPINGCART".length());
                purchaseShoppingCart(connection, client);

            } else if (message.startsWith("PURCHASE")) {
                String client = message.substring("PURCHASE".length(), message.indexOf("Product: "));
                String product = message.substring(message.indexOf("Product: ") + "Product: ".length());
                purchase(connection, client, product);

            } else if (message.startsWith("DISCONNECT")) {
                toDisconnect(connection);

            } else if (message.startsWith("INVALID")) {
                error(connection, message.toString());
            } else {
                error(connection, message.toString());
            }
        }
    }    
    

    private void databaseRequest(Connection connection) {
        writeMessage("Database requested by port: %d", port);
        try{
            Iterator<Product> iterator = database.iterator();
            connection.sendDatabase(store, iterator);
            connection.sendMessage("CONNECT");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void catalogue(Connection connection) {
        try {
            connection.sendDatabase(store, database.iterator());
        } catch(IOException ioe) {}
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
            if (c.getUsername().equals(u)) {
                if (c.getPassword() == p.hashCode()) {
                    country(connection, c);
                    try{
                        connection.sendMessage("CONNECT");
                    } catch (IOException ioe) {}
                    return;
                } else {
                    try {
                        connection.sendMessage("DISCONNECT" + "Incorrect password. Now we'll " +
                                                "have to finish your connection to the server " +
                                                "in order to preserve the security of the store.\n");
                    } catch (IOException ioe) {}
                }
            }
        
        try {
            connection.sendMessage("DISCONNECT".concat("You have not signed up yet.\n"));
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
                country(connection, c);
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
        country(connection, newClient);
        try{
            connection.sendMessage("CONNECT");
        } catch (IOException ioe) {}
    }

    private void purchaseMode(Connection connection) {
        writeMessage("The connection %d has requested to enter the purchase mode.\n", port);
        catalogue(connection);
        try {
            connection.sendMessage("PURCHASEMODE");
            connection.sendMessage(options());
        } catch (IOException ioe) {
            System.out.println("Could not enter the purchase mode.\n");
            disconnect(connection);
        }
    }

    private void addToCart(Connection connection, String client, String product) {
        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), 
                                    client.indexOf("Username:"));
        Iterator<Product> iterator = database.iterator();
        Product producto = null;
        while (iterator.hasNext()) {
            Product next = iterator.next();
            if (next != null && next.getName().equals(product))
                producto = next;
        }
        store.addToCart(connection, n, producto);
        try {
            connection.sendMessage(options());
        } catch(IOException ioe) {}
    }

    private void removeFromCart(Connection connection, String client, String product) {
        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), 
                                    client.indexOf("Username:"));
        Iterator<Product> iterator = database.iterator();
        Product producto = null;
        while (iterator.hasNext()) {
            Product next = iterator.next();
            if (next != null && next.getName().equals(product))
                producto = next;
        }
        store.removeFromCart(connection, n, producto);
        try {
            connection.sendMessage(options());
        } catch(IOException ioe) {}
    }

    private void printShoppingCart(Connection connection, String client) {
        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), 
                                    client.indexOf("Username:"));
        store.printShoppingCart(connection, n);
        try {
            connection.sendMessage(options());
        } catch (IOException ioe) {}
    }

    private void purchaseShoppingCart(Connection connection, String client) {
        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), 
                                    client.indexOf("Username:"));
        store.purchaseShoppingCart(connection, n);
        try {
            connection.sendMessage(options());
        } catch (IOException ioe) {}
    }

    private void purchase(Connection connection, String client, String product) {
        String n = client.substring(client.indexOf("Name: ") + "Name: ".length(), 
                                    client.indexOf("Username:"));
        Iterator<Product> iterator = database.iterator();
        Product producto = null;
        while (iterator.hasNext()) {
            Product next = iterator.next();
            if (next != null && next.getName().equals(product))
                producto = next;
        }
        store.purchase(connection, n, producto);
        try {
            connection.sendMessage(options());
        } catch(IOException ioe) {}
    }

    private void country(Connection connection, ProxyClient proxy) {
        String country = proxy.getCountry();
        switch(country) {
            case "México" :
                store = new MexicoVirtualStore(database.iterator());
                store.add(proxy);
                break;
            case "Mexico" :
                store = new MexicoVirtualStore(database.iterator());
                store.add(proxy);
                break;
            case "España" : 
                store = new SpainVirtualStore(database.iterator());
                store.add(proxy);
                break;
            case "United States" : 
                store = new USAVirtualStore(database.iterator());
                store.add(proxy);
                break;
            case "USA" : 
                store = new USAVirtualStore(database.iterator());
                store.add(proxy);
                break;
            default : 
                try {
                    connection.sendMessage("Invalid name for a country.\n");
                } catch (IOException ioe) {
                    System.out.println("Error while trying to notify the client that " +
                                            "its country is not valid.\n");
                }
                break;
        }
    }

    private String options() {
        return "OPTIONS" + store.options();
    }

    private void connect(Connection connection) {
        try {
            connection.sendMessage("CONNECT");
        } catch (IOException ioe) {}
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
        
        Product audifonos = new Product("Earpods", 121, "ELECTRONICA", 500);
        Product cargadorcompu = new Product("Cargador de computadora", 122, "ELECTRONICA", 786);
        Product television = new Product("Televisión", 123, "ELECTRONICA", 387);
        Product bocina = new Product("Bocina", 124, "ELECTRONICA", 1678);
        Product laptop = new Product("Laptop", 125, "ELECTRONICA", 897);
        Product Cargador = new Product("Multicontacto", 126, "ELECTRONICA", 984);
        Product amplificador = new Product("Amplificador WIFI", 127, "ELECTRONICA", 1200);
        Product monitor = new Product("Monitor Gamer", 128, "ELECTRONICA", 24000);
        Product teclado = new Product("Teclado Gamer", 129, "ELECTRONICA",1094);
        Product telefono = new Product("Apple iPhone 15", 1210, "ELECTRONICA", 2097);
        Product tablet = new Product("Ipad", 1211, "ELECTRONICA", 3908);
        Product juego = new Product("Xbox 360", 1212, "ELECTRONICA", 948);
        Product alexa = new Product("Bocina inteligente Alexa", 1213, "ELECTRONICA", 2294);
        Product computadora = new Product("Computadora", 1214, "ELECTRONICA", 1755);
        Product bafle = new Product("Bafle", 1215, "ELECTRONICA", 2000);

        Product licuadora = new Product("Licuadora", 451, "ELECTRODOMESTICO", 560);
        Product closet = new Product("Clóset inteligente", 452, "ELECTRODOMESTICO", 19905);
        Product Apiradora = new Product("Aspiradora", 453, "ELECTRODOMESTICO", 797);
        Product Horno = new Product("Horno", 454, "ELECTRODOMESTICO", 13089);
        Product microondas = new Product("Microondas", 455, "ELECTRODOMESTICO", 1989);
        Product cafetera = new Product("Cafetera", 456, "ELECTRODOMESTICO", 2456);
        Product tostadora = new Product("Tostadora", 457, "ELECTRODOMESTICO", 891);
        Product batidora = new Product("Batidora", 458, "ELECTRODOMESTICO", 7075);
        Product swandichera = new Product("Sandwichera", 459, "ELECTRODOMESTICO", 794);
        Product plancha = new Product("Plancha", 4510, "ELECTRODOMESTICO", 687);
        Product molino = new Product("Molino de carne", 4511, "ELECTRODOMESTICO", 2531);
        Product extractor = new Product("Extractor de jugos", 4512, "ELECTRODOMESTICO", 3599);
        Product estufa = new Product("Estufa", 4513, "ELECTRODOMESTICO", 2198);
        Product planchaca = new Product("Plancha para el cabello", 4514, "ELECTRODOMESTICO", 1988);

        Product boing = new Product("Boing", 211, "ALIMENTICIO", 80);
        Product leche = new Product("Leche", 212, "ALIMENTICIO", 250);
        Product yoghurt = new Product("Yoghurt", 213, "ALIMENTICIO", 329);
        Product chile = new Product("Chile pasilla", 214, "ALIMENTICIO", 20);
        Product pepino = new Product("Pepino", 215, "ALIMENTICIO", 40);
        Product queso = new Product("Queso manchego", 216, "ALIMENTICIO", 139);
        Product salchica = new Product("Salchichas San Rafael", 217, "ALIMENTICIO", 298);
        Product jamon = new Product("Jamón", 218, "ALIMENTICIO", 146);
        Product cereal = new Product("Zucaritas", 219, "ALIMENTICIO", 100);
        Product fresas = new Product("Fresas", 2110, "ALIMENTICIO", 110);
        Product danonino = new Product("Danonino", 2111, "ALIMENTICIO", 213);
        Product tostadas = new Product("Tostadas", 2112, "ALIMENTICIO", 76);
        Product gelatina = new Product("Gelatinas", 2113, "ALIMENTICIO", 99);
        Product azucar = new Product("Azucar", 2114, "ALIMENTICIO", 199);
        Product huevo = new Product("Huevo", 2115, "ALIMENTICIO", 299);

        dataB.addProduct(audifonos);
        dataB.addProduct(laptop);
        dataB.addProduct(television);
        dataB.addProduct(bocina);
        dataB.addProduct(Cargador);
        dataB.addProduct(amplificador);
        dataB.addProduct(monitor);
        dataB.addProduct(teclado);
        dataB.addProduct(telefono);
        dataB.addProduct(tablet);
        dataB.addProduct(juego);
        dataB.addProduct(alexa);
        dataB.addProduct(computadora);
        dataB.addProduct(cargadorcompu);
        dataB.addProduct(bafle);


        dataB.addProduct(licuadora);
        dataB.addProduct(closet);
        dataB.addProduct(Apiradora);
        dataB.addProduct(Horno);
        dataB.addProduct(microondas);
        dataB.addProduct(cafetera);
        dataB.addProduct(tostadora);
        dataB.addProduct(batidora);
        dataB.addProduct(swandichera);
        dataB.addProduct(plancha);
        dataB.addProduct(molino);
        dataB.addProduct(extractor);
        dataB.addProduct(planchaca);
        dataB.addProduct(estufa);



        dataB.addProduct(boing);
        dataB.addProduct(leche);
        dataB.addProduct(yoghurt);
        dataB.addProduct(chile);
        dataB.addProduct(pepino);
        dataB.addProduct(queso);
        dataB.addProduct(salchica);
        dataB.addProduct(jamon);
        dataB.addProduct(cereal);
        dataB.addProduct(fresas);
        dataB.addProduct(danonino);
        dataB.addProduct(tostadas);
        dataB.addProduct(gelatina);
        dataB.addProduct(azucar);
        dataB.addProduct(huevo);

        return dataB;
    }
}