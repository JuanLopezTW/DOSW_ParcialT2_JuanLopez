package edu.dosw.parcial.DOSW_ParcialT2.core.validators;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ActiveOrderAlreadyExistsException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.InvalidOrderStatusException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.OrderPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final OrderPersistenceRepository orderRepository;

    public void validateNoActiveOrder(UUID userId) {
        if (orderRepository.existsByUserIdAndStatusIn(userId, List.of(Order.OrderStatus.CREATED, Order.OrderStatus.IN_PREPARATION))) {
            log.warn("user {} already has an active order", userId);
            throw new ActiveOrderAlreadyExistsException();
        }
    }

    public void validateOrderBelongsToUser(Order order, UUID userId) {
        if (!order.getUserId().equals(userId)) {
            throw new InvalidOrderStatusException("Order does not belong to user");
        }
    }

    public void validateOrderCancellable(Order order) {
        if (order.getStatus() != Order.OrderStatus.CREATED) {
            throw new InvalidOrderStatusException("Order can only be cancelled when in CREATED status");
        }
    }
}
