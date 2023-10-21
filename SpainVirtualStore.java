import java.util.Iterator;
import java.util.LinkedList;

public class SpainVirtualStore implements VirtualStore {
    
    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;

    public SpainVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
    }

    public String sayHi() {
        return "¡Enhorabuena! ¡Bienvenido seas a la Tienda Virtual de España!\n";
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
        System.out.println("¡Muchas gracias por habernos visitado!, esperamos que haya sido " +
                            "de tu agrado. ¡Hasta luego!\n");
    }

    public void add(ProxyClient client) {
        clients.add(client);
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