package com.example.alex.myselfexposeeffect.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.lang.invoke.MethodType;

/**
 * Created by Alex on 2018/1/22.
 */

public class ToastUtils {
    private static Toast sToast;

    private static void t(Context t,String msg){
        if (sToast == null){
            sToast = new Toast(t);
            LayoutInflater inflater = (LayoutInflater) t.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

    }

}
