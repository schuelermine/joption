package com.anselmschueler.joption;

public class NoneOptionException extends Exception {
    public NoneOptionException() {
        super();
    }

    public NoneOptionException(String reason) {
        super(reason + " - attempt to get underlying value of Option variant None");
    }
}
