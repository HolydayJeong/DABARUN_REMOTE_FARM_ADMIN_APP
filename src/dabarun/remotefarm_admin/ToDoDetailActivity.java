
package dabarun.remotefarm_admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
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

import dabarun.remotefarm_admin.main.LoginActivity;

import Variable.GlobalVariable;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ToDoDetailActivity extends ActionBarActivity implements View.OnClickListener {
	ImageView image;
	
	TextView poscrop;
	TextView name;
	TextView request;
	TextView startdate;
	TextView levelnum;
	
	Button Confirm;
	Button Cancel;

    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    
	JSONArray android = null;
	
	String reqId = "";
	static int req = 0;
	int seq = 0;
	int reqTemp = 0;
	
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String POSCROP = "poscrop";
	private static final String CROP = "type";
	private static final String FARMNUM = "farmNum";
	private static final String MODNUM = "modNum";
	private static final String REQUEST = "request";
	private static final String NAME = "name";
	private static final String STARTDATE = "startDate";
	private static final String LEVELNUM = "level";
	private static boolean isFirst = true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_detail);
		
		image = (ImageView) findViewById(R.id.dodetail_landImage);
		poscrop = (TextView) findViewById(R.id.dodetail_poscrop); 
		name = (TextView) findViewById(R.id.dodetail_name);
		request = (TextView) findViewById(R.id.dodetail_request);
		startdate = (TextView) findViewById(R.id.dodetail_startdate);
		levelnum = (TextView) findViewById(R.id.dodetail_levelnum);
		
		Confirm = (Button) findViewById(R.id.dodetail_btnconfirm);
		Confirm.setOnClickListener(this);
		Cancel= (Button) findViewById(R.id.dodetail_btncancel);
		Cancel.setOnClickListener(this);
		
		Intent intent = getIntent();
		seq = Integer.parseInt(intent.getStringExtra("seq"));
		
	}
	
	public void onClick(View view){
		if(view == Confirm)
			 reqTemp = seq;
		else if(view == Cancel)
			reqTemp = seq*10 + seq;
			
		new AlertDialog.Builder(this)
			.setTitle("확인")
			.setMessage("확인을 누르면 \""+getButtonText(reqTemp)+"\" 알람을 보냅니다.")
			.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {} })
			.setNeutralButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try{
	  					
	  					HttpClient client = new DefaultHttpClient();
	  					Log.d("test","confirm button");				
	  	    			try{
	  	    				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
	  	    				nameValuePairs1.add(new BasicNameValuePair("reqId", reqId));
	  	    				nameValuePairs1.add(new BasicNameValuePair("id", LoginActivity.id));
	  	    				nameValuePairs1.add(new BasicNameValuePair("req", ""+ reqTemp));	  	    				
	  	    				Log.d("test","confirm semd");
	  	    				
	  	    				HttpPost httpPost1 = new HttpPost(GlobalVariable.push);			
	  	    				UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
	  	    				httpPost1.setEntity(entityRequest1);
	  	    				ResponseHandler<String> handler1 = new BasicResponseHandler();
	  	    				String result1 = client.execute(httpPost1, handler1);
	  	    				
	  						result1 = result1.trim().toString();
	  						Log.d("test", "result1:"+result1);
	  						
	  		        		Log.d("test","check3");
	  	    			}catch(Exception e){e.printStackTrace();}					
  				}catch(Exception e){}		
				} })
			.show();							
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Connect().execute();
	}
	
	//AsyncTask
  	private class Connect extends AsyncTask<String, Object, JSONObject>{
  		// < > 사이에는 파라미터 타입이 들어가야 함.
  		private ProgressDialog pDialog;
  		
  		@Override
      	protected void onPreExecute() {
      		// TODO Auto-generated method stub
      		super.onPreExecute();
			Log.d("test", "Connect - onPreExecute()");
			Confirm = (Button) findViewById(R.id.dodetail_btnconfirm);
			Cancel = (Button) findViewById(R.id.dodetail_btncancel);
			pDialog = new ProgressDialog(ToDoDetailActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
      	}
      	
  	
  		@Override
  		protected JSONObject doInBackground(String... params){
			String result1 = "";
			JSONObject json = new JSONObject();
		//if(checkAllEditTextsFull()){
			try{
				HttpClient client = new DefaultHttpClient();
				Log.d("test","ing");				
    			try{
    				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
    				nameValuePairs1.add(new BasicNameValuePair("seq", ""+seq));
    				Log.d("test","seq send");
    				
    				HttpPost httpPost1 = new HttpPost(GlobalVariable.getDoDetail);			
    				UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
    				httpPost1.setEntity(entityRequest1);
    				ResponseHandler<String> handler1 = new BasicResponseHandler();
    				result1 = client.execute(httpPost1, handler1);
    				
					result1 = result1.trim().toString();
					Log.d("test", "result1:"+result1);
					
					json = new JSONObject(result1);
					
    			}catch(Exception e){}					
			}catch(Exception e){}	
			return json;
  		}
  		
		@Override
		protected void onPostExecute(JSONObject json) {
			int req = 0;
	  		pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				android = json.getJSONArray(RESULT);
				for (int i = 0; i < android.length(); i++) {
					JSONObject c = android.getJSONObject(i);
					req = Integer.parseInt(c.getString(REQUEST));
					reqId = c.getString("reqId");
					// Show on View
					poscrop.setText(c.getString(FARMNUM)+"-"+c.getString(MODNUM)+" "+GlobalVariable.getCropStr(c.getString(CROP)));
					startdate.setText("심은날짜 : "+c.getString(STARTDATE).substring(0,10));
					request.setText(GlobalVariable.getRequestStr(""+req));
					name.setText(c.getString(NAME));
					levelnum.setText(c.getString(LEVELNUM));
					Confirm.setText(getButtonText((req)));
					Cancel.setText(getButtonText(req*10+req));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
  		}
  	}
  	private String getButtonText(int request)
	{
		String text ="";
		
		switch(request)
		{
			case 1:
				text = "물을 주었습니다!";
				break;
			case 2:
				text = "비료를 주었습니다!";
				break;
			case 3:
				text = "잡초를 뽑았습니다!";
				break;
				
			case 11:
				text = "지금은 물을 줄 때가 아닙니다.";
				break;
			case 22:
				text = "지금은 비료를 줄 때가 아닙니다.";
				break;
			case 33:
				text = "지금은 잡초를 뽑을 때가 아닙니다";
				break;
		}			
		return text;
	}
}
	