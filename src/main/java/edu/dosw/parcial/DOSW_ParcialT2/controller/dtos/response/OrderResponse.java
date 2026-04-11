package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private UUID userId;
    private List<OrderItemResponse> items;
    private String status;
    private Double total;
    private LocalDateTime createdAt;
}
