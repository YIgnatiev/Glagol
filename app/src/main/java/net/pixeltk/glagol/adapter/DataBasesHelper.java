package net.pixeltk.glagol.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 24.03.16.
 */
public class DataBasesHelper extends SQLiteOpenHelper {

    Context context;

    private final String TAG = "DatabaseHelperClass";
    private static final int databaseVersion = 1;
    private static final String databaseName = "MyBooks";
    private static final String TABLE_BOOKMARKS = "Bookmarks";
    private static final String TABLE_HISTORY = "History";
    private static final String TABLE_DOWNLOAD = "Download";
    private static final String TABLE_BUY = "Buy";
    private static final String TABLE_LISTEN = "Listen";
    private static final String TABLE_BACK_PRESSED = "BackPressed";

    private static final String ID = "id";
    private static final String NAME_AUTHOR = "name_author";
    private static final String BOOK_NAME = "name_book";
    private static final String NAME_READER = "name_reader";
    private static final String PRICE = "price";
    private static final String IMG_URL = "img_url";
    private static final String ID_BOOK = "id_book";
    private static final String CURRENTPOSITION = "current_position";
    private static final String SEEKBAR_VALUE = "seekbar_value";
    private static final String TOTAL_DURATION = "total_duration";
    private static final String NOW_LISTENING = "now_listening";
    private static final String CLASS_NAME = "class_name";
    private static final String SOME_INFO = "some_info";


    ArrayList IdList = new ArrayList();

    private static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE " + TABLE_BOOKMARKS + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + NAME_AUTHOR + " TEXT, "
            + BOOK_NAME + " TEXT, "
            + NAME_READER + " TEXT, "
            + PRICE + " TEXT, "
            + IMG_URL + " TEXT, "
            + ID_BOOK + " TEXT) ";

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + NAME_AUTHOR + " TEXT, "
            + BOOK_NAME + " TEXT, "
            + NAME_READER + " TEXT, "
            + PRICE + " TEXT, "
            + IMG_URL + " TEXT, "
            + ID_BOOK + " TEXT) ";

    private static final String CREATE_TABLE_DOWNLOAD = "CREATE TABLE " + TABLE_DOWNLOAD + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + ID_BOOK + " TEXT) ";

    private static final String CREATE_BACK_PRESSED = "CREATE TABLE " + TABLE_BACK_PRESSED + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + CLASS_NAME + " TEXT ,"
            + SOME_INFO + " TEXT) ";

    private static final String CREATE_TABLE_BUY = "CREATE TABLE " + TABLE_BUY + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + NAME_AUTHOR + " TEXT, "
            + BOOK_NAME + " TEXT, "
            + NAME_READER + " TEXT, "
            + PRICE + " TEXT, "
            + IMG_URL + " TEXT, "
            + ID_BOOK + " TEXT) ";

    private static final String CREATE_TABLE_LISTEN = "CREATE TABLE " + TABLE_LISTEN + "("
            + ID + " INTEGER PRIMARY KEY ,"
            + NAME_AUTHOR + " TEXT, "
            + BOOK_NAME + " TEXT, "
            + NAME_READER + " TEXT, "
            + IMG_URL + " TEXT, "
            + CURRENTPOSITION + " TEXT, "
            + SEEKBAR_VALUE + " TEXT, "
            + TOTAL_DURATION + " TEXT, "
            + NOW_LISTENING + " TEXT, "
            + ID_BOOK + " TEXT) ";



