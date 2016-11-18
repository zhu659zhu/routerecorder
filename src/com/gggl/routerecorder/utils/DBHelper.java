package com.gggl.routerecorder.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {  
	  
    //��û��ʵ����,�ǲ����������๹�����Ĳ���,��������Ϊ��̬  

     private static final String name = "RouteData"; //���ݿ�����  

     private static final int version = 1; //���ݿ�汾  

     public DBHelper(Context context) {  

          //����������CursorFactoryָ����ִ�в�ѯʱ���һ���α�ʵ���Ĺ�����,����Ϊnull,����ʹ��ϵͳĬ�ϵĹ�����  

            super(context, name, null, version);  

     }  

    @Override  
    public void onCreate(SQLiteDatabase db) {  

          db.execSQL("CREATE TABLE IF NOT EXISTS mLocation (ID INTEGER primary key autoincrement,loctime TEXT,errorcode INTEGER, latitude TEXT, lontitude TEXT,radius TEXT,addr TEXT,describe TEXT,direction TEXT,uploadsign INTEGER,addtime TEXT)");     

     }  

    @Override   
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  

           //db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //����������һ��  

     }  
}  
