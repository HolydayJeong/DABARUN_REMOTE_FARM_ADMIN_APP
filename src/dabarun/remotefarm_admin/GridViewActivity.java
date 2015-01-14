package dabarun.remotefarm_admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GridViewActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_farm_view);
		
		Button[] g1_button = null;
		g1_button[1] = (Button)findViewById(R.id.Grid1_button1);
		g1_button[2] = (Button)findViewById(R.id.Grid1_button2);
		g1_button[3] = (Button)findViewById(R.id.Grid1_button3);
		g1_button[4] = (Button)findViewById(R.id.Grid1_button4);
		
		
		
		findViewById(R.id.Grid1_button1).setOnClickListener(cl);
		findViewById(R.id.Grid1_button2).setOnClickListener(cl);
		findViewById(R.id.Grid1_button3).setOnClickListener(cl);
		findViewById(R.id.Grid1_button4).setOnClickListener(cl);
	}
	OnClickListener cl = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.Grid1_button1:
				break;
			case R.id.Grid1_button2:
				break;
			case R.id.Grid1_button3:
				break;
			case R.id.Grid1_button4:
				break;
			}
			 
			
		}
	};
}
