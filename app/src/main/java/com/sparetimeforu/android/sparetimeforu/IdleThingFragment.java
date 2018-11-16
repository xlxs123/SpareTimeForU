package com.sparetimeforu.android.sparetimeforu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sparetimeforu.android.sparetimeforu.adapter.IdleThingAdapter;
import com.sparetimeforu.android.sparetimeforu.data.DataServer;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/14.
 */

interface RequestIdleThingCallBack {
    void success(List<IdleThing> data);

    void fail(Exception e);
}

class RequestIdleThing extends Thread {
    private RequestIdleThingCallBack mCallBack;
    private Handler mHandler;


    public RequestIdleThing(RequestIdleThingCallBack callBack) {
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        Random random = new Random();
        int i = random.nextInt(5);
        switch (i) {
            case 0:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.fail(new RuntimeException("fail"));
                    }
                });
                break;
            case 1:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getIdleThingData(15));
                    }
                });
                break;
            default:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.success(DataServer.getIdleThingData(15));
                    }
                });
                break;

        }
    }
}


public class IdleThingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private IdleThingAdapter mAdapter;
    private List<Study> mStudies;
    private SwipeRefreshLayout mIdleThingRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idle_thing_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_idle_thing_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter(DataServer.getIdleThingData(34));

        return view;
    }

    private void setupAdapter(List<IdleThing> idleThingData) {
        mAdapter = new IdleThingAdapter(idleThingData);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.isFirstOnly(false);

        //set main fragment header
        View view = getLayoutInflater().inflate(R.layout.header_main_fragment,
                (ViewGroup) mRecyclerView.getParent(), false);
        ImageView img = (ImageView) view.findViewById(R.id.header_main_fragment);
        Picasso.get().load(R.drawable.android_p)
                .resize(1080, 512)
                .centerCrop()
                .into(img);
        mAdapter.addHeaderView(view);

        mRecyclerView.setAdapter(mAdapter);
    }


}