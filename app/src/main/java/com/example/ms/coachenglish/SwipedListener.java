package com.example.ms.coachenglish;

/**
 * 定义列表子项滑动接口
 * Created by MS on 2017/10/15.
 */

public interface SwipedListener {
    void onItemMove(int fromPostition,int toPosition);
    void onItemLeft(int position);
}
