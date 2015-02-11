package dabarun.remotefarm_admin.chatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.main.JSONParser;

import Variable.GlobalVariable;
//import android.R;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class UserFragment extends Fragment {
    ListView list;
    ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
    Button refresh;
    List<NameValuePair> params;
    SharedPreferences prefs;
    
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    String id;
    
    ProgressDialog pDialog;
    
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    String userName;
    
    public static UserFragment newInstance(int sectionNumber) {
    	UserFragment fragment = new UserFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    	
        View view =inflater.inflate(R.layout.user_fragment, container, false);
        prefs = getActivity().getSharedPreferences("Chat", 0);
        
        userName = prefs.getString("FROM_NAME", "");

        list = (ListView)view.findViewById(R.id.listView);
        refresh = (Button)view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
                Fragment reg = new UserFragment();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, reg);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        new Register().execute();
        new Load().execute();

        return view;
    }

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobno", prefs.getString("REG_FROM","")));
            //JSONArray jAry = json.getJSONArray("http://10.0.2.2:8080/getuser",params);
            JSONArray jAry = json.getJSONArray( "http://54.65.196.112:8000/getuser",params);

            return jAry;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
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
                    Intent chat = new Intent(getActivity(), ChatActivity.class);
                    chat.putExtra("INFO", args);
                    startActivity(chat);
                }
            });
        }
    }
    
    private class Register extends AsyncTask<String, String, JSONObject> {
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected JSONObject doInBackground(String... args) {
   //     	JSONObject jObj = null;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(GlobalVariable.SENDER_ID);

                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();
                }

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
            }
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", prefs.getString(GlobalVariable.SPF_ID, "")));
            params.add(new BasicNameValuePair("mobno", prefs.getString(GlobalVariable.SPF_ID, "")));
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));

            JSONObject jObj = json.getJSONFromUrl("http://54.65.196.112:8000/login",params);
            return  jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
        	pDialog.dismiss();
             try {
            	 if(json != null){
	                 String res = json.getString("response");
	                 if(res.equals("Sucessfully Registered")) {
	                	 Toast.makeText(getActivity(),"Registered",Toast.LENGTH_SHORT).show();
	                 }else{
	                     Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
	                 }
                 	 SharedPreferences.Editor edit = prefs.edit();
                      edit.putString("REG_FROM", prefs.getString(GlobalVariable.SPF_ID, ""));	// ������ ���Ⱑ mobno
                      edit.putString("FROM_NAME", prefs.getString(GlobalVariable.SPF_ID, ""));
                      edit.commit();
                 }
            	 else
            		 Toast.makeText(getActivity(),"JSON NULL in ChatActivity, Register ",Toast.LENGTH_SHORT).show();
             }catch (Exception e) {}
        }
    }
}