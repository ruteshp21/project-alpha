package rp.rutepati.common.api.aggregate.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductAggregateApi {

    @GetMapping(
            value = "/product-aggregate/{productId}",
            produces = "application/json")
    ProductAggregate getProductAggregate(@PathVariable int productId);

}
