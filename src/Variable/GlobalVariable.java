package Variable;

//APIKEY
//AIzaSyDyH6nr07Iv8fGfraEqiu8d97k1pVPc1CQ

public class GlobalVariable {
<<<<<<< HEAD
	public final static String url = "http://211.39.253.201/Dabarun/";
	// public final static String url = "http://cra16.handong.edu/Dabarun/";
=======
	//public final static String url = "http://211.39.253.201/game/";
	//public final static String url = "http://cra16.handong.edu/Dabarun/";
	public final static String url = "http://211.39.253.201/Dabarun/";
	
>>>>>>> master
	public final static String login = url + "login.php";

	public final static String push = url + "push_all.php";

	public final static String getDoList = url + "getDoList.php";
<<<<<<< HEAD
	public final static String getTotalLand = url + "getTotalLand.php";
	public final static String getDetailLand = url + "getDetailLand.php";
<<<<<<< HEAD
	public final static String getDoDetail = url + "getDoDetail.php";
=======
=======
	public final static String getDoDetail = url + "getDoDetail.php";
	
>>>>>>> master
>>>>>>> d7edce60adc6c95d0ea1ba5dfa6860278482ea79
	public final static String setPoint = url + "setPoint.php";

	public final static String SPF_LOGIN = "LOGIN";
	public final static String SPF_ID = "ID";
	public final static String SPF_PW = "PW";
	public final static String SPF_SCORE = "SCORE";
	public final static String SPF_SVALUE = "SVALUE";
	public final static String NOTICE_READ = "noticeRead";
<<<<<<< HEAD
=======
	
	

	public static String getRequestStr(String request){
		switch(Integer.parseInt(request))
		{
		case 1:
			request = "���� �ּ���";
			break;
		case 2:
			request = "��Ḧ �ּ���";
			break;
		case 3:
			request = "���ʸ� �̾��ּ���";
			break;
		}
		return request;
	}
	
	public static String getCropStr(String crop)
	{
		switch(Integer.parseInt(crop))
		{
		case 1:
			crop = "����";
			break;
		case 2:
			crop = "�丶��";
			break;
		}
		return crop;
		
	}
}
>>>>>>> master

	// JSON Node Names
	public static final String RESULT = "result";
	public static final String SEQ = "seq";
	public static final String REQUEST = "request";
	public static final String ID = "id";
	public static final String MODNUM = "modNum";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String STARTDATE = "startDate";
	public static final String LEVEL = "level";


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
