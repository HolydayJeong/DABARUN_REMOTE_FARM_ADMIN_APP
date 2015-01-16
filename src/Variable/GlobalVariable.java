package Variable;

//APIKEY
//AIzaSyDyH6nr07Iv8fGfraEqiu8d97k1pVPc1CQ

public class GlobalVariable {
	//public final static String url = "http://211.39.253.201/game/";
	//public final static String url = "http://cra16.handong.edu/Dabarun/";
	public final static String url = "http://211.39.253.201/Dabarun/";
	
	public final static String login = url + "login.php";
	
	public final static String push = url + "push_all.php";

	public final static String getDoList = url + "getDoList.php";
	public final static String getDoDetail = url + "getDoDetail.php";
	
	public final static String setPoint = url + "setPoint.php";
	
	public final static String SPF_LOGIN = "LOGIN";
	public final static String SPF_ID = "ID";
	public final static String SPF_PW = "PW";
	public final static String SPF_SCORE = "SCORE";
	public final static String SPF_SVALUE = "SVALUE"; 
	public final static String NOTICE_READ = "noticeRead";
	
	

	public static String getRequestStr(String request){
		switch(Integer.parseInt(request))
		{
		case 1:
			request = "물을 주세요";
			break;
		case 2:
			request = "비료를 주세요";
			break;
		case 3:
			request = "잡초를 뽑아주세요";
			break;
		}
		return request;
	}
	
	public static String getCropStr(String crop)
	{
		switch(Integer.parseInt(crop))
		{
		case 1:
			crop = "딸기";
			break;
		case 2:
			crop = "토마토";
			break;
		}
		return crop;
		
	}
}

