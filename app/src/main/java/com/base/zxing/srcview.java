package com.base.zxing;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public  class srcview extends ScrollView{
	
	public srcview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		sda.dsaf(l, t, oldl, oldt);
	}
	adsga sda;
	interface adsga{
		void dsaf(int l, int t, int oldl, int oldt);
	};
	public void setscr(adsga sda){
		this.sda = sda;
	}                                                          
}
