package io.github.fver1004.parsinggrade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class MainActivity extends AppCompatActivity {
    SharedPreferences saveLogin;
    SharedPreferences.Editor editor;
    private String id,pw;
    EditText loginId,loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginId = (EditText) findViewById(R.id.loginId);
        loginPw = (EditText) findViewById(R.id.loginPw);
        Button button = (Button) findViewById(R.id.buttonToDetail);
        final CheckedTextView checkBox = (CheckedTextView) findViewById(R.id.checkbox);


        saveLogin = getSharedPreferences("Info", 0);
        editor=saveLogin.edit();
        //체크박스 상태 가져옴. 없을경우 false 반환.
        if(saveLogin.getBoolean("CHECKBOX",false)){
            checkBox.setChecked(true);
            loginId.setText(saveLogin.getString("ID", ""));
            loginPw.setText(saveLogin.getString("PW", ""));

        }



        /*button 리스너------------------------------------------------------------*/
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                id = loginId.getText().toString();
                pw = loginPw.getText().toString();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("pw", pw);
                startActivity(intent);

                if(checkBox.isChecked()){
                    editor.putString("ID", id);
                    editor.putString("PW", pw);
                    editor.putBoolean("CHECKBOX", true);
                    editor.commit();
                }else{
                    editor.putBoolean("CHECKBOX", false);
                    editor.commit();
                }
                finish();
            }
        });/*button.setOnClickListener()------------------------------------------*/
        /*checkbox 리스너-----------------------------------------------------------*/
        checkBox.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBox.isChecked())
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);
            }
        });/*checkbox.setOnClickListener()-----------------------------------------*/



    }




}