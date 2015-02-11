package dabarun.remotefarm_admin.main;

import java.util.ArrayList;
import java.util.HashMap;

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

//import dabarun.remotefarm_admin.ToDoDetailActivity.Connect;
import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.main.ToDoListFragment.JSONParse;
import Variable.GlobalVariable;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LogFragment extends Fragment {
	FragmentActivity fa;
	RelativeLayout rlayout;
	private static final String ARG_SECTION_NUMBER = "section_number";

	ListView userList;
	// TextView pos; /// position - section and module number.
	// TextView cropSeq;
	// TextView request;//actually request type
	// TextView requestDate;
	// TextView finnDate;
	// TextView isFinn;
	// TextView modNum;

	//ArrayList<HashMap<String, String>> logList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> userArray = new ArrayList<String>();
	JSONArray jsonArray = null;

	// //////////////////////////////////////////////////////
	public static LogFragment newInstance(int sectionNumber) {
		LogFragment fragment = new LogFragment();
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
		rlayout = (RelativeLayout) inflater.inflate(R.layout.fragment_log,
				container, false);

		userArray = new ArrayList<String>();
		executeJSONParse();

		// pos = (TextView) rlayout.findViewById(R.id.loglist_pos);
		// crop = (TextView) rlayout.findViewById(R.id.loglist_crop);
		// request = (TextView) rlayout.findViewById(R.id.loglist_request);
		// name = (TextView) rlayout.findViewById(R.id.loglist_name);

		return rlayout;
	}

	private void executeJSONParse() {
		new JSONParse().execute();
	}

	class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("test", "onPreExecute()");
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			Log.d("test", "parse start");
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(GlobalVariable.getDoList);
			Log.d("test", "parse end");
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				jsonArray = json.getJSONArray(GlobalVariable.RESULT);
				userArray.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);

					// Storing JSON item in a Variable
					String seq = c.getString(GlobalVariable.SEQ);
					String pos = c.getString("farmNum") + "-"
							+ c.getString("modNum");
					String type = GlobalVariable.getCropStr((c
							.getString(GlobalVariable.TYPE)));
					String request = GlobalVariable.getRequestStr(c
							.getString(GlobalVariable.REQUEST));

					String name = c.getString(GlobalVariable.NAME);
					// Adding value HashMap key => value
					
					// map.put(TAG_API, api);
					userArray.add(name);
					userList = (ListView) rlayout.findViewById(R.id.todolist);
//					ListAdapter adapter = new ArrayAdapter<String>(getActivity(), getId(), getId());
//					userList.setAdapter(adapter);
//					Log.d("test", "list setting end");
//					userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> parent,
//								View view, int position, long id) {
//
//							Intent intent = new Intent(fa,
//									DetailLogActivity.class);
//							intent.putExtra(
//									"id",
//									userArray.get(+position));
//
//							startActivity(intent);
//						}
//					});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
