package com.example.kiyeon.parsegrade;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class renewService extends Service {

    private String s, id, pw;
    Parse parse;
    Timer timer;
    int renew=1;

    @Override
    public IBinder onBind(Intent intent) {throw new UnsupportedOperationException("Not yet implemented");}

    @Override
    public void onCreate(){

        Log.d("asdf","Service is created");
        parse = new Parse();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){//여기서 intent를 수신함 .
        super.onStartCommand(intent, flags, startId);
        s = intent.getStringExtra("s");
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        parse.settingLogin(id,pw);

        timer = new Timer();
        timer.schedule(new renewTimerTask(), renew*60000, renew*60000);

        return startId;
    }

    @Override
    public void onDestroy(){
        timer.cancel();
        Toast toast = Toast.makeText(this, "파싱이 종료되었습니다.",
                Toast.LENGTH_SHORT);
        toast.show();
    }


    String newS;
    class renewTimerTask extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void run(){
            newS = parse.load();
            if(s.equals(newS)){
                //notifyOption();;
                Log.d("asdf","같다");
                s=newS;
            }else{
                notifyOption();;
                Log.d("asdf","다르다알람!");
                //알림창 뜨게 하고 확인 버튼 누르면 parsingActivity로 전환
                //parsingActivity가 작동 중일 경우엔 수정
                //액티비티가 꺼졌을 경우에는 다시 실행(intent로 아이디비번등 정보 다시 전달해줘야댈듯)

            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifyOption(){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.dae);//아이콘 설정
        mBuilder.setTicker("성적이 공시되었습니다");//알림이 잠깐 뜰때 표시되는 text
        mBuilder.setWhen(System.currentTimeMillis());//알림이 표시되는 시간
        mBuilder.setNumber(10);//미확인 알림의 개수
        mBuilder.setContentTitle("성적이 공시되었습니다");//상단바 알림 제목
        mBuilder.setContentText("공시 내용:");//상단바 알림 내용
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);//기본 설정
        mBuilder.setContentIntent(pendingIntent);//실행할 작업이 담긴 PendingIntent
        mBuilder.setAutoCancel(true);//터치하면 자동으로 지워지도록 설정

        mBuilder.setPriority(Notification.PRIORITY_MAX);//우선순위

        nm.notify(111, mBuilder.build());
    }



}
