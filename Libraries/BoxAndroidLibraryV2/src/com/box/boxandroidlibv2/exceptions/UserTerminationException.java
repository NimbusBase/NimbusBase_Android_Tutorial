package com.box.boxandroidlibv2.exceptions;

/**
 * Exception indicating user terminates action.
 */
public class UserTerminationException extends BoxAndroidLibException {

    private static final long serialVersionUID = 1L;

    private Object mContext;

    /**
     * Constructor.
     */
    public UserTerminationException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param context
     *            context related to this exception
     */
    public UserTerminationException(final Object context) {
        super();
        this.mContext = context;
    }

    /**
     * Get context context related to this exception.
     * 
     * @return context
     */
    public Object getContext() {
        return mContext;
    }
}
