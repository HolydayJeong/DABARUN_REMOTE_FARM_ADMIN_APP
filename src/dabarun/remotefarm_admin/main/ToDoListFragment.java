package dabarun.remotefarm_admin.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.main.JSONParser;
import dabarun.remotefarm_admin.main.TabActivity.PlaceholderFragment;
import Variable.GlobalVariable;
import Variable.TableMainLayout;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.appcompat.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class ToDoListFragment extends Fragment {

   ListView list;

   TextView seq;
   TextView cropSeq;

   TextView pos;
   TextView crop;

   TextView request;
   TextView name;

   Button Btngetdata;
   Button BtPlaygame;
   ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
   // URL to get JSON Array
   // JSON Node Names
   private static final String RESULT = "result";
   private static final String SEQ = "seq";private static final String POS = "pos";
   private static final String FARMNUM = "farmNum";
   private static final String MODNUM = "modNum";
   private static final String CROP = "type";
   private static final String REQUEST = "request";
   private static final String NAME = "name";
   private static boolean isFirst = true;

   JSONArray android = null;
   // ///////////////Fragment/////////////////////////////////
   FragmentActivity fa;
   RelativeLayout rlayout;

   // //////////////////////////////////////////////////////
   public static ToDoListFragment newInstance(int sectionNumber) {
      ToDoListFragment fragment = new ToDoListFragment();
      Bundle args = new Bundle();
      args.putInt(GlobalVariable.ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public View onCreateView(LayoutInflater inflater,
         @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      fa = (FragmentActivity) super.getActivity();
      rlayout = (RelativeLayout) inflater.inflate(
            R.layout.activity_to_do_list, container, false);

      oslist = new ArrayList<HashMap<String, String>>();
      parsingCheck();

      pos = (TextView) rlayout.findViewById(R.id.list_pos);
      crop = (TextView) rlayout.findViewById(R.id.list_crop);
      request = (TextView) rlayout.findViewById(R.id.list_request);
      name = (TextView) rlayout.findViewById(R.id.list_name);

      return rlayout;
   }

   private void parsingCheck() {
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
            android = json.getJSONArray(RESULT);
            oslist.clear();
            for (int i = 0; i < android.length(); i++) {
               JSONObject c = android.getJSONObject(i);

               // Storing JSON item in a Variable
               String seq = c.getString(SEQ);
               String pos = c.getString("farmNum") + "-"
                     + c.getString("modNum");
               String type = GlobalVariable
                     .getCropStr((c.getString(CROP)));
               String request = GlobalVariable.getRequestStr(c
                     .getString(REQUEST));

               String name = c.getString(NAME);
               // Adding value HashMap key => value
               HashMap<String, String> map = new HashMap<String, String>();
               map.put(SEQ, seq);
               map.put(POS, pos);
               map.put(CROP, type);
               map.put(REQUEST, request);
               map.put(NAME, name);
               // map.put(TAG_API, api);
               oslist.add(map);
               list = (ListView) rlayout.findViewById(R.id.todolist);
               ListAdapter adapter = new SimpleAdapter(getActivity(),
                     oslist, R.layout.list_v, new String[] { POS, CROP,
                           REQUEST, NAME }, new int[] { R.id.list_pos,
                           R.id.list_crop, R.id.list_request,
                           R.id.list_name });
               /*
                * ListAdapter adapter = new SimpleAdapter(
                * ToDoListActivity.this, oslist, R.layout.list_v, new
                * String[] { POS, CROP, REQUEST, NAME }, new int[] {
                * R.id.list_pos, R.id.list_crop, R.id.list_request,
                * R.id.list_name });
                */

               list.setAdapter(adapter);
               list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent,
                        View view, int position, long id) {

                     Intent intent = new Intent(fa,
                           ToDoDetailActivity.class);
                     intent.putExtra("seq",
                           oslist.get(+position).get(SEQ));

                     startActivity(intent);
                  }
               });
            }
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
   }

   /*
    * @Override protected void onCreate(Bundle savedInstanceState) {
    * super.onCreate(savedInstanceState);
    * 
    * 
    * setContentView(R.layout.activity_to_do_list);
    * 
    * oslist = new ArrayList<HashMap<String, String>>();
    * 
    * pos = (TextView) findViewById(R.id.list_pos); crop = (TextView)
    * findViewById(R.id.list_crop); request = (TextView)
    * findViewById(R.id.list_request); name = (TextView)
    * findViewById(R.id.list_name);
    * 
    * 
    * setContentView(R.layout.activity_to_do_list); oslist = new
    * ArrayList<HashMap<String, String>>();
    * 
    * pos = (TextView) findViewById(R.id.list_pos); crop = (TextView)
    * findViewById(R.id.list_crop); request = (TextView)
    * findViewById(R.id.list_request); name = (TextView)
    * findViewById(R.id.list_name);
    * 
    * 
    * }
    */

   /*
    * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
    * menu items for use in the action bar MenuInflater inflater =
    * getMenuInflater(); inflater.inflate(R.menu.main, menu); return
    * super.onCreateOptionsMenu(menu); }
    */

   // 엑션 메뉴의 메뉴 클릭시 실행되는 메서드 .
   /*
    * @Override public boolean onOptionsItemSelected(MenuItem item) { // TODO
    * Auto-generated method stub switch (item.getItemId()) { case
    * R.id.action_todolist:// todolist 클릭 - TodoListActivity 실행.
    * 
    * return true; case R.id.action_farm_gridview: // GridViewActivity 실행
    * ExecuteGridViewActivity(); return true; case R.id.action_message_menu: //
    * Message menu로 진입 return true; default: return
    * super.onOptionsItemSelected(item); } }
    */
   /*
    * private void ExecuteGridViewActivity() { // action menu에서 아이템 선택시 실행되는
    * 펑션. this.finish(); // 기존 액티비티를 종료하고 선택한 액티비티를 실행. Intent i = new
    * Intent(ToDoListActivity.this, GridViewActivity.class); startActivity(i);
    * }
    */

   /*
    * @Override protected void onResume() { super.onResume(); //oslist = new
    * ArrayList<HashMap<String, String>>(); oslist.clear(); parsingCheck(); };
    */

   /*   */

}