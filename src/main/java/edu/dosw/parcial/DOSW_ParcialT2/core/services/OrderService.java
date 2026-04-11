package edu.dosw.parcial.DOSW_ParcialT2.core.services;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.CreateOrderItemRequest;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.InvalidOrderStatusException;
import edu.dosw.parcial.DOSW_ParcialT2.core.exceptions.OrderNotFoundException;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.OrderItem;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Product;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.OrderValidator;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.ProductValidator;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.OrderPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.ProductPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.OrderPersistenceRepository;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.ProductPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderPersistenceRepository orderRepository;
    private final OrderPersistenceMapper orderMapper;
    private final ProductValidator productValidator;
    private final OrderValidator orderValidator;
    private final ProductPersistenceRepository productRepository;
    private final ProductPersistenceMapper productMapper;
    private final ProductService productService;

    public Order createOrder(UUID userId, List<CreateOrderItemRequest> itemRequests) {
        orderValidator.validateNoActiveOrder(userId);

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemRequest itemRequest : itemRequests) {
            Product product = productValidator.validateAvailableAndGetProduct(itemRequest.getCodQR());
            OrderItem item = new OrderItem();
            item.setProductCodQR(product.getCodQR());
            item.setProductName(product.getName());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice() * itemRequest.getQuantity());
            items.add(item);
        }

        double total = items.stream().mapToDouble(OrderItem::getSubtotal).sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setStatus(Order.OrderStatus.CREATED);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        for (CreateOrderItemRequest itemRequest : itemRequests) {
            productService.decrementStock(itemRequest.getCodQR(), itemRequest.getQuantity());
        }

        Order saved = orderMapper.toDomain(orderRepository.save(orderMapper.toEntity(order)));
        log.info("order created for userId: {}", userId);
        return saved;
    }

    public Order getById(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toDomain)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Order cancelOrder(UUID orderId, UUID userId) {
        Order order = getById(orderId);
        orderValidator.validateOrderBelongsToUser(order, userId);
        orderValidator.validateOrderCancellable(order);
        OrderEntity entity = orderRepository.findById(orderId).get();
        entity.setStatus(Order.OrderStatus.CANCELLED);
        log.info("order {} cancelled by userId: {}", orderId, userId);
        return orderMapper.toDomain(orderRepository.save(entity));
    }

    public Order updateStatus(UUID orderId, Order.OrderStatus newStatus) {
        Order order = getById(orderId);
        if (newStatus != Order.OrderStatus.IN_PREPARATION && newStatus != Order.OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Admin can only set IN_PREPARATION or DELIVERED");
        }
        OrderEntity entity = orderRepository.findById(orderId).get();
        entity.setStatus(newStatus);
        log.info("order {} status updated to: {}", orderId, newStatus);
        return orderMapper.toDomain(orderRepository.save(entity));
    }
}
