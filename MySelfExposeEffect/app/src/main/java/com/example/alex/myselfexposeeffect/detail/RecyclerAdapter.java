package com.example.alex.myselfexposeeffect.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.myselfexposeeffect.R;

import java.util.List;

/**
 * TODO
 *
 * @author YinGen Chu
 * @date 2017/12/6 15:21.
 * @email: youguyilin@126.com
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {
    private List<TestBean> mTestBeanList;
    private Context mContext;

    public RecyclerAdapter(List<TestBean> testBeanList, Context context) {
        mTestBeanList = testBeanList;
        mContext = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.mTextView.setText(mTestBeanList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTestBeanList.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public viewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
