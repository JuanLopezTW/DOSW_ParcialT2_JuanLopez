package edu.dosw.parcial.DOSW_ParcialT2.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String codQR;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private ProductStatus status;

    public enum ProductStatus {
        AVAILABLE,
        UNAVAILABLE
    }
}
