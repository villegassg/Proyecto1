import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Server {

    /* Imprime un mensaje de cómo usar el programa. */
    private static void usage() {
        System.out.println("Usage: java Server [port]");
        System.exit(0);
    }

    /* Bitácora del servidor. */
    private static void binnacle(String format, Object ... arguments) {
        ZonedDateTime now = ZonedDateTime.now();
        String ts = now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        System.err.printf(ts + " " + format + "\n", arguments);
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2)
            usage();

        int port = 1234;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            usage();
        }

        try {
            StoreServer server = new StoreServer(port);
            server.addListener((f, p) -> binnacle(f, p));
            server.serve();
        } catch (IOException ioe) {
            binnacle("Error al crear el servidor.");
        }
    }
}