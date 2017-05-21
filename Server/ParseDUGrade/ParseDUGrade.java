package parseDUGrade;

import java.util.Timer;
import java.util.TimerTask;

public class ParseDUGrade {
	static String time;
	static Parse parse;
	static FCMNotification fcmNotify;
	static boolean diff = false;
	static String preValue = "", nowValue="";
	
	public static void main(String []args) throws Exception{
		fcmNotify = new FCMNotification();
		parse = new Parse();
		parse.setUserInfo("type ID", "type PW");
		
		
		time = "update: "+new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		System.out.println(time);
		/*����*/
		preValue = parse.load();
		Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                
                try {
                	//0.ũ�Ѹ� �� �Ľ��ϱ�
                	nowValue = parse.load();
                	
                	//1. ������ �����Ϳ� ���ϰ� �ٸ��ٸ� fcm ����
                	if(preValue.equals(nowValue)){
                		//System.out.println("����!");
                		diff = false;}
                	else{
                		//System.out.println("�޶�!");
                		//System.out.println(preValue);
                		//System.out.println("-------------------------------");
                		//System.out.println(nowValue);
                		FCMNotification.pushFCMNotification("");
                		preValue = nowValue;
                		diff = true;}
                	
                }catch(Exception e){e.printStackTrace();timer.cancel();}
                
                time = "�ֱ� ���Žð�:"+new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
                System.out.println(time + ": " + diff);
            }};
        
        timer.schedule(timerTask, 1000*60, 1000*60*30);//1�ʵ� ����, 30�� �ֱ�
        /*Timer�� �ϴ� ���������� ������.*/
        
        
        
	}

}
