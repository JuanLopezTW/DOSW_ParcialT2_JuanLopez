package edu.dosw.parcial.DOSW_ParcialT2.core.exceptions;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID id) {
        super("Order not found with id: " + id);
    }
}
