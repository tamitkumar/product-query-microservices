package com.tech.brain.service.impl;

import com.tech.brain.entity.ProductEntity;
import com.tech.brain.exception.ErrorCode;
import com.tech.brain.model.Product;
import com.tech.brain.repository.QueryRepository;
import com.tech.brain.service.QueryService;
import com.tech.brain.utils.JSONUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QueryServiceImpl implements QueryService {

    private final QueryRepository queryRepository;

    private final JSONUtils jsonUtils;

    public QueryServiceImpl(QueryRepository queryRepository, JSONUtils jsonUtils) {
        this.queryRepository = queryRepository;
        this.jsonUtils = jsonUtils;
    }

    @Override
    public List<Product> getAllProduct() {
        return queryRepository.findAll().stream().map(entity ->{
            Product to = new Product();
            BeanUtils.copyProperties(entity, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long id) {
        Product result = new Product();
        Optional<ProductEntity> product = queryRepository.findById(id);
        BeanUtils.copyProperties(product, result);
        return result;
    }

    @Override
    public Product getProductByProductCode(String productCode) {
        Product result = new Product();
        Optional<ProductEntity> product = queryRepository.findByProductCode(productCode);
        BeanUtils.copyProperties(product, result);
        return result;
    }

    @Transactional
    @Override
    public Product createProduct(Product product) {
        log.info("📥 Creating Product: {}", jsonUtils.javaToJSON(product));
        ProductEntity[] products = new ProductEntity[1];
        queryRepository.findByProductCode(product.getProductCode()).ifPresentOrElse(existingProduct -> {
            log.error("Product is already in database : {}", existingProduct.getProductCode());
            copyNonNullProperties(product, existingProduct);
            products[0] = queryRepository.save(existingProduct);
//            throw new QueryException(ErrorCode.ERR009.getErrorCode(), ErrorSeverity.FATAL,
//                    ErrorCode.ERR009.getErrorMessage());
        }, ()->{
            ProductEntity entity = new ProductEntity();
            BeanUtils.copyProperties(product, entity);
            log.info("Creating product : {}", jsonUtils.javaToJSON(entity));
            products[0] = queryRepository.save(entity);
        });
        BeanUtils.copyProperties(products[0], product);
        return product;
    }

    @Transactional
    @Override
    public Product updateProduct(Product product) {
        log.info("📥 Updating Product: {}", jsonUtils.javaToJSON(product));
        AtomicReference<Product> result = new AtomicReference<>(new Product());
        ProductEntity[] products = new ProductEntity[1];
        queryRepository.findByProductCode(product.getProductCode()).ifPresentOrElse(existingProduct -> {
            copyNonNullProperties(product, existingProduct);
            products[0] = queryRepository.save(existingProduct);
            BeanUtils.copyProperties(products[0], result.get());
        }, () -> {
            log.error(ErrorCode.ERR002.getErrorMessage());
            result.set(createProduct(product));
        });
        return result.get();
    }

    @Transactional
    @Override
    public String deleteProduct(Product product) {
        log.info("📥 Deleting Product: {}", jsonUtils.javaToJSON(product));
        AtomicReference<String> response = new AtomicReference<>("");
        queryRepository.findByProductCode(product.getProductCode()).ifPresentOrElse(existingProduct -> {
            queryRepository.deleteById(existingProduct.getId());
            response.set("Deleted product " + existingProduct.getId());
        }, ()->
            response.set("Deletion failed product: " + product.getProductCode() + " May be there is no product")
        );
        return response.get();
    }
}
