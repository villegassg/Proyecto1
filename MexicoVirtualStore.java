import java.util.Iterator;
import java.util.LinkedList;

public class MexicoVirtualStore implements VirtualStore {

    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;

    public MexicoVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
    }

    public void sayHi() {
        System.out.println("¡Bienvenido a la Tienda Virtual Mexicana! " +
                            "¡Tenemos muchísimas cosas para ofrecerte!\n");
    }

    public void options() {

    }

    public void purchase(Product product) {

    } 

    public String deliveryDate() {
        String deliveryDate = "";
        return deliveryDate;
    }

    public void sendOffers() {
        
    }

    public void goodBye() {
        System.out.println("¡Muchas gracias por habernos visitado! ¡Hasta la próxima!\n");
    }

    public void add(ProxyClient client) {
        clients.addFirst(client);
    }

    public void remove(ProxyClient client) {
        clients.remove(client);
    }

    public void products() {
        while (iterator.hasNext()) {
            Product product = iterator.next();
            System.out.println(product.toString());
        }
    }
}