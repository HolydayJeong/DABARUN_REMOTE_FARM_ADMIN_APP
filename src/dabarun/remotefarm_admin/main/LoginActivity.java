package dabarun.remotefarm_admin.main;


import java.io.IOException;
import java.util.ArrayList;
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
import org.json.JSONObject;


import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import dabarun.remotefarm_admin.R;


import Variable.GlobalVariable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {


	Button b;
    EditText id_Edt,pw_Edt;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    
    static String id;
     
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("debug", "onCreate1");
    	super.onCreate(savedInstanceState);
    	GCMRegistrar.unregister(this);
        setContentView(R.layout.activity_login_main);
        
       
        Log.d("debug", "onCreate2");
        
        b = (Button)findViewById(R.id.Button01);  
        id_Edt = (EditText)findViewById(R.id.id_Edt);
        pw_Edt = (EditText)findViewById(R.id.pw_Edt);
        tv = (TextView)findViewById(R.id.tv);
         
      		SharedPreferences spf = getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);
      		//session key value
      
      		id = spf.getString(GlobalVariable.SPF_ID, "");
      		if(!"".equals(id))
      		{
      			id_Edt.setText(id);
      			
      			
      		//여긴 개발용 나중에 꼭 고쳐!!!!!!!!
      			Intent intent = new Intent(LoginActivity.this, TabActivity.class);                                                                                                                                             
					startActivity(intent);
      		}


        Log.d("debug", "onCreate3");
        
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	id = id_Edt.getText().toString();
            	 Log.d("debug", "onClick");
            	new Register().execute();
            }
        });
    }
     
    
    public void showAlert(){
    	 Log.d("debug", "alert");
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }
    
  //AsyncTask
  	private class Register extends AsyncTask<String, Object, Integer>{
  		
      	protected void onPreExecute() {
      		// TODO Auto-generated method stub
      		super.onPreExecute();
      	}
      	
  		@Override
  		protected Integer doInBackground(String... params){
  			
  			//if(checkAllEditTextsFull()){
  				try{
  					
  					HttpClient client = new DefaultHttpClient();
  					Log.d("test","ing");				
  	    			try{
  	    				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
  	    				nameValuePairs1.add(new BasicNameValuePair("id", id));
  	    				nameValuePairs1.add(new BasicNameValuePair("pw", pw_Edt.getText().toString()));
  	    				Log.d("test","i7");
  	    				
  	    				
  	    				HttpPost httpPost1 = new HttpPost(GlobalVariable.login);			
  	    				UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
  	    				httpPost1.setEntity(entityRequest1);
  	    				ResponseHandler<String> handler1 = new BasicResponseHandler();
  	    				String result1 = client.execute(httpPost1, handler1);
  	    				
  						result1 = result1.trim().toString();
  						Log.d("test", "result1:"+result1);
  						
  		        		Log.d("test","check3");
  		        		
  		        		//when login is successful
  		        		if(!result1.equals("not_exist")){
  		        			registerGCM();


  		        			SharedPreferences spf = getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);
  							SharedPreferences.Editor spfEdit = spf.edit();
  							
  							spfEdit.putString(GlobalVariable.SPF_ID, id_Edt.getText().toString());
  							spfEdit.commit();
  		        			Log.d("debug", "before startActivity");
  		        			
  		        			//Execute activity below
  		        			Intent intent = new Intent(LoginActivity.this, TabActivity.class);                                                                                                                                             
  							startActivity(intent);
  		        		}	        		
  	    			}catch(Exception e){
  	    				e.printStackTrace();
  	    			}					
  				}catch(Exception e){}				
  			return -1;


  		}
  	}
  		
  	/// 리팩토링 : 위에 부분을 나중에 밑에 집어넣어보자. 가능할거 같아. php 하나로 처리해서.
  	
  	public void registerGCM(){
		GCMRegistrar.checkDevice(this);		// 기기가 등록되었는지 확인.
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("test", "regId : "+regId);
		if ("".equals(regId) || null == regId) {
			Log.d("test", "regId check");
			
		  GCMRegistrar.register(this, GlobalVariable.PROJECT_ID);
		} 
		else {
			Log.d("test", "Already registered");
		}
		HttpClient client = new DefaultHttpClient();
		try{ 
	
			String result1;
			Log.d("test","MainActivity id : "+id);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("phid", regId));	//key,value
			nameValuePairs.add(new BasicNameValuePair("id", id));	//key, value
			HttpPost httpPost = new HttpPost(GlobalVariable.redIdSend);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
			httpPost.setEntity(entityRequest);
			ResponseHandler<String> handler = new BasicResponseHandler();
			result1 = client.execute(httpPost, handler); 
			result1 = result1.trim();
			Log.d("test", "phid reg : "+result1);
			
			}catch(Exception e){}
	}
  	//register 끝
  	
}
