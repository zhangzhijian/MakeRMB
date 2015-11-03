package com.dream.makermb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardUtil {
	public final static String SDCARD_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/vendor/";
	public final static String IMAGECACHE_DIR = SDCARD_DIR + "/cache/";
	public final static String DOWNLOAD_FILE_DIR = SDCARD_DIR + "/download/";
	private final static String TAG = "SDCardUtil";
	
	static {
		if (hasSDCard()) {
			File file = new File(SDCARD_DIR);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(IMAGECACHE_DIR);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(DOWNLOAD_FILE_DIR);
			if (!file.exists()) {
				file.mkdirs();
			}
//			file = new File(PHOTO_DIR);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
		
//			file = new File(NOMEID_FILE);
//			if (!file.exists()) {
//				try {
//					file.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}

	}

	public static boolean hasSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static Bitmap featBitmap(String path, int width) {
		Bitmap bitmap = null;
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = 1;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			int bitmap_w = options.outWidth;
			while (bitmap_w / (float) width > 2) {
				options.inSampleSize <<= 1;
				bitmap_w >>= 1;
			}
			return BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
		}
		return bitmap;
	}

	/**
	 * 获取合适尺寸的图片 图片的长或高中较大的值要 < suitableSize*factor
	 * 
	 * @param path
	 * @param suitableSize
	 * @return
	 */
	public static Bitmap featBitmapToSuitable(String path, int suitableSize,
			float factor) {
		Bitmap bitmap = null;
		try {
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = 1;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			int bitmap_w = options.outWidth;
			int bitmap_h = options.outHeight;
			int max_edge = bitmap_w > bitmap_h ? bitmap_w : bitmap_h;
			while (max_edge / (float) suitableSize > factor) {
				options.inSampleSize <<= 1;
				max_edge >>= 1;
			}
			return BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
		}
		return bitmap;
	}

	/**
	 * 清理sdcard上面时间早于time的缓存图片
	 * 
	 * @param time
	 */
	public static void clear(long time) {
		if (!SDCardUtil.hasSDCard()) {
			return;
		}
		clearFile(time, System.currentTimeMillis(), SDCardUtil.IMAGECACHE_DIR);
	}

	/**
	 * 
	 * @param time
	 * @param currentTime
	 * @param path
	 */
	private static void clearFile(long time, long currentTime, String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			// 删除早于time的文件
			if (file.lastModified() < time) {
				if (!file.getAbsolutePath().endsWith(".log")) {
					file.delete();
					// Debug.info(TAG, "delete file " + file.getAbsolutePath());
				}
			}
			// 清理由于系统时间不准确可能引入的脏文件
			else if (file.lastModified() > currentTime + 1000 * 60 * 60 * 24) {
				if (!file.getAbsolutePath().endsWith(".log")) {
					file.delete();
					// Debug.info(TAG,
					// "delete dirty file " + file.getAbsolutePath());
				}
			} else {
				// Debug.info(TAG, "skip file " + file.getAbsolutePath());
			}
		} else {
			File[] files = file.listFiles();
			if (files != null) {
				for (File file2 : files) {
					clearFile(time, currentTime, file2.getAbsolutePath());
				}
			}
		}

	}

	/**
	 * 删文件或者目录
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	/**
	 * 转换bitmap为字节数组
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap) {
		final int size = bitmap.getWidth() * bitmap.getHeight() * 4;
		final ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		byte[] image = out.toByteArray();
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;

	}

	/**
	 * 从exif信息获取图片旋转角度
	 * 
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 图片旋转90度
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotate(Bitmap bitmap, int rotate) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Bitmap rotateBitmap = bitmap;
		Matrix m = new Matrix();
		m.setRotate(rotate, w / 2f, h / 2f);
		try {
			rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m, false);
		} catch (OutOfMemoryError ex) {
			System.gc();
		}
		return rotateBitmap;
	}

	/**
	 * 对图片进行压缩选择处理
	 * 
	 * @param picPath
	 * @return
	 */
	public static Bitmap compressRotateBitmap(String picPath) {
		Bitmap bitmap = null;
		int degree = readPictureDegree(picPath);
		if (degree == 90) {
			bitmap = SDCardUtil.featBitmapToSuitable(picPath, 500, 1.8f);
			bitmap = rotate(bitmap, 90);
		} else {
			bitmap = SDCardUtil.featBitmapToSuitable(picPath, 500, 1.8f);
		}
		return bitmap;
	}

	public static boolean checkFileExits(String fileName) {
		File file = new File(IMAGECACHE_DIR, fileName);
		return file.exists();
	}

	public static String savePic(byte[] data, String path) {
		File file = new File(path);
		if (file.exists()) {
			file.mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return path;
	}

	public static String saveBitmap(String path, Bitmap bitmap, boolean recycle) {
		return saveBitmap(path, bitmap, Bitmap.CompressFormat.JPEG, recycle);
	}

	public static String saveBitmap(String path, Bitmap bitmap,
			Bitmap.CompressFormat format, boolean recycle) {
		File f = new File(path);
		try {
			f.createNewFile();
			FileOutputStream fOut = new FileOutputStream(f);
			bitmap.compress(format, 100, fOut);
			fOut.flush();
			fOut.close();
			if (recycle) {
				bitmap.recycle();
			}
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static String savePic(byte[] data) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
//				Locale.CHINESE);
//		String picName = sdf.format(new Date()) + ".png";
//		String path = PHOTO_DIR + picName;
//		return savePic(data, path);
//	}
}
