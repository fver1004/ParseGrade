package com.example.kiyeon.parsegrade;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.Timer;
import java.util.TimerTask;
/**
 * created by Kiyeon Kim, 7508A, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class ParsingActivity extends AppCompatActivity {
    private String s, id , pw;
    String[][] arraySquare;
    Parse parse;
    int num=0;
    Intent service;
    //리스트뷰 인스턴스 선언
    private ListView mListView = null;
    //어댑터 인스턴스 선언
    private ListViewAdapter mAdapter = null;
    //갱신주기(분)
    int renew =1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);
        parse = new Parse();

        JsoupAsyncTask thread = new JsoupAsyncTask();
        thread.execute();//로딩이있을수있으니 딜레이 넣어야됨!

    }//onCreate()


    public void onBackPressed(){
        moveTaskToBack(true);
    }
    //public void onPause(){
    //    super.onPause();
    //}

    //첫 파싱 후 timer로 주기적 파싱
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {

            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            pw = intent.getStringExtra("pw");
            parse.settingLogin(id,pw);
            try {
                s = parse.load();
                num = parse.countSubject();
            }catch(Exception e){e.printStackTrace();Log.d("asdff","loading error");}

            try {if(num!=0)
                arraySquare = parse.changeAsArrayList();
            } catch (Exception e) {e.printStackTrace();Log.d("asdff","cahngeArray erorr!");}

            return null;}

        @Override
        protected void onPostExecute(Void result) {
            mListView = (ListView) findViewById(R.id.mList);
            mAdapter = new ListViewAdapter(ParsingActivity.this);
            mListView.setAdapter(mAdapter);

            if(num!=0) {//0이 아니면 서비스 실행하기

                for (int i = 0; i < num; i++)
                    mAdapter.addItem(arraySquare[i]);

                startUpdate();


            }else{//과목이 0개일때
                String noArraySquare[] = {"자료가 존재하지 않아요."};
                mAdapter.addItem(noArraySquare);
            }

            //파싱 서비스 중지 버튼
            Button stopButton = (Button) findViewById(R.id.stopParsing);
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopService(service);
                }});

        }
    }//asyncTask



    public void startUpdate(){
        service = new Intent(this, renewService.class);
        service.putExtra("s",s);
        service.putExtra("id",id);
        service.putExtra("pw",pw);
        Log.d("asdf","s is =>"+s);
        startService(service);//서비스호출 메소드
    }




}