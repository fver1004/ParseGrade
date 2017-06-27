package io.github.fver1004.parsinggrade;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class IntroActivity extends AppCompatActivity {
    //running 값으로 현재 서버에 성적공시 확인을 위한 주기적 파싱이 돌아가고 있는지 확인
    boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //news토픽 구독
        //FirebaseMessaging.getInstance().subscribeToTopic("news");

        running = false;
        CheckStateAsyncTask thread = new CheckStateAsyncTask();
        thread.execute();

    }

    private class CheckStateAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

                MessageRS messageRs = new MessageRS();
                if(messageRs.message(3).equals("true"))
                    running = true;

            return null;}

        @Override
        protected void onPostExecute(Void result) {
        /*핸들러로 2초 대기 후 메인 액티비티로 전환---------------------------------------*/
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    //서버에 공시알림 쓰레드가 돌아가는지 확인
                    if(running == false) {
                        //서버에서 안돌아가면 새로 시작
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        //서버에서 돌아가고있으면 바로 GradeListActivity
                        Intent intent = new Intent(IntroActivity.this, GradeListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000);/*handler.postDelayed()-----------------------------------------*/
        }
    }//asyncTask


}