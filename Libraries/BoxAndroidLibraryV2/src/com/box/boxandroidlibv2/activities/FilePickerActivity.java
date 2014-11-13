package com.box.boxandroidlibv2.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.box.boxandroidlibv2.R;
import com.box.boxandroidlibv2.adapters.NavigationListAdapter;
import com.box.boxandroidlibv2.dao.BoxAndroidFile;
import com.box.boxandroidlibv2.dao.BoxAndroidFolder;
import com.box.boxandroidlibv2.dao.BoxAndroidOAuthData;
import com.box.boxandroidlibv2.viewdata.NavigationItem;
import com.box.boxjavalibv2.dao.BoxItem;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;

/**
 * This class is used to choose a particular file from the user's account and when chosen will return a result with a BoxAndroidFile in the extra
 * EXTRA_BOX_ANDROID_FILE.
 * 
 * @author dcung
 * 
 */
public class FilePickerActivity extends FolderNavigationActivity {

    public static final String EXTRA_BOX_ANDROID_FILE = "extraBoxAndroidFile";

    protected static final String EXTRA_BOX_ANDROID_FOLDER = "extraBoxAndroidFile";

    /** Used to keep track of whether or not this is the first activity launched */
    protected static final String EXTRA_FOLDER_NAME = "extraFolderName";

    protected String mFolderName;

    protected BoxAndroidFolder mCurrentFolder = null;

    private final int FILE_PICKER_REQUEST_CODE = 4;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mFolderName = getIntent().getStringExtra(EXTRA_FOLDER_NAME);
        }
        if (savedInstanceState != null) {
            mCurrentFolder = savedInstanceState.getParcelable(EXTRA_BOX_ANDROID_FOLDER);
            mFolderName = savedInstanceState.getString(EXTRA_FOLDER_NAME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentFolder == null && mFolderName != null) {
            setNavigationSpinner(new NavigationItem(mFolderName, mCurrentFolderId));
        }
        setNavigationSpinner(mCurrentFolder);
    }

    /**
     * Return the spinner used to navigate up folder heirarchy.
     */
    protected Spinner getNavigationSpinner() {
        return (Spinner) this.findViewById(R.id.folderChooserSpinner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // if someone further down the chain has chosen a file then send result back.
                setResult(resultCode, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Opens a new folder activity with given folderId.
     * 
     * @param folderId
     *            The folderId to navigate to.
     */
    private void openFolder(final String folderId) {
        if (folderId == null || mCurrentFolder != null && mCurrentFolder.getId().equals(folderId)) {
            return;
        }
        try {
            Intent intent = getLaunchIntent(this, folderId, (BoxAndroidOAuthData) mClient.getAuthData(), clientId, clientSecret);

            intent.setClass(this, getClass());
            intent.putExtra(EXTRA_NAV_NUMBER, (mNavNumber + 1));

            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        }
        catch (AuthFatalFailureException e) {
            // This shouldn't happen.
        }
    }

    /**
     * Opens a new folder activity with given folderId and folder name.
     * 
     * @param folderName
     *            name of folder to open.
     * @param folderId
     *            The folderId to navigate to.
     */
    private void openFolder(final String folderName, final String folderId) {
        if (folderId == null || mCurrentFolder != null && mCurrentFolder.getId().equals(folderId)) {
            return;
        }

        try {
            Intent intent = getLaunchIntent(this, folderName, folderId, (BoxAndroidOAuthData) mClient.getAuthData(), clientId, clientSecret);
            intent.setClass(this, getClass());
            intent.putExtra(EXTRA_NAV_NUMBER, (mNavNumber + 1));

            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        }
        catch (AuthFatalFailureException e) {
            // This shouldn't happen.
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_BOX_ANDROID_FOLDER, mCurrentFolder);
        outState.putString(EXTRA_FOLDER_NAME, mFolderName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initializeViews() {
        setContentView(R.layout.boxandroidlibv2_layout_file_picker);
        TextView customTitle = (TextView) this.findViewById(R.id.customTitle);
        customTitle.setText(getTitle());
        // to make dialog theme fill the full view.
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    protected ListView getListView() {
        return (ListView) this.findViewById(R.id.PickerListView);
    }

    @Override
    protected void handleFileClick(final BoxAndroidFile file) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BOX_ANDROID_FILE, file);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onFetchedFolder(Intent intent) {
        if (intent.getBooleanExtra(Controller.ARG_SUCCESS, false)) {
            if (mCurrentFolder == null) {
                mCurrentFolder = intent.getParcelableExtra(Controller.ARG_BOX_FOLDER);
            }
            setNavigationSpinner(mCurrentFolder);
        }
        super.onFetchedFolder(intent);
    }

    @Override
    protected void handleFolderClick(BoxAndroidFolder folder) {
        openFolder(folder.getName(), folder.getId());

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
        Intent intent = FolderNavigationActivity.getLaunchIntent(context, folderId, oauth, clientId, clientSecret);
        intent.setClass(context, FilePickerActivity.class);
        return intent;
    }

    /**
     * Create an intent to launch an instance of this activity to navigate folders. This version will immediately show the given name in the navigation spinner
     * to before information about it has been fetched from the server.
     * 
     * @param context
     *            current context.
     * @param folderName
     *            Name to show in the navigation spinner. Should be name of the folder.
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
    public static Intent getLaunchIntent(Context context, final String folderName, final String folderId, final BoxAndroidOAuthData oauth,
        final String clientId, final String clientSecret) {
        Intent intent = getLaunchIntent(context, folderId, oauth, clientId, clientSecret);
        intent.putExtra(EXTRA_FOLDER_NAME, folderName);
        return intent;
    }

    /**
     * Shows a single navigation item in the spinner.
     * 
     * @param navigationItem
     *            The navigation item to show.
     */
    protected void setNavigationSpinner(final NavigationItem navigationItem) {
        if (navigationItem == null) {
            return;
        }

        ArrayList<NavigationItem> navigationFolders = new ArrayList<NavigationItem>();
        navigationFolders.add(navigationItem);
        Spinner spinner = getNavigationSpinner();
        spinner.setAdapter(new NavigationListAdapter(this, navigationFolders));

    }

    /**
     * Shows a navigation spinner with all items in the provided folder.
     * 
     * @param currentFolder
     *            The folder to show in the navigation spinner.
     */
    protected void setNavigationSpinner(final BoxAndroidFolder currentFolder) {
        if (currentFolder == null) {
            return;
        }

        ArrayList<NavigationItem> navigationFolders = new ArrayList<NavigationItem>();
        navigationFolders.add(new NavigationItem(currentFolder.getName(), currentFolder.getId()));
        for (int i = currentFolder.getPathCollection().getEntries().size() - 1; i >= 0; i--) {
            BoxItem item = (BoxItem) currentFolder.getPathCollection().getEntries().get(i);
            navigationFolders.add(new NavigationItem(item.getName(), item.getId()));
        }
        Spinner spinner = getNavigationSpinner();
        spinner.setAdapter(new NavigationListAdapter(this, navigationFolders));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                NavigationItem item = (NavigationItem) parent.getItemAtPosition(pos);
                openFolder(item.getFolderId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

    @Override
    protected String getSourceType() {
        return "file_picker";
    }

}
