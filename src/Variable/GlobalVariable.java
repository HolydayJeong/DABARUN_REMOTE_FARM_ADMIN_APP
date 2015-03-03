package Variable;

//APIKEY
//AIzaSyDyH6nr07Iv8fGfraEqiu8d97k1pVPc1CQ

public class GlobalVariable {
	
	public final static String SENDER_ID = "130953040990";
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	/////////////////////////////////////////////////////////////////
	public final static String url = "http://211.39.253.201/Dabarun/farmer/";
	// public final static String url = "http://cra16.handong.edu/Dabarun/";

	public final static String getDoList = url + "getDoList.php";

	public final static String getTotalLand = url + "getTotalLand.php";
	public final static String getDetailLand = url + "getDetailLand.php";
	public final static String getDoDetail = url + "getDoDetail.php";
	
	public final static String setReqFinn = url + "setReqFinn.php";
	public final static String setDetailModule = url + "setDetailModule.php";

	public final static String setPoint = url + "setPoint.php";
	//////////////////////////////////////////////////////////////////
	
	public final static String chatUrl = "http://54.65.196.112:8000/";

	// ///////////////////////////////////////////////////////////////
	public final static String url1 = "http://211.39.253.201/Dabarun/";
	public final static String push = "http://cra16.handong.edu/Dabarun/dragonflight/push_test.php";
	public final static String login = url1 + "login.php";
	public final static String push_all = url1 + "push_all.php";
	public final static String PROJECT_ID = "486669052747";
	public final static String redIdSend = url1 + "push_insert.php";
	public final static String userLog = "http://211.39.253.201/Dabarun/farmer/getRequestList.php";
	// ////////////////////////////////////////////////////////////////
	public final static String DABARUNFARMER = "DABARUNFARMER";
	public final static String SPF_ID = "ID";
	public final static String SPF_PW = "PW";
	public final static String SPF_SCORE = "SCORE";
	public final static String SPF_SVALUE = "SVALUE";
	public final static String NOTICE_READ = "noticeRead";

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
	public static final String FARMNO = "farmNum";
	public static final String POS = "pos";

	public static String getRequestStr(String request) {
		switch (Integer.parseInt(request)) {
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

	public static String getCropStr(String crop) {
		if (crop.length() == 0) {
			crop = "없음";
		} else {
			switch (Integer.parseInt(crop)) {
			case 1:
				crop = "딸기";
				break;
			case 2:
				crop = "배추";
				break;
			}
		}
		return crop;

	}
}
