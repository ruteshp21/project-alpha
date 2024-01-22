package rp.rutepati.microservices.core.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import rp.rutepati.common.api.core.review.Review;
import rp.rutepati.microservices.core.review.persistence.ReviewEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Review entityToDto(ReviewEntity reviewEntity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ReviewEntity dtoToEntity(Review review);

    List<Review> entityListToDtoList(List<ReviewEntity> reviewEntityList);
    List<ReviewEntity> dtoListToEntityList(List<Review> reviewList);

}
