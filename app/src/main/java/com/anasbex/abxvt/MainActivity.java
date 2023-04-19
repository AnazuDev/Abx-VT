package com.anasbex.abxvt;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	public final int REQ_CD_PICKAVT = 101;
	
	private Timer _timer = new Timer();
	
	private String AVATAR_IMG = "";
	private double num = 0;
	private String CAP = "";
	
	private ArrayList<String> ListImgAvt = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linearBase;
	private LinearLayout linearBaseLogo;
	private LinearLayout linearOption;
	private LinearLayout linear10;
	private ImageView imageview1;
	private TextView textVersion;
	private LinearLayout linear7;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private LinearLayout linear12;
	private Button pickimg;
	private LinearLayout linear8;
	private LinearLayout linear9;
	private TextView textTools;
	private Switch switchRemove;
	private Button OnMain;
	private Button OffMain;
	private LinearLayout linear11;
	private TextView textCaption;
	private TextView textNote;
	
	private Intent in = new Intent();
	private Intent pickAvt = new Intent(Intent.ACTION_GET_CONTENT);
	private SharedPreferences DB;
	private TimerTask time;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		vscroll1 = findViewById(R.id.vscroll1);
		linearBase = findViewById(R.id.linearBase);
		linearBaseLogo = findViewById(R.id.linearBaseLogo);
		linearOption = findViewById(R.id.linearOption);
		linear10 = findViewById(R.id.linear10);
		imageview1 = findViewById(R.id.imageview1);
		textVersion = findViewById(R.id.textVersion);
		linear7 = findViewById(R.id.linear7);
		linear5 = findViewById(R.id.linear5);
		linear6 = findViewById(R.id.linear6);
		linear12 = findViewById(R.id.linear12);
		pickimg = findViewById(R.id.pickimg);
		linear8 = findViewById(R.id.linear8);
		linear9 = findViewById(R.id.linear9);
		textTools = findViewById(R.id.textTools);
		switchRemove = findViewById(R.id.switchRemove);
		OnMain = findViewById(R.id.OnMain);
		OffMain = findViewById(R.id.OffMain);
		linear11 = findViewById(R.id.linear11);
		textCaption = findViewById(R.id.textCaption);
		textNote = findViewById(R.id.textNote);
		pickAvt.setType("image/*");
		pickAvt.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		DB = getSharedPreferences("DBTOOLS", Activity.MODE_PRIVATE);
		
		pickimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(pickAvt, REQ_CD_PICKAVT);
			}
		});
		
		switchRemove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					switchRemove.setText("RemoveBGOn");
					DB.edit().putString("bg", "on").commit();
					num++;
				}
				else {
					switchRemove.setText("RemoveBG");
					DB.edit().putString("bg", "off").commit();
					num--;
				}
			}
		});
		
		OnMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				OnMain.setVisibility(View.GONE);
				OffMain.setVisibility(View.VISIBLE);
				int LAYOUT_FLAG;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						    LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
				} else {
						    LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
				}
				
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				    WindowManager.LayoutParams.WRAP_CONTENT,
				    WindowManager.LayoutParams.WRAP_CONTENT,
				    LAYOUT_FLAG,
				      
				    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL 
				     ,
				
				    PixelFormat.TRANSLUCENT);
				params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				
				  final  WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
				    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				  
				
				final View myView = (View) getLayoutInflater().inflate(R.layout.view_composer, null); 
				
				
				final LinearLayout baseBg = (LinearLayout)myView.findViewById(R.id.baseBg);
				if (DB.getString("bg", "").equals("on")) {
					baseBg.setBackgroundColor(Color.TRANSPARENT);
				}
				else {
					if (DB.getString("bg", "").equals("off")) {
						baseBg.setBackgroundColor(0xFF4CAF50);
					}
					else {
						baseBg.setBackgroundColor(0xFF4CAF50);
					}
				}
				final ImageView img1 = (ImageView)myView.findViewById(R.id.img1);
				img1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(AVATAR_IMG, 1024, 1024));
				img1.setOnTouchListener(new OnTouchListener() {
						
						private int x;
						private int y;
						
						       @Override
						       public boolean onTouch(View v, MotionEvent event) {
								
								switch (event.getAction()) { 
										case MotionEvent.ACTION_DOWN: 
										x = (int) event.getRawX(); 
										y = (int) event.getRawY(); 
										break;
										case MotionEvent.ACTION_MOVE:
										int nowX = (int) event.getRawX(); 
										int nowY = (int) event.getRawY(); 
										int movedX = nowX - x; 
										int movedY = nowY - y; 
										x = nowX;
										y = nowY; 
										params.x = params.x + movedX; 
										params.y = params.y + movedY;
										wm.updateViewLayout(myView, params); 
										break;
										default:
										break;
								}
								 return true;
								 }
						 });
					 params.gravity = Gravity.TOP | Gravity.LEFT;
				       params.x = 0;
				       params.y = 0;
				if (android.provider.Settings.canDrawOverlays(MainActivity.this)) {
						wm.addView(myView, params);
				} else {
						Intent in = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + getPackageName()));
						startActivity(in);
				}
				
			}
		});
		
		OffMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				OffMain.setVisibility(View.GONE);
				OnMain.setVisibility(View.VISIBLE);
				int LAYOUT_FLAG;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						    LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
				} else {
						    LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
				}
				
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				    WindowManager.LayoutParams.WRAP_CONTENT,
				    WindowManager.LayoutParams.WRAP_CONTENT,
				    LAYOUT_FLAG,
				      
				    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL 
				     ,
				
				    PixelFormat.TRANSLUCENT);
				params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				
				  final  WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
				    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				  
				
				final View myView = (View) getLayoutInflater().inflate(R.layout.view_composer, null); 
				
				
				final LinearLayout baseBg = (LinearLayout)myView.findViewById(R.id.baseBg);
				if (DB.getString("bg", "").equals("on")) {
					baseBg.setBackgroundColor(Color.TRANSPARENT);
				}
				else {
					if (DB.getString("bg", "").equals("off")) {
						baseBg.setBackgroundColor(0xFF4CAF50);
					}
					else {
						baseBg.setBackgroundColor(0xFF4CAF50);
					}
				}
				final ImageView img1 = (ImageView)myView.findViewById(R.id.img1);
				img1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(AVATAR_IMG, 1024, 1024));
				img1.setOnTouchListener(new OnTouchListener() {
						
						private int x;
						private int y;
						
						       @Override
						       public boolean onTouch(View v, MotionEvent event) {
								
								switch (event.getAction()) { 
										case MotionEvent.ACTION_DOWN: 
										x = (int) event.getRawX(); 
										y = (int) event.getRawY(); 
										break;
										case MotionEvent.ACTION_MOVE:
										int nowX = (int) event.getRawX(); 
										int nowY = (int) event.getRawY(); 
										int movedX = nowX - x; 
										int movedY = nowY - y; 
										x = nowX;
										y = nowY; 
										params.x = params.x + movedX; 
										params.y = params.y + movedY;
										wm.updateViewLayout(myView, params); 
										break;
										default:
										break;
								}
								 return true;
								 }
						 });
					 params.gravity = Gravity.TOP | Gravity.LEFT;
				       params.x = 0;
				       params.y = 0;
				if (android.provider.Settings.canDrawOverlays(MainActivity.this)) {
						wm.addView(myView, params);
				} else {
						Intent in = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + getPackageName()));
						startActivity(in);
				}
				
			}
		});
	}
	
	private void initializeLogic() {
		OffMain.setVisibility(View.GONE);
		OnMain.setVisibility(View.GONE);
		_create();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICKAVT:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				AVATAR_IMG = _filePath.get((int)(0));
				if (AVATAR_IMG.contains("png")) {
					OnMain.setVisibility(View.VISIBLE);
				}
				else {
					OnMain.setVisibility(View.GONE);
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	public void _create() {
		CAP = "Tujuan ABX-VT Menjalankan Objek Mengambang Di Depan Home Ponsel\nDan Juga bisa Di Tempat Tertentu Seperti Anda sedang memainkan game,\nDisitu akan terdapat APNG Animation PNG\nDan Anda juga bisa menjalankan nya Di Live YouTube, Virtual YouTuber,VTuber\n\nAplikasi ini dibuat oleh Anas Bex Development\n©2023 - AnasBex Development";
		textCaption.setText(CAP);
		_style_ui();
		_db_me();
	}
	
	
	public void _style_ui() {
		pickimg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xFF222430));
		OnMain.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xFF222430));
		OffMain.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)50, 0xFF222430));
		pickimg.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		OnMain.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		OffMain.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		textNote.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/roboto_nom.ttf"), 0);
		textCaption.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
	}
	
	
	public void _db_me() {
		if (DB.getString("bg", "").equals("on")) {
			switchRemove.setChecked(true);
		}
		else {
			if (DB.getString("bg", "").equals("off")) {
				switchRemove.setChecked(false);
			}
			else {
				switchRemove.setChecked(false);
			}
		}
	}
	
	
	public void _Titilium_Font_(final TextView _textView) {
		_textView.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/titilium_gonime.ttf"), 0);
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