import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
//La calse ClientManagerUSA va a implementar ClientManagerInterface
public class ClientManagerUSA implements ClientManagerInterface{
//Declaramos una variable in del tipo BufferReader
    BufferedReader in;
//El constructor ClientManagerUSA va a inicializar un onjeto BufferedReader
    public ClientManagerUSA(BufferedReader in) {
        this.in = in;
    }
     //El método logIn va a iniciar la sesión y registro del cliente en la tienda.
    public void logIn(String country) {
        System.out.println("Welcome to the best online Store!\n Please enter the action you want to execute.\n");
        
        int action = -1;
        Client client = new Client();

        do {
            System.out.println("These are the actions you can do:\n" +
                                "1. Sign in\n" +
                                "2. Sign up\n" +
                                "0. Disconnect\n\n");
            try {
                action = Integer.parseInt(in.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid number.\n");
                continue;
            } catch (IOException ioe) {
                System.out.println("We're sorry, an error ocurred.\n");
                System.exit(0);
            }
            switch(action) {
                case 0 : 
                    System.out.println("Thank you for your time :). We hope you're visiting us again!\n");
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
                    System.out.println("Please enter a valid number.\n");
                    continue;
            }
            break;
        } while(true);
    }
//El método Client va a recopilar el nombre del cliente y su contraseña desde la entrada estandar
    //y va a crear un objeyo client.
    public Client signIn() {
        String username = "";
        String password = "";
        
        try {
            System.out.printf("\n\nPlease enter your username: ");
            username = in.readLine();
            System.out.printf("\n\nNow please enter your password: ");
            password = in.readLine();
        } catch (IOException ioe) {
            System.out.println("It has ocurred an error while trying to sign in. We're sorry.\n");
            System.exit(1);
        }
        
        Client nuevoCliente = new Client();
        nuevoCliente.setUsername(username);
        nuevoCliente.setPassword(password);
        nuevoCliente.setCountry("USA");
        return nuevoCliente;
    }
//El método sig Up va a delarar las variables para almacenar la informacion del cliente,
//así como su nombre, nombre de usuario, contraseña, numero de telefono, numero de cuenta 
//bancaria, dinero y país
    public Client signUp(String country) {
        String name = "";
        String username = "";
        String password = "";
        long phoneNumber = 0;
        String address = "";
        long bankAccount = 0;
        double money = 0;
        
        try {
            System.out.printf("\n\nPlease enter your name (It doesn't need to be full name): ");
            name = in.readLine();
            System.out.printf("\n\nPlease enter your username: ");
            username = in.readLine();
            System.out.printf("\n\nPlease enter your password: ");
            password = in.readLine();
            while(true) {
                System.out.println("\n\nPlease enter your phone number (Without spaces, hyphens, etc.): ");
                try {
                    phoneNumber = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Please enter a valid phone number.\n");
                }
            }
            System.out.println("\n\nPlease enter your address: ");
            address = in.readLine();
            while (true) {
                System.out.println("\n\nPlease enter your bank account (No spaces, hyphens, etc.): ");
                try {
                    bankAccount = Long.parseLong(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Please enter a valid bank account.\n");
                }
            }
            while (true) {
                System.out.println("\n\nPlease enter the money you would like to have in your account: ");
                try {
                    money = Double.parseDouble(in.readLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Please enter a valid amount of money.\n");
                }
            }
        } catch (IOException ioe) {
            System.out.println("It has ocurred an error while trying to sign up you. We're sorry.\n");
            System.exit(1);
        } 

        return new Client(username, password, name, phoneNumber, address, bankAccount, country, money);
    }
}
