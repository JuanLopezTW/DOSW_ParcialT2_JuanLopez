package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull
    @NotEmpty
    private List<@Valid CreateOrderItemRequest> items;
}
