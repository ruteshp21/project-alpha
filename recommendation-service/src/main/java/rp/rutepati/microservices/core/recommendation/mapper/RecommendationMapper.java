package rp.rutepati.microservices.core.recommendation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import rp.rutepati.common.api.core.recommendation.Recommendation;
import rp.rutepati.microservices.core.recommendation.persistence.RecommendationEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true),
            @Mapping(target = "rate", source = "recommendationEntity.rating")
    })
    Recommendation entityToDto(RecommendationEntity recommendationEntity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "rating", source = "recommendation.rate")
    })
    RecommendationEntity dtoToEntity(Recommendation recommendation);

    List<Recommendation> entityListToDtoList(List<RecommendationEntity> recommendationEntities);

    List<RecommendationEntity> dtoListToEntityList(List<Recommendation> recommendations);

}
