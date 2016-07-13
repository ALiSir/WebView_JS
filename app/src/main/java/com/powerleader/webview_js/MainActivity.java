package com.powerleader.webview_js;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.powerleader.webview_js.view.MyActivity;
import com.powerleader.webview_js.view.MyWebView;

import java.util.List;

public class MainActivity extends MyActivity {
    private MyWebView mvb;
    private Button jsbtn;
    private Button Invokebtn;
    private TextView textView2;
    private JsInvokeAndroid ina;
    private Button gpsBut;
    private TextView gpsText;
    private LocationManager lm;
    private String lcprovider;
    private Location lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mvb = (MyWebView)findViewById(R.id.webView);
        jsbtn = (Button) findViewById(R.id.button);
        Invokebtn = (Button) findViewById(R.id.button4);
        gpsBut = (Button) findViewById(R.id.button2);
        textView2 = (TextView) findViewById(R.id.textView2);
        gpsText = (TextView) findViewById(R.id.textView3);

        ina = new JsInvokeAndroid(this,textView2);

        WebSettings ws = mvb.getSettings();
        ws.setJavaScriptEnabled(true);
        mvb.addJavascriptInterface(ina,"myObj");

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mvb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

        jsbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast toast = null;
                if(toast == null){
                    toast = Toast.makeText(MainActivity.this,"已点击调用js按钮",Toast.LENGTH_LONG);
                }
                toast.show();
                mvb.loadUrl("javascript:jsButFromAndroid()");
            }
        });

        Invokebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                textView2.setText("调用测试通过！");
            }
        });

        //获取所有的位置提供器
        List<String> providers = lm.getProviders(true);
        if(providers.contains(lm.GPS_PROVIDER)){
            lcprovider = lm.GPS_PROVIDER;
        }else if(providers.contains(lm.NETWORK_PROVIDER)) {
            lcprovider = lm.NETWORK_PROVIDER;
        }else if(providers.contains(lm.PASSIVE_PROVIDER)){
            lcprovider = lm.PASSIVE_PROVIDER;
        }else {
            Toast.makeText(MainActivity.this,"无法获取当前位置",Toast.LENGTH_LONG).show();
        }

        if((lcprovider!=null)&&(PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.INTERNET","com.powerleader.webview_js"))&&(PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION","com.powerleader.webview_js"))&&(PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION","com.powerleader.webview_js"))){
            lc = lm.getLastKnownLocation(lcprovider);
        }else{
            Toast.makeText(MainActivity.this,"权限不足，无法获取当前位置！",Toast.LENGTH_LONG).show();
        }
        if(lc == null){
            Toast.makeText(MainActivity.this,"无法获取当前位置！",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this,"经度："+lc.getLongitude()+"；维度：" +lc.getLatitude(),Toast.LENGTH_LONG).show();
        }

        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MainActivity.this,"经度："+lc.getLongitude()+"；维度：" +lc.getLatitude(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //监听位置的变化情况
        lm.requestLocationUpdates(lcprovider,1000,1,ll);

        //获取当前经纬度
        gpsBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(lc == null){
                    gpsText.setText("无法获取当前位置！");
                }else{
                    gpsText.setText("经度："+lc.getLongitude()+"；维度：" +lc.getLatitude());
                }
            }
        });

        mvb.loadUrl("file:///android_asset/test.html");

    }

}
