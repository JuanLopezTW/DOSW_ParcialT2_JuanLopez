package edu.dosw.parcial.DOSW_ParcialT2.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;
    private UUID userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private Double total;
    private LocalDateTime createdAt;

    public enum OrderStatus {
        CREATED,
        IN_PREPARATION,
        DELIVERED,
        CANCELLED
    }
}
