package dabarun.remotefarm_admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import dabarun.remotefarm_admin.Grid.JSONParse;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class DetailModuleActivity extends Activity{
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
	
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String REQUEST = "request";
	private static final String ID = "id";
	private static final String MODNUM = "modNum";
	private static final String NAME = "name";
	// JSON Array
	JSONArray jsonArray = null;
	// extra info from previous activity
	ArrayList<String> extras = new ArrayList<String>();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_module_view);
		// retreive intent and get Extra information and set the text with the information.
		Intent intent = getIntent();
		extras = intent.getStringArrayListExtra("idAndName");
		name = (TextView) findViewById(R.id.username_textview);
		name.setText(extras.get(1));
		
		
		buttonAddToArray();

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
			break;
		case R.id.Grid2_button2:
			Log.d("test", "Grid1_button2");
			break;
		case R.id.Grid2_button3:
			Log.d("test", "Grid1_button3");
			break;
		case R.id.Grid2_button4:
			Log.d("test", "Grid1_button4");
			break;
		case R.id.Grid2_button5:
			Log.d("test", "Grid1_button5");
			break;
		case R.id.Grid2_button6:
			Log.d("test", "Grid1_button6");
			break;
		case R.id.Grid2_button7:
			Log.d("test", "Grid1_button6");
			break;
		case R.id.Grid2_button8:
			Log.d("test", "Grid1_button6");
			break;
		default:
			break;
		}
	}

	

	private void ExecuteToDoActivity() { // action menu에서 아이템 선택시 실행되는 펑션.
		this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행.
		//Intent i = new Intent(GridViewActivity3.this, ToDoListActivity.class);
		//startActivity(i);
	}

	// ///////////// 액션메뉴 관련 1 끝

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
			//name = (TextView) findViewById(R.id.name);
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
					//String cropSeq = c.getString(CROPSEQ);
					// JSON에서 부터 받아온 String을 가지고 이제 button들에 setText하면 되겠지?
					gridButton.get(i).setText(seq);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	// ////////////JSONParse 관련 1 끝


}
