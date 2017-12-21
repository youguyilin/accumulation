package com.example.alex.myselfexposeeffect.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.myselfexposeeffect.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author YinGen Chu
 * @date 2017/12/6 14:06.
 * @email: youguyilin@126.com
 */

public class CommentFrag extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    private List<TestBean> mTestBeanList = new ArrayList<>();
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 20;i++){
            TestBean bean = new TestBean();
            bean.setName("测试" + i);
            mTestBeanList.add(bean);
        }
        mRecyclerAdapter = new RecyclerAdapter(mTestBeanList,getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
