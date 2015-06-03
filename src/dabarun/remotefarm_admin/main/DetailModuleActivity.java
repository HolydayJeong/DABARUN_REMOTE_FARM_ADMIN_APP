package dabarun.remotefarm_admin.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dabarun.remotefarm_admin.R;

//import dabarun.remotefarm_admin.Grid.JSONParse;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailModuleActivity extends Activity implements View.OnClickListener {
	private ArrayList<Button> gridButton = new ArrayList<Button>(); // 버튼 어레이
	private ArrayList<String> buttonText = new ArrayList<String>(); // 버튼 텍스트를
																	// 저장하는
																	// String

	// for JSONparse // 근데 사실 이게 필요할지 의문...
	TextView name; // 씀
	TextView cropDate; // 씀
	TextView cropName;// 씀
	TextView request;
	TextView id;
	TextView modNum;// 씀
	ImageView cropImage;
	ListView userLogList;
	
	Button water, weed, fertilizer, levelup, harvest;
	Button takePhoto;
	
	int req = 0;
	int cmd = 0;	// 5 - 수확불가
	String modNum1;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	// JSON Node Namesㄴㄴ
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String REQUEST = "request";
	private static final String ID = "id";
	private static final String MODNUM = "modNum";
	private static final String NAME = "name";
	// JSON user log names
	private static final String CROPSEQ = "cropSeq";
	private static final String REQUESTDATE = "requestDate";
	private static final String FINNDATE = "finnDate";
	private static final String ISFINN = "isFinn";
	private static final String STARTDATE = "startDate";
	private static final String TYPE = "type";
	
	HttpPost httpPost1;
	// JSON Array
	JSONArray jsonArray = null;
	// extra info from previous activity
	HashMap<String, String> extras = new HashMap<String, String>();
	Intent intent;
	ArrayList<HashMap<String, String>> logList = new ArrayList<HashMap<String, String>>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_module_view);
		// retreive intent and get Extra information and set the text with the
		// information.
		intent = getIntent();
		extras = (HashMap<String, String>) intent.getSerializableExtra("info");
		modNum1 = extras.get(MODNUM);
		name = (TextView) findViewById(R.id.textView_name);
		name.setText(intent.getStringExtra("name"));
		modNum = (TextView) findViewById(R.id.text_land);
		modNum.setText("모듈 " + extras.get(GlobalVariable.MODNUM));
		cropDate = (TextView) findViewById(R.id.textView_plant_date);
		cropDate.setText(extras.get(GlobalVariable.STARTDATE));
		cropName = (TextView) findViewById(R.id.textView_cropname);
		cropName.setText(GlobalVariable.getCropStr(extras
				.get(GlobalVariable.TYPE)));
		cropImage = (ImageView) findViewById(R.id.imageView_crop);
		cropImage.setImageDrawable(getCropImg(extras.get(GlobalVariable.TYPE)));
		
		
		//버튼 등록 그리고 수확하기(harverst) 버튼은 색상을 바꿈.
		water = (Button)findViewById(R.id.button_give_water);
		water.setOnClickListener(this);
		weed = (Button)findViewById(R.id.button_remove_weed);
		weed.setOnClickListener(this);
		fertilizer = (Button)findViewById(R.id.button_fertilize);
		fertilizer.setOnClickListener(this);
		levelup = (Button)findViewById(R.id.button_levelup);
		levelup.setOnClickListener(this);
		harvest = (Button)findViewById(R.id.button_harvest);
		harvest.setOnClickListener(this);
		harvest.getBackground().setColorFilter(new LightingColorFilter(0x006666, 0xFF0000));
		takePhoto = (Button)findViewById(R.id.button_take_photo);// 카메라 촬영 버튼 등록.
		takePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(DetailModuleActivity.this, CameraMainActivity.class); 
				intent1.putExtra("id", intent.getStringExtra("id"));
				startActivity(intent1);
			}
		});
		
		buttonAddToArray();
	}

	private Drawable getCropImg(String crop) {
		Drawable drawable = null;

		extras.get(GlobalVariable.TYPE);
		switch (Integer.parseInt(crop)) {
		case 1:
			drawable = getResources().getDrawable(R.drawable.strawberry1);
			break;
		case 2:
			drawable = getResources().getDrawable(R.drawable.cabbage1);
			break;
		}
		return drawable;
	}

	// 버튼 셋팅 - 버튼 4개를 arrayList에 등록하고 버튼 각각 setText 해줌.
	private void buttonAddToArray() {
		// 화면에 출력되는 버튼 4개를 button 변수에 저장.

	}

	// XML에 아래의 버튼들에 onclick = "onClickButton"을 해줌. 이제 그 버튼들을 클릭시
	// 아래의 "onClickButton" 메서드가 실행됨. onClickListener 대신 사용함.
	@Override
	public void onClick(View v) {
		nameValuePairs.clear();
		
		if(v == takePhoto){
				Intent intent1 = new Intent(DetailModuleActivity.this, CameraMainActivity.class); 
				intent1.putExtra("id", intent.getStringExtra("id"));
				startActivity(intent1);
		}
		
		else if(v == harvest){
			req = 5;
			httpPost1 = new HttpPost(GlobalVariable.setHarvest);
			nameValuePairs.add(new BasicNameValuePair("id", intent.getStringExtra("id")));
		}
		else if (v == levelup){
			req = 4;
			httpPost1 = new HttpPost(GlobalVariable.setLevelUp);
			nameValuePairs.add(new BasicNameValuePair("id", intent.getStringExtra("id")));
		}
		else if (v == water){
			req = 1;
			httpPost1 = new HttpPost(GlobalVariable.insertRequest);
			nameValuePairs.add(new BasicNameValuePair("id", intent.getStringExtra("id")));
			nameValuePairs.add(new BasicNameValuePair("modNum", modNum1));
			nameValuePairs.add(new BasicNameValuePair("request", String.valueOf(req)));
		}
		else if (v == weed){
			req = 2;
			httpPost1 = new HttpPost(GlobalVariable.insertRequest);
			nameValuePairs.add(new BasicNameValuePair("id", intent.getStringExtra("id")));
			nameValuePairs.add(new BasicNameValuePair("modNum", modNum1));
			nameValuePairs.add(new BasicNameValuePair("request", String.valueOf(req)));
		}
		else if (v == fertilizer){
			req = 3;
			httpPost1 = new HttpPost(GlobalVariable.insertRequest);
			nameValuePairs.add(new BasicNameValuePair("id", intent.getStringExtra("id")));
			nameValuePairs.add(new BasicNameValuePair("modNum", modNum1));
			nameValuePairs.add(new BasicNameValuePair("request", String.valueOf(req)));
		}
		
		new AlertDialog.Builder(this)
        .setTitle("확인")
        .setMessage("확인을 누르면 \""+getButtonText(req)+"\" 알람을 보냅니다.")
        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {} })
        .setNeutralButton("확인", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
              try{
               	 new GCMSend().execute();
               	 
                }catch(Exception e){e.printStackTrace();}
           } })
        .show();     
	}

	private void ExecuteToDoActivity() { // action menu에서 아이템 선택시 실행되는 펑션.
		this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행.
		// Intent i = new Intent(GridViewActivity3.this,
		// ToDoListActivity.class);
		// startActivity(i);
	}

	// ///////////// 액션메뉴 관련 1 끝

	// ////////////JSONParse 관련 1 시작
	@Override
	protected void onResume() {
		// buttonText를 가지고 parse 해야겠지?
		super.onResume();
		// parsingCheck();
		executeLogJSON();
	};

	private void executeJSON() {
		new JSONParse().execute();
	}

	private void executeLogJSON() {
		new UserLogJSON().execute();
	}
	
	private void makeToast(String txt){
		Toast.makeText(DetailModuleActivity.this, txt, Toast.LENGTH_SHORT).show();
	}
	
	private String getButtonText(int request)
	   {
	      String text ="";
	      
	      switch(request)
	      {
	         case 1:
	            text = "물을 줄 때가 되었습니다!";
	            break;
	         case 2:
	            text = "비료를 줄 때가 되었습니다!";
	            break;
	         case 3:
	            text = "잡초를 뽑을 때가 되었습니다!";
	            break;
	         case 4:
	        	 text = "작물이 한 단계 성장 했습니다!";
	        	 break;
	         case 5:
	        	 text = "작물이 수확되었습니다!";
	        	 break;
	      }         
	      return text;
	   }

	private class UserLogJSON extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(DetailModuleActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(GlobalVariable.userLog
					+ "?cropSeq=" + intent.getStringExtra("cropSeq"));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				jsonArray = result.getJSONArray(RESULT);
				// oslist.clear();
				logList.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);

					String cropSeq = c.getString(CROPSEQ);
					String id = c.getString("sendId");
					String modNum = c.getString(MODNUM);
					String request = c.getString(REQUEST);
					String requestDate = c.getString(REQUESTDATE);
					String finnDate = c.getString(FINNDATE);
					int isFinn = Integer.parseInt(c.getString("isFinn"));
					HashMap<String, String> attr = new HashMap<String, String>();
					HashMap<String, String> attr2 = new HashMap<String, String>();
					if (id.equals(intent.getStringExtra("id"))
							&& extras.get(MODNUM).equals(modNum)) {
						if (isFinn != 0) {
							attr.clear();
							attr.put(CROPSEQ, cropSeq);
							attr.put(ID, id);
							attr.put(
									REQUEST,
									"[완료]"
											+ GlobalVariable
													.getRequestStr(request));
							attr.put("date", finnDate.substring(5));
							logList.add(attr);
						}
						attr2.clear();
						attr2.put(CROPSEQ, cropSeq);
						attr2.put(ID, id);
						attr2.put(REQUEST,
								"[요청]" + GlobalVariable.getRequestStr(request));
						attr2.put("date", requestDate.substring(5));
						logList.add(attr2);

					}
					

				}
				Collections.sort(logList,
						new Comparator<HashMap<String, String>>() {

							@Override
							public int compare(HashMap<String, String> lhs,
									HashMap<String, String> rhs) {
								// TODO Auto-generated method stub

								
								return rhs.get("date").compareTo(lhs.get("date"));
							}
						});
				
				userLogList = (ListView) findViewById(R.id.listview_user_log);

				ListAdapter adapter = new SimpleAdapter(
						DetailModuleActivity.this, logList,
						R.layout.list_log, new String[] { REQUEST,
								"date" }, new int[] { R.id.list_request, R.id.list_date });

				userLogList.setAdapter(adapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(DetailModuleActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(GlobalVariable.getDoList);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				jsonArray = json.getJSONArray(RESULT);
				// oslist.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);
					// Storing JSON item in a Variable
					String id = c.getString(ID);
					String name = c.getString(NAME);
					String seq = c.getString(SEQ);
					// String cropSeq = c.getString(CROPSEQ);
					// JSON에서 부터 받아온 String을 가지고 이제 button들에 setText하면 되겠지?
					gridButton.get(i).setText(seq);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	// ////////////JSONParse 관련 1 끝
	
	private class GCMSend extends AsyncTask<String, String, JSONObject> {
		 // GCM 보내기
 	
 	     @Override
 	     protected JSONObject doInBackground(String... args) {
 	    	 int isFinn = 0;
 	    	 try{
 	             HttpClient client = new DefaultHttpClient();
 	             Log.d("test","ing");            
 	              try{
 	                Log.d("test","req send");
 	                
 	                         
 	                UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
 	                httpPost1.setEntity(entityRequest1);
 	                ResponseHandler<String> handler1 = new BasicResponseHandler();
 
 	                String result = client.execute(httpPost1, handler1);
 	                result = result.trim().toString();
 	                
 	                Log.d("test", "result1:"+result);
 	                
 	                if("success".equals(result))
 	                {
 	                	JSONParser json = new JSONParser();
 	               	 	List<NameValuePair> params = new ArrayList<NameValuePair>();
 
 	                    params.add(new BasicNameValuePair("from", ""));
 	                    params.add(new BasicNameValuePair("fromn", ""));
 	                    params.add(new BasicNameValuePair("to", intent.getStringExtra("id")));
 	                    params.add((new BasicNameValuePair("msg",getButtonText(req))));

	                    JSONObject jObj = json.getJSONFromUrl(GlobalVariable.chatUrl+"send" ,params);
	                    cmd = 1;
	       	         return jObj;
 	                }
 	                else if("requestExist".equals(result))
 	                	cmd = 4;
 	                else if("NotYet".equals(result))
 	                	cmd = 5;
 	              }catch(Exception e){e.printStackTrace();}               
 	          }catch(Exception e){e.printStackTrace();}
	    	 return null;	    	 
 	     }
	     @Override
 	     protected void onPostExecute(JSONObject json) {
	    	 if(cmd == 1){
	    		 makeToast("전송했습니다");
	    		 Intent intent = new Intent(DetailModuleActivity.this, TabActivity.class);                                                                                                                                             
				startActivity(intent);
	    		 return;
	    	 }
	    	 else if(cmd == 4){
	    		 makeToast("이미 요청을 하셨습니다");
	    		 return;
	    	 }
	    	 else if(cmd == 5){
	    		 makeToast("아직 충분한 단계가 아닙니다.\n레벨업을 먼저 해주세요");
	    		 return;
	    	 }
	    	 
 	         String res = null;
 	         try {
 	        	 if(json != null){
	 	        	 res = json.getString("response");
	 	             if(res.equals("Failure")){
	 	                 Toast.makeText(getApplicationContext(),"The user has logged out. You cant send message anymore !",Toast.LENGTH_SHORT).show();
	 	             }
		             else
		            	 Toast.makeText(getApplicationContext(),"전송했습니다",Toast.LENGTH_SHORT).show();
 	        	 }
 	        	
 	         } catch (JSONException e) {
 	             e.printStackTrace();
 	         }
 	     }
 	 }


}
