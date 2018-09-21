package zqx.rj.com.lovecar.utils;

import android.widget.EditText;
import android.widget.TextView;

/**
 * author：  HyZhan
 * created：2018/9/19 18:20
 * desc：    封装 EditText
 */

public class Get {

    public static String text(TextView textView){
        return textView.getText().toString().trim();
    }

}
