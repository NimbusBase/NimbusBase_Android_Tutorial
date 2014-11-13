package com.box.boxandroidlibv2.views;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.box.boxandroidlibv2.BoxAndroidClient;
import com.box.boxandroidlibv2.BoxAndroidConfigBuilder;
import com.box.boxandroidlibv2.R;
import com.box.boxandroidlibv2.dao.BoxAndroidOAuthData;
import com.box.boxandroidlibv2.exceptions.BoxAndroidLibException;
import com.box.boxandroidlibv2.exceptions.UserTerminationException;
import com.box.boxandroidlibv2.jsonparsing.AndroidBoxResourceHub;
import com.box.boxandroidlibv2.viewlisteners.OAuthWebViewListener;
import com.box.boxandroidlibv2.viewlisteners.StringMessage;
import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.authorization.IAuthEvent;
import com.box.boxjavalibv2.authorization.IAuthFlowListener;
import com.box.boxjavalibv2.authorization.IAuthFlowMessage;
import com.box.boxjavalibv2.authorization.IAuthFlowUI;
import com.box.boxjavalibv2.authorization.OAuthDataMessage;
import com.box.boxjavalibv2.authorization.OAuthWebViewData;
import com.box.boxjavalibv2.events.OAuthEvent;
import com.box.boxjavalibv2.jsonparsing.BoxJSONParser;
import com.box.restclientv2.httpclientsupport.HttpClientURIBuilder;

/**
 * A WebView used for OAuth flow.
 */
public class OAuthWebView extends WebView implements IAuthFlowUI {

    private boolean allowShowingRedirectPage = true;

    private OAuthWebViewData mWebViewData;

    private OAuthWebViewClient mWebClient;

    private String deviceId;

    private String deviceName;

    private final List<OAuthWebViewListener> mListeners = new ArrayList<OAuthWebViewListener>();

    /**
     * Constructor.
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public OAuthWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    protected OAuthWebViewClient getWebViewClient() {
        return mWebClient;
    }

    /**
     * Set the state, this is optional.
     * 
     * @param optionalState
     *            state
     */
    public void setOptionalState(final String optionalState) {
        getWebviewData().setOptionalState(optionalState);
    }

    @Override
    public void initializeAuthFlow(final Object activity, final String clientId, final String clientSecret) {
        initializeAuthFlow(activity, clientId, clientSecret, null);
    }

