package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.yanzhenjie.album.Album;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.ImageGVAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReportActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 0;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_takePic)
    LinearLayout llTakePic;
    @BindView(R.id.gv_image)
    GridView gvImage;
    private ArrayList<String> pathList = new ArrayList<>();
    private ImageGVAdapter imageGVAdapter;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffffffff, 0);
        imageGVAdapter = new ImageGVAdapter(this, pathList);
        gvImage.setAdapter(imageGVAdapter);
    }

    @OnClick({R.id.fl_back, R.id.tv_next, R.id.ll_takePic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_next:
                Log.e("image",pathList.size()+"");
                Intent intent = new Intent(this, ReportSubmitActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bid",getIntent().getStringExtra("bid"));
                bundle.putStringArrayList("images", pathList);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
            case R.id.ll_takePic:
                Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO
                        , 9 - pathList.size()              // 指定选择数量。
                        , 0xff888888     // 指定Toolbar的颜色。
                        , 0x88888888);  // 指定状态栏的颜色。
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
                pathList.addAll(Album.parseResult(data));
                imageGVAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
            }
        }else if(requestCode==1){
            if(resultCode==RESULT_OK){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
