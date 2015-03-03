package dabarun.remotefarm_admin.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Lock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import dabarun.remotefarm_admin.R;
import dabarun.remotefarm_admin.chatting.UserFragment;

import Variable.GlobalVariable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TabActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	GridFarmViewFragment gridFarmViewFragment = new GridFarmViewFragment();
	
	List<NameValuePair> params;
    SharedPreferences prefs;
    
    String userName;

    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    String id;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		prefs = getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);
		new Register().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		setContentView(R.layout.activity_tab);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
/*	
	@Override
	protected void onResume(){
		super.onResume();
		// Set up the action bar.
				final ActionBar actionBar = getSupportActionBar();
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

				// Create the adapter that will return a fragment for each of the three
				// primary sections of the activity.
				mSectionsPagerAdapter = new SectionsPagerAdapter(
						getSupportFragmentManager());

				// Set up the ViewPager with the sections adapter.
				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(mSectionsPagerAdapter);

				// When swiping between different sections, select the corresponding
				// tab. We can also use ActionBar.Tab#select() to do this if we have
				// a reference to the Tab.
				mViewPager
						.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
							@Override
							public void onPageSelected(int position) {
								actionBar.setSelectedNavigationItem(position);
							}
						});
/*
				// For each of the sections in the app, add a tab to the action bar.
				for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
					// Create a tab with text corresponding to the page title defined by
					// the adapter. Also specify this Activity object, which implements
					// the TabListener interface, as the callback (listener) for when
					// this tab is selected.
					actionBar.addTab(actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
				}

	}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	 

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			// return PlaceholderFragment.newInstance(position + 1);
			switch (position) {
			case 0:
				return ToDoListFragment.newInstance(position+1);
			case 1:
				return GridFarmViewFragment.newInstance(position+1);
			case 2:
				return UserFragment.newInstance(position + 1);
			default:
				return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(GlobalVariable.ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab, container,
					false);
			return rootView;
		}
	}
	
	 private class Register extends AsyncTask<String, String, JSONObject> {
	    	@Override
			protected void onPreExecute() {
			}
	        @Override
	        protected JSONObject doInBackground(String... args) {
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
	        	 JSONObject jObj = null;
	        	if(!"".equals(prefs.getString("REGID", "")) || prefs.getString("REG_ID", "") != null)
	        	{
		        	     	//JSONObject jObj = null;
		            JSONParser json = new JSONParser();
		            params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("name", prefs.getString(GlobalVariable.SPF_ID, "")));
		            params.add(new BasicNameValuePair("mobno", prefs.getString(GlobalVariable.SPF_ID, "")));
		            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));
		            prefs.getString("REG_FROM","");
		            jObj = json.getJSONFromUrl("http://54.65.196.112:8000/login",params);
		            return  jObj;
	        	}
	            return jObj;
	        }
	        @Override
	        protected void onPostExecute(JSONObject json) {
	             try {
	            	 if(json != null){
		                 String res = json.getString("response");
		                 if(res.equals("Sucessfully Registered")) {
		                	 Toast.makeText(TabActivity.this,"Registered",Toast.LENGTH_SHORT).show();
		                 }else{
		                     Toast.makeText(TabActivity.this,res,Toast.LENGTH_SHORT).show();
		                 }
	                 	 SharedPreferences.Editor edit = prefs.edit();
	                      edit.putString("REG_FROM", prefs.getString(GlobalVariable.SPF_ID, ""));	// ������ ���Ⱑ mobno
	                      edit.putString("FROM_NAME", prefs.getString(GlobalVariable.SPF_ID, ""));
	                      edit.commit();
	                 }
	            	 else
	            		 Toast.makeText(TabActivity.this,"JSON NULL in ChatActivity, Register ",Toast.LENGTH_SHORT).show();
	             }catch (Exception e) {}
	        }
	    }

}
