package edu.dosw.parcial.DOSW_ParcialT2.core.services;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.CreateOrderItemRequest;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ActiveOrderAlreadyExistsException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.InvalidOrderStatusException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.OrderNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.ProductUnavailableException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.OrderItem;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.OrderValidator;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.ProductValidator;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderItemEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.OrderPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.ProductPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.OrderPersistenceRepository;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderPersistenceRepository orderRepository;
    @Mock
    private OrderPersistenceMapper orderMapper;
    @Mock
    private ProductValidator productValidator;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private ProductPersistenceRepository productRepository;
    @Mock
    private ProductPersistenceMapper productMapper;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_success() {
        UUID userId = UUID.randomUUID();

        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setCodQR("QR001");
        itemRequest.setQuantity(2);

        Product product = new Product("QR001", "Product A", "desc", 10.0, 5, Product.ProductStatus.AVAILABLE);
        when(productValidator.validateAvailableAndGetProduct("QR001")).thenReturn(product);

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setUserId(userId);
        savedEntity.setStatus(Order.OrderStatus.CREATED);
        savedEntity.setTotal(20.0);
        savedEntity.setCreatedAt(LocalDateTime.now());
        savedEntity.setItems(new ArrayList<>());

        when(orderRepository.save(any())).thenReturn(savedEntity);

        Order domainOrder = new Order();
        domainOrder.setId(savedEntity.getId());
        domainOrder.setUserId(userId);
        domainOrder.setStatus(Order.OrderStatus.CREATED);
        domainOrder.setTotal(20.0);
        domainOrder.setItems(List.of(new OrderItem(null, "QR001", "Product A", 2, 10.0, 20.0)));

        when(orderMapper.toEntity(any())).thenReturn(savedEntity);
        when(orderMapper.toDomain(savedEntity)).thenReturn(domainOrder);

        Order result = orderService.createOrder(userId, List.of(itemRequest));

        assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.CREATED);
        assertThat(result.getTotal()).isEqualTo(20.0);
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createOrder_activeOrderExists_throwsException() {
        UUID userId = UUID.randomUUID();
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setCodQR("QR001");
        itemRequest.setQuantity(1);

        doThrow(new ActiveOrderAlreadyExistsException()).when(orderValidator).validateNoActiveOrder(userId);

        assertThatThrownBy(() -> orderService.createOrder(userId, List.of(itemRequest)))
                .isInstanceOf(ActiveOrderAlreadyExistsException.class);
    }

    @Test
    void createOrder_productUnavailable_throwsException() {
        UUID userId = UUID.randomUUID();
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setCodQR("QR001");
        itemRequest.setQuantity(1);

        doNothing().when(orderValidator).validateNoActiveOrder(userId);
        when(productValidator.validateAvailableAndGetProduct("QR001"))
                .thenThrow(new ProductUnavailableException("QR001"));

        assertThatThrownBy(() -> orderService.createOrder(userId, List.of(itemRequest)))
                .isInstanceOf(ProductUnavailableException.class);
    }

    @Test
    void cancelOrder_success() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setStatus(Order.OrderStatus.CREATED);
        order.setItems(new ArrayList<>());

        Order cancelledOrder = new Order();
        cancelledOrder.setId(orderId);
        cancelledOrder.setStatus(Order.OrderStatus.CANCELLED);
        cancelledOrder.setItems(new ArrayList<>());

        OrderEntity entity = new OrderEntity();
        entity.setId(orderId);
        entity.setUserId(userId);
        entity.setStatus(Order.OrderStatus.CREATED);
        entity.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));
        when(orderMapper.toDomain(entity)).thenReturn(order, cancelledOrder);
        when(orderRepository.save(entity)).thenReturn(entity);
        doNothing().when(orderValidator).validateOrderBelongsToUser(any(Order.class), eq(userId));
        doNothing().when(orderValidator).validateOrderCancellable(any(Order.class));

        Order result = orderService.cancelOrder(orderId, userId);

        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(Order.OrderStatus.CANCELLED);
    }

    @Test
    void cancelOrder_notCreatedStatus_throwsException() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setStatus(Order.OrderStatus.IN_PREPARATION);
        order.setItems(new ArrayList<>());

        OrderEntity entity = new OrderEntity();
        entity.setId(orderId);
        entity.setStatus(Order.OrderStatus.IN_PREPARATION);
        entity.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));
        when(orderMapper.toDomain(entity)).thenReturn(order);
        doNothing().when(orderValidator).validateOrderBelongsToUser(order, userId);
        doThrow(new InvalidOrderStatusException("Order can only be cancelled when in CREATED status"))
                .when(orderValidator).validateOrderCancellable(order);

        assertThatThrownBy(() -> orderService.cancelOrder(orderId, userId))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void updateStatus_success_admin() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.CREATED);
        order.setItems(new ArrayList<>());

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus(Order.OrderStatus.IN_PREPARATION);
        updatedOrder.setItems(new ArrayList<>());

        OrderEntity entity = new OrderEntity();
        entity.setId(orderId);
        entity.setStatus(Order.OrderStatus.CREATED);
        entity.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));
        when(orderMapper.toDomain(entity)).thenReturn(order, updatedOrder);
        when(orderRepository.save(entity)).thenReturn(entity);

        Order result = orderService.updateStatus(orderId, Order.OrderStatus.IN_PREPARATION);

        ArgumentCaptor<OrderEntity> captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(Order.OrderStatus.IN_PREPARATION);
    }

    @Test
    void getById_notFound_throwsException() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(orderId))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
