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

    private static final String ID = "id";
    private static final String NAME_AUTHOR = "name_author";
    private static final String BOOK_NAME = "name_book";
    private static final String NAME_READER = "name_reader";
    private static final String PRICE = "price";
    private static final String IMG_URL = "img_url";
    private static final String ID_BOOK = "id_book";


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




    public DataBasesHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOKMARKS);
        sqLiteDatabase.execSQL(CREATE_TABLE_HISTORY);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
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
  /*  public void deleteContact(String book_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARKS, BOOK_NAME + " = " + book_name );
        db.close();
    }*/

}