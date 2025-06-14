package com.tech.brain.service;

import com.tech.brain.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface QueryService {

    List<Product> getAllProduct();

    Product getProductById(Long id);

    Product getProductByProductCode(String productCode);

    Product createProduct(Product product);
    Product updateProduct(Product product);
    String deleteProduct(Product product);

    default void copyNonNullProperties(Object dto, Object entity) {
        BeanUtils.copyProperties(dto, entity, getNullPropertyName(dto));
    }

    private String[] getNullPropertyName(Object dto) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(dto);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object dtoValue = beanWrapper.getPropertyValue(pd.getName());
            if (ObjectUtils.isEmpty(dtoValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
