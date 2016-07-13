package com.powerleader.webview_js;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ALiSir on 2016/7/12.
 */
public class JsInvokeAndroid {
    private Context context;
    private TextView textView;


    public JsInvokeAndroid(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @JavascriptInterface
    public String JsCallAndroid(){
        Toast toast = null;
        if(toast == null){
            toast = Toast.makeText(context,"Js调用android成功！",Toast.LENGTH_LONG);
        }
        toast.show();
        textView.setText("JS调用成功！");
        return "这是android发出的信息！";
    }


}
