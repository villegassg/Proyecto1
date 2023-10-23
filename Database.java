import java.util.Iterator;
import java.util.NoSuchElementException;

public class Database{

    private Product[][] products;

    private class DatabaseIterator implements Iterator<Product>{
        int i, j;

        private DatabaseIterator() {
            i = 0;
            j = 0;
        }

        public boolean hasNext() {
            return i < products.length && j < products[i].length;
        }

        public Product next() {
            if (hasNext()) { 
                Product product = products[i][j++];
                if (j >= products[i].length) {
                    j = 0;
                    i++;
                }
                return product;
            } throw new NoSuchElementException(); 
        }
    }

    int food = 0;
    int appliance = 0;
    int electronic = 0;

    public Database() {
        products = new Product[3][15];
    }

    //Barcode tiene qué cambiar al departamento.
    public void addProduct(Product product) {
        switch(product.getDepartment()) {
            case "ALIMENTICIO": 
                products[0][food++] = product;
                break;
            case "ELECTRODOMESTICO": 
                products[1][appliance++] = product;
                break;
            case "ELECTRONICA":
                products[2][electronic++] = product;
                break;
        }
    }

    //Barcode tiene qué cambiar al departamento.
    public void removeProduct(Product product) {
        boolean removed = false;
        for (int i = 0 ; i < products.length ; i++)
            for (int j = 0 ; j < products[i].length ; j++)
                if (products[i][j].equals(product)) {
                    products[i][j] = null;
                    removed = true;
                }

        if (!removed)
                System.out.println("Could not remove the product " + product.getName() +
                                    " because it is actually not in the database.\n");
    }

    public Iterator<Product> iterator() {
        return new DatabaseIterator();
    }
}