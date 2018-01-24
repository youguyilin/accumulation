package com.example.alex.myselfexposeeffect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.alex.myselfexposeeffect.activity.CloseGesturePsw;
import com.example.alex.myselfexposeeffect.activity.SetGesturePsw;
import com.example.alex.myselfexposeeffect.databinding.DatabindingActivity;
import com.example.alex.myselfexposeeffect.detail.DetailActivity;
import com.example.alex.myselfexposeeffect.utils.PreferenceCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static long lastClickTime = 0;//上次点击的时间
    private static long startClickTime = 0;//开始点击的时间

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;
    @BindView(R.id.hand_switch)
    ImageView mHandSwitch;
    @BindView(R.id.im_reset)
    ImageView mImReset;
    @BindView(R.id.reset_hand_psw)
    LinearLayout mResetHandPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        //        mBtn1.setOnLongClickListener(new View.OnLongClickListener() {
        //            @Override
        //            public boolean onLongClick(View mView) {
        //
        //                lastClickTime = System.currentTimeMillis();
        //                Log.e("lastClickTime",lastClickTime + "ms");
        //                long time = lastClickTime - mBtn1.getStartClickTime();
        //                Log.e("click","长按一次 时间间隔" + time + "ms");
        //                return true;
        //            }
        //        });
        //        mBtn1.setOnClickListener(mListener);
        //        mBtn2.setOnClickListener(mListener);
    }

    private void initData() {
       if (PreferenceCache.getGestureFlag()){
           mHandSwitch.setImageResource(R.drawable.auto_bidding_off);
           mResetHandPsw.setVisibility(View.VISIBLE);
       } else{
           mHandSwitch.setImageResource(R.drawable.auto_bidding_on);
           mResetHandPsw.setVisibility(View.GONE);
       }
    }

    //    OnNoDoubleClickListener mListener = new OnNoDoubleClickListener() {
    //        @Override
    //        public void onNoFastClick(View v) {
    //            Log.e("click","非快速点击" + v.getId());
    //        }
    //    };

    public void jump(Class<? extends Activity> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    @OnClick({R.id.btn_1, R.id.btn_2,R.id.hand_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                jump(DetailActivity.class);
                break;
            case R.id.btn_2:
                jump(DatabindingActivity.class);
                break;
            case R.id.hand_switch:
                Log.e("hand_switch","------------");
                if (!PreferenceCache.getGestureFlag()){
                    Intent intent = new Intent(MainActivity.this,SetGesturePsw.class);
                    startActivityForResult(intent,1);
                } else {
                    Intent intent = new Intent(MainActivity.this,CloseGesturePsw.class);
                    intent.putExtra("gestureflg",1);
                    startActivityForResult(intent,1);
                }
                break;
            case R.id.im_reset:
                Intent intent = new Intent(MainActivity.this,CloseGesturePsw.class);
                intent.putExtra("gestureflg",2);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            initData();
        }
    }
}