    @Override
    public void initializeAuthFlow(Object activity, String clientId, String clientSecret, String redirectUrl) {
        AndroidBoxResourceHub hub = new AndroidBoxResourceHub();
        initializeAuthFlow(activity, clientId, clientSecret, redirectUrl, new BoxAndroidClient(clientId, clientSecret, hub, new BoxJSONParser(hub),
            (new BoxAndroidConfigBuilder()).build()));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initializeAuthFlow(Object activity, String clientId, String clientSecret, String redirectUrl, BoxClient boxClient) {
        this.mWebViewData = new OAuthWebViewData(boxClient.getOAuthDataController());
        if (StringUtils.isNotEmpty(redirectUrl)) {
            mWebViewData.setRedirectUrl(redirectUrl);
        }
        mWebClient = createOAuthWebViewClient(mWebViewData, activity, boxClient);
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(mWebClient);
        setDevice(deviceId, deviceName);
    }

    protected OAuthWebViewData getWebviewData() {
        return mWebViewData;
    }

    @Override
    public void authenticate(IAuthFlowListener listener) {
        addAuthFlowListener(listener);

        for (IAuthFlowListener l : getOAuthWebViewListeners()) {
            mWebClient.addListener(wrapOAuthWebViewListener(l));
        }

        try {
            loadUrl(getWebviewData().buildUrl().toString());
        } catch (URISyntaxException e) {
            if (listener != null) {
                listener.onAuthFlowException(e);
            }
        }
    }

    @Override
    public void addAuthFlowListener(IAuthFlowListener listener) {
        getOAuthWebViewListeners().add(wrapOAuthWebViewListener(listener));
    }

    public void setDevice(final String id, final String name) {
        deviceId = id;
        deviceName = name;
        if (mWebClient != null) {
            mWebClient.setDevice(id, name);
        }
    }

    private static OAuthWebViewListener wrapOAuthWebViewListener(IAuthFlowListener listener) {
        if (listener instanceof OAuthWebViewListener) {
            return (OAuthWebViewListener) listener;
        } else {
            return new WrappedOAuthWebViewListener(listener);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mWebClient != null) {
            mWebClient.destroy();
        }
    }

    /**
     * @return the allowShowingRedirectPage
     */
    public boolean allowShowRedirectPage() {
        return allowShowingRedirectPage;
    }

    /**
     * @param allowShowingRedirectPage
     *            the allowShowingRedirectPage to set
     */
    public void setAllowShowingRedirectPage(boolean allowShowingRedirectPage) {
        this.allowShowingRedirectPage = allowShowingRedirectPage;
    }

    protected OAuthWebViewClient createOAuthWebViewClient(OAuthWebViewData data, Object activity, BoxClient boxClient) {
        OAuthWebViewClient c = new OAuthWebViewClient(data, (Activity) activity, boxClient);
        c.setAllowShowRedirectPage(allowShowRedirectPage());
        return c;
    }

    protected List<OAuthWebViewListener> getOAuthWebViewListeners() {
        return mListeners;
    }

    /**
     * WebViewClient for the OAuth WebView.
     */
    public static class OAuthWebViewClient extends WebViewClient {

        private static enum OAuthAPICallState {
            PRE, STARTED, FINISHED,
        };

        private static Dialog dialog;
        private BoxClient mBoxClient;
        private final OAuthWebViewData mwebViewData;
        private boolean allowShowRedirectPage = true;
        private OAuthAPICallState oauthAPICallState = OAuthAPICallState.PRE;
        private boolean sslErrorDialogButtonClicked;

        private String deviceId;

        private String deviceName;

        private final List<OAuthWebViewListener> mListeners = new ArrayList<OAuthWebViewListener>();
        private Activity mActivity;

        /**
         * Constructor.
         * 
         * @param webViewData
         *            data
         * @param listener
         *            listener
         * @param activity
         *            activity hosting this webview
         */
        public OAuthWebViewClient(final OAuthWebViewData webViewData, final Activity activity, final BoxClient boxClient) {
            super();
            this.mwebViewData = webViewData;
            this.mActivity = activity;
            this.mBoxClient = boxClient;
        }

        public void setDevice(final String id, final String name) {
            deviceId = id;
            deviceName = name;
        }

        public void addListener(final OAuthWebViewListener listener) {
            this.mListeners.add(listener);
        }

        private void setOAuthAPICallState(OAuthAPICallState state) {
            oauthAPICallState = state;
        }

        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            for (IAuthFlowListener listener : mListeners) {
                if (listener != null) {
                    listener.onAuthFlowEvent(OAuthEvent.PAGE_STARTED, new StringMessage(StringMessage.MESSAGE_URL, url));
                }
            }

            String code = null;
            try {
                code = getResponseValueFromUrl(url);
            } catch (URISyntaxException e) {
                fireExceptions(e);
            }
            if (StringUtils.isNotEmpty(code)) {
                for (IAuthFlowListener listener : mListeners) {
                    if (listener != null) {
                        listener.onAuthFlowMessage(new StringMessage(mwebViewData.getResponseType(), code));
                    }
                }
                startMakingOAuthAPICall(code, view);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ((oauthAPICallState != OAuthAPICallState.PRE) && !allowShowRedirectPage()) {
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedHttpAuthRequest(final WebView view, final HttpAuthHandler handler, final String host, final String realm) {
            for (IAuthFlowListener listener : mListeners) {
                listener.onAuthFlowEvent(OAuthEvent.AUTH_REQUEST_RECEIVED, new StringMessage(host, realm));
            }
            LayoutInflater factory = mActivity.getLayoutInflater();
            final View textEntryView = factory.inflate(R.layout.boxandroidlibv2_alert_dialog_text_entry, null);

            AlertDialog loginAlert = new AlertDialog.Builder(mActivity).setTitle(R.string.boxandroidlibv2_alert_dialog_text_entry).setView(textEntryView)
                .setPositiveButton(R.string.boxandroidlibv2_alert_dialog_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        String userName = ((EditText) textEntryView.findViewById(R.id.username_edit)).getText().toString();
                        String password = ((EditText) textEntryView.findViewById(R.id.password_edit)).getText().toString();
                        handler.proceed(userName, password);
                    }
                }).setNegativeButton(R.string.boxandroidlibv2_alert_dialog_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        fireExceptions(new UserTerminationException());
                    }
                }).create();
            loginAlert.show();
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            fireEvents(OAuthEvent.PAGE_FINISHED, new StringMessage(StringMessage.MESSAGE_URL, url));
        }

