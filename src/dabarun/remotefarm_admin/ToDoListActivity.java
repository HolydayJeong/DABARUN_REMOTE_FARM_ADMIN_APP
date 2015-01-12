package dabarun.remotefarm_admin;

import android.support.v7.appcompat.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class ToDoListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar 
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

}
