package edu.dosw.parcial.DOSW_ParcialT2.core.validators;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductUnavailableException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.ProductPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductPersistenceRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public Product validateAvailableAndGetProduct(String codQR) {
        ProductEntity entity = productRepository.findByCodQR(codQR)
                .orElseThrow(() -> new ProductNotFoundException(codQR));
        Product product = productPersistenceMapper.toDomain(entity);
        if (product.getStatus() != Product.ProductStatus.AVAILABLE) {
            throw new ProductUnavailableException(codQR);
        }
        if (product.getStock() <= 0) {
            throw new ProductUnavailableException(codQR);
        }
        return product;
    }
}
