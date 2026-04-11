package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private Order.OrderStatus status;
}
