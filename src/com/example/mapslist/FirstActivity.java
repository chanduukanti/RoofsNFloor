package com.example.mapslist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends Activity{

	Button viewProjects,viewProjectsonMap,tabView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar ac = getActionBar();
		ac.setTitle("RoofsAndFloors.com");
		ac.setDisplayShowHomeEnabled(true);
		setContentView(R.layout.first_activity);
		viewProjects = (Button)findViewById(R.id.viewProjects);
		viewProjectsonMap = (Button)findViewById(R.id.viewProjectsonMap);
		tabView = (Button)findViewById(R.id.tabsView);
		if(viewProjects != null) {
			viewProjects.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent in = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(in);
				}
			});
		}
	      if(tabView != null) {
	          tabView.setOnClickListener(new View.OnClickListener() {            
	                @Override
	                public void onClick(View v) {
	                    // TODO Auto-generated method stub
	                    Intent in = new Intent(getApplicationContext(), RoofnFloorsActivity.class);
	                    startActivity(in);
	                }
	            });
	        }
	      if(viewProjectsonMap != null) {
	          viewProjectsonMap.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    // TODO Auto-generated method stub
	                    Intent in = new Intent(getApplicationContext(), MapView.class);
	                    startActivity(in);
	                }
	            });
	        }
		
	}	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
