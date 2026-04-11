package edu.dosw.parcial.DOSW_ParcialT2.core.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String codQR) {
        super("Product not found with QR: " + codQR);
    }
}
