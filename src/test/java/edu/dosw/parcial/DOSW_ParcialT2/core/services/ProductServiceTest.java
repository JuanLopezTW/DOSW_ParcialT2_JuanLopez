package edu.dosw.parcial.DOSW_ParcialT2.core.services;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductUnavailableException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.ProductPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPersistenceRepository productRepository;
    @Mock
    private ProductPersistenceMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void getByQrCode_success() {
        ProductEntity entity = new ProductEntity("QR001", "Product A", "desc", 10.0, 5, Product.ProductStatus.AVAILABLE);
        Product product = new Product("QR001", "Product A", "desc", 10.0, 5, Product.ProductStatus.AVAILABLE);

        when(productRepository.findByCodQR("QR001")).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(product);

        Product result = productService.getByQrCode("QR001");

        assertThat(result.getCodQR()).isEqualTo("QR001");
        assertThat(result.getName()).isEqualTo("Product A");
        assertThat(result.getPrice()).isEqualTo(10.0);
    }

    @Test
    void getByQrCode_notFound_throwsException() {
        when(productRepository.findByCodQR("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getByQrCode("INVALID"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void decrementStock_success() {
        ProductEntity entity = new ProductEntity("QR001", "Product A", "desc", 10.0, 3, Product.ProductStatus.AVAILABLE);

        when(productRepository.findByCodQR("QR001")).thenReturn(Optional.of(entity));

        productService.decrementStock("QR001", 2);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getStock()).isEqualTo(1);
        assertThat(captor.getValue().getStatus()).isEqualTo(Product.ProductStatus.AVAILABLE);
    }

    @Test
    void decrementStock_toZero_setsUnavailable() {
        ProductEntity entity = new ProductEntity("QR001", "Product A", "desc", 10.0, 1, Product.ProductStatus.AVAILABLE);

        when(productRepository.findByCodQR("QR001")).thenReturn(Optional.of(entity));

        productService.decrementStock("QR001", 1);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getStock()).isEqualTo(0);
        assertThat(captor.getValue().getStatus()).isEqualTo(Product.ProductStatus.UNAVAILABLE);
    }

    @Test
    void decrementStock_insufficientStock_throwsException() {
        ProductEntity entity = new ProductEntity("QR001", "Product A", "desc", 10.0, 1, Product.ProductStatus.AVAILABLE);

        when(productRepository.findByCodQR("QR001")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> productService.decrementStock("QR001", 5))
                .isInstanceOf(ProductUnavailableException.class);
    }
}
