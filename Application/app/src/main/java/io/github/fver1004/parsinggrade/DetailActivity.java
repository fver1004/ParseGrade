package io.github.fver1004.parsinggrade;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class DetailActivity extends AppCompatActivity  {
    Intent oldIntent;
    private String id, pw;
    final int MENU_COUNT = 4;
    Switch saveSwitch;
    SharedPreferences saveDetail;
    SharedPreferences.Editor editor;
    RelativeLayout[] menu = new RelativeLayout[MENU_COUNT];
    TextView[] state = new TextView[MENU_COUNT];
    int menuId[] = {R.id.menu0, R.id.menu1, R.id.menu2, R.id.menu3};
    int stateId[] = {R.id.state0, R.id.state1, R.id.state2, R.id.state3};
    int selectedMenu[] = {0, 0, 1,2};//기본 설정 옵션 인덱스(현재 2017, 1, 소리, 30분)
    String menuitems[][] = {
            {"2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010"},//
            {"1", "S", "2", "W"},
            {"true","false"},
            {"10분", "20분", "30분"}
    };

    /*OnCreate----------------------------------------------------------------------*/
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Intent 수신
        oldIntent = getIntent();
        id =  oldIntent.getStringExtra("id");
        pw =  oldIntent.getStringExtra("pw");

        TextView button = (TextView) findViewById(R.id.buttonToParsing);
        saveSwitch = (Switch) findViewById(R.id.select1);


        saveDetail = getSharedPreferences("Detail", 0);
        editor=saveDetail.edit();
        //저장 상태 가져옴. 없을경우 false 반환. 뷰 수정해주는게 아닌 selectedMenu값 수정
        if(saveDetail.getBoolean("SAVESWITCH",false)){
            saveSwitch.setChecked(true);
            for(int i= 0; i < MENU_COUNT; i++)
                selectedMenu[i] = saveDetail.getInt("selectedMenu"+i,selectedMenu[i]);//없을경우 기본값 반환

        }

        /*----------뷰 인스턴스 for문으로 받아보자---------*/
        for(int i = 0; i < MENU_COUNT; i++){
            menu[i] = (RelativeLayout) findViewById(menuId[i]);
            state[i] = (TextView) findViewById(stateId[i]);
            state[i].setText(menuitems[i][selectedMenu[i]]);//기본설정 텍스트 세팅.
            menu[i].setOnClickListener(menuListener);
        }

        /*----------파싱시작버튼 클릭 리스너---------------*/
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                StartAsyncTask thread = new StartAsyncTask();
                thread.execute();

            }
        });


    }//onCreate()

    /*menu 클릭 리스너----------------------------------------------------------------*/
    private View.OnClickListener menuListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            for(int i = 0; i < MENU_COUNT; i++){
                if (v.getId() == menu[i].getId()){
                    open(i);break;
                }}}
    };

    /*menu 클릭하면 나오는 대화상자 메소드--------------------------------------------------*/
    private void open(final int i){
        final List<String> ListItems = new ArrayList<>();

        for(int j =0; j < menuitems[i].length; j++)
            ListItems.add(menuitems[i][j]);


        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("선택하세요.");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                state[i].setText(selectedText);
                selectedMenu[i]=pos;
            }
        });
        builder.show();
    }

    /*AsyncTask로 MessageRS 인스턴스 만들어서 서버와 통신------------------------------------*/
    private class StartAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            MessageRS messageRS = new MessageRS();
            messageRS.message(1, id, pw, menuitems[0][selectedMenu[0]],menuitems[1][selectedMenu[1]], menuitems[2][selectedMenu[2]],Integer.parseInt(menuitems[3][selectedMenu[3]].replaceAll("[^0-9]","")));


            return null;}

        @Override
        protected void onPostExecute(Void result) {
            if(saveSwitch.isChecked()) {
                for(int i=0;i<MENU_COUNT;i++)
                    editor.putInt("selectedMenu"+i, selectedMenu[i]);
                editor.putBoolean("SAVESWITCH",true);
                editor.commit();
            }else{
                editor.putBoolean("SAVESWITCH",false);
                editor.commit();
            }

            Toast.makeText(DetailActivity.this, "공시 알림이 시작되었습니다.\n성적 공시 알림이나 어플을 터치하면\n성적을 확인할 수 있습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }//asyncTask

}