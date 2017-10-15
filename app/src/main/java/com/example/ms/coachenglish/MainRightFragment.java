package com.example.ms.coachenglish;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by MS on 2017/10/4.
 */

public class MainRightFragment extends Fragment {
    private View view;
    private TextView word_textview;
    private TextView phonetic_textview;
    private TextView tran_textview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_right_fragment,container,false);
        word_textview = (TextView)view.findViewById(R.id.word_textview);           //单词
        phonetic_textview = (TextView)view.findViewById(R.id.phonetic_textview);   //音标
        tran_textview = (TextView)view.findViewById(R.id.tran_textview);           //释义
        return view;
    }

    //刷新word和phonetic和tran
    public void refresh(String word,String phonetic,String tran){
        word_textview.setText(word);
        phonetic_textview.setText(phonetic);
        tran_textview.setText(tran);
    }

    //清空
    public void clear(){
        word_textview.setText("");
        phonetic_textview.setText("");
        tran_textview.setText("");
    }

    public TextView getWord_textview() {
        return word_textview;
    }

    public TextView getPhonetic_textview() {
        return phonetic_textview;
    }

    public TextView getTran_textview() {
        return tran_textview;
    }

}
