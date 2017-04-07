package com.example.kiyeon.parsegrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * created by Kiyeon Kim, 7508A, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class DetailActivity extends AppCompatActivity  {
    Intent oldIntent;
    private String id, pw;
    int menuCount = 5;
    RelativeLayout[] menu = new RelativeLayout[menuCount];

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Intent 수신
        oldIntent = getIntent();
        id =  oldIntent.getStringExtra("id");
        pw =  oldIntent.getStringExtra("pw");


        TextView button = (TextView) findViewById(R.id.buttonToParsing);
        menu[0] = (RelativeLayout) findViewById(R.id.menu0);
        menu[1] = (RelativeLayout) findViewById(R.id.menu1);
        menu[2] = (RelativeLayout) findViewById(R.id.menu2);
        menu[3] = (RelativeLayout) findViewById(R.id.menu3);
        menu[4] = (RelativeLayout) findViewById(R.id.menu4);


        /*버튼클릭 리스너---------------------------------------------------------------*/
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(DetailActivity.this, ParsingActivity.class);
                intent.putExtras(oldIntent);
                intent.putExtra("id",id);
                intent.putExtra("pw",pw);

                startActivity(intent);
                finish();
            }
        });/*button.setOnClickListener()--------------------------------------------*/




    }//onCreate()
}

