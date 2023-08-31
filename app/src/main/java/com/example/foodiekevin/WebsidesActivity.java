package com.example.foodiekevin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class WebsidesActivity extends AppCompatActivity {
ImageButton bt1,bt2,bt3,bt4,bt5,bt6;

    private View.OnClickListener onClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websides);

        OnClick onClick = new OnClick();
        bt1 = findViewById(R.id.wv_websites1);
        bt2 = findViewById(R.id.wv_websites2);
        bt3 = findViewById(R.id.wv_websites3);
        bt4 = findViewById(R.id.wv_websites4);
        bt5 = findViewById(R.id.wv_websites5);
        bt6 = findViewById(R.id.wv_websites6);
        bt1.setOnClickListener(onClick);
        bt2.setOnClickListener(onClick);
        bt3.setOnClickListener(onClick);
        bt4.setOnClickListener(onClick);
        bt5.setOnClickListener(onClick);
        bt6.setOnClickListener(onClick);

    }
    public class OnClick implements View.OnClickListener{
        Intent intent = getIntent();

        public void onClick(View v) {
                switch (v.getId()){
                case R.id.wv_websites1:
                    intent.putExtra("website","https://map.baidu.com/mobile/webapp/index/index/");
                    intent.setClass(WebsidesActivity.this,WebsidesDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.wv_websites2:
                    intent.putExtra("website","https://www.amap.com");
                    intent.setClass(WebsidesActivity.this,WebsidesDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.wv_websites3:
                    intent.putExtra("website","https://map.qq.com/m/");
                    intent.setClass(WebsidesActivity.this,WebsidesDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.wv_websites4:
                    intent.putExtra("website","https://mktm.zuche.com/html5/2020/firstday/index4.html?sharefrom=newtouch061701&ispc=pc&monitoringOrigin=baidusem001&utm_medium=baidusem&utm_source=baidusem&utm_term=m_20170804009618&bd_vid=12304931909072032999");
                    intent.setClass(WebsidesActivity.this,WebsidesDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.wv_websites5:
                    intent.putExtra("website","https://m.dianping.com");
                    startActivity(intent);
                    break;
                case R.id.wv_websites6:
                    intent.putExtra("website", "http://www.didichuxing.com/#/");
                    intent.setClass(WebsidesActivity.this,WebsidesDetailActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }
}