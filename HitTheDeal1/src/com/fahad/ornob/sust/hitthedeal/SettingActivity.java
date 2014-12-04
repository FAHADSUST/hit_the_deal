package com.fahad.ornob.sust.hitthedeal;

import com.fahad.ornob.sust.hitthedeal.contants.Constants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends Activity {

	ToggleButton serviceOnOff;
	SeekBar distSeekBar;
	TextView distTxt;

	int high = 6;
	int low = 0;
	String distanceSt[] = { "1", "1.5", "2", "2.5", "3", "3.5", "4" };
	int progress = 1;
	public SettingActivity(){		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_viwer);
		serviceOnOff = (ToggleButton) findViewById(R.id.serviceOnOffTogB);
		distSeekBar = (SeekBar) findViewById(R.id.disSeekBar1);
		distTxt = (TextView) findViewById(R.id.disTxt);

		listenerSeekBar();
		listenerToggle();
		

	}
	boolean click=false;
	private void listenerToggle() {
		// TODO Auto-generated method stub
		serviceOnOff.setOnClickListener(new View.OnClickListener() {                       //toggle button for date and time picker
		
		@Override
		public void onClick(View view) {
			if (serviceOnOff.isChecked()) {
				click=true;
									
				Toast.makeText(SettingActivity.this, "ON", Toast.LENGTH_SHORT).show();
			}
			 else{
									                                      //if toggle is not checked cancel the pending intent and alarm
				Toast.makeText(SettingActivity.this, "Off", Toast.LENGTH_SHORT).show();
			}
				
		}
	});
	}

	private void listenerSeekBar() {
		// TODO Auto-generated method stub
		
		progress=(int)(getValueSharedPref(Constants.distKey)*2) - 2;
		distTxt.setText(distanceSt[progress]);
		distSeekBar.setMax(high - low);
		distSeekBar.setProgress(progress);

		distSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			int stepSize = 1;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {

				progress = progresValue;
				progress = ((int) Math.round(progress / stepSize)) * stepSize;
				seekBar.setProgress(progress);

				int progModValue = progress + low;
				distTxt.setText(distanceSt[progModValue]);
				Constants.Distance=Double.parseDouble(distanceSt[progModValue]);
				setdataIntoShaeredPref(Constants.distKey,Double.parseDouble(distanceSt[progModValue]));

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}

	public   void setdataIntoShaeredPref(String key,double value){
		SharedPreferences.Editor editor = getSharedPreferences("my_fref", MODE_PRIVATE).edit();
		
		editor.putFloat(key, (float)value);
		editor.commit();
	}
	
	public  float getValueSharedPref(String key){
		SharedPreferences prefs = getSharedPreferences("my_fref", MODE_PRIVATE); 
		Float restoredText = prefs.getFloat(key, 2);
		
		return restoredText;
	}
}