        /**
         * If you don't need the dialog, just return null.
         */
        protected Dialog showDialogWhileWaitingForAuthenticationAPICall() {
            return ProgressDialog.show(mActivity, mActivity.getText(R.string.boxandroidlibv2_Authenticating),
                mActivity.getText(R.string.boxandroidlibv2_Please_wait));
        }

        /**
         * Start to create OAuth after getting the code.
         * 
         * @param config
         *            config
         * @param code
         *            code
         * @param view
         */
        private void startMakingOAuthAPICall(final String code, WebView view) {
            if (oauthAPICallState != OAuthAPICallState.PRE) {
                return;
            }

            oauthAPICallState = OAuthAPICallState.STARTED;
            try {
                dialog = showDialogWhileWaitingForAuthenticationAPICall();
            } catch (Exception e) {
                // WindowManager$BadTokenException will be caught and the app would not display
                // the 'Force Close' message
                dialog = null;
                return;
            }

            if (!allowShowRedirectPage()) {
                view.setVisibility(View.INVISIBLE);
            }
            AsyncTask<Null, Null, BoxAndroidOAuthData> task = new AsyncTask<Null, Null, BoxAndroidOAuthData>() {

                private Exception mCreateOauthException;

                @Override
                protected BoxAndroidOAuthData doInBackground(final Null... params) {
                    BoxAndroidOAuthData oauth = null;
                    try {
                        oauth = (BoxAndroidOAuthData) mBoxClient.getOAuthManager().createOAuth(code, mwebViewData.getClientId(),
                            mwebViewData.getClientSecret(), mwebViewData.getRedirectUrl(), deviceId, deviceName);
                    } catch (Exception e) {
                        oauth = null;
                        mCreateOauthException = e;
                    }
                    return oauth;
                }

                @Override
                protected void onPostExecute(final BoxAndroidOAuthData result) {
                    setOAuthAPICallState(OAuthAPICallState.FINISHED);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (result != null) {
                        try {
                            fireEvents(OAuthEvent.OAUTH_CREATED, new OAuthDataMessage(result, mBoxClient.getJSONParser(), mBoxClient.getResourceHub()));
                        } catch (Exception e) {
                            fireExceptions(new BoxAndroidLibException(e));
                        }
                    } else {
                        fireExceptions(new BoxAndroidLibException(mCreateOauthException));
                    }
                }
            };
            task.execute();
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            if (!allowShowRedirectPage() && oauthAPICallState != OAuthAPICallState.PRE) {
                // Error happens after oauth api call started. This can only be the redirect page. In case user wants to ignore redirect page, we
                // swallow this error.
                return;
            }
            for (OAuthWebViewListener listener : mListeners) {
                if (listener != null) {
                    listener.onError(errorCode, description, failingUrl);
                }
            }
        }

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
            Resources resources = view.getContext().getResources();
            StringBuilder sslErrorMessage = new StringBuilder(
                resources.getString(R.string.boxandroidlibv2_There_are_problems_with_the_security_certificate_for_this_site));
            sslErrorMessage.append(" ");
            String sslErrorType;
            switch (error.getPrimaryError()) {
                case SslError.SSL_DATE_INVALID:
                    sslErrorType = view.getResources().getString(R.string.boxandroidlibv2_ssl_error_warning_DATE_INVALID);
                    break;
                case SslError.SSL_EXPIRED:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_EXPIRED);
                    break;
                case SslError.SSL_IDMISMATCH:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_ID_MISMATCH);
                    break;
                case SslError.SSL_NOTYETVALID:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_NOT_YET_VALID);
                    break;
                case SslError.SSL_UNTRUSTED:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_UNTRUSTED);
                    break;
                case SslError.SSL_INVALID:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_INVALID);
                    break;
                default:
                    sslErrorType = resources.getString(R.string.boxandroidlibv2_ssl_error_warning_INVALID);
                    break;
            }
            sslErrorMessage.append(sslErrorType);
            sslErrorMessage.append(" ");
            sslErrorMessage.append(resources.getString(R.string.boxandroidlibv2_ssl_should_not_proceed));
            // Show the user a dialog to force them to accept or decline the SSL problem before continuing.
            sslErrorDialogButtonClicked = false;
            AlertDialog loginAlert = new AlertDialog.Builder(view.getContext()).setTitle(R.string.boxandroidlibv2_Security_Warning)
                .setMessage(sslErrorMessage.toString()).setIcon(R.drawable.boxandroidlibv2_dialog_warning)
                .setPositiveButton(R.string.boxandroidlibv2_Continue, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        sslErrorDialogButtonClicked = true;
                        handler.proceed();
                        sendoutSslError(error, false);
                    }
                }).setNegativeButton(R.string.boxandroidlibv2_Go_back, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        sslErrorDialogButtonClicked = true;
                        handler.cancel();
                        sendoutSslError(error, true);
                    }
                }).create();
            loginAlert.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!sslErrorDialogButtonClicked) {
                        sendoutSslError(error, true);
                    }
                }
            });
            loginAlert.show();
        }

        protected void handleReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            // By default, doing nothing.
        }

        /**
         * Destroy.
         */
        public void destroy() {
            mListeners.clear();
            mBoxClient = null;
            mActivity = null;
        }

        private void sendoutSslError(final SslError error, final boolean canceled) {
            for (OAuthWebViewListener listener : mListeners) {
                if (listener != null) {
                    listener.onSslError(error, canceled);
                }
            }
        }

        /**
         * Get response value.
         * 
         * @param url
         *            url
         * @return response value
         * @throws URISyntaxException
         *             exception
         */
        private String getResponseValueFromUrl(final String url) throws URISyntaxException {
            HttpClientURIBuilder builder = new HttpClientURIBuilder(url);
            List<NameValuePair> query = builder.getQueryParams();
            for (NameValuePair pair : query) {
                if (pair.getName().equalsIgnoreCase(mwebViewData.getResponseType())) {
                    return pair.getValue();
                }
            }
            return null;
        }

        private void fireExceptions(Exception e) {
            for (IAuthFlowListener listener : mListeners) {
                if (listener != null) {
                    listener.onAuthFlowException(e);
                }
            }
        }

        private void fireEvents(IAuthEvent event, IAuthFlowMessage message) {
            for (IAuthFlowListener listener : mListeners) {
                if (listener != null) {
                    listener.onAuthFlowEvent(event, message);
                }
            }
        }

        /**
         * @return the allowShowRedirectPage
         */
        public boolean allowShowRedirectPage() {
            return allowShowRedirectPage;
        }

        /**
         * @param allowShowRedirectPage
         *            the allowShowRedirectPage to set
         */
        public void setAllowShowRedirectPage(boolean allowShowRedirectPage) {
            this.allowShowRedirectPage = allowShowRedirectPage;
        }
    }

    private static class WrappedOAuthWebViewListener extends OAuthWebViewListener {

        private final IAuthFlowListener mListener;

        WrappedOAuthWebViewListener(IAuthFlowListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onAuthFlowMessage(IAuthFlowMessage message) {
            mListener.onAuthFlowMessage(message);
        }

        @Override
        public void onAuthFlowException(Exception e) {
            mListener.onAuthFlowException(e);
        }

        @Override
        public void onAuthFlowEvent(IAuthEvent event, IAuthFlowMessage message) {
            mListener.onAuthFlowEvent(event, message);
        }

        @Override
        public void onSslError(SslError error, boolean canceled) {
        }

        @Override
        public void onError(int errorCode, String description, String failingUrl) {
        }
    }
}
