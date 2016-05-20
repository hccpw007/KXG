package com.cqts.kxg.views;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.cqts.kxg.R;

/**
 * 商品收藏的变幻动画
 */
public class FavoriteAnimation extends Animation {
	/** Z轴上最大深度。 */
	public static final float DEPTH_Z = 310.0f;
	/** 动画显示时长。 */
	public static final long DURATION = 1000;
	private final float centerX;
	private final float centerY;
	private Camera camera;
	/** 用于监听动画进度。当值过半时需更新txtNumber的内容。 */
	ImageView imageView;
	boolean toSolid;
	boolean can;
	public FavoriteAnimation(ImageView imageView, boolean toSolid) {
		this.imageView = imageView;
		this.toSolid = toSolid;
		centerX = imageView.getWidth() / 2.0f;
		centerY = imageView.getHeight() / 2.0f;
		setDuration(DURATION);
		setFillAfter(true);
	}
	
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		// 在构造函数之后、getTransformation()之前调用本方法。
		super.initialize(width, height, parentWidth, parentHeight);
		camera = new Camera();
	}

	protected void applyTransformation(float interpolatedTime,
			Transformation transformation) {
		// interpolatedTime:动画进度值，范围为[0.0f,1.0f]
		if (interpolatedTime == 0.0f) {
			can = true;
			imageView.setEnabled(false);
		}
		if (interpolatedTime == 1.0f) {
			imageView.setEnabled(true);
		}
		if (interpolatedTime > 0.5f&&can) {
			can = false;
			if (toSolid) {
				imageView.setImageResource(R.mipmap.home_taoxin_hover);
			}else {
				imageView.setImageResource(R.mipmap.home_taoxin);
			}
		}
		
		float from = 0.0f, to = 0.0f;
		from = 360.0f;
		to = 180.0f;
		float degree = from + (to - from) * interpolatedTime;
		boolean overHalf = (interpolatedTime > 0.5f);
		if (overHalf) {
			// 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。
			degree = degree - 180;
		}
		// float depth = 0.0f;
		float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;
		final Matrix matrix = transformation.getMatrix();
		camera.save();
		camera.translate(0.0f, 0.0f, depth);
		camera.rotateY(degree);
		camera.getMatrix(matrix);
		camera.restore();
		// 确保图片的翻转过程一直处于组件的中心点位置
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}