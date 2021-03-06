package com.example.ms.coachenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.wordlist_item:
                Intent intent1 = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent1);
                break;
            case R.id.chinadaily_item:
                Intent intent2 = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent2);
                break;
            case R.id.hint_item:
                Toast.makeText(MainActivity.this,"单词本单词左滑可删除，长按拖动可排序\n英语新闻长按单词，点复制可翻译或收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.developer_item:
                Toast.makeText(MainActivity.this,"Developed by MS",Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit_item:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
