package main.java.ru.clevertec.check.product;

import java.util.Objects;

public class Product {
    private int id;
    private String description;
    private float price;
    private int quantityInStock;
    private boolean wholesale;

    private Product(int id,
                    String description,
                    float price,
                    int quantityInStock,
                    boolean wholesale){
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.wholesale = wholesale;
    }

    public static Product getProduct(
            int id,
            String description,
            float price,
            int quantityInStock,
            boolean wholesale){
        return new Product(
                id,
                description,
                price,
                quantityInStock,
                wholesale);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Float.compare(price, product.price) == 0 &&
                quantityInStock == product.quantityInStock &&
                wholesale == product.wholesale &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, quantityInStock, wholesale);
    }
}
