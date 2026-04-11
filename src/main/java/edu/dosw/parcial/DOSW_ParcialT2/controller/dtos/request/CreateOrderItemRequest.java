package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItemRequest {

    @NotBlank
    private String codQR;

    @NotNull
    @Min(1)
    private Integer quantity;
}
