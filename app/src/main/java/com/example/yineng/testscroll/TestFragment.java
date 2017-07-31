package com.example.yineng.testscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.linearlistview.LinearListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TestFragment extends Fragment implements ScrollViewFinal.OnScrollListener {


//    @Bind(R.id.fl_empty_view)
//    FrameLayout mFlEmptyView;
    @Bind(R.id.linear_list_view)
    LinearListView mLinearListView;
    @Bind(R.id.sv_games)
    ScrollViewFinal mSvGames;
    @Bind(R.id.ptr_layout)
    PtrClassicFrameLayout mPtrLayout;

    private android.os.Handler handler = new android.os.Handler();

    private List<GameInfo> mGameList;
    private NewGameListAdapter mNewGameListAdapter;

    private EditText search_edit;
    LinearLayout search01,search02;

    RelativeLayout rlayout;
    int searchLayoutTop;

    private int mPage = 1;
    private int statusBarHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                if(hasFocus){
                    searchLayoutTop = rlayout.getBottom();//获取searchLayout的顶部位置
                    Log.e("距离TestFragment：   ",searchLayoutTop+"");
                }
            }
        });
        statusBarHeight = getStatusBarHeight();

        search_edit = (EditText)view.findViewById(R.id.search_edit);
        search01 = (LinearLayout)view.findViewById(R.id.search01);
        search02 = (LinearLayout)view.findViewById(R.id.search02);
        rlayout = (RelativeLayout)view.findViewById(R.id.rlayout);
        mSvGames.setOnScrollListener(this);

//        mPtrLayout.onRefreshComplete();
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(getContext(), mGameList);
        mLinearListView.setAdapter(mNewGameListAdapter);
//
        setSwipeRefreshInfo();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setSwipeRefreshInfo() {
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestData(1);
                    }
                },2000);

            }
        });
        mSvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mGameList.clear();
//                        for (int i = 0; i < 5; i++) {
//                            GameInfo info = new GameInfo();
//                            info.setName("你是我大哥");
//                            mGameList.add(info);
//                        }
                        mSvGames.setHasLoadMore(false);
//                        mSvGames.showFailUI();
                        mSvGames.onLoadMoreComplete();
                        mNewGameListAdapter.notifyDataSetChanged();
                    }
                },5000);
//                requestData(mPage);

            }
        });
        mSvGames.setNoLoadMoreHideView(true);
        mPtrLayout.autoRefresh();
    }

    private void requestData(final int page) {

        for (int i = 0; i < 5; i++) {
            GameInfo info = new GameInfo();
            info.setName("你是我大哥");
            mGameList.add(info);
        }
        mSvGames.setHasLoadMore(true);
//        mFlEmptyView.setVisibility(View.GONE);
        mPtrLayout.onRefreshComplete();
        mNewGameListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onScroll(int scrollY) {
        Log.e("距离：   ",scrollY+""+"     改变布局距离：   "+rlayout.getBottom()+""+"    listview：高度"+mSvGames.getHeight()+"");

        if(scrollY >= rlayout.getBottom()+mSvGames.getHeight()-statusBarHeight){
            if (search_edit.getParent()!=search01) {
                search02.removeView(search_edit);
                search01.addView(search_edit);
            }
        }else{
            if (search_edit.getParent()!=search02) {
                search01.removeView(search_edit);
                search02.addView(search_edit);
            }
        }

    }
}
