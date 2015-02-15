package dabarun.remotefarm_admin.chatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.main.GridFarmDetailViewActivity;
import dabarun.remotefarm_admin.main.GridFarmViewFragment;
import dabarun.remotefarm_admin.main.JSONParser;
import dabarun.remotefarm_admin.main.ToDoDetailActivity;

import Variable.GlobalVariable;
//import android.R;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class UserFragment extends Fragment {
    ListView list;
    ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
    Button refresh;
    List<NameValuePair> params;
    SharedPreferences prefs;
    
    String userName;

    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    String id;
    
    Lock lock;
    
    ProgressDialog pDialog;
    
    // ///////////////Fragment/////////////////////////////////
    FragmentActivity fa;
    LinearLayout llayout;
    /////////////////////////////////////////////////////////////
    public static UserFragment newInstance(int sectionNumber) {
    	UserFragment fragment = new UserFragment();
		Bundle args = new Bundle();
		args.putInt(GlobalVariable.ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
    @Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	prefs = getActivity().getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);

    	fa = (FragmentActivity) super.getActivity();
		llayout = (LinearLayout) inflater.inflate(R.layout.user_fragment,
				container, false);
		list = (ListView)llayout.findViewById(R.id.listView);
		refresh = (Button)llayout.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	new Load().execute();
            }
        });
        
        userName = prefs.getString("FROM_NAME", "");
        new Load().execute();
		return llayout;
	}

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));
            Log.d("test", prefs.getString("REG_FROM",""));
            //JSONArray jAry = json.getJSONArray("http://10.0.2.2:8080/getuser",params);
            JSONArray jAry = json.getJSONArray( "http://54.65.196.112:8000/getuser",params);

            return jAry;
        }
        
        
        @Override
        protected void onPostExecute(JSONArray json) {
        	if(json != null){
        		users.clear();
	            for(int i = 0; i < json.length(); i++){
	                JSONObject c = null;
	                try {
	                    c = json.getJSONObject(i);
	                    String name = c.getString("name");
	                    String mobno = c.getString("mobno");
	                    HashMap<String, String> map = new HashMap<String, String>();
	                    map.put("name", name);
	                    map.put("mobno", mobno);
	                    users.add(map);
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            }
	            ListAdapter adapter = new SimpleAdapter(getActivity(), users,
	                    R.layout.user_list_single,
	                    new String[] { "name","mobno" }, new int[] {
	                    R.id.name, R.id.mobno});
	            list.setAdapter(adapter);
	            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            	@Override
	                public void onItemClick(AdapterView<?> parent, View view,
	                                        int position, long id) {
	                    Bundle args = new Bundle();
	                    args.putString("mobno", users.get(position).get("mobno"));
	                    Log.d("test", "mobno : " + users.get(position).get("mobno"));
	                    Intent chat = new Intent(fa, ChatActivity.class);
	                    chat.putExtra("INFO", args);
	                    startActivity(chat);
	                }
	            });
        	}
        	else
        		Toast.makeText(getActivity(), "JSON Length 0 in UserFragment", Toast.LENGTH_SHORT).show();
        }
    }
}