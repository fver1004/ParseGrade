package io.github.fver1004.parsinggrade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class GradeListActivity extends AppCompatActivity {
    private String s;
    String[][] arraySquare;
    int num=0;
    private ListView mListView = null;//리스트뷰 인스턴스 선언
    private ListViewAdapter mAdapter = null;//어댑터 인스턴스 선언
    MessageRS messageRS;

    /*OnCreate----------------------------------------------------------------------*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);

        GetGradeAsyncTask thread = new GetGradeAsyncTask();
        thread.execute();//로딩이있을수있으니 딜레이 넣어야됨!


    }//onCreate()

    /*뒤로가기할 경우 어떻게해줄까---------------------------------------------------------*/
    //public void onBackPressed(){moveTaskToBack(true);}


    /*성적값 서버에서 받기(AsyncTask로 messageRS 생성 후 사용)------------------------------*/
    private class GetGradeAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try{//파싱값 불러오기
                messageRS = new MessageRS();
                s = messageRS.message(2);
                countSubject();
                arraySquare = changeAsArrayList();
                Log.d("asdf","성공"+s);
            }catch(Throwable e){
                e.printStackTrace();
                Log.d("asdf","실패");
            }

            return null;}

        @Override
        protected void onPostExecute(Void result) {

            mListView = (ListView) findViewById(R.id.mList);
            mAdapter = new ListViewAdapter(GradeListActivity.this);
            mListView.setAdapter(mAdapter);

            if(num!=0) {//0이 아니면 서비스 실행하기

                for (int i = 0; i < num; i++)
                    mAdapter.addItem(arraySquare[i]);

            }else{//과목이 0개일때
                String noArraySquare[] = {"자료가 존재하지 않아요."};
                mAdapter.addItem(noArraySquare);
            }

            //파싱 서비스 중지 버튼
            Button stopButton = (Button) findViewById(R.id.stopParsing);
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StopAsyncTask stopThread = new StopAsyncTask();
                    stopThread.execute();
                    Toast.makeText(GradeListActivity.this, "종료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    //꺼지면서 토스트 뿌리기.
                }});
            //중지되면서 앱 꺼지게 하기

        }
    }//asyncTask

    private class StopAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            messageRS.message(0, null, null, null, null, null, 0);
            return null;
        }
    }

    //과목수 세기()
    void countSubject() throws IOException {
        int lastLine= s.lastIndexOf("\r\n");//과목 개수..lastLine은 라인이아닌 문자열 인덱스임
        if(lastLine!=-1)//\r\n으로 나눠서 -1이면 자료가 없습니다 임
            num = Integer.parseInt(s.substring(lastLine+1).replaceAll("[^0-9]", ""));
        //return num;
    }


    private ArrayList<String> list, resultList ;
    private String[][] resultArray;

    //파싱값 배열 변환()
    String[][] changeAsArrayList() throws IOException {
        int ii=0;
        String read, sum="";
        BufferedReader in = new BufferedReader(new StringReader(s));
        resultList = new ArrayList<String>();
        resultArray = new String[num][21];
        //18개가 들어오거나 pass자료가 들어오거나 자료가 없습니다 들어옴 자료가 없습니다는 그전에 처리해줌

        int lineMin = 0;//pass 과목 없으면 그대로 쓰고 pass 과목 있으면 1씩 빼주면 됨
        while(((read = in.readLine()) != null) && (ii!=num*3-lineMin)){
            list = new ArrayList<String>(Arrays.asList(read.split("\\s+")));
            if(read.contains("ass")){//pass일 경우!!!!!!!(pass 라는 단어 없을때는?)

                read = list.remove(1) + "(pass)";//패스과목명 임시로 read에넣고사용
                while(list.size()!=0)list.remove(0);
                while(list.size()!=19)list.add(" ");
                list.add(0, read);//pass 과목은 어떻게 나오는지 모르니까 과목만 표시해주기
                resultArray[(ii+lineMin)/3]=list.toArray(new String[list.size()]);
                in.readLine();//이의신청기간 없애기
                ii++;
                lineMin++;//pass과목은 2줄이라 ii계산 맞지 않기때문에 이걸로 맞춰줌

            }else{
                if((ii+lineMin)%3==0){
                    Log.d("asdf","뚜뚜"+list);
                    list.remove(0);
                    list.remove(1);
                    //list.remove(1);
                    while(list.size()>15)list.remove(1);//강의만족도나 수업평가 안했을경우 임시방안
                    sum = list.remove(5);
                    for (int i = 0; i < 3; i++)
                        sum = sum + "+" + list.remove(5);
                    list.add(5, sum);// 이름/중/실/기/실/과/합/실/출/실/실점/학점/
                    resultList.addAll(list);
                }
                if((ii+lineMin)%3==1){
                    list.remove(0);//중/실/기/실/과/실/출/실/
                    resultList.addAll(list);
                    resultArray[(ii+lineMin)/3]=resultList.toArray(new String[resultList.size()]);
                    resultList.clear();
                    Log.d("asdf",num+"건의 자료"+Arrays.toString(resultArray[(ii+lineMin)/3]));

                    in.readLine();//이의신청 지우기
                    ii++;
                }
            }

            ii++;}


        return resultArray;
    }



}