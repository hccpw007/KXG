package com.base.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.base.utils.PhotoUtil;
import com.cqts.kxg.R;

public class PhotoPopupWindow {

	Activity context;
	LayoutInflater inflater;
	PopupWindow popupWindow;
	public static Uri imageUri;
	public static String tempfile;
	private PhotoUtil photoUtil;
	private View view;
	private int which;

	public PhotoPopupWindow(Activity context, PhotoUtil photoUtil) {
		this.context = context;
		this.photoUtil = photoUtil;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.pop_photo, null);
		Button pop_btn_camera = (Button) view.findViewById(R.id.pop_btn_camera);
		Button pop_btn_photo = (Button) view.findViewById(R.id.pop_btn_photo);
		Button pop_btn_no = (Button) view.findViewById(R.id.pop_btn_no);

		popupWindow = new PopupWindow(context);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		pop_btn_camera.setOnClickListener(click);
		pop_btn_photo.setOnClickListener(click);
		pop_btn_no.setOnClickListener(click);
	}

	public void showpop(View view) {
		this.view = view;
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}
	public void showpop(View view,int which) {
		this.view = view;
		this.which = which;
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	public ImageView getImageView() {
		return (ImageView) view;
	}
	public int getInt() {
		return which;
	}

	OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			File file = Environment.getExternalStorageDirectory();
			switch (v.getId()) {
			case R.id.pop_btn_camera:
				photoUtil.startCamera();
				popupWindow.dismiss();
				break;
			case R.id.pop_btn_photo:
				photoUtil.startPhoto();
				popupWindow.dismiss();
				break;
			case R.id.pop_btn_no:
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	public static String getPATH() {
		return tempfile;
	}

	public static Uri getImgUri() {
		return imageUri;
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
