package dabarun.remotefarm_admin.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dabarun.remotefarm_admin.R;

//import dabarun.remotefarm_admin.Grid.JSONParse;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GridFarmDetailViewActivity extends Activity {
	private ArrayList<Button> gridButton = new ArrayList<Button>(); // 버튼 어레이
	private ArrayList<String> buttonText = new ArrayList<String>(); // 버튼 텍스트를
																	// 저장하는
																	// String

	// for JSONparse // 근데 사실 이게 필요할지 의문...
	TextView name;
	TextView seq;
	TextView cropSeq;
	TextView request;
	TextView id;
	TextView modNum;
	

	// JSON Array
	JSONArray jsonArray = null;
	// extra info from previous activity
	ArrayList<String> extras = new ArrayList<String>();
	// HashMap<String,String> parsedInfo = new HashMap<String,String>();
	ArrayList<HashMap<String, String>> parsedInfoList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_farm_detail_view);
		// retreive intent and get Extra information and set the text with the
		// information.
		Intent intent = getIntent();
		extras = intent.getStringArrayListExtra("idAndName");
		name = (TextView) findViewById(R.id.username_textview);
		name.setText(extras.get(1));

		buttonAddToArray();
		/*
		 * for(int i = 0 ; i < parsedInfo.size();i++){
		 * setButtonText(parsedInfo,i); }
		 */

	}

	// 버튼 셋팅 - 버튼 4개를 arrayList에 등록하고 버튼 각각 setText 해줌.
	private void buttonAddToArray() {
		// 화면에 출력되는 버튼 4개를 button 변수에 저장.
		gridButton.add((Button) findViewById(R.id.Grid2_button1));
		gridButton.add((Button) findViewById(R.id.Grid2_button2));
		gridButton.add((Button) findViewById(R.id.Grid2_button3));
		gridButton.add((Button) findViewById(R.id.Grid2_button4));
		gridButton.add((Button) findViewById(R.id.Grid2_button5));
		gridButton.add((Button) findViewById(R.id.Grid2_button6));
		gridButton.add((Button) findViewById(R.id.Grid2_button7));
		gridButton.add((Button) findViewById(R.id.Grid2_button8));

		parsingCheck();
	}

	// XML에 아래의 버튼들에 onclick = "onClickButton"을 해줌. 이제 그 버튼들을 클릭시
	// 아래의 "onClickButton" 메서드가 실행됨. onClickListener 대신 사용함.
	public void onClickButton(View v) {
		switch (v.getId()) {
		case R.id.Grid2_button1:
			Log.v("test", "Grid1_button1");
			ShowDetailExecute(v, 0);
			break; 
		case R.id.Grid2_button2:
			Log.d("test", "Grid1_button2");
			ShowDetailExecute(v, 1);
			break;
		case R.id.Grid2_button3:
			Log.d("test", "Grid1_button3");
			ShowDetailExecute(v, 2);
			break;
		case R.id.Grid2_button4:
			Log.d("test", "Grid1_button4");
			ShowDetailExecute(v, 3);
			break;
		case R.id.Grid2_button5:
			Log.d("test", "Grid1_button5");
			ShowDetailExecute(v, 4);
			break;
		case R.id.Grid2_button6:
			Log.d("test", "Grid1_button6");
			ShowDetailExecute(v, 5);
			break;
		case R.id.Grid2_button7:
			Log.d("test", "Grid1_button7");
			ShowDetailExecute(v, 6);
			break;
		case R.id.Grid2_button8:
			Log.d("test", "Grid1_button8");
			ShowDetailExecute(v, 7);
			break;
		default:
			break;
		}
	}

	public void ShowDetailExecute(View v, int modNum) {
		if (parsedInfoList.size()!=0 && parsedInfoList.get(modNum).get("modNum") != null) {
			Intent intent2 = new Intent(GridFarmDetailViewActivity.this,
					DetailModuleActivity.class);
			intent2.putExtra("info", parsedInfoList.get(modNum));
			intent2.putExtra("name", getIntent().getStringArrayListExtra("idAndName").get(1));
			intent2.putExtra("id", getIntent().getStringArrayListExtra("idAndName").get(0));
			startActivity(intent2);
		} 
	}

	private void ExecuteToDoActivity() { // action menu에서 아이템 선택시 실행되는 펑션.
		this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행.
		// Intent i = new Intent(GridViewActivity2.this,
		// ToDoListActivity.class);
		// startActivity(i);
	}

	// ////////////JSONParse 관련 1 시작
	@Override
	protected void onResume() {
		// buttonText를 가지고 parse 해야겠지?
		super.onResume();
		// parsingCheck();
	};

	private void parsingCheck() {
		new JSONParse().execute();
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(GridFarmDetailViewActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser
					.getJSONFromUrl(GlobalVariable.getDetailLand + "?id="
							+ extras.get(0));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				jsonArray = json.getJSONArray(GlobalVariable.RESULT);
				// oslist.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);
					// Storing JSON item in a Variable
					String seq = new String(String.valueOf(i + 1));
					String type = c.getString(GlobalVariable.TYPE);
					String startDate = c.getString(GlobalVariable.STARTDATE);
					int modNum = Integer.parseInt(c
							.getString(GlobalVariable.MODNUM));
					String level = c.getString(GlobalVariable.LEVEL);
					// String cropSeq = c.getString(CROPSEQ);
					//String name = c.getString(GlobalVariable.NAME);

					HashMap<String, String> parsedInfo = new HashMap<String, String>();
					//parsedInfo.put(GlobalVariable.NAME, name);
					for(int i1 = 0 ; i1 < 8 ; i1 ++)
						parsedInfoList.add(new HashMap<String, String>());
					
					parsedInfo.put(GlobalVariable.SEQ, seq);
					parsedInfo.put(GlobalVariable.TYPE, type);
					parsedInfo.put(GlobalVariable.STARTDATE, startDate);
					parsedInfo.put(GlobalVariable.MODNUM,
							c.getString(GlobalVariable.MODNUM));
					parsedInfo.put(GlobalVariable.LEVEL, level);
					parsedInfoList.set(modNum, parsedInfo);

					gridButton.get(modNum).setText(
							modNum + ". " + GlobalVariable.getCropStr(type)
									+ "\n Level : " + level);

					// 여기에서 type 가지고 setText할 거임.

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * private void setButtonText(HashMap<String,String> h,int i){
	 * gridButton.get(i).setText( h.get(GlobalVariable.MODNUM)+" "+
	 * h.get(GlobalVariable.TYPE)+" \n"+ h.get(GlobalVariable.LEVEL)); }
	 */
	// ////////////JSONParse 관련 1 끝

}
