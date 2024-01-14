package rp.rutepati.microservices.core.product.controller;

import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.product.Product;
import rp.rutepati.common.api.core.product.ProductApi;
import rp.rutepati.common.util.ServiceUtil;

@RestController
public class ProductController implements ProductApi {

    private final ServiceUtil serviceUtil;

    public ProductController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        return new Product(productId, "name-" + productId, 183, serviceUtil.getServiceAddress());
    }
}
