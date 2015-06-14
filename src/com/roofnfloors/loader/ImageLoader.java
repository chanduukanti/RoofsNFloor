package com.roofnfloors.loader;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;

import com.roofnfloors.cache.FileCache;
import com.roofnfloors.cache.MemoryCache;
import com.roofnfloors.net.ServerDataFetcher;
import com.roofnfloors.ui.ProjectDetailsActivity;
import com.roofnfloors.util.Utility;

public class ImageLoader {
    private ProjectDetailsActivity mCallBack;
    private int mImagePosition;

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();

        private static ImageLoader getImageLoader(
                ProjectDetailsActivity activity) {
            INSTANCE.mCallBack = activity;
            INSTANCE.mExecutorService = Executors.newFixedThreadPool(5);
            return INSTANCE;
        }
    }

    public static ImageLoader getInstance(ProjectDetailsActivity activity) {
        return ImageLoaderHolder.getImageLoader(activity);
    }

    public interface ImageLoaderCallBack {
        public void onImageLoadingCompleted(Bitmap map, int pos);
    }

    private Map<String, Bitmap> mImagesMap = Collections
            .synchronizedMap(new WeakHashMap<String, Bitmap>());

    private ExecutorService mExecutorService;

    public ImageLoader() {
    }

    public void stopImageLoader() {
        if (null != mExecutorService) {
            mExecutorService.shutdown();
            mExecutorService = null;
            mImagesMap.clear();
        }
    }

    public void displayImage(String url) {
        Bitmap map = mImagesMap.get(url);
        if (map != null) {
            mCallBack.onImageLoadingCompleted(map, mImagePosition);
        } else {
            map = MemoryCache.getInstance().get(url);
            if (map != null) {
                mImagesMap.put(url, map);
                mCallBack.onImageLoadingCompleted(map, mImagePosition);
            } else {
                addtoDownloadImageQueue(url);
            }
        }
    }

    private void addtoDownloadImageQueue(String url) {
        PhotoToLoad p = new PhotoToLoad(url, mImagePosition);
        mExecutorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f = FileCache.getInstance(mCallBack).getFile(url);

        // from SD cache
        Bitmap b = Utility.decodeFile(f);
        if (b != null)
            return b;

        // from web
        try {
            return ServerDataFetcher.downloadImage(url, mCallBack);
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                MemoryCache.getInstance().clear();
            return null;
        }
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public int position;

        public PhotoToLoad(String u, int pos) {
            url = u;
            position = pos;
        }
    }

    private class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            Bitmap bmp = getBitmap(photoToLoad.url);
            if (bmp != null) {
                mCallBack.onImageLoadingCompleted(bmp, photoToLoad.position);
                mImagesMap.put(photoToLoad.url, bmp);
                MemoryCache.getInstance().put(photoToLoad.url, bmp);
            }
        }
    }

    public void clearCache() {
        MemoryCache.getInstance().clear();
        FileCache.getInstance(mCallBack).clear();
    }

    public int getmImagePosition() {
        return mImagePosition;
    }

    public void setmImagePosition(int mImagePosition) {
        this.mImagePosition = mImagePosition;
    }

}
