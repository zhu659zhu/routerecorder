package com.gggl.routerecorder.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {  
	  
    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态  

     private static final String name = "RouteData"; //数据库名称  

     private static final int version = 1; //数据库版本  

     public DBHelper(Context context) {  

          //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类  

            super(context, name, null, version);  

     }  

    @Override  
    public void onCreate(SQLiteDatabase db) {  

          db.execSQL("CREATE TABLE IF NOT EXISTS mLocation (ID INTEGER primary key autoincrement,loctime TEXT,errorcode INTEGER, latitude TEXT, lontitude TEXT,radius TEXT,addr TEXT,describe TEXT,direction TEXT,uploadsign INTEGER,addtime TEXT)");     

     }  

    @Override   
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  

           //db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列  

     }  
}  
