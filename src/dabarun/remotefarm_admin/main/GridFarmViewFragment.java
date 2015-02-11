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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GridFarmViewFragment extends Fragment {
	private ArrayList<Button> gridButton = new ArrayList<Button>(); // 버튼 어레이
	// private ArrayList<String> buttonText = new ArrayList<String>(); //
	// in here we gonna save up id and user's name. so i used 2d String
	// ArrayList
	private ArrayList<ArrayList<String>> infoStrings = new ArrayList<ArrayList<String>>();

	// for JSONparse // 근데 사실 이게 필요할지 의문...
	TextView seq;
	TextView cropSeq;
	TextView request;
	TextView id;
	TextView modNum;
	TextView name;
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String ID = "id";
	private static final String NAME = "name";
	// JSON Array
	JSONArray jsonArray = null;

	FragmentActivity fa;
	LinearLayout llayout;
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static GridFarmViewFragment newInstance(int sectionNumber) {
		GridFarmViewFragment fragment = new GridFarmViewFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fa = (FragmentActivity) super.getActivity();
		llayout = (LinearLayout) inflater.inflate(R.layout.grid_farm_view,
				container, false);

		parsingCheck();
		buttonAddToArray();

		return llayout;
	}

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_farm_view);
		buttonAddToArray();

	}*/

	// 버튼 셋팅 - 버튼 4개를 arrayList에 등록하고 버튼 각각 setText 해줌.
	private void buttonAddToArray() {
		// 화면에 출력되는 버튼 4개를 button 변수에 저장.
		Log.d("test", "ButtonAddToArray");
		OnClickListener l = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.Grid1_button1:
					Log.v("test", "Grid1_button1");
					if (infoStrings.size() > 0)
						ExecuteGrid2Activity(infoStrings.get(0));
					break;
				case R.id.Grid1_button2:
					Log.d("test", "Grid1_button2");
					if (infoStrings.size() > 1)
						ExecuteGrid2Activity(infoStrings.get(1));
					break;
				case R.id.Grid1_button3:
					Log.d("test", "Grid1_button3");
					if (infoStrings.size() > 2)
						ExecuteGrid2Activity(infoStrings.get(2));
					break;
				case R.id.Grid1_button4:
					Log.d("test", "Grid1_button4");
					if (infoStrings.size() > 3)
						ExecuteGrid2Activity(infoStrings.get(3));
					break;
				case R.id.Grid1_button5:
					Log.d("test", "Grid1_button5");
					if (infoStrings.size() > 4)
						ExecuteGrid2Activity(infoStrings.get(4));
					break;
				case R.id.Grid1_button6:
					Log.d("test", "Grid1_button6");
					if (infoStrings.size() > 5)
						ExecuteGrid2Activity(infoStrings.get(5));
					break;
				default:
					break;
				}
			}
		};
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button1));
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button2));
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button3));
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button4));
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button5));
		gridButton.add((Button) llayout.findViewById(R.id.Grid1_button6));
		
		for(Button i : gridButton){
			i.setOnClickListener(l);
		}
		parsingCheck();
	}

	// XML에 아래의 버튼들에 onclick = "onClickButton"을 해줌. 이제 그 버튼들을 클릭시
	// 아래의 "onClickButton" 메서드가 실행됨. onClickListener 대신 사용함.
	

	private void ExecuteGrid2Activity(ArrayList<String> a) {
		if (a.get(0) != null) {
			// this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행.
			Intent i = new Intent(
					fa, GridFarmDetailViewActivity.class);
			i.putExtra("idAndName", a);
			startActivity(i);
		}
	}

	// ////////////JSONParse 관련 1 시작

	private void parsingCheck() {
		new JSONParse().execute();
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(fa);
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
					.getJSONFromUrl(GlobalVariable.getTotalLand);
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
					String farmNum = c.getString("farmNum");
					// String cropSeq = c.getString(CROPSEQ);
					// JSON에서 부터 받아온 String을 가지고 이제 button들에 setText하면 되겠지?
					gridButton.get(i).setText(name);
					ArrayList<String> idAndName = new ArrayList<String>();
					idAndName.add(0, id);
					idAndName.add(1, name);
					idAndName.add(2, farmNum);
					infoStrings.add(idAndName); //
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	// ////////////JSONParse 관련 1 끝

}
