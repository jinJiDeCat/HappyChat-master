package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfitActivity extends AppCompatActivity {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_getMoneyHistory)
    TextView tvGetMoneyHistory;
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_aname)
    EditText etAname;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        unbinder = ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交...");
        progressDialog.setCancelable(false);
        initData();
    }

    private void initData() {
        getEarn(sharedPreferences.getString("userid", ""));
    }

    private void getEarn(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getearn?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    tvToday.setText(jsonObject.getString("today") + "聊币");
                    tvTotal.setText(jsonObject.getString("num") + "聊币");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @OnClick({R.id.fl_back, R.id.tv_getMoneyHistory, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_getMoneyHistory:
                startActivity(new Intent(this, GetMoneyHistoryActivity.class));
                break;
            case R.id.btn_submit:
                if(TextUtils.isEmpty(etAccount.getText().toString().trim())){
                    Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etAname.getText().toString().trim())){
                    Toast.makeText(this, "请输入支付宝名字", Toast.LENGTH_SHORT).show();
                }else{
                    withDraw(sharedPreferences.getString("userid",""),etAccount.getText().toString().trim()
                    ,etAname.getText().toString().trim());
                }
                break;
        }
    }

    private void withDraw(String userid, String account, String aname) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userid);
        map.put("account", account);
        map.put("aname", aname);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/withdraw", map, new OnHttpCallBack() {
            @Override
            public void onBefore() {
                super.onBefore();
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(ProfitActivity.this, "提现申请提交成功", Toast.LENGTH_SHORT).show();
                getEarn(sharedPreferences.getString("userid",""));
                etAccount.setText("");
                etAname.setText("");
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
