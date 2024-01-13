package rp.rutepati.common.api.core.product;

public class Product {

    private final int productId;
    private final String name;
    private final float weight;
    private final String serviceAddress;

    public Product() {
        this.productId = 0;
        this.name = null;
        this.weight = 0.0f;
        this.serviceAddress = null;
    }

    public Product(int productId, String name, float weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }
}
