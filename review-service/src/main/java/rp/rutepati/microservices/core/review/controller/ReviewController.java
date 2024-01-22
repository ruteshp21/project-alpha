package rp.rutepati.microservices.core.review.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.review.Review;
import rp.rutepati.common.api.core.review.ReviewApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.util.ServiceUtil;
import rp.rutepati.microservices.core.review.mapper.ReviewMapper;
import rp.rutepati.microservices.core.review.persistence.ReviewEntity;
import rp.rutepati.microservices.core.review.persistence.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewController implements ReviewApi {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    private final ServiceUtil serviceUtil;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewController(ServiceUtil serviceUtil,
                            ReviewRepository reviewRepository,
                            ReviewMapper reviewMapper) {
        this.serviceUtil = serviceUtil;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        List<ReviewEntity> reviewEntityList = reviewRepository.findByProductId(productId);
        List<Review> reviewList = reviewMapper.entityListToDtoList(reviewEntityList);
        reviewList.forEach(review -> review.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("getReviews: response size: {}", reviewList.size());

        return reviewList;
    }

    @Override
    public Review createReview(Review review) {
        try {
            ReviewEntity reviewEntity = reviewMapper.dtoToEntity(review);
            ReviewEntity newEntity = reviewRepository.save(reviewEntity);

            LOG.debug("createReview: created a review entity: {}/{}", review.getProductId(), review.getReviewId());
            return reviewMapper.entityToDto(newEntity);

        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + review.getProductId() + ", Review Id:" + review.getReviewId());
        }
    }

    @Override
    public void deleteReview(int productId) {
        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        reviewRepository.deleteAll(reviewRepository.findByProductId(productId));
    }
}
