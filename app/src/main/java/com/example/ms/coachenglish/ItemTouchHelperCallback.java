package com.example.ms.coachenglish;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * 列表子项滑动删除和拖动移动
 * Created by MS on 2017/10/15.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private SwipedListener mAdapter;
    public ItemTouchHelperCallback(SwipedListener adapter){
        mAdapter = adapter;
    }



    //设置拖动方向和侧滑方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlages = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//设置拖拽方向为上下
        final int swipeFlage = ItemTouchHelper.LEFT;//设置侧滑方向为从右到左
        return makeMovementFlags(dragFlages,swipeFlage); //设置方向参数
    }

    //移动列表子项调用此方法
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    //侧滑列表子项时调用此方法
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemLeft(viewHolder.getAdapterPosition());
    }
}
