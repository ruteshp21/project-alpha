package rp.rutepati.common.api.aggregate.product;

import java.util.List;

public class ProductAggregate {

    private final int productId;
    private final String name;
    private final float weight;
    private final List<RecommendationSummary> recommendations;
    private final List<ReviewSummary> reviews;
    private final ServiceAddressSummary serviceAddresses;

    public ProductAggregate(
            int productId,
            String name,
            float weight,
            List<RecommendationSummary> recommendations,
            List<ReviewSummary> reviews,
            ServiceAddressSummary serviceAddresses) {

        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.recommendations = recommendations;
        this.reviews = reviews;
        this.serviceAddresses = serviceAddresses;
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

    public List<RecommendationSummary> getRecommendations() {
        return recommendations;
    }

    public List<ReviewSummary> getReviews() {
        return reviews;
    }

    public ServiceAddressSummary getServiceAddressSummary() {
        return serviceAddresses;
    }

}
