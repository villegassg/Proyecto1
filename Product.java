//Clase producto
public class Product {
//Creamos una variable name privada del tipo String
    private String name;
//Creamos una variable barcode privada del tipo long
    private long barcode;
//Creamos una variable privada department del tipo String
    private String department;
//Creamos una variable privada price del tipo double
    private double price;
//El método Product crea un constructor del Product el cual nos va dar el name, barcode, department and price del product
    public Product(String name, long barcode, String department, double price) {
        this.name = name;
        this.barcode = barcode;
        this.department = department;
        this.price = price;
    }
//Regresa el name del producto
    public String getName() {
        return name;
    }
//Regresa el barcode del producto
    public long getBarcode() {
        return barcode;
    }

    public String getDepartment() {
        return department;
    }
//Regresa el precio del producto
    public double getPrice() {
        return price;
    }
//El metodo toString regresa una cadena de texto de las caracteristicas del producto 
    @Override public String toString() {
        String n = String.format("Name: %s\n", name);
        String b = String.format("Barcode: %s\n", barcode);
        String d = String.format("Department: %s\n", department);
        String p = String.format("Price: %s\n", price);

        return n.concat(b).concat(d).concat(p);
    }
//El metodo toString2 regresa una cadena de texto de las caracteristicas del productos
    public String toString2() {
        String n = String.format("Name: %s\t", name);
        String b = String.format("Barcode: %s\t", barcode);
        String d = String.format("Department: %s\t", department);
        String p = String.format("Price: %s\t\t", price);

        return n.concat(b).concat(d).concat(p);
    }
//El metodo equals compara si dos productos don iguales.
    @Override public boolean equals(Object object) {
        if (!(object instanceof Product))
            return false;

        Product product = (Product)object;

        return this.name.equals(product.getName()) &&
                this.barcode == product.getBarcode() &&
                this.department.equals(product.getDepartment()) &&
                this.price == product.getPrice();
    } 
}
