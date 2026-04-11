package edu.dosw.parcial.DOSW_ParcialT2.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private UUID id;
    private String productCodQR;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
