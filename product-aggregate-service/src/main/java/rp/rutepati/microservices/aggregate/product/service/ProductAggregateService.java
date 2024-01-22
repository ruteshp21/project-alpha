package rp.rutepati.microservices.aggregate.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rp.rutepati.common.api.core.product.Product;
import rp.rutepati.common.api.core.product.ProductApi;
import rp.rutepati.common.api.core.recommendation.Recommendation;
import rp.rutepati.common.api.core.recommendation.RecommendationApi;
import rp.rutepati.common.api.core.review.Review;
import rp.rutepati.common.api.core.review.ReviewApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.api.exceptions.NotFoundException;
import rp.rutepati.common.util.HttpErrorInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductAggregateService implements ProductApi, RecommendationApi, ReviewApi {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAggregateService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    public ProductAggregateService(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") int recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review";
    }

    @Override
    public Product getProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            LOG.debug("Will call getProduct API on URL: {}", url);

            Product product = restTemplate.getForObject(url, Product.class);
            LOG.debug("Found a product with id: {}", product.getProductId());

            return product;
        } catch (HttpClientErrorException ex) {
            switch (HttpStatus.resolve(ex.getStatusCode().value())) {
                case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
                case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));
                default -> {
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
                }
            }
        }
    }

    @Override
    public Product createProduct(Product product) {
        try {
            return restTemplate.postForObject(
                    productServiceUrl, product, Product.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            LOG.debug("Will call the deleteProduct API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try {
            String url = recommendationServiceUrl + "?productId=" + productId;

            LOG.debug("Will call getRecommendations API on URL: {}", url);
            List<Recommendation> recommendationList = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
                    })
                    .getBody();

            LOG.debug("Found {} recommendations for a product with id: {}", recommendationList.size(), productId);
            return recommendationList;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        try {
            String url = recommendationServiceUrl;
            LOG.debug("Will post a new recommendation to URL: {}", url);

            Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
            LOG.debug("Created a recommendation with id: {}", recommendation.getProductId());

            return recommendation;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteRecommendation(int productId) {
        try {
            String url = recommendationServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the deleteProduct API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;

            LOG.debug("Will call getReviews API on URL: {}", url);
            List<Review> reviewList = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
                    })
                    .getBody();
            LOG.debug("Found {} reviews for a product with id: {}", reviewList.size(), productId);
            return reviewList;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Review createReview(Review body) {
        try {
            String url = reviewServiceUrl;
            LOG.debug("Will post a new review to URL: {}", url);

            Review review = restTemplate.postForObject(url, body, Review.class);
            LOG.debug("Created a review with id: {}", review.getProductId());

            return review;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteReview(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the deleteProduct API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (HttpStatus.resolve(ex.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(ex));

            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
