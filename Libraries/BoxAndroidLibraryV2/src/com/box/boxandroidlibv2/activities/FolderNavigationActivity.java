package com.box.boxandroidlibv2.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.box.boxandroidlibv2.BoxAndroidClient;
import com.box.boxandroidlibv2.BoxAndroidConfigBuilder;
import com.box.boxandroidlibv2.R;
import com.box.boxandroidlibv2.adapters.BoxListItemAdapter;
import com.box.boxandroidlibv2.adapters.BoxListItemAdapter.ViewHolder;
import com.box.boxandroidlibv2.dao.BoxAndroidCollection;
import com.box.boxandroidlibv2.dao.BoxAndroidFile;
import com.box.boxandroidlibv2.dao.BoxAndroidFolder;
import com.box.boxandroidlibv2.dao.BoxAndroidOAuthData;
import com.box.boxandroidlibv2.manager.ThumbnailManager;
import com.box.boxandroidlibv2.viewdata.BoxListItem;
import com.box.boxjavalibv2.dao.BoxItem;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxjavalibv2.requests.requestobjects.BoxImageRequestObject;
import com.box.boxjavalibv2.requests.requestobjects.BoxPagingRequestObject;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxDefaultRequestObject;

/**
 * This class is used to navigate a users folder tree.
 * 
 * @author dcung
 * 
 */
public class FolderNavigationActivity extends Activity implements OnItemClickListener {

    /** Broadcast receiver for controller actions. */
    private BroadcastReceiver mControllerReceiver;
    /** Local broadcast manager. */
    private LocalBroadcastManager mLocalBroadcastManager;

    protected static final String EXTRA_FOLDER_ID = "extraFolderId";

    protected static final String EXTRA_BOX_CLIENT_OAUTH = "extraClient_oauth";

    protected static final String EXTRA_BOX_CLIENT_ID = "extraClientId";

    protected static final String EXTRA_BOX_CLIENT_SECRET = "extraClientSecret";

    protected static final String EXTRA_NAV_NUMBER = "nav";

    protected static final String EXTRA_SOURCE_TYPE = "sdk_source";

    private Controller mController;

    private ListView mListView;

    private ThumbnailManager mThumbnailManager;

    protected String mCurrentFolderId = "0";

    protected BoxAndroidClient mClient;

    protected String clientId;

    protected String clientSecret;

    /** Because this activity will launch instances of itself this indicates how deep the activity stack is for these internal activities. */
    protected int mNavNumber = 0;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        mListView = getListView();

        mThumbnailManager = initializeThumbnailManager();
        if (mThumbnailManager == null) {
            return;
        }

        mListView.setAdapter(initializeBoxListItemAdapter(mThumbnailManager));
        mListView.setOnItemClickListener(this);
        if (getIntent() != null) {
            clientId = getIntent().getStringExtra(EXTRA_BOX_CLIENT_ID);
            clientSecret = getIntent().getStringExtra(EXTRA_BOX_CLIENT_SECRET);
            BoxAndroidOAuthData authData = getIntent().getParcelableExtra(EXTRA_BOX_CLIENT_OAUTH);
            mClient = new BoxAndroidClient(clientId, clientSecret, null, null, (new BoxAndroidConfigBuilder()).build());
            mClient.authenticate(authData);
            mCurrentFolderId = getIntent().getStringExtra(EXTRA_FOLDER_ID);
            mNavNumber = getIntent().getIntExtra(EXTRA_NAV_NUMBER, 0);
        }
        if (savedInstanceState != null) {

            clientId = savedInstanceState.getString(EXTRA_BOX_CLIENT_ID);
            clientSecret = savedInstanceState.getString(EXTRA_BOX_CLIENT_SECRET);
            BoxAndroidOAuthData authData = savedInstanceState.getParcelable(EXTRA_BOX_CLIENT_OAUTH);
            mClient = new BoxAndroidClient(clientId, clientSecret, null, null, (new BoxAndroidConfigBuilder()).build());
            mClient.authenticate(authData);
            mCurrentFolderId = savedInstanceState.getString(EXTRA_FOLDER_ID);
            mNavNumber = savedInstanceState.getInt(EXTRA_NAV_NUMBER, 0);
        }
        if (mClient == null) {
            if (mClient == null) {
                Toast.makeText(this, "No client provided", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        IntentFilter filter = initializeReceiverFilters();
        mControllerReceiver = initializeReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(mControllerReceiver, filter);
        mController = new Controller();
        ((BoxListItemAdapter) mListView.getAdapter()).add(new BoxListItem(mController.fetchFolder(mCurrentFolderId), Controller.ACTION_FETCHED_FOLDER));
        mThumbnailManager.deleteFilesInCacheDirectory();
    }

    /**
     * 
     * @return The intent filter used to listen to broadcasts for folder navigation.
     */
    protected IntentFilter initializeReceiverFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Controller.ACTION_FETCHED_FOLDER);
        filter.addAction(Controller.ACTION_FETCHED_FOLDER_ITEMS);
        filter.addAction(Controller.ACTION_DOWNLOADED_FILE_THUMBNAIL);
        return filter;
    }

