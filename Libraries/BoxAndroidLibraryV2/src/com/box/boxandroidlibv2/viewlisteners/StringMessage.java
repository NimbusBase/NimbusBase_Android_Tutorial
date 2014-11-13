package com.box.boxandroidlibv2.viewlisteners;

import com.box.boxjavalibv2.authorization.IAuthFlowMessage;

/**
 * A String message.
 */
public class StringMessage implements IAuthFlowMessage {

    public static final String MESSAGE_URL = "url";

    private final String mKey;
    private final String mValue;

    /**
     * Constructor.
     * 
     * @param key
     *            key of the message
     * @param value
     *            value of the message string
     */
    public StringMessage(final String key, final String value) {
        this.mValue = value;
        this.mKey = key;
    }

    @Override
    public Object getData() {
        return mValue;
    }

    @Override
    public String getKey() {
        return mKey;
    }
}
