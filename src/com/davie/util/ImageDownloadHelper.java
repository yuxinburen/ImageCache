package com.davie.util;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.util.LruCache;

public class ImageDownloadHelper {

	private Map<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();// 软引用集合
	private MyLruCache lruCache;
	private Context context;

	public ImageDownloadHelper(Context context) {
		super();
		this.context = context;
		int memoryCount = (int) Runtime.getRuntime().maxMemory();
		lruCache = new MyLruCache(memoryCount / 8);
	}

	/**
	 * 根据url获取图片方法
	 * @param url
	 * @return
	 */
	public Bitmap getImage(final String url) {
		//1、从强引用软引用以及SD卡中查找图片资源
		Bitmap bm = getImageFromCacheAndSD(url);
		if (bm != null) {//得到图片 返回
			return bm;
		} else {//没有得到图片,去网络端查找
			final String imageName = url.substring(url.lastIndexOf("/") + 1);
			
			new DownLoadImage(new OnSaveImageListener() {
				@Override
				public void onSaveImage(byte[] data) {
//					SDCardHelper.saveFileToSDCardPrivateCacheDir(data,
//							imageName , context);
					SDCardHelper.saveFileToSDCardPrivateCacheDir(data,
							imageName , context);

				}
			}).execute(url);
			Bitmap bm2 = createThumbnail(SDCardHelper
					.getSDCardPrivateCacheDir(context)
					+ File.separator
					+ imageName );
			if (bm2 != null) {
				lruCache.put(imageName, bm2);
				return bm2;
			}
		}
		return null;
	}

	public interface OnLoadResultListener{
		void onLoadResult(Bitmap bitmap);
	}

	private OnLoadResultListener loadResultListener;
	public void getImage(final String url,final OnLoadResultListener loadResultListener) {
		//1、从强引用软引用以及SD卡中查找图片资源
		Bitmap bm = getImageFromCacheAndSD(url);
		if (bm != null) {//得到图片 返回
			loadResultListener.onLoadResult(bm);
		} else {//没有得到图片,去网络端查找
			final String imageName = url.substring(url.lastIndexOf("/") + 1);
			new DownLoadImage(new OnSaveImageListener() {
				@Override
				public void onSaveImage(byte[] data) {
//					SDCardHelper.saveFileToSDCardPrivateCacheDir(data,
//							imageName , context);
					SDCardHelper.saveFileToSDCardPrivateCacheDir(data,
							url , context);

					Bitmap bm2 = createThumbnail(SDCardHelper
							.getSDCardPrivateCacheDir(context)
							+ File.separator
							+ imageName );
					if (bm2 != null) {
						lruCache.put(imageName, bm2);
						loadResultListener.onLoadResult(bm2);
					}
				}
			}).execute(url);
		}
	}

	/**
	 * 从强引用、软引用以及SD卡中查找图片
	 * @param url
	 * @return
	 */
	public Bitmap getImageFromCacheAndSD(String url) {
		Bitmap bm = null;
		String imageName = url.substring(url.lastIndexOf("/") + 1);
		bm = lruCache.get(imageName);// 从强引用中查找图片
		if (bm != null) {
			return bm;
		} else {
			SoftReference<Bitmap> softReference = map.get(imageName);// 从软引用中查找图片
			if (softReference != null) {
				bm = softReference.get();
				if (bm != null) {
					lruCache.put(imageName, bm);// 将图片存入强引用
					map.remove(imageName);// 将图片移除软引用
					return bm;
				}
			} else {
				byte[] urlMd5 = EncryptUtil.md5(url.getBytes());
				String urlHex = EncryptUtil.toHex(urlMd5);
				String fileName = urlHex + ".jpg";
				File file = new File(
						SDCardHelper.getSDCardPrivateCacheDir(context)
								+ fileName );
				if (file.exists()) {
					bm = createThumbnail(file.getAbsolutePath());
					lruCache.put(imageName, bm);
					return bm;
				}
			}
		}
		return null;
	}

	/**
	 * 二次采用获取图片
	 * @param path
	 * @return
	 */
	private Bitmap createThumbnail(String path) {
		int width = 100;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int imagewidth = options.outWidth;
		int ratioWidth = imagewidth / width;
		options.inSampleSize = ratioWidth;
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	class MyLruCache extends LruCache<String, Bitmap> {

		public MyLruCache(int maxSize) {// 强引用的数目 超出的放入软引用
			super(maxSize);
		}

		@Override
		protected int sizeOf(String key, Bitmap value) {// 计算Bitmap的尺寸，即这个图片占用多大的空间
			return value.getByteCount();// 得到字节数量，高版本计算方法
		}

		@Override
		//
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			super.entryRemoved(evicted, key, oldValue, newValue);
			if (evicted) {
				SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(
						oldValue);// 将不常用的图片移入软引用
				map.put(key, softReference);
			}
		}
	}

	class DownLoadImage extends AsyncTask<String, Void, byte[]> {

		private OnSaveImageListener listener;

		public DownLoadImage(OnSaveImageListener listener) {
			this.listener = listener;
		}

		@Override
		protected byte[] doInBackground(String... params) {
			byte[] imageBytes = HttpURLConnHelper.loadByteFromURL(params[0]);
			if (imageBytes != null)
				return imageBytes;
			else
				return null;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			super.onPostExecute(result);
			if(result!=null)
			listener.onSaveImage(result);
		}
	}

	public interface OnSaveImageListener {
		void onSaveImage(byte[] data);
	}
}