    public DataBasesHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOKMARKS);
        sqLiteDatabase.execSQL(CREATE_TABLE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_DOWNLOAD);
        sqLiteDatabase.execSQL(CREATE_BACK_PRESSED);
        sqLiteDatabase.execSQL(CREATE_TABLE_BUY);
        sqLiteDatabase.execSQL(CREATE_TABLE_LISTEN);


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BACK_PRESSED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BUY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTEN);

        onCreate(sqLiteDatabase);
    }

    public void insertBookMarks(String name_author, String book_name, String name_reader, String price, String img_url, String id_book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put(NAME_AUTHOR, name_author);
            values.put(BOOK_NAME, book_name);
            values.put(NAME_READER, name_reader);
            values.put(PRICE, price);
            values.put(IMG_URL, img_url);
            values.put(ID_BOOK, id_book);
            db.insert(TABLE_BOOKMARKS, null, values);
        db.close();
    }
    public void insertListen(String name_author, String book_name, String name_reader, String img_url, String current_position, String seekbar_value, String total_duration, String now_listening, String id_book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_AUTHOR, name_author);
        values.put(BOOK_NAME, book_name);
        values.put(NAME_READER, name_reader);
        values.put(IMG_URL, img_url);
        values.put(CURRENTPOSITION, current_position);
        values.put(SEEKBAR_VALUE, seekbar_value);
        values.put(TOTAL_DURATION, total_duration);
        values.put(NOW_LISTENING, now_listening);
        values.put(ID_BOOK, id_book);
        db.insert(TABLE_LISTEN, null, values);
        db.close();
    }

    public void insertBuy(String name_author, String book_name, String name_reader, String price, String img_url, String id_book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_AUTHOR, name_author);
        values.put(BOOK_NAME, book_name);
        values.put(NAME_READER, name_reader);
        values.put(PRICE, price);
        values.put(IMG_URL, img_url);
        values.put(ID_BOOK, id_book);
        db.insert(TABLE_BUY, null, values);
        db.close();
    }


    public void insertDownload(String id_book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_BOOK, id_book);
        db.insert(TABLE_DOWNLOAD, null, values);
        db.close();
    }
    public void insertBackPressed(String class_name, String some_info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME, class_name);
        values.put(SOME_INFO, some_info);
        db.insert(TABLE_BACK_PRESSED, null, values);
        db.close();
    }



    public void insertHistory(String name_author, String book_name, String name_reader, String price, String img_url, String id_book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_AUTHOR, name_author);
        values.put(BOOK_NAME, book_name);
        values.put(NAME_READER, name_reader);
        values.put(PRICE, price);
        values.put(IMG_URL, img_url);
        values.put(ID_BOOK, id_book);
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public ArrayList getidRow() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_BOOKMARKS, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{
                Log.d("MyLog", String.valueOf(c));
                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }
    public ArrayList getIdHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_HISTORY, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{

                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }

    public ArrayList getIdListen() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_LISTEN, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{

                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }

    public ArrayList getIdDownload() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_DOWNLOAD, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{

                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }

    public ArrayList getIdBackPressed() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_BACK_PRESSED, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{

                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }

    public ArrayList getIdBuy() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_BUY, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{

                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }




    public BookMarksHelper getProduct(String productid, String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.query(TABLE_NAME,
                new String[] {ID, NAME_AUTHOR, BOOK_NAME, NAME_READER, PRICE, IMG_URL, ID_BOOK},ID
                        +" LIKE '"+productid+"%'", null, null, null, null);

        BookMarksHelper bookMarksHelper = new BookMarksHelper();
        if (cursor2.moveToFirst()) {
            do {
                bookMarksHelper.setName_author(cursor2.getString(1));
                bookMarksHelper.setName_book(cursor2.getString(2));
                bookMarksHelper.setName_reader(cursor2.getString(3));
                bookMarksHelper.setPrice(cursor2.getString(4));
                bookMarksHelper.setImg_url(cursor2.getString(5));
                bookMarksHelper.setId_book(cursor2.getString(6));

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return bookMarksHelper;
    }
    public BookMarksHelper getProductListen(String productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.query(TABLE_LISTEN,
                new String[] {ID, NAME_AUTHOR, BOOK_NAME, NAME_READER, IMG_URL, CURRENTPOSITION, SEEKBAR_VALUE, TOTAL_DURATION, NOW_LISTENING, ID_BOOK},ID
                        +" LIKE '"+productid+"%'", null, null, null, null);

        BookMarksHelper bookMarksHelper = new BookMarksHelper();
        if (cursor2.moveToFirst()) {
            do {
                bookMarksHelper.setName_author(cursor2.getString(1));
                bookMarksHelper.setName_book(cursor2.getString(2));
                bookMarksHelper.setName_reader(cursor2.getString(3));
                bookMarksHelper.setImg_url(cursor2.getString(4));
                bookMarksHelper.setCurrent_position(cursor2.getString(5));
                bookMarksHelper.setSeekbar_value(cursor2.getString(6));
                bookMarksHelper.setTotal_duration(cursor2.getString(7));
                bookMarksHelper.setNow_listening(cursor2.getString(8));
                bookMarksHelper.setId_book(cursor2.getString(9));

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return bookMarksHelper;
    }
    public void updatedetails(String id_book, String now_listen, String current_position, String seekbar_value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(NOW_LISTENING, now_listen);
        args.put(CURRENTPOSITION, current_position);
        args.put(SEEKBAR_VALUE, seekbar_value);
        db.update(TABLE_LISTEN, args, ID_BOOK + "=" + id_book, null);
    }
    public BookMarksHelper getProductDownload(String productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.query(TABLE_DOWNLOAD,
                new String[] {ID, ID_BOOK},ID
                        +" LIKE '"+productid+"%'", null, null, null, null);

        BookMarksHelper bookMarksHelper = new BookMarksHelper();
        if (cursor2.moveToFirst()) {
            do {
                bookMarksHelper.setId_book(cursor2.getString(1));

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return bookMarksHelper;
    }
    public BookMarksHelper getBackPressed(String productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.query(TABLE_BACK_PRESSED,
                new String[] {ID, CLASS_NAME, SOME_INFO},ID
                        +" LIKE '"+productid+"%'", null, null, null, null);

        BookMarksHelper bookMarksHelper = new BookMarksHelper();
        if (cursor2.moveToFirst()) {
            do {
                bookMarksHelper.setClass_name(cursor2.getString(1));
                bookMarksHelper.setSome_info(cursor2.getString(2));

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return bookMarksHelper;
    }

}