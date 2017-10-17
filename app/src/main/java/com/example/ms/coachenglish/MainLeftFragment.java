package com.example.ms.coachenglish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.LitePal;



/**
 * 翻译原理：输入需要查询的单词，从有道翻译官网网址中获取对应的HTML文档，然后通过解析文档得到音标和释义。
 * Created by MS on 2017/10/4.
 */

public class MainLeftFragment extends Fragment {

    private View view;
    private EditText translation_edit;
    private MainRightFragment mainRightFragment;
    private InputMethodManager inputMethodManager;           //控制显示或隐藏输入法面板
    private IntentFilter intentFilter;                        //广播
    private NetWorkChangeReceiver netWorkChangeReceiver;     //广播
    private WorkTask workTask;
    private String word;
    private String phonetic;
    private String tran;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_left_fragment, container, false);

        //广播接收
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        MyApplication.getContext().registerReceiver(netWorkChangeReceiver,intentFilter);


        translation_edit = (EditText) view.findViewById(R.id.translation_edit);
        Button clear_button = (Button) view.findViewById(R.id.clear_button);
        Button translation_button = (Button) view.findViewById(R.id.translation_button);
        Button collection_button = (Button) view.findViewById(R.id.collection_button);

        LitePal.getDatabase();        //创建数据

        //清空
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translation_edit.setText("");
                mainRightFragment = (MainRightFragment) getFragmentManager().findFragmentById(R.id.main_right_fragment);
                mainRightFragment.clear();

            }
        });

        //翻译
        translation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(netWorkChangeReceiver.judgeNet()){
                    mainRightFragment = (MainRightFragment) getFragmentManager().findFragmentById(R.id.main_right_fragment);
                    if (TextUtils.isEmpty(translation_edit.getText().toString())) {
                        Toast.makeText(MyApplication.getContext(), "请输入翻译内容", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try{
                            inputMethodManager = (InputMethodManager) MyApplication.getContext().getSystemService(MyApplication.getContext().INPUT_METHOD_SERVICE);  //调用系统服务键盘
                            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);                     //在显示和隐藏软键盘之间切换，前者为显示软键盘时使用的标记为0，后者为隐藏软键盘时使用的标记
                            workTask = new WorkTask();
                            workTask.execute();          //启动
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "翻译失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //收藏到单词本
        collection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(translation_edit.getText().toString())) {
                    Toast.makeText(MyApplication.getContext(), "请输入翻译内容", Toast.LENGTH_SHORT).show();
                }
                else{
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
            }
        });

        return view;
    }

    //对网络进行操作的内部类
    class WorkTask extends AsyncTask {                             //AsyncTask是Android框架中的一个轻量线程池
        String url = "http://dict.youdao.com/search?q=";

        public WorkTask() {
            word = translation_edit.getText().toString();
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
            mainRightFragment.getWord_textview().setText(word);
            mainRightFragment.getPhonetic_textview().setText(phonetic);
            mainRightFragment.getTran_textview().setText(tran);
        }
    }



    //判断网络连接情况的内部类
    class NetWorkChangeReceiver extends BroadcastReceiver{

        private boolean judgenet;

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()){
                judgenet = true;
            }
            else{
                judgenet = false;
            }
        }

        public boolean judgeNet(){
            return judgenet;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getContext().unregisterReceiver(netWorkChangeReceiver);
    }
}
