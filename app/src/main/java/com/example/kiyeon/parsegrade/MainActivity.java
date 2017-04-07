package com.example.kiyeon.parsegrade;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * created by Kiyeon Kim, 7508A, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class MainActivity extends AppCompatActivity {
    private String id,pw;
    EditText loginId,loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginId = (EditText) findViewById(R.id.loginId);
        loginPw = (EditText) findViewById(R.id.loginPw);
        Button button = (Button) findViewById(R.id.buttonToDetail);

        /*button 리스너------------------------------------------------------------*/
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                id = loginId.getText().toString();
                pw = loginPw.getText().toString();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("pw", pw);
                startActivity(intent);
                finish();
            }
        });/*button.setOnClickListener()------------------------------------------*/
    }




}
