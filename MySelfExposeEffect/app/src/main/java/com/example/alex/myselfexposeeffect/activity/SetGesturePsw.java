package com.example.alex.myselfexposeeffect.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.alex.myselfexposeeffect.R;
import com.example.alex.myselfexposeeffect.utils.PreferenceCache;
import com.example.alex.myselfexposeeffect.widget.GesturePsw;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alex on 2018/1/24.
 */

public class SetGesturePsw extends AppCompatActivity implements  GesturePsw.GestureCallBack{

    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.gesture)
    GesturePsw mGesture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.set_gesture_layout);
        ButterKnife.bind(this);

        mGesture.setGestureCallBack(this);
        //不调用这个方法会造成第二次启动程序直接进入手势识别而不是手势设置
        mGesture.clearCache();
    }

    @OnClick({R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                SetGesturePsw.this.finish();
                break;
        }
    }

    @Override
    public void gestureVerifySuccessListener(int stateFlag, List<GesturePsw.GestureBean> data, boolean success) {
        if (stateFlag == GesturePsw.STATE_LOGIN){
            PreferenceCache.putGestureFlag(true);
            finish();
        }
    }
}
