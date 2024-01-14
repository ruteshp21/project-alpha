package rp.rutepati.microservices.aggregate.product.controller;

import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.aggregate.product.*;
import rp.rutepati.common.api.core.product.Product;
import rp.rutepati.common.api.core.recommendation.Recommendation;
import rp.rutepati.common.api.core.review.Review;
import rp.rutepati.common.api.exceptions.NotFoundException;
import rp.rutepati.common.util.ServiceUtil;
import rp.rutepati.microservices.aggregate.product.service.ProductAggregateService;

import java.util.List;

@RestController
public class ProductAggregateController implements ProductAggregateApi {

    private final ServiceUtil serviceUtil;
    private final ProductAggregateService productAggregateService;

    public ProductAggregateController(ServiceUtil serviceUtil, ProductAggregateService productAggregateService) {
        this.serviceUtil = serviceUtil;
        this.productAggregateService = productAggregateService;
    }

    @Override
    public ProductAggregate getProductAggregate(int productId) {

        Product product = productAggregateService.getProduct(productId);
        if(product == null) {
            throw new NotFoundException("No product found for productId: " + productId);
        }
        List<Recommendation> recommendations = productAggregateService.getRecommendations(productId);

        List<Review> reviews = productAggregateService.getReviews(productId);

        String serviceAddress = serviceUtil.getServiceAddress();

        return createProductAggregate(product, recommendations, reviews, serviceAddress);
    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        float weight = product.getWeight();


        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null : recommendations.stream()
                .map(rec -> new RecommendationSummary(rec.getRecommendationId(), rec.getAuthor(), rec.getRate()))
                .toList();

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null : reviews.stream()
                .map(rev -> new ReviewSummary(rev.getReviewId(), rev.getAuthor(), rev.getSubject()))
                .toList();

        // 4. Create info regarding the involved microservices addresses
        String productServiceAddress = product.getServiceAddress();
        String recommendationServiceAddress = (recommendations != null && !recommendations.isEmpty()) ? recommendations.get(0).getServiceAddress() : "";
        String reviewServiceAddress = (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";
        ServiceAddressSummary serviceAddressSummary = new ServiceAddressSummary(serviceAddress, productServiceAddress, reviewServiceAddress, recommendationServiceAddress);
        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddressSummary);
    }
}
