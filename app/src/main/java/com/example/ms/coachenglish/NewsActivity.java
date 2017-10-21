package com.example.ms.coachenglish;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class NewsActivity extends AppCompatActivity {

    private WebView webView;
    private AlertDialog.Builder alertDialog;
    private TextView dialog_word_textview;
    private TextView dialog_phometic_textview;
    private TextView dialog_tran_textview;
    private String addedText;

    private String word;
    private String phonetic;
    private String tran;


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

        registerClipEvents();
    }


    //监听剪切板获取剪切板内容
    private void registerClipEvents() {
        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            private long previousTime = 0;
            @Override
            public void onPrimaryClipChanged() {
                long now = System.currentTimeMillis();
                if(now - previousTime < 250){
                    previousTime = now;
                    return;
                }
                previousTime = now;
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                    addedText = manager.getPrimaryClip().getItemAt(0).getText().toString();
                    if (addedText != null) {
                        dialog(addedText);
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(),"没有选中任何内容",Toast.LENGTH_SHORT).show();
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
        dialog_word_textview = (TextView)view.findViewById(R.id.dialog_word_textview);
        dialog_phometic_textview = (TextView)view.findViewById(R.id.dialog_phometic_textview);
        dialog_tran_textview = (TextView)view.findViewById(R.id.dialog_tran_textview);

        try{
            WorkTask workTask = new WorkTask();
            workTask.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        alertDialog.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(phonetic) && TextUtils.isEmpty(tran)){
                    Toast.makeText(MyApplication.getContext(), "请翻译之后再收藏", Toast.LENGTH_SHORT).show();
                }
                else{
                    Words words = new Words();
                    words.setWord(word);
                    words.setPhonetic(phonetic);
                    words.setTran(tran);
                    words.save();
                    if(words.save()){
                        Toast.makeText(MyApplication.getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }





    //对网络进行操作的内部类
    class WorkTask extends AsyncTask {                             //AsyncTask是Android框架中的一个轻量线程池
        String url = "http://dict.youdao.com/search?q=";

        public WorkTask() {
            word = addedText.toString();
            url = url + word;
        }

        @Override
        protected Object doInBackground(Object[] objects) {             //将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。这里执行从网站获取音标和解释。
            try {
                Document document = Jsoup.connect(url).timeout(3000).get();
                tran = document.select(".trans-container").first().text();
                phonetic = document.select(".phonetic").first().text();
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {           //在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，后台的计算结果将通过该方法传递到UI thread。
            super.onPostExecute(o);
            if(!TextUtils.isEmpty(word)){
                dialog_word_textview.setText(word);
                dialog_phometic_textview.setText(phonetic);
                dialog_tran_textview.setText(tran);
            }
        }
    }

}
