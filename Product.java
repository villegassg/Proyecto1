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

    public String toString2() {
        String n = String.format("Name: %s\t", name);
        String b = String.format("Barcode: %s\t", barcode);
        String d = String.format("Department: %s\t", department);
        String p = String.format("Price: %s\t\t", price);

        return n.concat(b).concat(d).concat(p);
    }

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