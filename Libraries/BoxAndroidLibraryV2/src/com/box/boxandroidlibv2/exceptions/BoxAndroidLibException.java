package com.box.boxandroidlibv2.exceptions;

/**
 * A wrapper class for exceptions.
 */
public class BoxAndroidLibException extends Exception implements IBoxAndroidLibException {

    private static final long serialVersionUID = 1L;

    private Exception rawException;

    /**
     * Constructor.
     */
    public BoxAndroidLibException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param e
     *            The raw exception.
     */
    public BoxAndroidLibException(final Exception e) {
        this.rawException = e;
    }

    /**
     * Get the raw exception.
     * 
     * @return exception
     */
    public Exception getRawException() {
        return rawException;
    }
}
