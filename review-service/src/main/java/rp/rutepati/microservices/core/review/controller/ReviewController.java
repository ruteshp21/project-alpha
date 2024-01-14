package rp.rutepati.microservices.core.review.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.review.Review;
import rp.rutepati.common.api.core.review.ReviewApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.util.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewController implements ReviewApi {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    private final ServiceUtil serviceUtil;

    public ReviewController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if (productId == 213) {
            LOG.debug("No reviews found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Review> list = new ArrayList<>();
        list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/reviews response size: {}", list.size());

        return list;
    }
}
