package edu.dosw.parcial.DOSW_ParcialT2.controller.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.ProductResponse;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .codQR(product.getCodQR())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .build();
    }
}
