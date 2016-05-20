package com.cqts.kxg.classify;

        import android.os.Bundle;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;

        import com.base.BaseFragment;
        import com.base.views.MyGridDecoration;
        import com.base.views.MyItemDecoration;
        import com.cqts.kxg.R;
        import com.cqts.kxg.classify.adapter.ClassifyRecyclerViewAdapter1;
        import com.cqts.kxg.classify.adapter.ClassifyRecyclerViewAdapter2;
        import com.cqts.kxg.classify.bean.ClassifyListInfo;

        import java.util.ArrayList;

public class ClassifyFragment extends BaseFragment {

    private RecyclerView classify_rv1;
    private RecyclerView classify_rv2;
    private ArrayList<ClassifyListInfo> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_classify, null);
            InitView();
        }
        return view;
    }

    private void InitView() {
        classify_rv1 = (RecyclerView) view.findViewById(R.id.classify_rv1);
        classify_rv2 = (RecyclerView) view.findViewById(R.id.classify_rv2);

        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ClassifyListInfo classifyListInfo = new ClassifyListInfo();
            classifyListInfo.abc =1;
            list.add(classifyListInfo);
        }

        InitRecyclerView1();
        InitRecyclerView2();
    }

    private void InitRecyclerView1() {
        classify_rv1.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        classify_rv1.setLayoutManager(manager);
        classify_rv1.setAdapter(new ClassifyRecyclerViewAdapter1(getActivity(),list));
    }

    private void InitRecyclerView2() {
        classify_rv2.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        classify_rv2.setLayoutManager(manager);
        MyGridDecoration myGridDecoration = new MyGridDecoration(20, 10, getResources().getColor(R.color.white), false);
        myGridDecoration.setImageView(R.id.item_classifyrv2_img,1);
        classify_rv2.addItemDecoration(myGridDecoration);
        classify_rv2.setAdapter(new ClassifyRecyclerViewAdapter2(getActivity(),list));
    }
}
