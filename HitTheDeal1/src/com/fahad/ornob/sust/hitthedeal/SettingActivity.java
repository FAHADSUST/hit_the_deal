package com.fahad.ornob.sust.hitthedeal;

import com.fahad.ornob.sust.hitthedeal.contants.Constants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingActivity extends Activity {

	ToggleButton serviceOnOff;
	SeekBar distSeekBar;
	TextView distTxt;

	int high = 6;
	int low = 0;
	String distanceSt[] = { "1", "1.5", "2", "2.5", "3", "3.5", "4" };
	int progress = 1;

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

	private void listenerToggle() {
		// TODO Auto-generated method stub
		//serviceOnOff.setOnCheckedChangeListener(new View.)
	}

	private void listenerSeekBar() {
		// TODO Auto-generated method stub
		for (int i = 0; i < distanceSt.length; i++) {
			if (distanceSt[i].endsWith(String.valueOf(Constants.Distance))) {
				progress = Integer.parseInt(distanceSt[i]);
			}
		}
		
		distTxt.setText(String.valueOf(progress));
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
		editor.putString("name", "Elena");
		editor.putInt("idName", 12);
		editor.commit();
	}
	
	public  float getValueSharedPref(String key){
		SharedPreferences prefs = getSharedPreferences("my_fref", MODE_PRIVATE); 
		Float restoredText = prefs.getFloat(key, 2);
		if (restoredText != null) {
		  String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
		  int idName = prefs.getInt("idName", 0); //0 is the default value.
		}
		return restoredText;
	}
}
