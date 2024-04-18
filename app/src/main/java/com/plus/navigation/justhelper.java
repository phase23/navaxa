package com.plus.navigation;

import android.app.Activity;
import android.view.WindowManager;

public class justhelper {



    public static void setBrightness(Activity activity, int brightnessPercent) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = brightnessPercent / 100.0f;
        activity.getWindow().setAttributes(layoutParams);
    }



}
