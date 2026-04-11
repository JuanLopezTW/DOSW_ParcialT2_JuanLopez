package edu.dosw.parcial.DOSW_ParcialT2.persistence;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.OrderPersistenceRepository;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.UserPersistenceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PersistenceLayerTest {

    @Autowired
    private ProductPersistenceRepository productRepository;

    @Autowired
    private OrderPersistenceRepository orderRepository;

    @Autowired
    private UserPersistenceRepository userRepository;

    @Test
    void productRepository_saveAndFindByCodQR() {
        ProductEntity entity = ProductEntity.builder()
                .codQR("QR-001")
                .name("Café")
                .description("Desc")
                .price(2000.0)
                .stock(10)
                .status(Product.ProductStatus.AVAILABLE)
                .build();

        productRepository.save(entity);

        Optional<ProductEntity> result = productRepository.findByCodQR("QR-001");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Café");
    }

    @Test
    void orderRepository_saveAndFindByUserId() {
        UUID userId = UUID.randomUUID();

        OrderEntity entity = OrderEntity.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .status(Order.OrderStatus.CREATED)
                .total(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(entity);

        List<OrderEntity> result = orderRepository.findByUserId(userId);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    void orderRepository_existsByUserIdAndStatusIn() {
        UUID userId = UUID.randomUUID();

        OrderEntity entity = OrderEntity.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .status(Order.OrderStatus.CREATED)
                .total(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(entity);

        boolean existsCreated = orderRepository.existsByUserIdAndStatusIn(userId, List.of(Order.OrderStatus.CREATED));
        boolean existsCancelled = orderRepository.existsByUserIdAndStatusIn(userId, List.of(Order.OrderStatus.CANCELLED));

        assertThat(existsCreated).isTrue();
        assertThat(existsCancelled).isFalse();
    }
}
