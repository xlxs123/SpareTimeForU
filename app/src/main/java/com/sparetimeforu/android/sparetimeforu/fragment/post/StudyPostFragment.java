package com.sparetimeforu.android.sparetimeforu.fragment.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.R;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.adapter.CommentAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.PostImageAdapter;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.sparetimeforu.android.sparetimeforu.util.HandleMessageUtil;
import com.sparetimeforu.android.sparetimeforu.util.VerifyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter.parseDateString;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/21.
 * Email:17wjli6@stu.edu.cn
 */

public class StudyPostFragment extends Fragment {

    private final String PICTURE_URL = STFUConfig.HOST + "/static/study_pictures/";
    private final String COMMENT_URL = STFUConfig.HOST + "/study/comment/";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.post_avatar)
    ImageView mAvatarImageView;
    @BindView(R.id.post_nick_name)
    TextView mNicknameTextView;
    @BindView(R.id.post_date)
    TextView mDateTextView;
    @BindView(R.id.post_reward)
    TextView mMoneyTextView;
    @BindView(R.id.post_accept)
    Button mStatusButton;
    @BindView(R.id.post_caption)
    TextView mContentText;
    @BindView(R.id.post_image_list)
    RecyclerView mImageList;
    @BindView(R.id.comment_list)
    RecyclerView mCommentList;
    @BindView(R.id.summary_comment)
    TextView mCommentTextView;
    @BindView(R.id.summary_like)
    TextView mLikeTextView;
    @BindView(R.id.post_comment_content)
    EditText mCommentEditText;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.post_like)
    ImageView mLikeImageView;
    @BindView(R.id.confirm_finish)
    Button mConfirmBottom;

    PostImageAdapter mPostImageAdapter;
    CommentAdapter mCommentAdapter;

    int postID;

    public static StudyPostFragment newInstance() {
        StudyPostFragment fragment = new StudyPostFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_study, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent, R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this::refresh);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postID = getActivity().getIntent().getIntExtra("study_id", 1);
        initPost();
    }

    @OnClick(R.id.menu_back_icon)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.post_send)
    public void comment() {
        if (!VerifyUtil.isLogin(getActivity())) {
            return;
        }
        String content = mCommentEditText.getText().toString();
        if (!content.equals("")) {
            FormBody formBody = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("content", content)
                    .add("post_id", postID + "").build();

            OkHttpUtil.sendOkHttpPostRequest(COMMENT_URL + "add", formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(getView(), "评论失败，请检查网络", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    String status = "";
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        status = jsonObject.getString("status");
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                    if (status.equals("success")) {

                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            mCommentEditText.setText("");
                            Gson gson = new Gson();
                            try {
                                JSONObject responseJson = new JSONObject(responseString);
                                String data = responseJson.getString("data");
                                Comment comment = gson.fromJson(data, Comment.class);
                                refresh();
//                                Snackbar.make(getView(), "评论成功", BaseTransientBottomBar.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Logger.e(e.toString());
                            }

                        });
                    }
                }
            });


        } else {
            Toast.makeText(getActivity(), R.string.right_content, Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.post_like)
    public void like() {
        if (!VerifyUtil.isLogin(getActivity())) {
            return;
        }
        if (postID == 0 || STFUConfig.sUser == null) {
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("post_type", 2 + "")
                .add("post_id", postID + "")
                .add("auth_token", STFUConfig.sUser.getAuth_token())
                .build();
        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/like", body, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    if (jsonObject.getString("status").equals("success")) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> refresh());
                    }
                } catch (JSONException e) {
                    Logger.e(e.toString());
                }
            }
        });
    }

    @OnClick(R.id.confirm_finish)
    public void confirm() {
        if (!VerifyUtil.isLogin(getActivity())) {
            return;
        }
        if (postID == 0 || STFUConfig.sUser == null) {
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("post_id", postID + "")
                .add("auth_token", STFUConfig.sUser.getAuth_token())
                .build();
        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/study/confirm_finish", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> refresh());
                    }
                } catch (JSONException e) {
                    Logger.e(e.toString());
                }
            }
        });

    }

    private void initPost() {
        mRefreshLayout.setRefreshing(true);
        FormBody body = new FormBody.Builder()
                .add("post_id", postID + "")
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/study/get_post_by_id", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    status = jsonObject.getString("status");
                } catch (Exception e) {
                    Logger.e(e.toString());
                }
                if (status.equals("success")) {
                    Study study = HandleMessageUtil.handlePost_Study_Message(responseString);
                    List<Comment> comments = HandleMessageUtil.handlePost_Errand_Comment_Message(responseString);//获取评论信息
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        mRefreshLayout.setRefreshing(false);
                        updateViews(study, comments);
                    });
                }
            }
        });
    }

    private void updateViews(Study study, List<Comment> comments) {
        Picasso.get()
                .load(STFUConfig.HOST + "/static/avatar/" + study.getUser_Avatar())
                .resize(200, 200)
                .centerCrop()
                .into(mAvatarImageView);

        mNicknameTextView.setText(study.getUser_Nickname());
        mDateTextView.setText(parseDateString(study.getRelease_time()));
        mMoneyTextView.setText("");
        mStatusButton.setVisibility(View.VISIBLE);
        mConfirmBottom.setVisibility(View.GONE);
        if (study.getIs_solved() == 0) {
            mStatusButton.setText("待解决");
        } else {
            mStatusButton.setText("解决了");
        }
        mContentText.setText(study.getContent());
        mCommentTextView.setText(getString(R.string.comment_number,
                study.getComment_number() + ""));
        mLikeTextView.setText(getString(R.string.like_number,
                study.getLike_number() + ""));

        //设置图片
        List<String> urls = new ArrayList<String>();
        if (!study.getPicture_url_1().equals(""))
            urls.add(PICTURE_URL + study.getPicture_url_1());
        if (!study.getPicture_url_2().equals(""))
            urls.add(PICTURE_URL + study.getPicture_url_2());
        if (!study.getPicture_url_3().equals(""))
            urls.add(PICTURE_URL + study.getPicture_url_3());
        mImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostImageAdapter = new PostImageAdapter(urls);
        ViewCompat.setNestedScrollingEnabled(mImageList, false);
        mImageList.setAdapter(mPostImageAdapter);

        mCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentAdapter = new CommentAdapter(comments);
        ViewCompat.setNestedScrollingEnabled(mCommentList, false);
        mCommentList.setAdapter(mCommentAdapter);

        if (VerifyUtil.isLoginStatus(getActivity())) {
            FormBody body = new FormBody.Builder()
                    .add("auth_token", STFUConfig.sUser.getAuth_token())
                    .add("post_type", 2 + "")
                    .add("post_id", postID + "")
                    .build();

            OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/like/is_like", body, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            if (jsonObject.getString("is_like").equals("like")) {
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(
                                        () -> mLikeImageView.setImageResource(R.drawable.ic_like));
                            } else {
                                if (getActivity() == null) return;
                                getActivity().runOnUiThread(
                                        () -> mLikeImageView.setImageResource(R.drawable.ic_like_before));
                            }
                        }
                    } catch (JSONException e) {
                        Logger.e(e.toString());
                    }
                }
            });
        }
        if (VerifyUtil.isLoginStatus(getActivity())) {
            // 主人 + 未完成
            if (study.getUser_Email().equals(STFUConfig.sUser.getEmail()) && study.getIs_solved() == 0) {
                mStatusButton.setVisibility(View.GONE);
                mConfirmBottom.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refresh() {
        mCommentAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mRefreshLayout.setRefreshing(true);
        new RefreshPost(new RefreshCallBack<Study>() {
            @Override
            public void success(Study study, List<Comment> comments) {
                //do something
                if (getView() == null) return;
                Snackbar.make(getView(), R.string.refresh_success, BaseTransientBottomBar.LENGTH_SHORT).show();
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    updateViews(study, comments);
                    mCommentAdapter.setEnableLoadMore(true);
                    mRefreshLayout.setRefreshing(false);
                });

            }

            @Override
            public void fail(Exception e) {
                if (getView() == null) return;
                Snackbar.make(getView(), R.string.network_error, BaseTransientBottomBar.LENGTH_SHORT).show();
                mCommentAdapter.setEnableLoadMore(true);
                mRefreshLayout.setRefreshing(false);
            }
        }, getActivity(), postID, RefreshPost.STUDY_TYPE).start();
    }
}
