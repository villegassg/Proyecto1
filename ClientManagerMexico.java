import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientManagerMexico implements ClientManagerInterface {

    BufferedReader in;

    public ClientManagerMexico(BufferedReader in) {
        this.in = in;
    }
    
    public void logIn(String country) {
        System.out.println("¡Bienvenid@ a la mejor tienda en línea!\n " +
                            "Ingresa la acción que quieras ejecutar.\n");
        
        int action = -1;
        Client client = new Client();

        do {
            System.out.println("Estas son las acciones que puedes hacer:\n" +
                                "1. Iniciar sesión \n" +
                                "2. Registrarse \n" +
                                "0. Desconectarse \n\n");
            try {
                action = Integer.parseInt(in.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Por favor ingresa un número válido.\n");
                continue;
            } catch (IOException ioe) {
                System.out.println("Lo sentimos, ha ocurrido un error.\n");
                System.exit(0);
            }
            switch(action) {
                case 0 : 
                    System.out.println("¡Muchas gracias! ¡Esperamos que nos visites pronto!\n");
                    System.exit(0);
                    break;
                case 1 : 
                    client = signIn();
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(in, socket, proxy);
                        clientConnection.signIn();
                    } catch (IOException ioe) {}
                    break;
                case 2: 
                    client = signUp(country);
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(in, socket, proxy);
                        clientConnection.signUp();
                    } catch (IOException ioe) {}
                    break;
                default: 
                    System.out.println("Por favor ingresa un número válido.\n");
                    continue;
            }
            break;
        } while(true);
    }

    public Client signIn() {
        String username = "";
        String password = "";
        
        try {
            System.out.printf("\n\nIngresa tu nombre de usuario: ");
            username = in.readLine();
            System.out.printf("\n\nAhora ingresa tu contraseña: ");
            password = in.readLine();
        } catch (IOException ioe) {
            System.out.println("Ha ocurrido un error mientras intentabas iniciar sesión. " +
                                "Lo sentimos.\n");
            System.exit(1);
        }
        
        Client nuevoCliente = new Client();
        nuevoCliente.setUsername(username);
        nuevoCliente.setPassword(password);
        return nuevoCliente;
    }

    public Client signUp(String country) {
        String name = "";
        String username = "";
        String password = "";
        long phoneNumber = 0;
        String address = "";
        long bankAccount = 0;
        double money = 0;
        
        try {
            System.out.printf("\n\nIngresa tu nombre (no necesita ser nombre completo): ");
            name = in.readLine();
            System.out.printf("\n\nIngresa tu nombre de usuario: ");
            username = in.readLine();
            System.out.printf("\n\nIngresa tu contraseña: ");
            password = in.readLine();
            while(true) {
                System.out.println("\n\nIngresa tu número de teléfono (sin espacios, guiones, etc.): ");
                try {
                    phoneNumber = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingresa un número de teléfono válido.\n");
                }
            }
            System.out.println("\n\nIngresa tu dirección: ");
            address = in.readLine();
            while (true) {
                System.out.println("\n\nIngresa tu cuenta de banco (sin espacios, guiones, etc.): ");
                try {
                    bankAccount = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingresa una cuenta de banco válida.\n");
                }
            }
            while (true) {
                System.out.println("\n\nIngresa la cantidad de dinero que quieras tener en tu cuenta: ");
                try {
                    money = Double.parseDouble(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingresa una cantidad de dinero válida.\n");
                }
            }
        } catch (IOException ioe) {
            System.out.println("Ha ocurrido un error mientras tratabas de iniciar sesión. Lo sentimos.\n");
            System.exit(1);
        } 

        return new Client(username, password, name, phoneNumber, address, bankAccount, country, money);
    }
}
