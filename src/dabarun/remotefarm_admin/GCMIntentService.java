package dabarun.remotefarm_admin;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import Variable.GlobalVariable;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService(){ 
		this(GlobalVariable.PROJECT_ID); 
	}
	public GCMIntentService(String project_id){
		super(project_id); 
	}
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		String title = context.getString(R.string.app_name);
		
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		Intent notificationIntent = new Intent(context, LoginActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, title, message, intent);
		notificationManager.notify(0, notification);
	}	
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
	}
	
	//�몄떆濡�諛쏆� 硫붿떆吏�/
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("test","push receive");
		String msg = intent.getStringExtra("msg");
		
		generateNotification(context, msg);
	}
	@Override
	protected void onRegistered(Context context, String reg_id) {
		// TODO Auto-generated method stub
		Log.d("test","등록ID:"+reg_id);
				
		new GCMProgress().execute(reg_id);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.e("test", "등록 해제");
	}
	
	/* 11.28 �߰� phid GCM�� ���� ����ϱ� */
  	
	private class GCMProgress extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String...params) {
			// TODO Auto-generated method stub
			String phid =params[0];
			String result ="";

			HttpClient client = new DefaultHttpClient();
			try{
				Log.d("test","phid : "+phid);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("phid", phid));	//key,value
				nameValuePairs.add(new BasicNameValuePair("id", LoginActivity.id));	//key,value
				HttpPost httpPost = new HttpPost(GlobalVariable.redIdSend);
				UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
				httpPost.setEntity(entityRequest);
				ResponseHandler<String> handler = new BasicResponseHandler();
				result = client.execute(httpPost, handler); 
			}catch(Exception e){
			}
			result = result.trim();
			Log.d("test","result GCM "+result.trim());
			
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result.trim().equals("success")){
				Log.d("test","result GCM1 "+result.trim());
			}
		}
	}
}
