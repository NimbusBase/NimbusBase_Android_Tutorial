package com.box.boxandroidlibv2.viewlisteners;

import android.net.http.SslError;

import com.box.boxjavalibv2.authorization.IAuthEvent;
import com.box.boxjavalibv2.authorization.IAuthFlowListener;
import com.box.boxjavalibv2.authorization.IAuthFlowMessage;

/**
 * Listener listening to the {@link com.box.boxandroidlibv2.views.OAuthWebView}.
 */
public abstract class OAuthWebViewListener implements IAuthFlowListener {

    @Override
    public abstract void onAuthFlowMessage(IAuthFlowMessage message);

    @Override
    public abstract void onAuthFlowException(Exception e);

    @Override
    public abstract void onAuthFlowEvent(IAuthEvent event, IAuthFlowMessage message);

    /**
     * This indicates a SSL Error.
     * 
     * @param error
     *            error details.
     * @param canceled
     *            Whether the operation is canceled due to the ssl error.
     */
    public abstract void onSslError(SslError error, boolean canceled);

    /**
     * This indicates the webview receives some error. Implement this method to handle properly.
     * 
     * @param errorCode
     *            error code
     * @param description
     *            description of the error
     * @param failingUrl
     *            the failing url
     */
    public abstract void onError(int errorCode, String description, String failingUrl);
}
