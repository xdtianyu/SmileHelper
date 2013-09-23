package org.xdty.smilehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smile_helper";
    private static final String TB_NAME = "app_list";
    private static final String COLUMNS_ID = "id";
    private static final String COLUMNS_INDEX = "indexed";
    private static final String COLUMNS_NAME = "name";
    private static final String COLUMNS_CHECK = "checked";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_NAME + " (" + COLUMNS_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNS_INDEX
                + " INTEGER," +COLUMNS_NAME+ " TEXT," +COLUMNS_CHECK
                + " BOOLEAN)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
        onCreate(db);
    }
    /**
     * 返回所有数据
     * @return
     */
    public Cursor selectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, null, null,null, null, "id desc");
        return cursor;
    }
    /**
     * 根据条件查询
     * @param columnsName
     * @return
     */
    public Cursor selectForChecked(boolean isChecked) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = COLUMNS_CHECK+"=?";
        int check = 0;
        if(isChecked){
         check = 1;
        }
        String[] whereValue={Integer.toString(check)};
        Cursor cursor = db.query(TB_NAME, null, where, whereValue,null, null, "id desc");
        return cursor;
    }
    
    public long insert(int index,String name,boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS_INDEX, index);
        cv.put(COLUMNS_NAME, name);
        cv.put(COLUMNS_CHECK, checked);
        long row = db.insert(TB_NAME, null, cv);
        db.close();
        return row;
        
    }
    
    public void delete(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where=COLUMNS_INDEX+"=?";
        String[] whereValue = {Integer.toString(index)};
        db.delete(TB_NAME, where, whereValue);
    }
    
    public void update(int index,boolean check) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where=COLUMNS_INDEX+"=?";
        String[] whereValue = {Integer.toString(index)};
        ContentValues cv = new ContentValues(); 
        cv.put(COLUMNS_CHECK, check);
        db.update(TB_NAME, cv, where, whereValue);
    }

}