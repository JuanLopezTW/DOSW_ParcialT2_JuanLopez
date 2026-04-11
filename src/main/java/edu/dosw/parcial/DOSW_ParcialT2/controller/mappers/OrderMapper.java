package edu.dosw.parcial.DOSW_ParcialT2.controller.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.OrderItemResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.OrderResponse;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                itemResponses.add(OrderItemResponse.builder()
                        .productCodQR(item.getProductCodQR())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build());
            }
        }
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .items(itemResponses)
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
