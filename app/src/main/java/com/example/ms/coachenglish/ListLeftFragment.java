package com.example.ms.coachenglish;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MS on 2017/10/4.
 */

public class ListLeftFragment extends Fragment {

    private boolean landscape;
    private View view;
    private RecyclerView recyclerView;
    private MainRightFragment mainRightFragment;
    private List<Words> wordsList = new ArrayList<>();
    private WordsAdapter wordsAdapter;
    private ItemTouchHelperCallback itemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    //判断竖屏与横屏
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            landscape = false;  //竖屏
        }
        else{
            landscape = true; //横屏
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_left_fragment,container,false);

        wordsList = DataSupport.findAll(Words.class);

        recyclerView = (RecyclerView)view.findViewById(R.id.list_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);
        wordsAdapter = new WordsAdapter(wordsList);
        recyclerView.setAdapter(wordsAdapter);

        itemTouchHelperCallback = new ItemTouchHelperCallback(wordsAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }


    //适配器内部类
    class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> implements SwipedListener {

        private List<Words> mWordsList;

        public WordsAdapter(List<Words> wordsList){
            mWordsList = wordsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item,parent,false);
            final ViewHolder holder = new WordsAdapter.ViewHolder(view);

            //列表子项点击事件
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Words words = mWordsList.get(holder.getAdapterPosition());
                    if(landscape){      //启动横屏模式显示
                        mainRightFragment = (MainRightFragment)getFragmentManager().findFragmentById(R.id.main_list_right_land_fragment);
                        mainRightFragment.refresh(words.getWord(),words.getPhonetic(),words.getTran());
                    }
                    else{                //启动竖屏模式显示
                        RightActivity.actionStart(getActivity(),words.getWord(),words.getPhonetic(),words.getTran());
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(WordsAdapter.ViewHolder holder, int position) {
            Words words = mWordsList.get(position);
            holder.worditem.setText(words.getWord());
        }

        @Override
        public int getItemCount() {
            return mWordsList.size();
        }

        //当列表子项被左滑的时候调用
        @Override
        public void onItemLeft(int position) {
            String word = mWordsList.get(position).getWord();
            mWordsList.remove(position);
            notifyItemRemoved(position);
            DataSupport.deleteAll(Words.class,"word = ?",word);
            if(landscape){
                mainRightFragment.clear();
            }
            Toast.makeText(MyApplication.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
        }

        //当列表子项被移动的时候调用
        @Override
        public void onItemMove(int fromPosition,int toPosition) {
            Collections.swap(mWordsList,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
        }

        //内部类
        class ViewHolder extends RecyclerView.ViewHolder{

            TextView worditem;

            public ViewHolder(View itemView) {
                super(itemView);
                worditem = (TextView)itemView.findViewById(R.id.worditem);
            }

        }

    }

}
