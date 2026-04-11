package edu.dosw.parcial.DOSW_ParcialT2.core.services;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductUnavailableException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.ProductPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPersistenceRepository productRepository;
    private final ProductPersistenceMapper productMapper;

    public Product getByQrCode(String codQR) {
        return productRepository.findByCodQR(codQR)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException(codQR));
    }

    public void decrementStock(String codQR, int quantity) {
        ProductEntity entity = productRepository.findByCodQR(codQR)
                .orElseThrow(() -> new ProductNotFoundException(codQR));
        if (entity.getStock() < quantity) {
            throw new ProductUnavailableException(codQR);
        }
        entity.setStock(entity.getStock() - quantity);
        if (entity.getStock() == 0) {
            entity.setStatus(Product.ProductStatus.UNAVAILABLE);
        }
        productRepository.save(entity);
        log.info("stock decremented for product: {}", codQR);
    }
}
