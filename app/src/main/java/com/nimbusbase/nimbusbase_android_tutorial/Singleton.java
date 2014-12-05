package com.nimbusbase.nimbusbase_android_tutorial;

import android.content.Context;

import com.nimbusbase.nimbusbase.Base;
import com.nimbusbase.nimbusbase.Constant.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 11/12/14.
 */
public class Singleton {
    public static Context CONTEXT;

    public static Base base() {
        return SingletonHolder.sBaseInstance;
    }

    private static class SingletonHolder {

        private final static Base sBaseInstance = new Base(
                CONTEXT,
                new MDLDatabaseManager(CONTEXT),
                getBaseConfigs()
        );

        public static Map<String, Object> getBaseConfigs() {

            final String appName = "Nimbus iOS Tutorial";

            return new HashMap<String, Object>() {
                {
                    put(Config.SERVERS,
                            new ArrayList<Map<String,Object>>() {
                                {
                                    add(new HashMap<String, Object>() {
                                        {
                                            put(Config.CLOUD, Config.GDRIVE);
                                            put(Config.APP_NAME, appName);
                                            put(Config.AUTH_SCOPE, Config.AuthScope.ROOT);
                                        }
                                    });
                                    add(new HashMap<String, Object>() {
                                        {
                                            put(Config.CLOUD, Config.DROPBOX);
                                            put(Config.APP_NAME, appName);
                                            put(Config.APP_ID, "x0e7vb4ls3lub5d");
                                            put(Config.APP_SECRET, "jl1xp49sumwe7tf");
                                            put(Config.AUTH_SCOPE, Config.AuthScope.APP_DATA);
                                        }
                                    });
                                    add(new HashMap<String, Object>() {
                                        {
                                            put(Config.CLOUD, Config.BOX);
                                            put(Config.APP_NAME, appName);
                                            put(Config.APP_ID, "eky66lnq5fnlmulhos7080u1c4a2isv2");
                                            put(Config.APP_SECRET, "6bMkjEzGKU1ghcmXbCii32ykGHJp4xZT");
                                            put(Config.AUTH_SCOPE, Config.AuthScope.ROOT);
                                        }
                                    });
                                }
                            });
                }
            };
        }
    }
}
