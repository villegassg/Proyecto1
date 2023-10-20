import java.util.Hashtable;
import java.util.Iterator;

public class Database {

    private Hashtable<String, Product> products;

    public Database() {
        products = new Hashtable<>();
    }

    //Barcode tiene qué cambiar al departamento.
    public void addProduct(Product product) {
        products.put(product.getDepartment(), product);
    }

    //Barcode tiene qué cambiar al departamento.
    public void removeProduct(Product product) {
        products.remove(product.getDepartment(), product);
    }

    public Iterator<Product> iterator() {
        return products.values().iterator();
    }
}