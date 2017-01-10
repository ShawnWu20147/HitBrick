package com.nju.hb;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class EntryActivity extends Activity {

	Button start, end;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_entry);
		
		
		start = (Button)findViewById(R.id.start);
		
		start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(EntryActivity.this, GameActivity.class);
				startActivity(it);
				
			}
		});
		
		end = (Button)findViewById(R.id.end);
		
		end.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EntryActivity.this.finish();
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
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
	
	
	/*
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(EntryActivity.this)   
		.setTitle("确定要退出游戏吗")  
		.setMessage("请选择")  
		.setPositiveButton("否", new DialogInterface.OnClickListener(){ 
            
			@Override 
            public void onClick(DialogInterface dialog, int which) {
            	
				
				
            }
		})  
		.setNegativeButton("是", new DialogInterface.OnClickListener(){ 
            @Override 
            public void onClick(DialogInterface dialog, int which) {
            	setClose();
            }
            })  
		.show();  
	}
	*/
	
	public void setClose(){
		super.onBackPressed();
	}
	
}
