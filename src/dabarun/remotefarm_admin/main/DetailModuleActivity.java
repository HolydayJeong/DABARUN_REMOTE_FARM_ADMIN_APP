package dabarun.remotefarm_admin.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class DetailModuleActivity extends Activity {
	private ArrayList<Button> gridButton = new ArrayList<Button>(); // 버튼 어레이
	private ArrayList<String> buttonText = new ArrayList<String>(); // 버튼 텍스트를
																	// 저장하는
																	// String

	// for JSONparse // 근데 사실 이게 필요할지 의문...
	TextView name; // 씀
	TextView cropDate; // 씀
	TextView cropName;// 씀
	TextView request;
	TextView modNum;// 씀
	ImageView cropImage;
	ListView userLogList;
	
	Button water;
	Button fertilizer;
	Button harvest;
	Button levelup;
	Button camera;
	
	SharedPreferences spf;
	
	String cropSeq;
	int level;
	int req;
	String id;

	   
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String REQUEST = "request";
	private static final String ID = "id";
	private static final String SENDID = "sendId";
	private static final String MODNUM = "modNum";
	private static final String NAME = "name";
	// JSON user log names
	private static final String CROPSEQ = "cropSeq";
	private static final String LEVEL = "level";
	private static final String REQUESTDATE = "requestDate";
	private static final String FINNDATE = "finnDate";
	private static final String ISFINN = "isFinn";
	private static final String STARTDATE = "startDate";
	private static final String TYPE = "type";

	// JSON Array
	JSONArray jsonArray = null;
	// extra info from previous activity
	HashMap<String, String> extras = new HashMap<String, String>();
	Intent intent;
	ArrayList<HashMap<String, String>> logList = new ArrayList<HashMap<String, String>>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    spf = getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);
	    id = spf.getString(GlobalVariable.SPF_ID, "");
	      
		setContentView(R.layout.activity_detail_module_view);
		// retreive intent and get Extra information and set the text with the
		// information.
		intent = getIntent();
		extras = (HashMap<String, String>) intent.getSerializableExtra("info");
		name = (TextView) findViewById(R.id.textView_name);
		name.setText(intent.getStringExtra("name"));
		modNum = (TextView) findViewById(R.id.text_land);
		modNum.setText("모듈 " + extras.get(GlobalVariable.MODNUM));
		cropSeq = extras.get(CROPSEQ);
		cropDate = (TextView) findViewById(R.id.textView_plant_date);
		cropDate.setText(extras.get(GlobalVariable.STARTDATE));
		cropName = (TextView) findViewById(R.id.textView_cropname);
		cropName.setText(GlobalVariable.getCropStr(extras
				.get(GlobalVariable.TYPE)));
		cropImage = (ImageView) findViewById(R.id.imageView_crop);
		cropImage.setImageDrawable(getCropImg(extras.get(GlobalVariable.TYPE)));
		
		water = (Button)findViewById(R.id.button_give_water);
		fertilizer = (Button)findViewById(R.id.button_fertilize);
		levelup = (Button)findViewById(R.id.button_levelup);
		harvest = (Button)findViewById(R.id.button_harvest);
		harvest.getBackground().setColorFilter(new LightingColorFilter(0x006666, 0xFF0000));
		buttonAddToArray();
		
		camera = (Button)findViewById(R.id.button_take_photo);
		new UserLogJSON().execute();
	/*	camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				Intent intent = new Intent(DetailModuleActivity.this, CameraMainActivity.class);
				startActivity(intent);

				//내장카메라 호출
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				
				//동영상 품질
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				//동영상 시간 제한
				intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 111);// 시간
				//동영상 용량 제한
				intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, (long) (1024 * 1024 * 10));// 용량 MB
				//동영상 저장 경로
				String mImageMovieUri = "/sdcard/Download/exam/";
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageMovieUri);
				startActivityForResult(intent, 2);
			
				//new UploadTask().execute();
				}
			});
			*/
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
	public void onClickButton(View v) {
		req = 0;
		 switch (v.getId()) {
			case R.id.button_give_water:
				req = 1;
				break;
			case R.id.button_harvest:
				if(level < 5)
					Toast.makeText(getApplicationContext(),"아직 수확할 단계가 아닙니다.",Toast.LENGTH_SHORT).show();
				else{
					req = 9; // 수확
				}
			break;
			case R.id.button_fertilize:
				req = 2;
				break;
			case R.id.button_levelup:
				if(level == 5)
					Toast.makeText(getApplicationContext(),"최고 단계입니다.",Toast.LENGTH_SHORT).show();
				else
					req = 8; // 레벨업 
				break;
			case R.id.button_take_photo:
				req = 10;
				Intent intent = new Intent(DetailModuleActivity.this, CameraMainActivity.class);
				startActivity(intent);
				break;
		 }
	
		if(req > 0){
			 new AlertDialog.Builder(this)
	         .setTitle("확인")
	         .setMessage("확인을 누르면 \""+getReqText(req)+"\" 알람을 보냅니다.")
	         .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {} })
	         .setNeutralButton("확인", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
		             try{
		            	 new GCMSend().execute();
			             finish();
		             }catch(Exception e){
	                    	 e.printStackTrace();}               
	            } })
	         .show();   
		}
	}

	private void ExecuteToDoActivity() { // action menu에서 아이템 선택시 실행되는 펑션.
		this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행.
		// Intent i = new Intent(GridViewActivity3.this,
		// ToDoListActivity.class);
		// startActivity(i);
	}

	// ///////////// 액션메뉴 관련 1 끝

	private void takePhoto() {
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//		startActivityForResult(intent, 0);
		dispatchTakePictureIntent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i("TAG", "onActivityResult: " + this);
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			setPic();
//			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//			if (bitmap != null) {
//				mImageView.setImageBitmap(bitmap);
//				try {
//					sendPhoto(bitmap);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	public String getReqText(int req)
	{
		String msg = null;
		switch(req){
		case 1:
        	msg = "작물에 물을 줄 때가 되었습니다.";
        	break;
		case 2:
        	msg = "작물에 비료를 줄 때가 되었습니다.";
        	break;
		case 8:
			msg = "작물이 레벨업 했습니다!";
			break;
		case 9:
			msg = "작물을 수확 했습니다!!";
			break;
		}
		return msg;
	}
	
	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadTask().execute(bitmap);
	}
	
	// ////////////JSONParse 관련 1 시작
	@Override
	protected void onResume() {
		// buttonText를 가지고 parse 해야겠지?
		super.onResume();
		// parsingCheck();
		new UserLogJSON().execute();
	};

	private void executeJSON() {	// 애는 도데체 뭐하는 놈이냐
		new JSONParse().execute();
	}


	private class UserLogJSON extends AsyncTask<String, String, JSONObject> {
		// 작물에 대한 연혁을 추가하는 부분.
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
					+ "?cropSeq="+cropSeq);// + intent.getStringExtra("id"));
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

					String id = c.getString(SENDID);
					level = c.getInt(LEVEL);
					String modNum = c.getString(MODNUM);
					String request = c.getString(REQUEST);
					String requestDate = c.getString(REQUESTDATE);
					String finnDate = c.getString(FINNDATE);
					int isFinn = Integer.parseInt(c.getString("isFinn"));
					HashMap<String, String> attr = new HashMap<String, String>();
					HashMap<String, String> attr2 = new HashMap<String, String>();
					if (id.equals(intent.getStringExtra("id"))
							&& extras.get(MODNUM).equals(modNum)) {
						if (isFinn == 1) {
							attr.clear();
							attr.put(ID, id);
							attr.put(REQUEST,"[완료]"+ GlobalVariable.getRequestStr(request));
							attr.put("date", finnDate);
							logList.add(attr);
						}
						if (isFinn == 2) {
							attr.clear();
							attr.put(ID, id);
							attr.put(REQUEST,"[거절]"+ GlobalVariable.getRequestStr(request));
							attr.put("date", finnDate);
							logList.add(attr);
						}
						attr2.clear();
						attr2.put(ID, id);
						attr2.put(REQUEST,"[요청]" + GlobalVariable.getRequestStr(request));
						attr2.put("date", requestDate);
						logList.add(attr2);

					}
					else{
						attr2.clear();
						attr2.put(ID, id);
						attr2.put(REQUEST,"[권유]" + GlobalVariable.getRequestStr(request));
						attr2.put("date", requestDate);
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
/*						DetailModuleActivity.this, logList,
						R.layout.list_log, new String[] { CROPSEQ, REQUEST,
								"date" }, new int[] { R.id.list_cropseq,
								R.id.list_request, R.id.list_date });
*/
						DetailModuleActivity.this, logList,
						R.layout.list_log, new String[] { REQUEST,
								"date" }, new int[] {R.id.list_request, R.id.list_date });
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
	
	private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
		protected Void doInBackground(Bitmap... bitmaps) {
			if (bitmaps[0] == null)
				return null;
			setProgress(0);
			
			Bitmap bitmap = bitmaps[0];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
			InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost httppost = new HttpPost(
						GlobalVariable.url + "savetofile.php"); // server

				CameraMultipartEntity reqEntity = new CameraMultipartEntity();
				reqEntity.addPart("myFile",
						DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) 
						 + ".jpg", in);
				httppost.setEntity(reqEntity);

				Log.i("TAG", "request " + httppost.getRequestLine());
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
					Log.i("TAG", "response " + response.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (response != null)
						Log.i("TAG", "response " + response.getStatusLine().toString());
				} finally {}
			} finally {	}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(DetailModuleActivity.this, R.string.uploaded, Toast.LENGTH_LONG).show();
		}
	}
	
	String mCurrentPhotoPath;
	
	static final int REQUEST_TAKE_PHOTO = 1;
	File photoFile = null;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File

	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	            		Uri.fromFile(photoFile));
	            //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}

	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
	    File dir = new File(storageDir);
	    if (!dir.exists())
	    	dir.mkdir();
	    
	    File image = new File(storageDir + "/" + imageFileName + ".jpg");
	    //File image = new File(storageDir + "/" + imageFileName + ".mp4");
	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    Log.i("TAG", "photo path = " + mCurrentPhotoPath);
	    return image;
	}
	
	private void setPic() {
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    
	    Matrix mtx = new Matrix();
	    mtx.postRotate(90);
	    // Rotating Bitmap
	    Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

	    if (rotatedBMP != bitmap)
	    	bitmap.recycle();
	    
	    try {
			sendPhoto(rotatedBMP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	///  비디오 올리기
	/*
	private class UploadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			File file = new File("/storage/extSdCard/DCIM/Camera/20150218_215610.mp4");
			try {
			      HttpClient client = new DefaultHttpClient();  
			      HttpPost post = new HttpPost(GlobalVariable.url + "savetofile.php");
			      FileBody bin = new FileBody(file);
			      MultipartEntity reqEntity = new     
			      MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
			      reqEntity.addPart("myFile", bin);
			      post.setEntity(reqEntity);  
			      HttpResponse response = client.execute(post);  
			      HttpEntity resEntity = response.getEntity();  
			      if (resEntity != null) {    
			           Log.w("RESPONSE",EntityUtils.toString(resEntity));
			      }
			} catch (Exception e) {
			      e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(DetailModuleActivity.this, R.string.uploaded, Toast.LENGTH_LONG).show();
		}
	}
	*/
	
	private class GCMSend extends AsyncTask<String, String, JSONObject> {
		// 작물에 대한 연혁을 추가하는 부분. & GCM 보내기
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(DetailModuleActivity.this);
			pDialog.setMessage("잠시만 기다려주세요 ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	     @Override
	     protected JSONObject doInBackground(String... args) {
	             Log.d("test","GCM send ing");            
	                  Log.d("test","ing");       
	                  JSONObject jObj = null;
	                   try{
	 	            	  HttpClient client = new DefaultHttpClient();
	                     ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
	                     nameValuePairs1.add(new BasicNameValuePair("id", id));
	                     nameValuePairs1.add(new BasicNameValuePair("cropSeq", ""+cropSeq));
	                     nameValuePairs1.add(new BasicNameValuePair("request", ""+req));
	                     Log.d("test","req send");
	                     
	                     HttpPost httpPost1 = new HttpPost(GlobalVariable.setDetailModule);         
	                     UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
	                     httpPost1.setEntity(entityRequest1);
	                     ResponseHandler<String> handler1 = new BasicResponseHandler();

	                     String result = client.execute(httpPost1, handler1);
	                     result = result.trim().toString();
	                     
	                     if("success".equals(result))
	                     {
		                	JSONParser json = new JSONParser();
		               	 	List<NameValuePair> params = new ArrayList<NameValuePair>();
	
		                    params.add(new BasicNameValuePair("from", spf.getString("REG_FROM","")));
		                    params.add(new BasicNameValuePair("fromn", spf.getString("FROM_NAME","")));
		                    params.add(new BasicNameValuePair("to", intent.getStringExtra("id")));
		                    params.add((new BasicNameValuePair("msg",getReqText(req))));
	
		                    jObj = json.getJSONFromUrl(GlobalVariable.chatUrl+"send" ,params);
	                     }	                     
	              }catch(Exception e){}  
	    	 return jObj;	    	 
	     }
	     @Override
	     protected void onPostExecute(JSONObject json) {
	    	 if(json != null)
	    	 {
		         String res = null;
		         try {
		             res = json.getString("response");
		             if(res.equals("Failure")){
		                 Toast.makeText(getApplicationContext(),"The user has logged out. You cant send message anymore !",Toast.LENGTH_SHORT).show();
		             }
		             else
		            	 Toast.makeText(getApplicationContext(),"전송했습니다",Toast.LENGTH_SHORT).show();
		         } catch (JSONException e) {
		             e.printStackTrace();
		         }
	    	 }
	    	 else
	    		 Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
	     }
	 }
}
