import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientManagerSpain implements ClientManagerInterface{

    BufferedReader in;

    public ClientManagerSpain(BufferedReader in) {
        this.in = in;
    }
    
    public void logIn(String country) {
        System.out.println("¡Bienvenid@ seas a la mejor tienda en línea!\n " +
                            "Por favor ingresa la acción que deseas ejecutar.\n");
        
        int action = -1;
        Client client = new Client();

        do {
            System.out.println("Estas son las acciones que puedes realizar:\n" +
                                "1. Iniciar sesión \n" +
                                "2. Registrarme \n" +
                                "0. Desconectarme \n\n");
            try {
                action = Integer.parseInt(in.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Por favor ingresa un número válido.\n");
                continue;
            } catch (IOException ioe) {
                System.out.println("Lo sentimos, un error ha ocurrido.\n");
                System.exit(0);
            }
            switch(action) {
                case 0 : 
                    System.out.println("¡Muchas gracias por tu tiempo! " +
                                        "¡Esperamos que nos visites otra vez!\n");
                    System.exit(0);
                    break;
                case 1 : 
                    client = signIn();
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(socket, proxy);
                        clientConnection.signIn();
                    } catch (IOException ioe) {}
                    break;
                case 2: 
                    client = signUp(country);
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(socket, proxy);
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
            System.out.printf("\n\nPor favor ingresa tu nombre de usuario: ");
            username = in.readLine();
            System.out.printf("\n\nAhora por favor ingresa tu contraseña: ");
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
            System.out.printf("\n\nPor favor ingresa tu nombre (no necesita ser nombre completo): ");
            name = in.readLine();
            System.out.printf("\n\nPor favor ingresa tu nombre de usuario: ");
            username = in.readLine();
            System.out.printf("\n\nPor favor ingresa tu contraseña: ");
            password = in.readLine();
            while(true) {
                System.out.println("\n\nPor favor ingresa tu número de teléfono " +
                                    "(Sin espacios, guiones, etc.): ");
                try {
                    phoneNumber = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingresa un número de teléfono válido.\n");
                }
            }
            System.out.println("\n\nPor favor ingresa tu dirección: ");
            address = in.readLine();
            while (true) {
                System.out.println("\n\nPor favor ingresa tu cuenta de banco " +
                                        "(Sin espacios, guiones, etc.): ");
                try {
                    bankAccount = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingresa una cuenta de banco válida.\n");
                }
            }
            while (true) {
                System.out.println("\n\nPor favor ingresa la cantidad de dinero que te " +
                                    "gustaría tener en tu cuenta: ");
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
