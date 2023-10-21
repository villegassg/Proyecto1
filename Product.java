public class Product {

    private String name;
    private long barcode;
    private String department;
    private double price;

    public Product(String name, long barcode, String department, double price) {
        this.name = name;
        this.barcode = barcode;
        this.department = department;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getDepartment() {
        return department;
    }

    public double getPrice() {
        return price;
    }

    @Override public String toString() {
        String n = String.format("Name: %s\n", name);
        String b = String.format("Barcode: %s\n", barcode);
        String d = String.format("Department: %s\n", department);
        String p = String.format("Price: %s\n", price);

        return n.concat(b).concat(d).concat(p);
    }
}