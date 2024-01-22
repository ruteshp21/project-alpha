package rp.rutepati.common.api.core.review;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ReviewApi {

    @GetMapping(
            value = "/review",
            produces = "application/json")
    List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping(
            value = "/review",
            consumes = "application/json",
            produces = "application/json")
    Review createReview(@RequestBody Review review);

    @DeleteMapping(
            value = "/review")
    void deleteReview(@RequestParam(value = "productId", required = true) int productId);
}
