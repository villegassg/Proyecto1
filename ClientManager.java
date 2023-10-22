import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientManager {

    public static void main(String[] args) {
        System.out.println("Welcome to the best online Store! \n");
        BufferedReader in = 
            new BufferedReader(
                new InputStreamReader(System.in));
        
        int language = -1;
        while (true) {
            System.out.println("Please enter the language you want. (Remember that whatever " +
                                "the language you pick is, it is gonna be the place where you " +
                                "want to visit the store) \n\n" +
                                "1. English \n" +
                                "2. Español de México \n" +
                                "3. Español de España \n");
            try { 
                language = Integer.parseInt(in.readLine());
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid number.\n");
            } catch (IOException ioe) {}
        }
        ClientManagerInterface client;

        switch(language) {
            case 1: 
                client = new ClientManagerUSA(in);
                client.logIn("USA");
                break;
            case 2:
                client = new ClientManagerMexico(in);
                client.logIn("México");
                break;
            case 3:
                client = new ClientManagerSpain(in);
                client.logIn("España");
                break;
        }
    }
}