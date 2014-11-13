package com.box.boxandroidlibv2;

import com.box.boxandroidlibv2.dao.BoxAndroidOAuthData;
import com.box.boxandroidlibv2.jsonparsing.AndroidBoxResourceHub;
import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.BoxConnectionManagerBuilder.BoxConnectionManager;
import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.authorization.IAuthFlowMessage;
import com.box.boxjavalibv2.dao.BoxOAuthToken;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.IBoxResourceHub;

/**
 * This is the main entrance of the sdk. The client contains all resource managers and also handles authentication. Make sure you call authenticate method
 * before making any api calls. you can use the resource managers to execute requests <b>synchronously</b> against the Box REST API(V2). Full details about the
 * Box API can be found at {@see <a href="http://developers.box.com/docs">http://developers.box.com/docs</a>} . You must have an OpenBox application with a
 * valid API key to use the Box API. All methods in this class are executed in the invoking thread, and therefore are NOT safe to execute in the UI thread of
 * your application. You should only use this class if you already have worker threads or AsyncTasks that you want to incorporate the Box API into.
 */
public class BoxAndroidClient extends BoxClient {

    /**
     * This constructor has some connection parameters. They are used to periodically close idle connections that HttpClient opens.
     * 
     * @param clientId
     *            client id, you can get it from dev console.
     * @param clientSecret
     *            client secret, you can get it from dev console.
     * @param hub
     *            resource hub. use null if you want to use default one.
     * @param parser
     *            json parser, use null if you want to use default one(Jackson).
     * @param config
     *            BoxConfig. User BoxAndroidConfigBuilder to build. Normally you only need default config: (new BoxAndroidConfigBuilder()).build()
     * @param connectionManager
     *            BoxConnectionManager. Normally you only need default connection manager: (new BoxConnectionManagerBuilder()).build()
     */
    public BoxAndroidClient(final String clientId, final String clientSecret, final IBoxResourceHub hub, final IBoxJSONParser parser, final IBoxConfig config,
        final BoxConnectionManager connectionManager) {
        super(clientId, clientSecret, hub, parser, createMonitoredRestClient(connectionManager), config);
    }

    /**
     * @param clientId
     *            client id
     * @param clientSecret
     *            client secret
     * @param resourcehub
     *            resource hub, use null for default resource hub.
     * @param parser
     *            json parser, use null for default parser.
     * @param config
     *            BoxConfig. User BoxAndroidConfigBuilder to build. Normally you only need default config: (new BoxAndroidConfigBuilder()).build()
     */
    public BoxAndroidClient(final String clientId, final String clientSecret, final IBoxResourceHub resourcehub, final IBoxJSONParser parser,
        final IBoxConfig config) {
        super(clientId, clientSecret, resourcehub, parser, config);
    }

    @Deprecated
    public BoxAndroidClient(String clientId, String clientSecret, final IBoxConfig config) {
        super(clientId, clientSecret, config);
    }

    @Override
    protected IBoxResourceHub createResourceHub() {
        return new AndroidBoxResourceHub();
    }

    @Override
    protected BoxOAuthToken getOAuthTokenFromMessage(IAuthFlowMessage message) {
        return new BoxAndroidOAuthData(super.getOAuthTokenFromMessage(message));
    }
}
