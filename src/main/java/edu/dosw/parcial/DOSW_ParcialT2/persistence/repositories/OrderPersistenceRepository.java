package edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderPersistenceRepository extends JpaRepository<OrderEntity, UUID> {
    boolean existsByUserIdAndStatusIn(UUID userId, List<Order.OrderStatus> statuses);
    List<OrderEntity> findByUserId(UUID userId);
}
