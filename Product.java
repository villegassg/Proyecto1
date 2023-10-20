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
        String s = "Name: " + name + "\n" +
                    "Barcode: " + barcode + "\n" +
                    "Department: " + department + "\n" +
                    "Price : " + price + "\n";
        return s;
    }
}