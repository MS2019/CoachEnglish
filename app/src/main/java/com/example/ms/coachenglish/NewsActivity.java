package com.example.ms.coachenglish;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewsActivity extends AppCompatActivity {

    private WebView webView;
    private AlertDialog.Builder alertDialog;
    private TextView getDialog_word_textview;
    private TextView dialog_phometic_textview;
    private TextView dialog_tran_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://usa.chinadaily.com.cn/us/");

        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        // webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webView.getSettings().setLoadWithOverviewMode(true);

        registerClipEvents();

    }

    //监听剪切板获取剪切板内容
    private void registerClipEvents() {
        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                    if (addedText != null) {
                        dialog(addedText);
                    }
                }
            }
        });
    }

    //点击返回上一页面而不是退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //销毁WebView
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    //提示框
    public void dialog(CharSequence addedText){
        alertDialog = new AlertDialog.Builder(NewsActivity.this);
        LayoutInflater inflater = getLayoutInflater();   //填充器
        final View view = inflater.inflate(R.layout.dialog,null);
        alertDialog.setView(view);
        //alertDialog.setTitle(addedText);

        getDialog_word_textview = (TextView)view.findViewById(R.id.dialog_word_textview);
        dialog_phometic_textview = (TextView)view.findViewById(R.id.dialog_phometic_textview);
        dialog_tran_textview = (TextView)view.findViewById(R.id.dialog_tran_textview);

        getDialog_word_textview.setText(addedText);
        dialog_phometic_textview.setText("你好");
        dialog_tran_textview.setText("年后");
        alertDialog.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {




            }
        });


        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }




}
