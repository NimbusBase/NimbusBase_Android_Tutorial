package com.box.boxandroidlibv2;

import com.box.boxjavalibv2.BoxConfigBuilder;

public class BoxAndroidConfigBuilder extends BoxConfigBuilder {

    /** sdk version number */
    private final static String VERSION_NUMBER = "v3.0.3";

    private final static String COMBINED_VERSION = "java_%s,android_%s";

    /** Default User-Agent String. */
    private static final String USER_AGENT = "BoxAndroidLibraryV2";

    public BoxAndroidConfigBuilder() {
        super();
        this.setUserAgent(USER_AGENT);
        this.setVersion(getCombinedSdkVersion(super.getVersion(), VERSION_NUMBER));
    }

    private static String getCombinedSdkVersion(final String javaVersion, final String androidVersion) {
        return String.format(COMBINED_VERSION, javaVersion, androidVersion);
    }
}
