package parseDUGrade;

import java.io.IOException;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parse {
	private String id, pw="", s;
	
	
	protected void setUserInfo(String id, String pw){
		this.id = id;
		this.pw = pw;
	}
	
	
	protected String load() throws IOException{
		Connection.Response res = Jsoup.connect("https://sso.daegu.ac.kr/dgusso/ext/tigersweb/login_form.do")
				.method(Connection.Method.GET)
			    .userAgent("Mozila")
			    .timeout(0)
			    .execute();
		
		String sessionID = res.cookie("JSESSIONID");
		Map<String, String> cookies = res.cookies();
		
		Response res2 = Jsoup.connect("https://sso.daegu.ac.kr/dgusso/ext/tigersweb/login_process.do")
			    .data("Return_Url","https://tigers.daegu.ac.kr:8443/DGUnivProj/",
			    		"overLogin","true",
			    		"loginName", id,
			    		"password", pw)
			    .cookie("JSESSIONID", sessionID)
			    .timeout(0000)
			    .execute();
		
		cookies = res2.cookies();
		
		Response parse2 = Jsoup.connect("https://tigers.daegu.ac.kr:8443/DGUnivProj/std/suup/SUUP140LI02.jsp")
				.data("selYear","2017",
						"selHakgi","1",
						"__EventTarget","btnQuery",
			    		"__EventArgument","1",
			    		"__VIEWSTATE.ISPOSTBACK","")
			    .timeout(0)
				.cookies(cookies)
	            .execute();
		
		Document doc3=parse2.parse();
		
		Elements elem = doc3.select("table").remove(7).select("tr");
		
		s="No	교과목명	수업\r\nNo	교과목명	수업\r\n평가	게시\r\n"+elem.remove(0).text();
		
		for(Element el : elem){
			
			s =s + "\r\n";
			s =s + el.text();
		}
		
		//System.out.println(s);
		//System.out.println(s.contains(String.valueOf((char) 160)));//뭐하는거였지.. 까먹었다
		String test = s.replaceAll(String.valueOf((char) 160), "*");
		//System.out.println(test);
		return s;
		
		
	}

}
