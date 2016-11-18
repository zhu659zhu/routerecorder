package com.gggl.routerecorder;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.gggl.routerecorder.utils.DBHelper;

public class MainActivity extends Activity {

	//百度地图变量
	public LocationClient mLocationClient = null;
	public int span=10*1000;  //刷新间隔时间
	
	//下拉列表
	private static final String[] m={"GPS WIFI 网络精确定位","WIFI 网络定位","GPS离线定位"};
    private Spinner spinner,spinner2;
    private ArrayAdapter<String> adapter,adapter2;
    private static final String[] m2={"1秒","5秒","10秒","30秒","1分钟","5分钟","15分钟"};
	
    //数据库
    private DBHelper dbOpenHelper; 
    
    
    boolean lstart=false;
    boolean ltoast=true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.dbOpenHelper = new DBHelper(this);
		
	    mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( mListener );    //注册监听函数

        spinner = (Spinner) findViewById(R.id.Spinner01);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner.setVisibility(View.VISIBLE);
        spinner.setSelection(1, true);
        spinner2=(Spinner) findViewById(R.id.Spinner02);
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
        spinner2.setVisibility(View.VISIBLE);
        spinner2.setSelection(2, true);
        //timer.schedule(task, 5000, 5000);
        
        
        //Button b = (Button)findViewById(R.id.button1);
        //b.performClick();
        
        
	}
	
    //使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	LocationClientOption option = new LocationClientOption();
            switch (arg2)
            {
            	case 0: option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            		break;
            	case 1: option.setLocationMode(LocationMode.Battery_Saving);
            		break;
            	case 2: option.setLocationMode(LocationMode.Device_Sensors);
            		break;
            }
            option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
            
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        	mLocationClient.setLocOption(option);
        	Toast.makeText(MainActivity.this, "设置为"+m[arg2]+"模式...", Toast.LENGTH_SHORT).show();
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        	
        }
    }
	
    class SpinnerSelectedListener2 implements OnItemSelectedListener{
    	 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	
            switch (arg2)
            {
            	case 0: span=1000;
            		break;
            	case 1: span=5000;
            		break;
            	case 2: span=10000;
            		break;
            	case 3: span=30000;
        			break;	
            	case 4: span=60000;
        			break;
            	case 5: span=300000;
            		break;
            	case 6: span=900000;
            		break;
            }
            
        	Toast.makeText(MainActivity.this, "设置定位周期为"+m2[arg2]+"...", Toast.LENGTH_SHORT).show();
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        	
        }
    }
    
//    Timer timer = new Timer();
//    TimerTask task = new TimerTask() {
//
//        @Override
//        public void run() {
//            // 需要做的事
//        	mLocationClient.start();
//        }
//    };
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void testfunction(View v)
	{
		if(!lstart)
		{
			lstart = true;
			mLocationClient.start();
			Button b = (Button)findViewById(R.id.button1);
			b.setText("定位停止");
			Toast.makeText(MainActivity.this, "开始定位...", Toast.LENGTH_SHORT).show();
		}
		else
		{
			lstart = false;
			mLocationClient.stop();
			Button b = (Button)findViewById(R.id.button1);
			b.setText("定位开始");
			Toast.makeText(MainActivity.this, "停止定位...", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				String PoiStr = "";
				sb.append("time : ");
				/**
				 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				 */
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				sb.append("\nCountryCode : ");
				sb.append(location.getCountryCode());
				sb.append("\nCountry : ");
				sb.append(location.getCountry());
				sb.append("\ncitycode : ");
				sb.append(location.getCityCode());
				sb.append("\ncity : ");
				sb.append(location.getCity());
				sb.append("\nDistrict : ");
				sb.append(location.getDistrict());
				sb.append("\nStreet : ");
				sb.append(location.getStreet());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\nDescribe: ");
				sb.append(location.getLocationDescribe());
				sb.append("\nDirection(not all devices have value): ");
				sb.append(location.getDirection());
				sb.append("\nPoi: ");
				if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
					for (int i = 0; i < location.getPoiList().size(); i++) {
						Poi poi = (Poi) location.getPoiList().get(i);
						sb.append(poi.getName() + ";");
						PoiStr += poi.getName();
					}
				}
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());// 单位：km/h
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\nheight : ");
					sb.append(location.getAltitude());// 单位：米
					sb.append("\ndescribe : ");
					sb.append("gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// 运营商信息
					sb.append("\noperationers : ");
					sb.append(location.getOperators());
					sb.append("\ndescribe : ");
					sb.append("网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("\ndescribe : ");
					sb.append("离线定位成功，离线定位结果也是有效的");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("\ndescribe : ");
					sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("\ndescribe : ");
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("\ndescribe : ");
					sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
				TextView t = (TextView) findViewById(R.id.textView1);
				t.setText(sb.toString());
				WriteLoc(location.getTime(),location.getLocType(),location.getLatitude(),location.getLongitude(),location.getRadius(),location.getAddrStr(),location.getLocationDescribe(),PoiStr,0);
				//logMsg(sb.toString());
			}
		}

	};
	
	public void WriteLoc(String dt,int ec,double la,double lo,float ra,String ad,String de,String di,int lt)
	{
		
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase(); 
        Cursor cursor = db.rawQuery("select * from mLocation where loctime = '"+dt+"'", null);
        if(cursor.getCount()==0)
        {
        	db.execSQL("insert into mLocation(loctime,errorcode, latitude, lontitude,radius,addr,describe,direction,uploadsign,addtime) values(?,?,?,?,?,?,?,?,?,?)",new Object[]{dt,ec,la,lo,ra,ad,de,di,lt,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())});
        	if(ltoast)
        		Toast.makeText(MainActivity.this, "保存成功...", 500).show();
        }
        else
        {
        	if(ltoast)
        		Toast.makeText(MainActivity.this, "位置无变化...", 500).show();
        }
        db.close(); 
        
        //mLocationClient.stop();
        
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			ltoast=!ltoast;
			if(ltoast)
				Toast.makeText(MainActivity.this, "开启提示...", 500).show();
			else
				Toast.makeText(MainActivity.this, "关闭提示...", 500).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}

