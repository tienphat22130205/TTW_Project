package vn.edu.hcmuaf.fit.project_fruit.dao.model;

public class CartItem {
    private Product product;
    private int quantity;
    private double discount;

    // Getters, Setters

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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        return (product.getPrice() - discount) * quantity;
    }
}
