package edu.dosw.parcial.DOSW_ParcialT2.core.validators;

import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ActiveOrderAlreadyExistsException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.InvalidOrderStatusException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.OrderPersistenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderPersistenceRepository orderRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    void validateNoActiveOrder_noActiveOrder_passes() {
        UUID userId = UUID.randomUUID();
        when(orderRepository.existsByUserIdAndStatusIn(eq(userId), any())).thenReturn(false);

        assertThatCode(() -> orderValidator.validateNoActiveOrder(userId))
                .doesNotThrowAnyException();
    }

    @Test
    void validateNoActiveOrder_hasActiveOrder_throwsException() {
        UUID userId = UUID.randomUUID();
        when(orderRepository.existsByUserIdAndStatusIn(eq(userId), any())).thenReturn(true);

        assertThatThrownBy(() -> orderValidator.validateNoActiveOrder(userId))
                .isInstanceOf(ActiveOrderAlreadyExistsException.class);
    }

    @Test
    void validateOrderCancellable_createdStatus_passes() {
        Order order = new Order();
        order.setStatus(Order.OrderStatus.CREATED);

        assertThatCode(() -> orderValidator.validateOrderCancellable(order))
                .doesNotThrowAnyException();
    }

    @Test
    void validateOrderCancellable_notCreatedStatus_throwsException() {
        Order order = new Order();
        order.setStatus(Order.OrderStatus.IN_PREPARATION);

        assertThatThrownBy(() -> orderValidator.validateOrderCancellable(order))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void validateOrderBelongsToUser_correctUser_passes() {
        UUID userId = UUID.randomUUID();
        Order order = new Order();
        order.setUserId(userId);

        assertThatCode(() -> orderValidator.validateOrderBelongsToUser(order, userId))
                .doesNotThrowAnyException();
    }

    @Test
    void validateOrderBelongsToUser_wrongUser_throwsException() {
        UUID userId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID();
        Order order = new Order();
        order.setUserId(userId);

        assertThatThrownBy(() -> orderValidator.validateOrderBelongsToUser(order, differentUserId))
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}
