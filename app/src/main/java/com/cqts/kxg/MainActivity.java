package com.cqts.kxg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.BaseActivity;
import com.base.views.MyGridDecoration;
import com.cqts.kxg.main.MyActivity;

public class MainActivity extends MyActivity {

	private GridLayoutManager manager1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main3);
//		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recy);
////		LinearLayoutManager manager = new LinearLayoutManager(this);
////		manager.setOrientation(OrientationHelper.VERTICAL);
//
//
//		manager1 = new GridLayoutManager(this,3);
//
//		recyclerView.setLayoutManager(manager1);
//
//
//		recyclerView.addItemDecoration(new MyGridDecoration(50,50,Color.RED,false));
//
//		recyclerView.setAdapter(new RecyclerView.Adapter() {
//			@Override
//			public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//				View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_homerv,null);
//				Holser holser = new Holser(view);
//				return holser;
//			}
//			@Override
//			public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
//			}
//			@Override
//			public int getItemCount() {
//				return 22;
//			}
//		});
//		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
////				if (manager1.findViewByPosition(1).getBottom() ==recyclerView.getChildCount()){
////					System.out.println("dx:"+dx+"----dy:"+dy);
////				}
//				GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
////				int childCount = layoutManager.getChildCount();
////				System.out.println(layoutManager.findLastVisibleItemPosition()+"==="+childCount);
//				if (layoutManager.findLastVisibleItemPosition() == 20){
//					System.out.println(layoutManager.findLastVisibleItemPosition()+"===");
//				}
//			}
//		});
//	}
//
//	class Holser extends RecyclerView.ViewHolder{
//		public Holser(View itemView) {
//			super(itemView);
//		}
	}
}
