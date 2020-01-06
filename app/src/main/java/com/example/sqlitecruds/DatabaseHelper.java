package com.example.sqlitecruds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database
    private static final String DB_NAME = "User.db";
//    private static final String DB_TABLE = "Users_table";
    private static final String DB_TABLE = "USERDB";

    //colums
    private static final String ID = "ID";
    private static final String NAME = "NAME";

    //QUERY
    private static final String CREATE_TABLE = "CREATE TABLE "+DB_TABLE +"("+ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME +" TEXT NOT NULL )";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    //CREATE A METHOD TO INESRT DATA
    public boolean insertData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);

        long result = db.insert(DB_TABLE, null, values);
        return result != -1; //if result = -1 data doest not insert
    }

    //METHOD FOR VIEW DATA
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //METHOD FOR SEARCH
    public Cursor searchUsers(String text){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " +DB_TABLE+ "WHERE" +NAME+ "Like '%"+text+"%'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }
    public ArrayList<String> searchUsersstring(String text){
        ArrayList<String> listitem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " +DB_TABLE+ " WHERE " +NAME+ " Like '%"+text+"%'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0){
            listitem.add("not found");
        } else {
            while (cursor.moveToNext()){
                listitem.add(cursor.getString(1)); //index 1 is name, index 0 is id
            }

        }
        return listitem;

    }

}
