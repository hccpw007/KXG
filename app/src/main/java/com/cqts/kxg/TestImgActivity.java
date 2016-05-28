//package com.cqts.kxg;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.cqts.kxg.utils.MyHttp;
//
//public class TestImgActivity extends Activity {
//    private Button testbtn;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_img);
//        testbtn = (Button) findViewById(R.id.testbtn);
//        testbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PhotoUtil photoUtil =  new PhotoUtil(TestImgActivity.this, 3, 3);
//                photoUtil.startPhoto();
//            }
//        });
//    }
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case PhotoUtil.FromWhere.camera:
//            case PhotoUtil.FromWhere.photo:
//                photoUtil.onActivityResult(requestCode, resultCode, data);
//                break;
//            case PhotoUtil.FromWhere.forfex:
//                if (resultCode == getActivity().RESULT_OK) {
//                    MyHttp.uploadImage(http, null, photoUtil.getForfexPath(), new MyHttp.MyHttpResult() {
//                        @Override
//                        public void httpResult(Integer which, int code, String msg, Object bean) {
//                        }
//                    });
//                }
//                break;
//            default:
//                break;
//        }
//    }
//}
