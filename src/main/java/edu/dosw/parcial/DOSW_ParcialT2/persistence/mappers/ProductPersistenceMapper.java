package edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setCodQR(product.getCodQR());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setStatus(product.getStatus());
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        Product product = new Product();
        product.setCodQR(entity.getCodQR());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());
        product.setStock(entity.getStock());
        product.setStatus(entity.getStatus());
        return product;
    }
}
