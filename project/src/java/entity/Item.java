package entity;

public class Item {
    private Product product;
    private int quantity;
    private double price;
    private ProductVariant variant; // Quan trọng để biết Size/Color nào

    public Item() {
    }


    public Item(Product product, int quantity, double price, ProductVariant variantID) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.variant = variantID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return "Item{" + "product=" + product + ", quantity=" + quantity + ", price=" + price + ", variant=" + variant + '}';
    }
    
    
}