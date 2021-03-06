package com.sparetimeforu.android.sparetimeforu.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.BuildConfig;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.activity.ChangePWActivity;
import com.sparetimeforu.android.sparetimeforu.activity.STFUActivity;
import com.sparetimeforu.android.sparetimeforu.activity.SignUpActivity;
import com.sparetimeforu.android.sparetimeforu.entity.User;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/17.
 * Email:17wjli6@stu.edu.cn
 */

public class LoginFragment extends Fragment {
    private static String LoginServerUrl;
    private ImageView m22;
    private ImageView m33;
    private EditText mEmailEdit;
    private EditText mPasswordEdit;
    private Button mSignUpButton;


    private User mUser;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        JMessageClient.init(getActivity().getApplicationContext());
        LoginServerUrl = STFUConfig.HOST + "/auth/login";
        //防止截屏录屏
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        m22 = (ImageView) view.findViewById(R.id.ic_icon_left);
        m33 = (ImageView) view.findViewById(R.id.ic_icon_right);
        mEmailEdit = (EditText) view.findViewById(R.id.et_email);
        mEmailEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22));
                m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33));
            }
        });
        mPasswordEdit = (EditText) view.findViewById(R.id.et_password_login);
        mPasswordEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                m22.setImageDrawable(getResources().getDrawable(R.drawable.ic_22_hide));
                m33.setImageDrawable(getResources().getDrawable(R.drawable.ic_33_hide));

            }
        });

        mSignUpButton = (Button) view.findViewById(R.id.btn_sign_up);
        mSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivityForResult(intent, 0);
        });

        return view;
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (!Objects.equals(mEmailEdit.getText().toString(), "") &&
                !Objects.equals(mPasswordEdit.getText().toString(), "")) {
            FormBody body = new FormBody.Builder().
                    add("email", mEmailEdit.getText().toString() + "@stu.edu.cn").
                    add("password", mPasswordEdit.getText().toString()).
                    add("request_type", "login").
                    build();

            OkHttpUtil.sendOkHttpPostRequest(LoginServerUrl, body,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> Toast.makeText(LoginFragment.this.getActivity(),
                                    "无法获取用户信息，请检查网络是否正常", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final User user = HandleMessageUtil.handleLoginMessage(response.body().string());
                            if (user == null) {
                                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                                        Toast.makeText(LoginFragment.this.getActivity(),
                                                "用户名或者密码错误", Toast.LENGTH_SHORT).show());
                            } else {
                                mUser = user;//给成员变量mUser赋值，user从json解析得来
                                STFUConfig.sUser = user;//顺便帮全局User赋值，懒得搞
                                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                                    //添加账户
                                    JMessageClient.login(user.getEmail(), user.getEmail() + "1", new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
                                                Logger.i("极光登录成功");
                                            }
                                        }
                                    });
                                    check_init_avatar();
                                    addAccount();
                                    Intent intent = new Intent(getActivity(), STFUActivity.class);
                                    intent.putExtra("user", user);//把user返回之前的activity
                                    getActivity().setResult(Activity.RESULT_OK, intent);
                                    getActivity().finish();
                                });
                            }
                        }
                    });
        } else {
            Snackbar.make(getView(), "请输入邮箱和密码！", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private void check_init_avatar(){
        //看是否需要重置头像
        UserInfo userInfo=JMessageClient.getMyInfo();
        if(userInfo!=null){
            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if(bitmap==null){
                        try{
                            File mFile = Glide.with(getContext()).asFile().load(STFUConfig.HOST + "/static" + "/avatar/" + STFUConfig.sUser.getAvatar_url()).submit().get();
                            JMessageClient.updateUserAvatar(mFile, new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int i, String s, Bitmap bitmap) {
                                }
                            });
                        }catch (Exception e){
                        }
                    }
                }
            });
        }

    }
    @OnClick(R.id.login_forget_pw)
    public void forgetPW() {
        Intent intent = new Intent(getActivity(), ChangePWActivity.class);
        startActivity(intent);
    }

    public void addAccount() {
        AccountManager am = AccountManager.get(getContext()); // "this" references the current Context
        Account account = new Account(mEmailEdit.getText().toString() + "@stu.edu.cn",
                BuildConfig.APPLICATION_ID);
        boolean isAdded = am.addAccountExplicitly(account, "", null);//安全起见，不存密码
        if (isAdded) {
            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
            am.setAuthToken(account, "normal", mUser.getAuth_token());
        } else {
            Toast.makeText(getActivity(), "账号更新成功", Toast.LENGTH_SHORT).show();
            am.setAuthToken(account, "normal", mUser.getAuth_token());
        }

        am.setUserData(account, "email", mUser.getEmail());
        am.setUserData(account, "user_id", mUser.getUser_id() + "");
        am.setUserData(account, "nickname", mUser.getNickname());
        am.setUserData(account, "signature", mUser.getSignature());
        am.setUserData(account, "avatar_url", mUser.getAvatar_url());
        am.setUserData(account, "favourable_rate", mUser.getFavourable_rate());
        am.setUserData(account, "phone", mUser.getPhone());
        am.setUserData(account, "gender", mUser.getGender());
        am.setUserData(account, "bg_url", mUser.getBg_url());
        am.setUserData(account, "money", mUser.getMoney()+"");
    }
}
