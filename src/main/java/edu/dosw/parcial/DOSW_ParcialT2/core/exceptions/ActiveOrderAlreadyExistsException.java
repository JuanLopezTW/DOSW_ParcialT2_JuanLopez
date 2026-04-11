package edu.dosw.parcial.DOSW_ParcialT2.core.exceptions;

public class ActiveOrderAlreadyExistsException extends RuntimeException {
    public ActiveOrderAlreadyExistsException() {
        super("User already has an active order");
    }
}
