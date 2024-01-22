package rp.rutepati.microservices.core.recommendation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.recommendation.Recommendation;
import rp.rutepati.common.api.core.recommendation.RecommendationApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.util.ServiceUtil;
import rp.rutepati.microservices.core.recommendation.mapper.RecommendationMapper;
import rp.rutepati.microservices.core.recommendation.persistence.RecommendationEntity;
import rp.rutepati.microservices.core.recommendation.persistence.RecommendationRepository;

import java.util.List;

@RestController
public class RecommendationController implements RecommendationApi {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

    private final ServiceUtil serviceUtil;
    private final RecommendationMapper recommendationMapper;
    private final RecommendationRepository recommendationRepository;

    public RecommendationController(ServiceUtil serviceUtil,
                                    RecommendationMapper recommendationMapper,
                                    RecommendationRepository recommendationRepository) {
        this.serviceUtil = serviceUtil;
        this.recommendationMapper = recommendationMapper;
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if(productId < 1) {
            throw new InvalidInputException("Invalid productId : " + productId);
        }

        List<RecommendationEntity> recommendationEntityList = recommendationRepository.findByProductId(productId);
        List<Recommendation> responseList = recommendationMapper.entityListToDtoList(recommendationEntityList);
        responseList.forEach(resp -> resp.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("getRecommendations: response size: {}", responseList.size());

        return responseList;
    }

    @Override
    public Recommendation createRecommendation(Recommendation recommendation) {
        try {
            RecommendationEntity recommendationEntity = recommendationMapper.dtoToEntity(recommendation);
            RecommendationEntity newRecommendationEntity = recommendationRepository.save(recommendationEntity);

            LOG.debug("createRecommendation: created a recommendation entity: {}/{}", recommendation.getProductId(), recommendation.getRecommendationId());
            return recommendationMapper.entityToDto(newRecommendationEntity);
        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + recommendation.getProductId() + ", Recommendation Id:" + recommendation.getRecommendationId());
        }
    }

    @Override
    public void deleteRecommendation(int productId) {
        LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        recommendationRepository.deleteAll(recommendationRepository.findByProductId(productId));
    }
}
