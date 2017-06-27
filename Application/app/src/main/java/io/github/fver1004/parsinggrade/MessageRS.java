package io.github.fver1004.parsinggrade;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
/**
 * created by Kiyeon Kim, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
//통신쪽을 따로 클래스 만들어놓음. 매개변수 optiond으로 기능별 메소드 호출.
public class MessageRS {
    static final String IP = "IP";//"220.149.13.153"; //"192.168.219.100";
    static final int Port = 55555; //443;
    Socket client;
    InetSocketAddress ipep;
    OutputStream sender;
    InputStream receiver;
    DataInputStream dataReceiver;
    byte[] data;
    String message;


    String message(int option){
        return message(option, null, null, null, null, null, 0);

    }

    String message(int option, String id, String pw, String selYear, String selHakgi, String noSoundVibe, int period){

        try{
            client = new Socket();
            //클라이언트 초기화
            ipep = new InetSocketAddress(MessageRS.IP, MessageRS.Port);
            client.connect(ipep);

            //스트림 받아오기
            sender = client.getOutputStream();
            receiver = client.getInputStream();
            dataReceiver = new DataInputStream(receiver);
        }catch(Throwable e){
            e.printStackTrace();}

        if(option == 0 || option == 1)
            return startAndStop(option, id, pw, selYear, selHakgi, noSoundVibe, period);
        else if(option == 2)
            return getGrade();
        else if(option == 3)
            return getState();
        else
            return null;


    }

    String startAndStop(int option, String id, String pw, String selYear, String selHakgi, String noSoundVibe, int period){
        try{

            //서버로 데이터 보내기
            message =
                    FirebaseInstanceId.getInstance().getToken()
                            + "," + option//option
                            + "," + id
                            + "," + pw
                            + "," + selYear
                            + "," + selHakgi
                            + "," + noSoundVibe
                            + "," + period
                            + ",";
            data = message.getBytes();
            sender.write(data, 0, data.length);

        }catch(Throwable e){e.printStackTrace();}

        return null;
    }


    String getState(){
        String out = "";
        //서버로 데이터 보내기
        String message;
        message =
                FirebaseInstanceId.getInstance().getToken() + ",3,";
        data = message.getBytes();
        try {
            sender.write(data, 0, data.length);


        //데이터 받기
        out = dataReceiver.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(out.contains("true"))
            return "true";
        else
            return "false";
    }

    String getGrade(){
        String s = "";
        try{

            //서버로 데이터 보내기
            message =
                    FirebaseInstanceId.getInstance().getToken() + ",2,";
            data = message.getBytes();
            sender.write(data, 0, data.length);

            //서버로부터 데이터 받기
            s = dataReceiver.readUTF();
            Log.d("asdf","성공"+s);
        }catch(Throwable e){
            e.printStackTrace();
            Log.d("asdf","실패");
        }

        return s;
    }

}