    /**
     * 
     * @return the receiver used to receive broadcasts for folder navigation.
     */
    protected BroadcastReceiver initializeReceiver() {
        return new FolderNavigationReceiver();
    }

    /**
     * 
     * @param thumbNailManager
     *            The thumbnail manager provided by initializeThumbnailManager().
     * @return BoxListItemAdapter to use for showing files or folders.
     */
    protected BoxListItemAdapter initializeBoxListItemAdapter(final ThumbnailManager thumbNailManager) {
        return new FolderNavigationBoxListItemAdapter(this, mThumbnailManager);
    }

    /**
     * 
     * @return Thumbnail manager to be used for thumbnails for this navigation actiivty.
     */
    protected ThumbnailManager initializeThumbnailManager() {
        try {
            return new ThumbnailManager(getCacheDir());
        }
        catch (FileNotFoundException e) {
            finish();
            return null;

        }
    }

    /**
     * 
     * @return the listview that will be used to navigate.
     */
    protected void initializeViews() {
        setContentView(R.layout.boxandroidlibv2_layout_picker);
        // to make dialog theme fill the full view.
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 
     * @return the listview that will be used to navigate.
     */
    protected ListView getListView() {
        return (ListView) this.findViewById(R.id.PickerListView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocalBroadcastManager != null) {
            mLocalBroadcastManager.unregisterReceiver(mControllerReceiver);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FOLDER_ID, mCurrentFolderId);
        outState.putString(EXTRA_BOX_CLIENT_ID, clientId);
        outState.putString(EXTRA_BOX_CLIENT_SECRET, clientSecret);
        try {
            outState.putParcelable(EXTRA_BOX_CLIENT_OAUTH, (BoxAndroidOAuthData) mClient.getAuthData());
        }
        catch (AuthFatalFailureException e) {
            // This shouldn't happen at all.
        }
        outState.putInt(EXTRA_NAV_NUMBER, mNavNumber);
        super.onSaveInstanceState(outState);
    }

    /**
     * Create an intent to launch an instance of this activity to navigate folders.
     * 
     * @param context
     *            current context.
     * @param folderId
     *            folder id to navigate.
     * @param oauth
     *            oauth data for client.
     * @param clientId
     *            client id
     * @param clientSecret
     *            client secret
     * @return an intent to launch an instance of this activity.
     */
    public static Intent getLaunchIntent(Context context, final String folderId, final BoxAndroidOAuthData oauth, final String clientId,
        final String clientSecret) {
        Intent intent = new Intent(context, FolderNavigationActivity.class);
        intent.putExtra(EXTRA_FOLDER_ID, folderId);
        intent.putExtra(EXTRA_BOX_CLIENT_OAUTH, oauth);
        intent.putExtra(EXTRA_BOX_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_BOX_CLIENT_SECRET, clientSecret);
        return intent;
    }

    /**
     * Handles showing the initial folder after it has been fetched.
     * 
     * @param intent
     *            the intent containing the results of the network call.
     */
    protected void onFetchedFolder(final Intent intent) {
        if (!intent.getBooleanExtra(Controller.ARG_SUCCESS, false)) {
            Toast.makeText(this, getResources().getString(R.string.boxandroidlibv2_Problem_fetching_folder), Toast.LENGTH_LONG).show();
            return;
        }

        if (mCurrentFolderId.equals(intent.getStringExtra(Controller.ARG_FOLDER_ID))) {

            BoxAndroidFolder boxFolder = intent.getParcelableExtra(Controller.ARG_BOX_FOLDER);
            final BoxAndroidCollection collection = boxFolder.getItemCollection();

            BoxListItemAdapter adapter = ((BoxListItemAdapter) mListView.getAdapter());
            adapter.remove(intent.getAction());
            if (collection.getTotalCount() > 0) {
                // because the default folder items don't have all the fields we need do folder item fetch if there are any additional items.
                ((BoxListItemAdapter) mListView.getAdapter()).add(new BoxListItem(mController.fetchFolderItems(mCurrentFolderId, 0, collection.getEntries()
                    .size()), Controller.ACTION_FETCHED_FOLDER_ITEMS));

            }
        }
    }

    protected String getSourceType() {
        return "base_chooser";
    }

    /**
     * Handles showing other items in the folder after it has been fetched.
     * 
     * @param intent
     *            the intent containing the results of the network call.
     */
    protected void onFetchedFolderItems(final Intent intent) {
        if (!intent.getBooleanExtra(Controller.ARG_SUCCESS, false)) {
            Toast.makeText(this, getResources().getString(R.string.boxandroidlibv2_Problem_fetching_folder), Toast.LENGTH_LONG).show();
            return;
        }

        if (mCurrentFolderId.equals(intent.getStringExtra(Controller.ARG_FOLDER_ID))) {
            BoxAndroidCollection collection = intent.getParcelableExtra(Controller.ARG_BOX_COLLECTION);
            BoxListItemAdapter adapter = ((BoxListItemAdapter) mListView.getAdapter());
            int offset = intent.getIntExtra(Controller.ARG_OFFSET, -1);
            int limit = intent.getIntExtra(Controller.ARG_LIMIT, -1);
            adapter.remove(intent.getAction());
            adapter.add(collection);
            if (offset + collection.getEntries().size() < collection.getTotalCount()) {
                // if not all entries were fetched add a task to fetch more items if user scrolls to last entry.
                ((BoxListItemAdapter) mListView.getAdapter()).add(new BoxListItem(mController.fetchFolderItems(mCurrentFolderId, offset
                                                                                                                                 + collection.getEntries()
                                                                                                                                     .size(), limit),
                    Controller.ACTION_FETCHED_FOLDER_ITEMS));
            }

        }
    }

    /**
     * Handles showing new thumbnails after they have been downloaded.
     * 
     * @param intent
     */
    protected void onDownloadedThumbnail(final Intent intent) {
        if (intent.getBooleanExtra(Controller.ARG_SUCCESS, false)) {
            ((BoxListItemAdapter) mListView.getAdapter()).update(intent.getStringExtra(Controller.ARG_FILE_ID));
        }
    }

    /**
     * The Executor used for api calls.
     */
    private ThreadPoolExecutor apiExecutor;
    /**
     * The Executor used for thumbnail api calls.
     */
    private ThreadPoolExecutor thumbnailApiExecutor;

    /**
     * General Executor that we will submit our tasks to.
     * 
     * @return executor
     */
    protected ThreadPoolExecutor getApiExecutor() {
        if (apiExecutor == null || apiExecutor.isShutdown()) {
            apiExecutor = new ThreadPoolExecutor(2, 10, 3600, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return apiExecutor;
    }

    /**
     * Executor that we will submit thumbnail tasks to.
     * 
     * @return executor
     */
    protected ThreadPoolExecutor getThumbnailApiExecutor() {
        if (thumbnailApiExecutor == null || thumbnailApiExecutor.isShutdown()) {
            thumbnailApiExecutor = new ThreadPoolExecutor(1, 10, 3600, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return thumbnailApiExecutor;
    }

    /**
     * 
     * @return local broadcast manager used to provide status for api calls.
     */
    public LocalBroadcastManager getLocalBroadcastManager() {
        return mLocalBroadcastManager;
    }

    /**
     * Receiver used to receive broadcasts related to folder navigation.
     * 
     * 
     */
    protected class FolderNavigationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Controller.ACTION_FETCHED_FOLDER)) {
                onFetchedFolder(intent);
            }
            else if (intent.getAction().equals(Controller.ACTION_FETCHED_FOLDER_ITEMS)) {
                onFetchedFolderItems(intent);
            }
            else if (intent.getAction().equals(Controller.ACTION_DOWNLOADED_FILE_THUMBNAIL)) {
                onDownloadedThumbnail(intent);
            }
        }

    }

    /**
     * BoxListItemAdapter intended for navigating a folder.
     * 
     * 
     */
    protected class FolderNavigationBoxListItemAdapter extends BoxListItemAdapter {

        public FolderNavigationBoxListItemAdapter(Activity context, ThumbnailManager manager) {
            super(context, manager);
        }

        @Override
        protected void update(ViewHolder holder, BoxListItem listItem) {
            super.update(holder, listItem);

            if (listItem.getType() == BoxListItem.TYPE_FUTURE_TASK) {
                if (!listItem.getTask().isDone()) {
                    getApiExecutor().execute(listItem.getTask());
                }
                return;
            }
            else if (listItem.getType() == BoxListItem.TYPE_BOX_FILE_ITEM || (listItem.getType() == BoxListItem.TYPE_BOX_FOLDER_ITEM)) {

                if (listItem.getBoxItem() instanceof BoxAndroidFile) {
                    if (listItem.getTask() == null) {
                        listItem.setTask(mController.downloadThumbnail(listItem.getBoxItem().getId(),
                            mThumbnailManager.getThumbnailForFile(listItem.getBoxItem().getId()), holder));
                    }
                    else if (listItem.getTask().isDone()) {
                        try {
                            Intent intent = (Intent) listItem.getTask().get();
                            // if we were unable to get this thumbnail before try it again.
                            if (!intent.getBooleanExtra(Controller.ARG_SUCCESS, false)) {
                                listItem.setTask(mController.downloadThumbnail(listItem.getBoxItem().getId(),
                                    mThumbnailManager.getThumbnailForFile(listItem.getBoxItem().getId()), holder));
                            }
                        }
                        catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }

                    if (!listItem.getTask().isDone()) {
                        getThumbnailApiExecutor().execute(listItem.getTask());
                    }
                }

            }
        }

    }

    public class Controller {

        public static final String ACTION_FETCHED_FOLDER_ITEMS = "PickerActivity_FetchedFolderItems";
        public static final String ACTION_FETCHED_FOLDER = "PickerActivity_FetchedFolder";
        public static final String ACTION_DOWNLOADED_FILE_THUMBNAIL = "PickerActivity_DownloadedFileThumbnail";
        public static final String ARG_SUCCESS = "PickerActivity_ArgSuccess";
        public static final String ARG_FOLDER_ID = "PickerActivity_FolderId";
        public static final String ARG_FILE_ID = "PickerActivity_FileId";
        public static final String ARG_OFFSET = "PickerActivity_ArgOffset";
        public static final String ARG_LIMIT = "PickerActivity_Limit";
        public static final String ARG_BOX_FOLDER = "PickerActivity_Folde";
        public static final String ARG_BOX_COLLECTION = "PickerActivity_Collection";

        /**
         * Fetch a Box folder using v2.
         * 
         * @param folderId
         *            Folder id to be fetched.
         * @return A FutureTask that is tasked with fetching information on the given folder.
         */
        public FutureTask<Intent> fetchFolder(final String folderId) {
            return new FutureTask<Intent>(new Callable<Intent>() {

                @Override
                public Intent call() throws Exception {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_FETCHED_FOLDER);
                    intent.putExtra(ARG_FOLDER_ID, folderId);
                    try {
                        BoxDefaultRequestObject defaultRequest = new BoxDefaultRequestObject();
                        defaultRequest.getRequestExtras().addQueryParam(EXTRA_NAV_NUMBER, Integer.toString(mNavNumber));
                        defaultRequest.getRequestExtras().addQueryParam(EXTRA_SOURCE_TYPE, getSourceType());
                        BoxAndroidFolder bf = (BoxAndroidFolder) mClient.getFoldersManager().getFolder(folderId, defaultRequest);
                        if (bf != null) {
                            intent.putExtra(ARG_SUCCESS, true);
                            intent.putExtra(Controller.ARG_BOX_FOLDER, bf);
                        }

                    }
                    catch (AuthFatalFailureException e) {
                        e.printStackTrace();
                        handleAuthenticationError();
                    }
                    catch (BoxRestException e) {
                        e.printStackTrace();

                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    catch (BoxServerException e) {
                        e.printStackTrace();

                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    finally {
                        getLocalBroadcastManager().sendBroadcast(intent);
                    }

                    return intent;
                }
            });

        }

        /**
         * Fetch items from folder using given offset and limit.
         * 
         * @param folderId
         *            Folder id to be fetched.
         * @return A FutureTask that is tasked with fetching information on the given folder.
         */
        public FutureTask<Intent> fetchFolderItems(final String folderId, final int offset, final int limit) {
            return new FutureTask<Intent>(new Callable<Intent>() {

                @Override
                public Intent call() throws Exception {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_FETCHED_FOLDER_ITEMS);
                    intent.putExtra(ARG_OFFSET, offset);
                    intent.putExtra(ARG_LIMIT, limit);
                    intent.putExtra(ARG_FOLDER_ID, folderId);
                    try {

                        // this call the collection is just BoxObjectItems and each does not appear to be an instance of BoxItem.

                        ArrayList<String> itemFields = new ArrayList<String>();
                        itemFields.add(BoxAndroidFile.FIELD_NAME);
                        itemFields.add(BoxAndroidFile.FIELD_SIZE);
                        itemFields.add(BoxAndroidFile.FIELD_OWNED_BY);
                        BoxPagingRequestObject obj = BoxPagingRequestObject.pagingRequestObject(limit, offset);
                        obj.getRequestExtras().addFields(itemFields);
                        BoxAndroidCollection bc = (BoxAndroidCollection) mClient.getFoldersManager().getFolderItems(folderId, obj);
                        if (bc != null) {
                            intent.putExtra(ARG_SUCCESS, true);
                            intent.putExtra(Controller.ARG_BOX_COLLECTION, bc);

                        }

                    }
                    catch (AuthFatalFailureException e) {
                        e.printStackTrace();
                        handleAuthenticationError();
                    }
                    catch (BoxRestException e) {
                        e.printStackTrace();

                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    catch (BoxServerException e) {
                        e.printStackTrace();

                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    finally {
                        getLocalBroadcastManager().sendBroadcast(intent);
                    }

                    return intent;
                }
            });

        }

        /**
         * Download the thumbnail for a given file.
         * 
         * @param fileId
         *            file id to download thumbnail for.
         * @return A FutureTask that is tasked with fetching information on the given folder.
         */
        public FutureTask<Intent> downloadThumbnail(final String fileId, final File downloadLocation, final ViewHolder holder) {
            return new FutureTask<Intent>(new Callable<Intent>() {

                @Override
                public Intent call() throws Exception {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_DOWNLOADED_FILE_THUMBNAIL);
                    intent.putExtra(ARG_FILE_ID, fileId);
                    intent.putExtra(ARG_SUCCESS, false);
                    try {
                        // no need to continue downloading thumbnail if we already have a thumbnail
                        if (downloadLocation.exists() && downloadLocation.length() > 0) {
                            intent.putExtra(ARG_SUCCESS, false);
                            return intent;
                        }
                        // no need to continue downloading thumbnail if we are not viewing this thumbnail.
                        if (holder.getBoxListItem() == null || !(holder.getBoxListItem().getBoxItem() instanceof BoxAndroidFile)
                            || !holder.getBoxListItem().getBoxItem().getId().equals(fileId)) {
                            intent.putExtra(ARG_SUCCESS, false);
                            return intent;
                        }
                        // this call the collection is just BoxObjectItems and each does not appear to be an instance of BoxItem.
                        InputStream input = mClient.getFilesManager().getThumbnail(fileId, "png", BoxImageRequestObject.previewRequestObject()).getContent();
                        FileOutputStream output = new FileOutputStream(downloadLocation);
                        try {
                            IOUtils.copy(input, output);
                        }
                        finally {
                            IOUtils.closeQuietly(input);
                            IOUtils.closeQuietly(output);
                        }
                        if (downloadLocation.exists()) {
                            intent.putExtra(ARG_SUCCESS, true);
                        }
                    }
                    catch (AuthFatalFailureException e) {
                        handleAuthenticationError();
                    }
                    catch (BoxRestException e) {
                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    catch (BoxServerException e) {
                        intent.putExtra(ARG_SUCCESS, false);
                    }
                    finally {
                        getLocalBroadcastManager().sendBroadcast(intent);
                    }

                    return intent;
                }
            });

        }

    }

    @Override
    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
        BoxListItem listItem = (BoxListItem) listView.getItemAtPosition(position);
        if (listItem.getType() == BoxListItem.TYPE_BOX_FILE_ITEM || (listItem.getType() == BoxListItem.TYPE_BOX_FOLDER_ITEM)) {
            BoxItem item = listItem.getBoxItem();
            if (item instanceof BoxAndroidFolder) {
                handleFolderClick((BoxAndroidFolder) item);
                return;
            }
            else if (item instanceof BoxAndroidFile) {
                handleFileClick((BoxAndroidFile) item);
                return;
            }
            else {
                handleOtherClick(item);
                return;
            }
        }
    }

    /**
     * Handle response when a folder is clicked on.
     * 
     * @param folder
     *            The folder clicked on.
     */
    protected void handleFolderClick(BoxAndroidFolder folder) {
        try {
            Intent intent = getLaunchIntent(this, folder.getId(), (BoxAndroidOAuthData) mClient.getAuthData(), clientId, clientSecret);
            intent.setClass(this, getClass());
            startActivity(intent);
        }
        catch (AuthFatalFailureException e) {
            // This sohuld not happen.
        }

    }

    /**
     * Handle response when a file is clicked on.
     * 
     * @param file
     *            The file clicked on.
     */
    protected void handleFileClick(BoxAndroidFile file) {

    }

    /**
     * Handler response when an item other than a file or folder is clicked on.
     * 
     * @param item
     *            The item clicked on.
     */
    protected void handleOtherClick(BoxItem item) {

    }

    /**
     * Handle response if we are unable to authenticate our api calls with the given client.
     */
    protected void handleAuthenticationError() {
        finish();
    }

}
