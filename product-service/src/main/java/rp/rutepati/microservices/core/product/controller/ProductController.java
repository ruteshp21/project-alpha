package rp.rutepati.microservices.core.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import rp.rutepati.common.api.core.product.Product;
import rp.rutepati.common.api.core.product.ProductApi;
import rp.rutepati.common.api.exceptions.InvalidInputException;
import rp.rutepati.common.api.exceptions.NotFoundException;
import rp.rutepati.common.util.ServiceUtil;
import rp.rutepati.microservices.core.product.mapper.ProductMapper;
import rp.rutepati.microservices.core.product.persistence.ProductEntity;
import rp.rutepati.microservices.core.product.persistence.ProductRepository;

@RestController
public class ProductController implements ProductApi {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ServiceUtil serviceUtil;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductController(ServiceUtil serviceUtil, ProductRepository productRepository, ProductMapper productMapper) {
        this.serviceUtil = serviceUtil;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProduct(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        ProductEntity productEntity = productRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product product = productMapper.entityToDto(productEntity);
        product.setServiceAddress(serviceUtil.getServiceAddress());

        LOG.debug("getProduct: found productId: {}", product.getProductId());

        return product;
    }

    @Override
    public Product createProduct(Product product) {
        try {
            ProductEntity productEntity = productMapper.dtoToEntity(product);
            ProductEntity newEntity = productRepository.save(productEntity);

            LOG.debug("createProduct: entity created for productId: {}", product.getProductId());
            return productMapper.entityToDto(newEntity);
        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + product.getProductId());
        }
    }

    @Override
    public void deleteProduct(int productId) {
        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        productRepository
                .findByProductId(productId)
                .ifPresentOrElse(productRepository::delete,
                        () -> { throw new NotFoundException("No product found for productId: " + productId); });
    }
    
}
