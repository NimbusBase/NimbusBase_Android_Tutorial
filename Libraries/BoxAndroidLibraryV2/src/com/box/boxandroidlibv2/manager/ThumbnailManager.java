package com.box.boxandroidlibv2.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.box.boxandroidlibv2.R;
import com.box.boxjavalibv2.dao.BoxFolder;
import com.box.boxjavalibv2.dao.BoxItem;

/**
 * 
 * This class manages thumbnails to display to users. This class does not do network calls.
 */
public class ThumbnailManager {

    /** The prefix added for thumbnails in this manager. */
    private static final String THUMBNAIL_FILE_PREFIX = "thumbnail_";

    /** The extension added for thumbnails in this manager. */
    private static final String THUMBNAIL_FILE_EXTENSION = ".boxthumbnail";

    /** The path where files in thumbnail should be stored. */
    private File mCacheDirectory;

    /** Handler on ui thread */
    private Handler mHandler;
    
    /** Executor that we will submit our set thumbnail tasks to. */
    private ThreadPoolExecutor thumbnailManagerExecutor;

    /**
     * Constructor.
     * 
     * @param cacheDirectoryPath
     *            the directory to store thumbnail images.
     * @throws FileNotFoundException
     *             thrown if the directory given does not exist and cannot be created.
     */
    public ThumbnailManager(final String cacheDirectoryPath) throws FileNotFoundException {
        this(new File(cacheDirectoryPath));
    }

    /**
     * Constructor.
     * 
     * @param cacheDirectory
     *            a file representing the directory to store thumbnail images.
     * @throws FileNotFoundException
     *             thrown if the directory given does not exist and cannot be created.
     */
    public ThumbnailManager(final File cacheDirectory) throws FileNotFoundException {
        mCacheDirectory = cacheDirectory;
        mCacheDirectory.mkdirs();
        if (!mCacheDirectory.exists() || !mCacheDirectory.isDirectory()) {
            throw new FileNotFoundException();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Sets the best known image for given item into the given view.
     * 
     * @param icon
     *            view to set image for.
     * @param boxItem
     *            the BoxItem used to set the view.
     */
    public void setThumbnailIntoView(final ImageView icon, final BoxItem boxItem) {
        final WeakReference<ImageView> iconHolder = new WeakReference<ImageView>(icon);
        final Resources res = icon.getResources();
        getThumbnailExecutor().execute(new Runnable() {

            public void run() {
                setThumbnail(iconHolder.get(), getDefaultIconResource(boxItem));
                Bitmap cachedIcon = getCachedIcon(boxItem);

                if (cachedIcon != null) {
                    setThumbnail(iconHolder.get(), new BitmapDrawable(res, cachedIcon));
                }
            }
        });

    }

    /**
     * Set the given image resource into given view on ui thread.
     * 
     * @param icon
     *            the ImageView to put drawable into.
     * @param imageRes
     *            the image resource to set into icon.
     */
    private void setThumbnail(final ImageView icon, final int imageRes) {
        mHandler.post(new Runnable() {

            public void run() {
                if (icon != null) {
                    icon.setImageResource(imageRes);
                }
            }
        });
    }

    /**
     * Set the given drawable into given view on ui thread.
     * 
     * @param icon
     *            the ImageView to put drawable into.
     * @param drawable
     *            the drawable to set into icon.
     */
    private void setThumbnail(final ImageView icon, final Drawable drawable) {
        mHandler.post(new Runnable() {

            public void run() {
                if (icon != null) {
                    icon.setImageDrawable(drawable);

                }
            }
        });

    }

    /**
     * Gets the default icon resource depending on what kind of boxItem is being viewed.
     * 
     * @param boxItem
     *            The box item to show to user.
     * @return an integer resource.
     */
    private int getDefaultIconResource(final BoxItem boxItem) {
        if (boxItem instanceof BoxFolder) {
            return R.drawable.boxandroidlibv2_icon_folder_personal;
        }
        else {
            return R.drawable.boxandroidlibv2_generic;
        }
    }

    /**
     * Gets a bitmap for the item if one exists.
     * 
     * @param boxItem
     *            The box item to show to user.
     * @return an integer resource.
     */
    private Bitmap getCachedIcon(final BoxItem boxItem) {
        File file = getCachedFile(boxItem);
        if (file.exists() && file.isFile()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bitmap;
        }
        return null;
    }

    /**
     * Returns a file in a determinate location for the given boxItem.
     * 
     * @param boxItem
     *            the box item to generate file or get file for.
     * @return a File object where the icon is located or written to if non existent.
     */
    private File getCachedFile(final BoxItem boxItem) {
        return getThumbnailForFile(boxItem.getId());

    }

    /**
     * Returns a file in a determinate location for the given boxItem.
     * 
     * @param fileId
     *            The id of the box file.
     * @return a File object where the thumbnail is saved to or should be saved to.
     */
    public File getThumbnailForFile(final String fileId) {
        File file = new File(getCacheDirectory(), THUMBNAIL_FILE_PREFIX + fileId + THUMBNAIL_FILE_EXTENSION);
        return file;
    }

    /**
     * @return the cacheDirectory of this thumbnail manager.
     */
    public File getCacheDirectory() {
        return mCacheDirectory;
    }

    /**
     * Convenience method to delete all files in the provided cache directory.
     */
    public void deleteFilesInCacheDirectory() {
        File[] files = mCacheDirectory.listFiles();
        if (files != null) {
            for (int index = 0; index < files.length; index++) {
                if (files[index].isFile()) {
                    files[index].delete();
                }
            }
        }
    }

  

    /**
     * Executor that we will submit our set thumbnail tasks to.
     * 
     * @return executor
     */
    private ThreadPoolExecutor getThumbnailExecutor() {
        if (thumbnailManagerExecutor == null || thumbnailManagerExecutor.isShutdown()) {
            thumbnailManagerExecutor = new ThreadPoolExecutor(4, 10, 3600, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return thumbnailManagerExecutor;
    }

}
