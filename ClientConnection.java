import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/*Clase conexión con el cliente. La conexión del cliente tiene una conexión, un cliente, 
*saber si esta conectado y conexión con el cliente.
*/
public class ClientConnection {
    //Conexión del cliente
    private Connection connection;
    //Cliente
    private ProxyClient client;
    //Saber si esta conectado el cliente
    private boolean isConnected;
    //Conexión con el cliente
    private ClientConnectionInterface clientConnection;
    //Un lector de carácteres
    private BufferedReader in;

    //Se inicializa la instancia de ClientConnection para poder administrar y dirigir una conexión
    //de red, así mismo inica un hilo para recibir los mensajes en segundo plano.
    public ClientConnection(BufferedReader in, Socket socket, ProxyClient client) {
        try {
            this.connection = new Connection(socket);
            connection.addListener((c, m) -> receivedMessage(c, m));
            isConnected = true;
            new Thread(() -> connection.receiveMessages()).start();
        } catch (IOException ioe) {
            System.out.println("\nClientConnectionException:\n");
            ioe.printStackTrace();
        }
        this.client = client;
        this.in = in;
        countryConnection(client.getCountry());
    }

    // El método receivedMessage va a procesar mensajes que va arecibir a travez de una conexión de red
    // dependiendo del mensaje recibido, ete se va a imprimir en la consola, entre otras acciones específicas
    //para cada tipo de mensaje.
    public void receivedMessage(Connection connection, String message) {
        if(isConnected) {
            if (message.startsWith("SIGNUP")) {
                System.out.println(message.substring("SIGNUP".length()));
            } else if (message.startsWith("CONNECT")) {
                clientConnection.connect(connection);
            } else if (message.startsWith("DATABASEREQUESTED")) {
                database(message.substring("DATABASEREQUESTED".length()));
            } else if (message.startsWith("CATALOGUE")) {
                database(message.substring("CATALOGUE".length()));
            } else if (message.startsWith("PURCHASEMODE")) {
                clientConnection.purchaseMode();
            } else if (message.startsWith("OPTIONS")) {
                String options = message.substring("OPTIONS".length());
                options = options.replaceAll("\t", "\n");
                System.out.println(options);
                clientConnection.purchase(connection);
            } else if (message.startsWith("ADDEDTOCART")) {
                String success = message.substring("ADDEDTOCART".length());
                System.out.println(success);
            } else if (message.startsWith("FAILEDADDTOCART")) {
                String fail = message.substring("FAILEDADDTOCART".length());
                System.out.println(fail);
            } else if (message.startsWith("FAILEDREMOVEFROMCART")) {
                String fail = message.substring("FAILEDREMOVEFROMCART".length());
                System.out.println(fail);
            } else if (message.startsWith("REMOVEFROMSHOPPINGCART")) {
                String success = message.substring("REMOVEFROMSHOPPINGCART".length());
                System.out.println(success);
            } else if (message.startsWith("NOPRODUCT")) {
                String fail = message.substring("NOPRODUCT".length());
                System.out.println(fail);
            } else if (message.startsWith("PRINTSHOPPINGCART")) {
                String sc = message.substring("PRINTSHOPPINGCART".length());
                sc = sc.replaceAll("\t", "\n");
                System.out.println(sc);
            } else if (message.startsWith("EMPTYSHOPPINGCART")) {
                String fail = message.substring("EMPTYSHOPPINGCART".length());
                System.out.println(fail);
            } else if (message.startsWith("PURCHASESHOPPINGCART")) {
                String success = message.substring("PURCHASESHOPPINGCART".length());
                System.out.println(success);
            } else if (message.startsWith("NOTENOUGHMONEY")) {
                String fail = message.substring("NOTENOUGHMONEY".length());
                System.out.println(fail);
            } else if (message.startsWith("FAILEDPURCHASE")) {
                String fail = message.substring("FAILEDPURCHASE".length());
                System.out.println(fail);
            } else if (message.startsWith("PURCHASE")) {
                String success = message.substring("PURCHASE".length());
                System.out.println(success);
            } else if (message.startsWith("DELIVERY")) {
                String delivery = message.substring("DELIVERY".length());
                System.out.println(delivery);
            } else if (message.startsWith("DISCONNECT")) {
                String m = message.substring("DISCONNECT".length());
                System.out.println(m + "\n");
                System.out.println("Thank you for your time! We hope you're visiting us again!\n");
                System.exit(0);
            } else if (message.startsWith("INVALID")) {
                System.out.println(message.substring("INVALID".length()));
                System.out.println("Invalid message received, now the connection is insecure and we have to finish it.\n");
                try {
                    connection.sendMessage("INVALID");
                } catch (IOException ioe) {}
            } else {
                System.out.println(message);
            }
        }
    }
    
// El método database va a recibir una cadena de texto como argumento, y esta solo va a imprimir
    //el contenido de este mensaje por una salida estandar.
    public void database(String message) {
        System.out.println(message);
    }

    private void countryConnection(String country) {
        switch(country) {
            case "México": 
                clientConnection = new ClientConnectionMexico(client, in);
                break;
            case "USA" :
                clientConnection = new ClientConnectionUSA(client, in);
                break;
            case "España" :
                clientConnection = new ClientConnectionSpain(client, in);
                break;
        }
    }

    //El método signIn va a envíar un mensaje que va a iniciar la sesíon a través de una conexión de red
    //utilizando un objeto del parametro connection.

    public void signIn() {
        String signIn = "SIGNIN".concat(client.toString2());
        
        try {
            connection.sendMessage(signIn);
        } catch (IOException ioe) {
            System.out.println("Could not sign in.\n");
        }
    }
   //El método signUp va a envíar un mensaje que va a cerrar la sesíon a través de una conexión de red
    //utilizando un objeto del parametro connection.

    public void signUp() {
        try {
            connection.sendMessage("SIGNUP".concat(client.toString2()));
        } catch (IOException ioe) {
            System.out.println("Could not sign up.\n");
        }
    }
}
