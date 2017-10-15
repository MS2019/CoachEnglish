package com.example.ms.coachenglish;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RightActivity extends AppCompatActivity {

    //启动活动的方法
    public static void actionStart(Context context,String word,String phonetic,String tran){
        Intent intent = new Intent(context,RightActivity.class);
        intent.putExtra("word",word);
        intent.putExtra("phonetic",phonetic);
        intent.putExtra("tran",tran);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right);

        String word = getIntent().getStringExtra("word");           //获取传入的单词
        String phonetic = getIntent().getStringExtra("phonetic");  //获取传入的音标
        String tran = getIntent().getStringExtra("tran");           //获取传入的释义

        MainRightFragment mainRightFragment = (MainRightFragment)getSupportFragmentManager().findFragmentById(R.id.right_right_fragment);
        mainRightFragment.refresh(word,phonetic,tran);
    }
}
