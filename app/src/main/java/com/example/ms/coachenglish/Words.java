package com.example.ms.coachenglish;

import org.litepal.crud.DataSupport;

/**
 * Created by MS on 2017/10/5.
 */

public class Words extends DataSupport {
    private String word;          //单词
    private String phonetic;     //音标
    private String tran;          //释义

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getTran() {
        return tran;
    }

    public void setTran(String tran) {
        this.tran = tran;
    }
}
