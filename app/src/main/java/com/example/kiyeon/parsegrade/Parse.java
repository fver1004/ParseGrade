package com.example.kiyeon.parsegrade;
import android.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
/**
 * created by Kiyeon Kim, 7508A, Deagu Univ.
 *
 * mail: fver1004@gmail.com
 */
public class Parse extends IOException{
    private String id, pw="", s;
    int num=0;
    ArrayList<String> list, resultList ;
    String[][] resultArray;
    String selYear = "2016";
    String selHakgi = "2";

    //로그인 값()
    protected void settingLogin(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    //과목수 세기()
    protected int countSubject() throws IOException{
        int lastLine= s.lastIndexOf("\r\n");//과목 개수..lastLine은 라인이아닌 문자열 인덱스임
        if(lastLine!=-1)//\r\n으로 나눠서 -1이면 자료가 없습니다 임
            num = Integer.parseInt(s.substring(lastLine+1).replaceAll("[^0-9]", ""));
        return num;
    }



    //파싱값 배열 변환()
    protected String[][] changeAsArrayList() throws IOException {
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
                        list.remove(1);
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

    //파싱()
    protected String load(){
        try {
            Connection.Response res = Jsoup.connect("https://sso.daegu.ac.kr/dgusso/ext/tigersweb/login_form.do")
                    .method(Connection.Method.GET)
                    .timeout(5000)
                    .execute();

            String sessionID = res.cookie("JSESSIONID");
            Map<String, String> cookies = res.cookies();

            Connection.Response res2 = Jsoup.connect("https://sso.daegu.ac.kr/dgusso/ext/tigersweb/login_process.do")
                    .data("Return_Url", "https://tigers.daegu.ac.kr:8443/DGUnivProj/",
                            "overLogin", "true",
                            "loginName",id,
                            "password", pw)
                    .cookie("JSESSIONID", sessionID)
                    .timeout(5000)
                    .execute();

            cookies = res2.cookies();

            Connection.Response parse2 = Jsoup.connect("https://tigers.daegu.ac.kr:8443/DGUnivProj/std/suup/SUUP140LI02.jsp")
                    .data("selYear", selYear,
                            "selHakgi", selHakgi,
                            "__EventTarget", "btnQuery",
                            "__EventArgument", "1",
                            "__VIEWSTATE.ISPOSTBACK", "")
                    .timeout(5000)
                    .cookies(cookies)
                    .execute();

            Document doc3 = parse2.parse();
            Elements elem = doc3.select("table").remove(7).select("tr");
            elem.remove(0);
            elem.remove(0);
            s=""+elem.remove(0).text();


            for(Element el : elem){

                s =s + "\r\n";
                s =s + el.text();
            }
        }catch(Exception e){}
        String withOutNbsp = s.replaceAll(String.valueOf((char) 160),"-");
        Log.d("asdf",""+withOutNbsp);
        s=withOutNbsp;

        return s;//convert withOutNbsp
    }
}
