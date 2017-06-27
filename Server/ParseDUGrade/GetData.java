package parseDUGrade;

import java.util.HashMap;

//파싱요청이 들어오면 HashMap에 <유저키, 파싱객체>로 생성하고 중지요청들어오면 중지하고 제거 
public class GetData {
	
	HashMap<String, ParseDUGrade> parsList;
	String[] columns;
	

	GetData(){
		parsList = new HashMap<String, ParseDUGrade>();
	}
	
	
	//구분자 ','로 데이터 나누기[키, option, id, pw, pOption1, pOption2..]
	//columns 데이터수가 가변적이라 저장안시키고 메소드 파라미터로 옮기면서 사용함
	String extract(String receivedData){
		
		columns = receivedData.split(",");
		try{
			int opt = Integer.parseInt(columns[1]);
			
			//OPTION = 0: 파싱중지  1: 파싱요청  2: 파싱값요청  3: 파싱상태요청
			if(opt == 1 || opt == 0)
				return protoc(columns[0],Integer.parseInt(columns[1]),columns[2],columns[3],columns[4],columns[5],columns[6],Integer.parseInt(columns[7]));
			else if(opt == 2)
				return getGrade(columns[0]);
			else if(opt == 3)
				return getStatem(columns[0]);
			else
				return "";
		}catch(Exception e){
			System.out.println(Main.date() + " :: Exception :: wrong Data ::" + receivedData);
		}
		return "";//null처리관련 넣어야됨.	
	}
	
	
	/*파싱 요청, 중지 메세지 지속적으로 받는 메소드-----------------------------*/
	String protoc(String userKey, int Option, String ID, String PW, String selYear, String selHakgi, String noSoundVibe, int period){
		
		//파싱 Option 추가해서 2진수로 shift연산으로 각설정값을 조정하자@@
		//시작 요청 데이터
		if(Option == 1){
			start(userKey, ID, PW, selYear, selHakgi, noSoundVibe, period);
			return "파싱시작됨.";}
		//중지 요청 데이터
		else{
			stop(userKey);
			return "파싱중지됨.";}
	}
	
	
	/*파싱 중지 메소드--------------------------------------------------*/
	void stop(String userKey){
		ParseDUGrade Stopp = parsList.get(userKey);
		Stopp.stopParseDUGrade();
		parsList.remove(userKey);
		
		System.out.println(Main.date() + " :: Option0 :: parsing stopped :: " + parsList.size() + " Objects are alive.");
	}
	
	
	ParseDUGrade parseDUGrade;
	/*파싱 실행 메소드-------------------------------------------------*/
	void start(String userKey, String ID, String PW, String selYear, String selHakgi, String noSoundVibe, int period){
		parseDUGrade = new ParseDUGrade();
		
		try {parseDUGrade.startParseDUGrade(userKey, ID, PW, selYear, selHakgi, noSoundVibe, period);
		} catch (Exception e) {e.printStackTrace();}
		
		parsList.put(userKey, parseDUGrade);
		
		System.out.println(Main.date() + " :: Option1 :: parsing started :: " + parsList.size() + " Objects are alive.");
	}
	

	String getGrade(String userKey){
		System.out.println(Main.date() + " :: Option2 :: Grade Value returned");
		//System.out.println(parsList.get(userKey).getGrade());
		return parsList.get(userKey).getGrade();
	}
	
	
	String getStatem(String userKey){
		if(parsList.containsKey(userKey)){
			System.out.println(Main.date() + " :: Option3 :: State : running");
			return "true";
		}else{
			System.out.println(Main.date() + " :: Option3 :: State : stopped");
			return "false";
		}
		
	}

}
