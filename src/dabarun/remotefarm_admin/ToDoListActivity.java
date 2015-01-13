package dabarun.remotefarm_admin;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.JSONParser;
import Variable.GlobalVariable;
import Variable.TableMainLayout;
import android.support.v7.appcompat.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends Activity {

	ListView list;

	TextView seq;
	TextView cropSeq;
	TextView request;
	TextView id;
	TextView modNum;
	TextView name;

	Button Btngetdata;
	Button BtPlaygame;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	// URL to get JSON Array
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String CROPSEQ = "cropSeq";
	private static final String REQUEST = "request";
	private static final String ID = "id";
	private static final String MODNUM = "modNum";
	private static final String NAME = "name";
	private static boolean isFirst = true;

	JSONArray android = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.todolist_main);
		// setContentView(new TableMainLayout(this));
		oslist = new ArrayList<HashMap<String, String>>();
		// Btngetdata = (Button) findViewById(R.id.getdata);
		/*
		 * Btngetdata.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { new JSONParse().execute();
		 * } });
		 */
		// new JSONParse().execute();
		// BtPlaygame = (Button) findViewById(R.id.playgame);
		// BtPlaygame.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// Intent intent = new Intent(RankMainActivity.this,
		// MainActivity.class);
		// startActivity(intent);
		// }
		// });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_todolist://todolist 클릭 - 아무것도 안해야됨.
			return true;
		case R.id.action_farm_gridview: //Farm grid view 로 진입
			ExecuteGridActivity();
			return true;
		case R.id.action_message_menu: //Message menu로 진입
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}
	private void ExecuteGridActivity(){ // action menu에서 아이템 선택시 실행되는 펑션.
		Intent i = new Intent(ToDoListActivity.this,GridViewActivity.class);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		oslist = new ArrayList<HashMap<String, String>>();
		super.onResume();
		parsingCheck();
	};

	private void parsingCheck() {
		new JSONParse().execute();
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			id = (TextView) findViewById(R.id.id);
			name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(ToDoListActivity.this);
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
				android = json.getJSONArray(RESULT);
				oslist.clear();
				for (int i = 0; i < android.length(); i++) {
					JSONObject c = android.getJSONObject(i);
					// Storing JSON item in a Variable
					String id = c.getString(ID);
					String name = c.getString(NAME);
					String seq = c.getString(SEQ);
					String cropSeq = c.getString(CROPSEQ);
					// Adding value HashMap key => value
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(ID, id);
					map.put(NAME, name);
					// map.put(TAG_API, api);
					oslist.add(map);
					list = (ListView) findViewById(R.id.todolist);
					ListAdapter adapter = new SimpleAdapter(
							ToDoListActivity.this, oslist, R.layout.list_v,
							new String[] { ID, NAME }, new int[] { R.id.id,
									R.id.name });
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Toast.makeText(
									ToDoListActivity.this,
									"You Clicked at "
											+ oslist.get(+position).get("name"),
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
