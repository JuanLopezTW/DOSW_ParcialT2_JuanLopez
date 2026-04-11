package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private String productCodQR;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
