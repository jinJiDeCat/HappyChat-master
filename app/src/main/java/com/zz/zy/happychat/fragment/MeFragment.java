package com.zz.zy.happychat.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.activity.FocusListActivity;
import com.zz.zy.happychat.activity.GiftListActivity;
import com.zz.zy.happychat.activity.GradeActivity;
import com.zz.zy.happychat.activity.LoginActivity;
import com.zz.zy.happychat.activity.MainActivity;
import com.zz.zy.happychat.activity.PersonInfoActivity;
import com.zz.zy.happychat.activity.ProfitActivity;
import com.zz.zy.happychat.activity.RechargeActivity;
import com.zz.zy.happychat.activity.VipActivity;
import com.zz.zy.happychat.activity.VoiceActivity;
import com.zz.zy.happychat.mvp.model.CallState;
import com.zz.zy.happychat.mvp.model.ChangeVip;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.utils.DensityUtil;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.view.SquareImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class MeFragment extends Base2Fragment {
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 1;
    private static final int REQUEST_RECORDAUDIO = 2;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.ll_grade)
    LinearLayout llGrade;
    @BindView(R.id.ll_vip)
    LinearLayout llVip;
    @BindView(R.id.ll_pofit)
    LinearLayout llPofit;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.ll_gift)
    LinearLayout llGift;
    @BindView(R.id.ll_focus)
    LinearLayout llFocus;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    @BindView(R.id.iv_iconBig)
    ImageView ivIconBig;

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
            llSex.setVisibility(View.VISIBLE);
            tvLevel.setVisibility(View.VISIBLE);
            ivIcon.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              ImagePicker imagePicker=ImagePicker.getInstance();
                                              imagePicker.setImageLoader(new ImageLoader(){

                                                  @Override
                                                  public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                                                      Glide.with(activity).load(path).into(imageView);
                                                  }

                                                  @Override
                                                  public void clearMemoryCache() {

                                                  }
                                              });
                                              imagePicker.setShowCamera(true);
                                              imagePicker.setCrop(true);
                                              imagePicker.setSaveRectangle(true);
                                              imagePicker.setMultiMode(false);
                                              imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                                              imagePicker.setFocusWidth(600);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                                              imagePicker.setFocusHeight(600);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                                              imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
                                              imagePicker.setOutPutY(800);//保存文件的高度。单位像素
                                              Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                              startActivityForResult(intent, ACTIVITY_REQUEST_SELECT_PHOTO);
                                          }
                                      }
//                Album.startAlbum(getActivity(), ACTIVITY_REQUEST_SELECT_PHOTO
//                        , 1              // 指定选择数量。
//                        , 0xffEF889D    // 指定Toolbar的颜色。
//                        , 0xffEF889D)  // 指定状态栏的颜色。
            );
            getUserInfo(sharedPreferences.getString("userid", ""));
        } else {
            llSex.setVisibility(View.GONE);
            tvLevel.setVisibility(View.GONE);
            tvNickname.setText("未登录");
            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            ivIcon.setImageResource(R.drawable.meinv);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        Resources resources = getActivity().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int width=displayMetrics.widthPixels;
        ivIconBig.getLayoutParams().height= (int) (width/1.5);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("上传头像...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            llSetting.setPadding(0, MainActivity.getStatusBarHeight(getActivity()) + DensityUtil.dip2px(getActivity(), 8), DensityUtil.dip2px(getActivity(), 8), 0);
        } else {
            llSetting.setPadding(0, DensityUtil.dip2px(getActivity(), 8), DensityUtil.dip2px(getActivity(), 8), 0);
        }
    }

    @Override
    public void initPresenter() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeState(CallState callState){
        getUserInfo(sharedPreferences.getString("userid",""));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeVip(ChangeVip changeVip){
        getUserInfo(sharedPreferences.getString("userid",""));
    }
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ll_grade, R.id.ll_vip, R.id.ll_pofit, R.id.ll_balance, R.id.ll_gift, R.id.ll_focus, R.id.iv_setting, R.id.ll_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_grade:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), GradeActivity.class));
                }

                break;
            case R.id.ll_vip:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), VipActivity.class));
                }

                break;
            case R.id.ll_pofit:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), ProfitActivity.class));
                }

                break;
            case R.id.ll_balance:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), RechargeActivity.class));
                }

                break;
            case R.id.ll_gift:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), GiftListActivity.class);
                    intent.putExtra("userid", sharedPreferences.getString("userid", ""));
                    getActivity().startActivity(intent);
                }

                break;
            case R.id.ll_focus:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), FocusListActivity.class));
                }

                break;
            case R.id.iv_setting:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                }

                break;
            case R.id.ll_record:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    //getActivity().startActivity(new Intent(getActivity(), VoiceActivity.class));
                    checkRecordPermission();
                }

                break;
        }
    }

    private void checkRecordPermission() {
        boolean hasPermission= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
        .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
        }else{
            getActivity().startActivity(new Intent(getActivity(), VoiceActivity.class));
        }
    }

    private void getUserInfo(String userid) {
         HttpUtils.get(getActivity(), MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                llSex.setVisibility(View.VISIBLE);
                tvLevel.setVisibility(View.VISIBLE);
                People people = JSON.parseObject(s, People.class);
                Glide.with(getActivity()).load(MyApplication.BaseImage + people.getPict()).into(ivIcon);
                tvSex.setText(people.getSex());
                ivSex.setImageResource(people.getSex().equals("男") ? R.drawable.bainanxing : R.drawable.bainvxing);
                tvNickname.setText(people.getNickname());
                tvLevel.setText(people.getLevel() + "级");
                tvVip.setText(people.getMember().equals("0") ? "成为会员" : "会员");
                Logger.e("stop");
            }

            @Override
            public void onError(Exception e) {
                llSex.setVisibility(View.GONE);
                tvLevel.setVisibility(View.GONE);
                tvNickname.setText("未登录");
                ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
                ivIcon.setImageResource(R.drawable.meinv);
                tvVip.setText("成为会员");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if(data!=null&&requestCode==ACTIVITY_REQUEST_SELECT_PHOTO){
                // 拿到用户选择的图片路径List：
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Logger.w(images.get(0).path);
                uploadPic(images.get(0).path, sharedPreferences.getString("userid", ""));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_RECORDAUDIO){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getActivity().startActivity(new Intent(getActivity(), VoiceActivity.class));
            }
        }
    }

    private void uploadPic(final String imagepath, final String userid) {
        Glide.with(getActivity()).load(imagepath).into(ivIcon);
        HttpParams httpParams = new HttpParams();
        httpParams.put("pict", new File(imagepath));
        httpParams.put("id", userid);
        OkGo.post(MyApplication.BaseUrl + "index.php/api/index/editinfo").tag(getActivity())
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if (!progressDialog.isShowing())
                            progressDialog.show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("result") == 200) {
                                Glide.with(getActivity()).load(imagepath).into(ivIcon);
                            }else{
                                Toast.makeText(getActivity(), "设置失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//        Luban.get(getActivity())
//                .load(new File(imagepath))
//                .putGear(Luban.THIRD_GEAR)
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        if (!progressDialog.isShowing())
//                            progressDialog.show();
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if(progressDialog.isShowing()){
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(getActivity(), "请重试", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
