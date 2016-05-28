package com.base.utils;

/**
 * 调用系统图片工具
 */
import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

public  class PhotoUtil {
	private Activity context1;
	private Fragment context2;
	private Uri imgUri;
	private Uri forfexUri;
	private int X;
	private int Y;
	private ContentResolver contentResolver;
	private File fileDir;
	private String forfexPath;

	//返回裁剪过后的图片路径
	public String getForfexPath(){
		return forfexPath;
	}
	
	public PhotoUtil(Activity context1, int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.context1 = context1;
		contentResolver = context1.getContentResolver();
		getUri();
	}

	public PhotoUtil(Fragment context2, int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.context2 = context2;
		contentResolver = context2.getActivity().getContentResolver();
		getUri();
	}

	// 请求码标识符
	public static class FromWhere {
		public static final int camera = 100; // 相机
		public static final int photo = 101; // 相册
		public static final int forfex = 102; // 裁剪
	}

	// 启动相机
	public void startCamera() {
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
		
		if (null!=context1) {
			context1.startActivityForResult(intent, FromWhere.camera);
		}else {
			context2.startActivityForResult(intent, FromWhere.camera);
		}
	}

	// 启动相册
	public void startPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		
		if (null!=context1) {
			context1.startActivityForResult(intent, FromWhere.photo);
		}else {
			context2.startActivityForResult(intent, FromWhere.photo);
		}

	}

	// 裁剪图片
	public void startForfex(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", X);
		intent.putExtra("aspectY", Y);
		intent.putExtra("outputX", 100 * X);
		intent.putExtra("outputY", 100 * Y);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, forfexUri);
		intent.putExtra("noFaceDetection", true);

		if (null != context1) {
			context1.startActivityForResult(intent, FromWhere.forfex);
		} else {
			context2.startActivityForResult(intent, FromWhere.forfex);
		}
	}

	// 得到存放img的目录
	private void getUri() {

		// 裁剪后图片存放的位置
		String SDFile = "/Android/data/com.rryylsb.member";
		String Rootfile = "data/data/com.rryylsb.member";

		fileDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		String filePath;
		if (sdCardExist) {
			filePath = Environment.getExternalStorageDirectory().getPath() + SDFile;
		} else {
			filePath = Environment.getRootDirectory().getParentFile().getPath() + Rootfile;
		}
		fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();// 创建文件夹
		}
		
		imgUri = Uri.fromFile(new File(fileDir.getPath() + "/imgUri.jpg")); // 原图
		forfexUri = Uri.fromFile(new File(fileDir.getPath() + "/forfexUri.jpg")); // 裁剪后的图
		forfexPath = fileDir.getPath() + "/forfexUri.jpg";
	}

	// 返回数据
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			// 取消后不作任何操作
			return;
		}
		try {
			switch (requestCode) {
			case FromWhere.camera:
				startForfex(imgUri);
				break;
			case FromWhere.photo:
				Bitmap bitmap = null;
				if (null != data.getData()) {
					imgUri = data.getData();
				} else if (null != data.getDataString() && !"".equals(data.getDataString())) {
					imgUri = Uri.parse(data.getDataString());
				} else if (null != data.getExtras()) {
					bitmap = data.getExtras().getParcelable("data");
					imgUri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, null, null));
				}
				startForfex(imgUri);
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
	}
}
