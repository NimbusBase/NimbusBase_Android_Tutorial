package com.box.boxandroidlibv2.jsonparsing;

import com.box.boxandroidlibv2.dao.BoxAndroidCollaboration;
import com.box.boxandroidlibv2.dao.BoxAndroidCollection;
import com.box.boxandroidlibv2.dao.BoxAndroidComment;
import com.box.boxandroidlibv2.dao.BoxAndroidEmailAlias;
import com.box.boxandroidlibv2.dao.BoxAndroidEvent;
import com.box.boxandroidlibv2.dao.BoxAndroidEventCollection;
import com.box.boxandroidlibv2.dao.BoxAndroidFile;
import com.box.boxandroidlibv2.dao.BoxAndroidFileVersion;
import com.box.boxandroidlibv2.dao.BoxAndroidFolder;
import com.box.boxandroidlibv2.dao.BoxAndroidGroup;
import com.box.boxandroidlibv2.dao.BoxAndroidGroupMembership;
import com.box.boxandroidlibv2.dao.BoxAndroidItemPermissions;
import com.box.boxandroidlibv2.dao.BoxAndroidOAuthData;
import com.box.boxandroidlibv2.dao.BoxAndroidUser;
import com.box.boxandroidlibv2.dao.BoxAndroidWebLink;
import com.box.boxjavalibv2.dao.BoxResourceType;
import com.box.boxjavalibv2.dao.IBoxType;
import com.box.boxjavalibv2.jsonparsing.BoxResourceHub;

/**
 * Resource hub to direct the parsing of the api responses into our android sdk data objects.
 */
public class AndroidBoxResourceHub extends BoxResourceHub {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class getObjectClassGivenConcreteIBoxType(IBoxType type) {
        switch ((BoxResourceType) type) {
            case FILE:
                return BoxAndroidFile.class;
            case FOLDER:
                return BoxAndroidFolder.class;
            case USER:
                return BoxAndroidUser.class;
            case GROUP:
                return BoxAndroidGroup.class;
            case GROUP_MEMBERSHIP:
                return BoxAndroidGroupMembership.class;
            case FILE_VERSION:
                return BoxAndroidFileVersion.class;
            case COMMENT:
                return BoxAndroidComment.class;
            case COLLABORATION:
                return BoxAndroidCollaboration.class;
            case EMAIL_ALIAS:
                return BoxAndroidEmailAlias.class;
            case OAUTH_DATA:
                return BoxAndroidOAuthData.class;
            case WEB_LINK:
                return BoxAndroidWebLink.class;
            case EVENT:
                return BoxAndroidEvent.class;
            case ITEM_PERMISSIONS:
                return BoxAndroidItemPermissions.class;
            case ITEMS:
            case FILES:
            case USERS:
            case GROUPS:
            case COMMENTS:
            case FILE_VERSIONS:
            case COLLABORATIONS:
            case EMAIL_ALIASES:
            case WEB_LINKS:
            case GROUP_MEMBERSHIPS:
                return BoxAndroidCollection.class;
            case EVENTS:
                return BoxAndroidEventCollection.class;
            default:
                return super.getObjectClassGivenConcreteIBoxType(type);
        }
    }
}
