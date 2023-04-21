package com.anasbex.abxvt;

import android.Manifest;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.munon.turboimageview.*;
import com.zolad.zoominimageview.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class AbxEngineActivity extends Activity {
	
	private LinearLayout linearBg;
	private LinearLayout linear;
	private ScrollView vscroll1;
	private LinearLayout target;
	private ImageView imageview1;
	private LinearLayout linear2;
	private LinearLayout linear8;
	private LinearLayout linear5;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private LinearLayout linear9;
	private TextView textT1;
	private TextView w;
	private TextView textview3;
	private TextView h;
	private TextView textview1;
	private SeekBar height_sb;
	private TextView textview2;
	private SeekBar weight_sb;
	private TextView textT2;
	private ImageView imageview2;
	
	private SharedPreferences DB;
	private ObjectAnimator anim = new ObjectAnimator();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.abx_engine);
		initialize(_savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linearBg = findViewById(R.id.linearBg);
		linear = findViewById(R.id.linear);
		vscroll1 = findViewById(R.id.vscroll1);
		target = findViewById(R.id.target);
		imageview1 = findViewById(R.id.imageview1);
		linear2 = findViewById(R.id.linear2);
		linear8 = findViewById(R.id.linear8);
		linear5 = findViewById(R.id.linear5);
		linear3 = findViewById(R.id.linear3);
		linear4 = findViewById(R.id.linear4);
		linear9 = findViewById(R.id.linear9);
		textT1 = findViewById(R.id.textT1);
		w = findViewById(R.id.w);
		textview3 = findViewById(R.id.textview3);
		h = findViewById(R.id.h);
		textview1 = findViewById(R.id.textview1);
		height_sb = findViewById(R.id.height_sb);
		textview2 = findViewById(R.id.textview2);
		weight_sb = findViewById(R.id.weight_sb);
		textT2 = findViewById(R.id.textT2);
		imageview2 = findViewById(R.id.imageview2);
		DB = getSharedPreferences("DBTOOLS", Activity.MODE_PRIVATE);
		
		textview1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				
				return true;
			}
		});
		
		height_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				target.setLayoutParams(new LinearLayout.LayoutParams ((int) getDip(weight_sb.getProgress()) , (int)getDip(height_sb.getProgress())));
				textview1.setText("Height ".concat("+   ".concat(String.valueOf((long)(_progressValue)).concat("dp"))));
				h.setText(String.valueOf((long)(_progressValue)));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		weight_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				target.setLayoutParams(new LinearLayout.LayoutParams ((int) getDip(weight_sb.getProgress()) , (int)getDip(height_sb.getProgress())));
				textview2.setText("Weight ".concat("+   ".concat(String.valueOf((long)(_progressValue)).concat("dp"))));
				w.setText(String.valueOf((long)(_progressValue)));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
	}
	
	private void initializeLogic() {
		imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(DB.getString("ava", ""), 1024, 1024));
		linear.post(new Runnable() { 
			@Override public void run() {
				float density = AbxEngineActivity.this.getResources().getDisplayMetrics().density; 
				height_sb.setMax((int)Math.round(linear.getHeight() / density));
				int maxProgress = Math.round(linear.getWidth() / density); 
				height_sb.setMax((int)maxProgress);
				weight_sb.setMax((int)maxProgress);
			} });
		_extra();
	}
	
	public void _extra() {
		textT1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		textT2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
		w.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
		h.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}