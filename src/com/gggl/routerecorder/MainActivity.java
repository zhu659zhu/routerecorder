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

	//�ٶȵ�ͼ����
	public LocationClient mLocationClient = null;
	public int span=10*1000;  //ˢ�¼��ʱ��
	
	//�����б�
	private static final String[] m={"GPS WIFI ���羫ȷ��λ","WIFI ���綨λ","GPS���߶�λ"};
    private Spinner spinner,spinner2;
    private ArrayAdapter<String> adapter,adapter2;
    private static final String[] m2={"1��","5��","10��","30��","1����","5����","15����"};
	
    //���ݿ�
    private DBHelper dbOpenHelper; 
    
    
    boolean lstart=false;
    boolean ltoast=true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.dbOpenHelper = new DBHelper(this);
		
	    mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
	    mLocationClient.registerLocationListener( mListener );    //ע���������

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
	
    //ʹ��������ʽ����
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	LocationClientOption option = new LocationClientOption();
            switch (arg2)
            {
            	case 0: option.setLocationMode(LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
            		break;
            	case 1: option.setLocationMode(LocationMode.Battery_Saving);
            		break;
            	case 2: option.setLocationMode(LocationMode.Device_Sensors);
            		break;
            }
            option.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
            
            option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
            option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
            option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
            option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
            option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
            option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
            option.setIgnoreKillProcess(false);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��  
            option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
            option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
        	mLocationClient.setLocOption(option);
        	Toast.makeText(MainActivity.this, "����Ϊ"+m[arg2]+"ģʽ...", Toast.LENGTH_SHORT).show();
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
            
        	Toast.makeText(MainActivity.this, "���ö�λ����Ϊ"+m2[arg2]+"...", Toast.LENGTH_SHORT).show();
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        	
        }
    }
    
//    Timer timer = new Timer();
//    TimerTask task = new TimerTask() {
//
//        @Override
//        public void run() {
//            // ��Ҫ������
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
			b.setText("��λֹͣ");
			Toast.makeText(MainActivity.this, "��ʼ��λ...", Toast.LENGTH_SHORT).show();
		}
		else
		{
			lstart = false;
			mLocationClient.stop();
			Button b = (Button)findViewById(R.id.button1);
			b.setText("��λ��ʼ");
			Toast.makeText(MainActivity.this, "ֹͣ��λ...", Toast.LENGTH_SHORT).show();
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
				 * ʱ��Ҳ����ʹ��systemClock.elapsedRealtime()���� ��ȡ�����Դӿ���������ÿ�λص���ʱ�䣻
				 * location.getTime() ��ָ����˳����ν����ʱ�䣬���λ�ò������仯����ʱ�䲻��
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
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());// ��λ��km/h
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\nheight : ");
					sb.append(location.getAltitude());// ��λ����
					sb.append("\ndescribe : ");
					sb.append("gps��λ�ɹ�");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
					// ��Ӫ����Ϣ
					sb.append("\noperationers : ");
					sb.append(location.getOperators());
					sb.append("\ndescribe : ");
					sb.append("���綨λ�ɹ�");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
					sb.append("\ndescribe : ");
					sb.append("���߶�λ�ɹ������߶�λ���Ҳ����Ч��");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("\ndescribe : ");
					sb.append("��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("\ndescribe : ");
					sb.append("���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("\ndescribe : ");
					sb.append("�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�");
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
        		Toast.makeText(MainActivity.this, "����ɹ�...", 500).show();
        }
        else
        {
        	if(ltoast)
        		Toast.makeText(MainActivity.this, "λ���ޱ仯...", 500).show();
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
				Toast.makeText(MainActivity.this, "������ʾ...", 500).show();
			else
				Toast.makeText(MainActivity.this, "�ر���ʾ...", 500).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}

