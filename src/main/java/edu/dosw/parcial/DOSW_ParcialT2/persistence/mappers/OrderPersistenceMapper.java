package edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.OrderItem;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setStatus(order.getStatus());
        entity.setTotal(order.getTotal());
        entity.setCreatedAt(order.getCreatedAt());

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemEntity itemEntity = new OrderItemEntity();
                itemEntity.setId(item.getId());
                itemEntity.setProductCodQR(item.getProductCodQR());
                itemEntity.setProductName(item.getProductName());
                itemEntity.setQuantity(item.getQuantity());
                itemEntity.setUnitPrice(item.getUnitPrice());
                itemEntity.setSubtotal(item.getSubtotal());
                itemEntity.setOrder(entity);
                itemEntities.add(itemEntity);
            }
        }
        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setUserId(entity.getUserId());
        order.setStatus(entity.getStatus());
        order.setTotal(entity.getTotal());
        order.setCreatedAt(entity.getCreatedAt());

        List<OrderItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            for (OrderItemEntity itemEntity : entity.getItems()) {
                OrderItem item = new OrderItem();
                item.setId(itemEntity.getId());
                item.setProductCodQR(itemEntity.getProductCodQR());
                item.setProductName(itemEntity.getProductName());
                item.setQuantity(itemEntity.getQuantity());
                item.setUnitPrice(itemEntity.getUnitPrice());
                item.setSubtotal(itemEntity.getSubtotal());
                items.add(item);
            }
        }
        order.setItems(items);
        return order;
    }
}
