package rp.rutepati.microservices.core.recommendation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.recommendation.Recommendation;
import rp.rutepati.common.api.core.recommendation.RecommendationApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.util.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationController implements RecommendationApi {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

    private final ServiceUtil serviceUtil;

    public RecommendationController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if(productId < 1)
            throw new InvalidInputException("Invalid productId : " + productId);

        if (productId == 113) {
            LOG.debug("No recommendations found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 3, "Author 3", 3, "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/recommendation response size: {}", list.size());

        return list;
    }
}
