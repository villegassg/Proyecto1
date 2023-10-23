import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
 //La clase ClientConnectionMexico va a implementar la clase ClinetConnectionInterface

public class ClientConnectionMexico implements ClientConnectionInterface {
    //Declaramos una variable cliente del tipoProxyClient
    ProxyClient client;
    //Declararemos una llada scanner del tipo BufferedReader
    BufferedReader scanner;

    //El constructor ClientConnectionMexico va inicializar dos objetos ProxyCLient y BuggerdReader
    public ClientConnectionMexico(ProxyClient client, BufferedReader in) {
        this.client = client;
        this.scanner = in;
    }
 //El método purchase va a dirigir y administrar el modo de compra que el cliente le guste más
    //así el cliente va a elegir si quiere agregar los productos al carrito, eliminar, imprimir contenido
    //del producto, realizar compra, comprar porductos directament5e o salir del modo de compra.
    public void purchase(Connection connection) {
        int action = -1;

        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ocurrió una NumberFormatException. " +
                                "Por favor ingresa una acción válida.\n");
        } catch (IOException ioe) {

        } catch (NoSuchElementException nsee) {
            
        }
        do {
            switch(action) {
                case 1:
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres agregar a tu carrito: ");
                    try {
                        String product1 = scanner.readLine();
                        connection.sendMessage("ADDTOCART" + client.toString2() + 
                                                    "Product: " + product1);
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres eliminar de tu carrito: ");
                    try {
                        String product2 = scanner.readLine();
                        connection.sendMessage("REMOVEFROMSHOPPINGCART" + 
                                                client.toString2() +
                                                "Product: " + product2);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 3: 
                    try {
                        connection.sendMessage("PRINTSHOPPINGCART" + client.toString2());
                    } catch(IOException ioe) {}
                    break;
                case 4: 
                    try {
                        connection.sendMessage("PURCHASESHOPPINGCART" + client.toString2());
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 5 :
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres comprar directamente: ");
                    try {
                        String product3 = scanner.readLine();
                        connection.sendMessage("PURCHASE" + client.toString2() + 
                                                "Product: " + product3);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("\nSaliendo del modo de compra.\n");
                    try {
                        connection.sendMessage("CONNECT");
                    } catch (IOException ioe) {}
                    break;
                default:
                    System.out.println("Por favor ingresa una acción valida.\n");
                    continue;
            }   
            break;
        } while (true);
    }
//El método connect va a permitir al cliente seleccionar alguna de las 3 opciones, las cuales son
 //mostrar el cátalogo, comprar y desconectar, despues de seleccionar una opcion se va a enviar un mensaje
 // al servidor a través de la conexión.
    public void connect(Connection connection) {
        System.out.println("\n¿Qué quieres hacer ahora?\n");
        int action = -1;
        
        System.out.println("Acciones para hacer con el servidor:\n" + 
                            "1. Mostrar catálogo.\n" +
                            "2. Comprar.\n" +
                            "0. Desconectar.\n");
        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ocurrió una NumberFormatException. " +
                                "Por favor ingresa una acción válida.\n");
        } catch (IOException ioe) {

        } catch (NoSuchElementException nsee) {
            
        }
        switch(action) {
            case 1: 
                try {
                    connection.sendMessage("CATALOGUE");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 2: 
                try {
                    connection.sendMessage("PURCHASEMODE");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 0: 
                try {
                    connection.sendMessage("DISCONNECT");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            default :
                System.out.println("Por favor ingresa una acción válida.\n");
        }
    }
  //El método ourchaseMode solo va a imprimir el mensaje ¨Entrando al modo de compra¨

    public void purchaseMode() {
        System.out.println("Entrando al modo de compra: \n");
    }
}
