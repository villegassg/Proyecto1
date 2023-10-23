import java.util.Iterator;
import java.util.NoSuchElementException;
//Clase Database contiene la base de datos de la tienda
public class Database{

    //Declaramos una variable privada del objeto que crea un arredlo Product
    private Product[][] products;
//Clase de base de datos de la tienda virtual que implementa al Iterator
    private class DatabaseIterator implements Iterator<Product>{
        int i, j;
//Inicializamos los arreglos de la base de datos de la tienda en 0
        private DatabaseIterator() {
            i = 0;
            j = 0;
        }
//El metodo hasNext verifica si hay más elementos para recorrer la lista de los productos
        public boolean hasNext() {
            return i < products.length && j < products[i].length;
        }
//El metodo next verifica si hay un siguiente elemento disponible en la coleccion llamando al
//método hastnext
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
//Inicializamos el arreglo 
    public Database() {
        products = new Product[3][15];
    }

    //
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

    //El método removeProduct organiza y almacena los productos en el arreglo segun su 
    //tipo de departamento.
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
//El método iterator regresa un nuevo itrdaor de la base de datos
    public Iterator<Product> iterator() {
        return new DatabaseIterator();
    }
}
