package edu.dosw.parcial.DOSW_ParcialT2.core.exceptions;

public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException(String codQR) {
        super("Product not available: " + codQR);
    }
}
