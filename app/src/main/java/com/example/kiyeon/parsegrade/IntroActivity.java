package com.example.kiyeon.parsegrade;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
/**
 * created by Kiyeon Kim, 7508A, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /*핸들러로 2초 대기 후 메인 액티비티로 전환---------------------------------------*/
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);/*handler.postDelayed()-----------------------------------------*/


    }


}
