package com.example.zohaibsiddique.expensecalculator;

/**
 * Created by deXter on 17/06/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "expense_calculator.db";
    private DB myDBHelper;
    private Context context;
    private SQLiteDatabase db;

    public static final String TABLE_SUB_TYPE = "sub_type";
    public static final String ID_SUB_TYPE = "id";
    public static final String NAME_SUB_TYPE = "name";
    public static final String KEY_SEARCH = "key_search";

    public static final String CREATE_SUB_TYPE_TABLE = "CREATE TABLE " + TABLE_SUB_TYPE
            + "(" + ID_SUB_TYPE + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + NAME_SUB_TYPE + " TEXT, "
            + KEY_SEARCH + " TEXT,"
            + "UNIQUE("+NAME_SUB_TYPE+"))";

    public static final String TABLE_MAIN_TYPE = "main_type";
    public static final String ID_MAIN_TYPE = "id";
    public static final String NAME_MAIN_TYPE = "name";

    public static final String CREATE_MAIN_TYPE_TABLE = "CREATE TABLE " + TABLE_MAIN_TYPE
            + "(" + ID_MAIN_TYPE + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + NAME_MAIN_TYPE + " TEXT,"
            + KEY_SEARCH + " TEXT,"
            + "UNIQUE("+NAME_MAIN_TYPE+"))";

    public static final String TABLE_TYPE = "type";
    public static final String ID_TYPE = "id";
    public static final String FK_MAIN_TYPE_TABLE_TYPE = "fk_main_type";

    public static final String CREATE_TYPE_TABLE = "CREATE TABLE type(id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,fk_main_type INTEGER,fk_sub_type INTEGER,FOREIGN KEY(fk_main_type) REFERENCES main_type(id), FOREIGN KEY(fk_sub_type) REFERENCES sub_type(id), UNIQUE(fk_main_type, fk_sub_type));";

    public static final String TABLE_EXPENSE = "expense";
    public static final String ID_EXPENSE = "id";
    public static final String NAME_EXPENSE = "name";
    public static final String VALUE_EXPENSE = "value";
    public static final String TYPE_ID_EXPENSE = "type_id";
    public static final String DATE_EXPENSE = "date";
    public static final String KEY_SEARCH_DATE = "key_search_date";
    public static final String FOREIGN_KEY_LEDGER = "froeign_key_ledger";

    public static final String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSE
            + "(" + ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + NAME_EXPENSE + " TEXT,"
            + VALUE_EXPENSE + " TEXT,"
            + DATE_EXPENSE + " TEXT,"
            + KEY_SEARCH_DATE + " TEXT,"
            + TYPE_ID_EXPENSE + " TEXT,"
            + FOREIGN_KEY_LEDGER + " TEXT)";

    public static final String TABLE_LEDGER = "ledger";
    public static final String ID_LEDGER = "id";
    public static final String TITLE_LEDGER = "title";
    public static final String STARTING_BALANCE_LEDGER = "starting_balance";
    public static final String DATE_LEDGER = "date";
    public static final String FROM_DATE_LEDGER = "from_date";
    public static final String TO_DATE_LEDGER = "to_date";

    public static final String CREATE_LEDGER_TABLE = "CREATE TABLE " + TABLE_LEDGER
            + "(" + ID_LEDGER + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + TITLE_LEDGER + " TEXT,"
            + STARTING_BALANCE_LEDGER + " TEXT,"
            + DATE_LEDGER + " TEXT,"
            + FROM_DATE_LEDGER + " TEXT,"
            + TO_DATE_LEDGER + " TEXT)";

    // Constructor for creating database
    public DB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL(CREATE_MAIN_TYPE_TABLE);
            db.execSQL(CREATE_SUB_TYPE_TABLE);
            db.execSQL(CREATE_TYPE_TABLE);
            db.execSQL(CREATE_EXPENSE_TABLE);
            db.execSQL(CREATE_LEDGER_TABLE);
            db.execSQL("INSERT INTO main_type(name) VALUES('all');");
        } catch (Exception e) {
            Log.d("Creating Database:", "error "+ e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_MAIN_TYPE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_SUB_TYPE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_EXPENSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_LEDGER_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        super.onOpen(db);
    }

    public void getWritableDB() throws SQLException {
        db = this.getWritableDatabase();
    }

    public void closeDB() {
        if (myDBHelper != null) {
            myDBHelper.close();
        }
    }

    public Cursor selectExpense(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_EXPENSE + "," + NAME_EXPENSE + ","+ VALUE_EXPENSE
                    + ","+ DATE_EXPENSE + ","+ TYPE_ID_EXPENSE + " FROM " + TABLE_EXPENSE
                    + " ORDER BY " + DATE_EXPENSE + " DESC", null);
        } catch (Exception e) {
            Log.d("selectExpenses", " error " + e.getMessage());
        }

        return cursor;
    }

    public Cursor selectLedger(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_LEDGER + "," + TITLE_LEDGER + ","+ STARTING_BALANCE_LEDGER
                    + ","+ DATE_LEDGER+ ","+ FROM_DATE_LEDGER + ","+ TO_DATE_LEDGER + " FROM " + TABLE_LEDGER
                    + " ORDER BY " + DATE_LEDGER + " DESC", null);
        } catch (Exception e) {
            Log.d("selectLedger", " error " + e.getMessage());
        }

        return cursor;
    }


    public Cursor selectDateOfExpense(String date){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_EXPENSE + "," + NAME_EXPENSE + ","+ VALUE_EXPENSE
                    + ","+ DATE_EXPENSE + ","+ TYPE_ID_EXPENSE + " FROM " + TABLE_EXPENSE
                    + " WHERE " + KEY_SEARCH_DATE + "='" + date + "'", null);
        } catch (Exception e) {
            Log.d("selectDateOfExpense", " error " + e.getMessage());
        }

        return cursor;
    }

    public Cursor selectFromToDate(String from, String to){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_EXPENSE + "," + NAME_EXPENSE + ","+ VALUE_EXPENSE
                    + ","+ DATE_EXPENSE + ","+ TYPE_ID_EXPENSE + " FROM " + TABLE_EXPENSE
                    + " WHERE " + KEY_SEARCH_DATE + " BETWEEN '" + from + "' AND '" + to + "'", null);
        } catch (Exception e) {
            Log.d("selectFromToDate", " error " + e.getMessage());
        }

        return cursor;
    }

    public Cursor selectValueOfExpense(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + VALUE_EXPENSE + " FROM " + TABLE_EXPENSE, null);
        } catch (Exception e) {
            Log.d("selectValueOfExpense", " error " + e.getMessage());
        }

        return cursor;
    }

    public boolean addSubType(String subType){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(NAME_SUB_TYPE, subType);
            db.insert(TABLE_SUB_TYPE, null, contentValues);

        } catch (Exception e) {
            Log.d("insertSubType", " error " + e.getMessage());
        }
        return true;
    }

    public boolean isSubTypeExisted(String inputSubType){
        Cursor cursor;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + NAME_SUB_TYPE + " FROM " + TABLE_SUB_TYPE + " WHERE " + NAME_SUB_TYPE + "='" + inputSubType + "'", null);
            cursor.moveToFirst();
            cursor.getString(cursor.getColumnIndex(NAME_SUB_TYPE));
            return true;
        } catch(Exception e) {
            Log.d("isSubTypeExisted"," error is " + e.getMessage());
        }
        return false;
    }

    public String selectIdByMainTypeName(String name){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE + " WHERE " + NAME_MAIN_TYPE + "='" + name + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("selectIdByMainTypeName"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(ID_MAIN_TYPE));
    }

    public Cursor selectExpenseByType(String type){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_EXPENSE + "," + NAME_EXPENSE + "," + VALUE_EXPENSE
                    + "," + DATE_EXPENSE + "," + TYPE_ID_EXPENSE + " FROM " + TABLE_EXPENSE
                    + " WHERE " + TYPE_ID_EXPENSE + "='" + type + "'"
                    + " ORDER BY " + DATE_EXPENSE + " DESC", null);
        } catch(Exception e) {
            Log.d("selectExpenseByType"," error is " + e.getMessage());
        }
        return cursor;
    }

    public boolean addMainType(String mainType){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(NAME_MAIN_TYPE, mainType);
            db.insert(TABLE_MAIN_TYPE, null, contentValues);

        } catch (Exception e) {
            Log.d("addMainType", " error " + e.getMessage());
        }
        return true;
    }

    public boolean addExpense(String name, String value, String date, String keyDate, String type){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(NAME_EXPENSE, name);
            contentValues.put(VALUE_EXPENSE, value);
            contentValues.put(DATE_EXPENSE, date);
            contentValues.put(KEY_SEARCH_DATE, keyDate);
            contentValues.put(TYPE_ID_EXPENSE, type);
            db.insert(TABLE_EXPENSE, null, contentValues);

        } catch (Exception e) {
            Log.d("addExpense", " error " + e.getMessage());
        }
        return true;
    }

    public boolean addLedger(String title, String startingBalance, String date, String fromDate, String toDate){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(TITLE_LEDGER, title);
            contentValues.put(STARTING_BALANCE_LEDGER, startingBalance);
            contentValues.put(DATE_LEDGER, date);
            contentValues.put(FROM_DATE_LEDGER, fromDate);
            contentValues.put(TO_DATE_LEDGER, toDate);
            db.insert(TABLE_LEDGER, null, contentValues);

        } catch (Exception e) {
            Log.d("addLedger", " error " + e.getMessage());
        }
        return true;
    }


    public boolean deleteExpense(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXPENSE + " WHERE " + ID_EXPENSE + "='" + id +"'");
        return true;
    }

    public boolean deleteType(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MAIN_TYPE + " WHERE " + ID_MAIN_TYPE + "='" + id +"'");
        return true;
    }

    public boolean updateExpense(long id, String name, String value, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_EXPENSE, name);
        contentValues.put(VALUE_EXPENSE, value);
        contentValues.put(TYPE_ID_EXPENSE, type);
        db.update(TABLE_EXPENSE, contentValues, ID_EXPENSE + "= ?", new String[]{Long.toString(id)});
        return true;
    }

    public boolean updateType(long id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_MAIN_TYPE, name);
        db.update(TABLE_MAIN_TYPE, contentValues, ID_MAIN_TYPE + "= ?", new String[]{Long.toString(id)});
        return true;
    }

    public boolean isMainTypeExisted(String inputMainType){
        Cursor cursor;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + NAME_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE + " WHERE " + NAME_MAIN_TYPE + "='" + inputMainType + "'", null);
            cursor.moveToFirst();
            cursor.getString(cursor.getColumnIndex(NAME_SUB_TYPE));
            return true;
        } catch(Exception e) {
            Log.d("isMainTypeExisted"," error is " + e.getMessage());
        }
        return false;
    }

    public Cursor selectMainType(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_MAIN_TYPE + "," + NAME_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE
                    + " ORDER BY " + ID_MAIN_TYPE + " DESC", null);
        } catch(Exception e) {
            Log.d("selectMainType"," error is " + e.getMessage());
        }
        return cursor;
    }

    public String selectTypeById(String id){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + NAME_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE + " WHERE " + ID_MAIN_TYPE + "='" + id + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("selectMainType"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(NAME_MAIN_TYPE));
    }

    public Cursor selectSubType(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + NAME_SUB_TYPE + " FROM " + TABLE_SUB_TYPE, null);
        } catch(Exception e) {
            Log.d("selectSubType"," error is " + e.getMessage());
        }
        return cursor;
    }

    public String selectSubTypeById(String id){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + NAME_SUB_TYPE + " FROM " + TABLE_SUB_TYPE + " WHERE " + ID_SUB_TYPE + "='" + id + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("selectMainType"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(NAME_MAIN_TYPE));
    }



    public int getSubTypeIdByName(String name){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_SUB_TYPE + " FROM " + TABLE_SUB_TYPE + " WHERE " + NAME_SUB_TYPE + "='" + name +"'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("getSubTypeIdByName"," error is " + e.getMessage());
        }
        return cursor.getInt(cursor.getColumnIndex(ID_SUB_TYPE));
    }

    public String getIdByType(String type){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE + " WHERE " + NAME_MAIN_TYPE + "='" + type + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("getIdByType"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(ID_MAIN_TYPE));
    }


    public String getItemMainTypeIdByName(String name){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE + " WHERE " + NAME_MAIN_TYPE + "='" + name + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("getItemMainTypeIdByName"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(ID_MAIN_TYPE));
    }

    public String getItemSubTypeIdByName(String name){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + ID_SUB_TYPE + " FROM " + TABLE_SUB_TYPE + " WHERE " + NAME_SUB_TYPE + "='" + name + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("getItemSubTypeIdByName"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(ID_SUB_TYPE));
    }


    public boolean addConfiguration(int mainType){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(FK_MAIN_TYPE_TABLE_TYPE, mainType);
            db.insertOrThrow(TABLE_TYPE, null, contentValues);
            return true;
        } catch (SQLiteConstraintException e) {
            Log.d("addConfiguration", " error " + e.getMessage());
        }
        return false;
    }


    public boolean isMainTypeConfigurationExisted(String id){
        Cursor cursor;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + FK_MAIN_TYPE_TABLE_TYPE + " FROM " + TABLE_TYPE + " WHERE " + FK_MAIN_TYPE_TABLE_TYPE + "='" + id + "'", null);
            cursor.moveToFirst();
            cursor.getString(cursor.getColumnIndex(FK_MAIN_TYPE_TABLE_TYPE));
            return true;
        } catch(Exception e) {
            Log.d("isMainTypeConfigExisted"," error is " + e.getMessage());
        }
        return false;
    }





    public String mainTypeValue(String id){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT " + FK_MAIN_TYPE_TABLE_TYPE + " FROM " + TABLE_TYPE + " WHERE " + FK_MAIN_TYPE_TABLE_TYPE + "='" + id + "'", null);
            cursor.moveToFirst();
        } catch(Exception e) {
            Log.d("isMainTypeConfigExisted"," error is " + e.getMessage());
        }
        return cursor.getString(cursor.getColumnIndex(FK_MAIN_TYPE_TABLE_TYPE));
    }


    public Cursor selectMainTypeConfiguration(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT DISTINCT " + FK_MAIN_TYPE_TABLE_TYPE +" FROM " + TABLE_TYPE, null);
//            cursor = db.rawQuery("SELECT DISTINCT " + TABLE_MAIN_TYPE + "."+ NAME_MAIN_TYPE + " FROM " + TABLE_MAIN_TYPE +
//                    " LEFT OUTER JOIN " + TABLE_TYPE +
//                    " ON " + TABLE_MAIN_TYPE + "." + ID_MAIN_TYPE
//                    + "=" + TABLE_TYPE + "." + FK_MAIN_TYPE_TABLE_TYPE, null);
        } catch(Exception e) {
            Log.d("selectMainType"," error is " + e.getMessage());
        }
        return cursor;
    }

    // delete by duration
    public void deleteType(String mainType, String subType) {
        Cursor cursor;
        String id;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + ID_TYPE + " FROM " + TABLE_TYPE + " WHERE " + FK_MAIN_TYPE_TABLE_TYPE + "='" + mainType + "'" + " AND " + FK_SUB_TYPE_TABLE_TYPE + "='" + subType + "'", null);
//            cursor.moveToFirst();
//            id = cursor.getString(cursor.getColumnIndex(ID_TYPE));
//            db.execSQL("DELETE FROM " + TABLE_TYPE + " WHERE " + ID_TYPE + "='" + id + "'");
        } catch(Exception e) {
            Log.d("selectSubTypeByMainType"," error is " + e.getMessage());
        }
    }
//    public Cursor selectSubType(){
//        Cursor cursor = null;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + SUB_TYPE + " FROM " + ITEM_TYPE_TABLE, null);
//        } catch(Exception e) {
//            Log.d("selectSubType"," error is " + e.getMessage());
//        }
//        return cursor;
//    }

//    // insert from inbox to Block list
//    public boolean insertInboxToBlockList(String originatingAddress, String name, String messageBody, long time, long duration) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues value_TABLE_BLOCK_LIST = new ContentValues();
//        ContentValues value_TABLE_BLOCKED_SMS = new ContentValues();
//
//        value_TABLE_BLOCK_LIST.put(ORIGINATING_ADDRESS_BLOCKlIST, originatingAddress);
//        value_TABLE_BLOCK_LIST.put(NAME_BLOCK_LIST, name);
//        value_TABLE_BLOCK_LIST.put(TIME_BLOCK_LIST, time);
//        db.insert(TABLE_BLOCK_LIST, null, value_TABLE_BLOCK_LIST);
//
//        value_TABLE_BLOCKED_SMS.put(ORIGINATING_ADDRESS_BLOCKED_SMS, originatingAddress);
//        value_TABLE_BLOCKED_SMS.put(NAME_BLOCKED_SMS, name);
//        value_TABLE_BLOCKED_SMS.put(MESSAGE_BODY, messageBody);
//        value_TABLE_BLOCKED_SMS.put(TIME_BLOCKED_SMS, time);
//        value_TABLE_BLOCKED_SMS.put(DURATION, duration);
//        db.insert(TABLE_BLOCKED_SMS, null, value_TABLE_BLOCKED_SMS);
//
//        return true;
//    }
//
//    // insert from inbox to Block list
//    public boolean importToBlocklist(String originatingAddress) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues value_TABLE_BLOCK_LIST = new ContentValues();
//
//        value_TABLE_BLOCK_LIST.put(ORIGINATING_ADDRESS_BLOCKlIST, originatingAddress);
//        db.insert(TABLE_BLOCK_LIST, null, value_TABLE_BLOCK_LIST);
//        return true;
//    }
//
//    // insert Blocked sms to inbox
//    public boolean insertBlockedSMSToInbox(long id, String originatingAddress, String messageBody, long time, Context context) {
//        boolean check = false;
//        try {
//            ContentValues values = new ContentValues();
//            values.put("address", originatingAddress);
//            values.put("body", messageBody);
//            values.put("date", time);
//            context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
//
//            // delete that message from block list
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.execSQL("DELETE FROM " + TABLE_BLOCKED_SMS + " WHERE " + ID_BLOCKED_SMS + "=" + id);
//
//            check = true;
//        } catch (Exception ex) {
//            check = false;
//        }
//
//        return check;
//    }
//
//    // delete inbox sms
//    public boolean deleteInboxSms(String id, Context context) {
//        context.getContentResolver().delete(Uri.parse("content://sms"), "_id = ?", new String[]{id});
//        return true;
//    }
//
//    // add number series to Block list
//    public boolean addSeriesToBlockList(String number, long time){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues value_TABLE_BLOCK_LIST = new ContentValues();
//            value_TABLE_BLOCK_LIST.put(ORIGINATING_ADDRESS_BLOCKlIST, number);
//            value_TABLE_BLOCK_LIST.put(TIME_BLOCK_LIST, time);
//            db.insert(TABLE_BLOCK_LIST, null, value_TABLE_BLOCK_LIST);
//        } catch (Exception e) {
//            Log.d("Add series", " error is " + e.getMessage());
//        }
//        return true;
//    }
//
//    // add number series to Block list
//    public boolean addNumberToBlockList(String number, long time){
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues value_TABLE_BLOCK_LIST = new ContentValues();
//            value_TABLE_BLOCK_LIST.put(ORIGINATING_ADDRESS_BLOCKlIST, number);
//            value_TABLE_BLOCK_LIST.put(TIME_BLOCK_LIST, time);
//            db.insert(TABLE_BLOCK_LIST, null, value_TABLE_BLOCK_LIST);
//        } catch (Exception e) {
//            Log.d("Add series", " error is " + e.getMessage());
//        }
//        return true;
//    }
//
//    // Update deletion time to delete Blocked SMS
//    public int updateDeletionTime(long deletionTime){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DELETION_TIME, deletionTime);
//        return db.update(TABLE_DELETION_TIME, contentValues, ID_DELETION_TIME + "= ?", new String[]{Long.toString(1)});
//    }
//
//    // Select deletion time to delete Blocked SMS
//    public Cursor selectDeletionTime(){
//        Cursor cursor = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        cursor = db.rawQuery("SELECT " + DELETION_TIME + " FROM " + TABLE_DELETION_TIME
//                + " WHERE " + ID_DELETION_TIME + "= 1", null);
//        return cursor;
//    }
//
//    // Select duration to delete Blocked SMS
//    public Cursor selectDuration(long time){
//        Cursor cursor = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        cursor = db.rawQuery("SELECT " + DURATION + " FROM " + TABLE_BLOCK_LIST
//                + " WHERE " + DURATION + "=" + time, null);
//        return cursor;
//    }
//    // delete by duration
//    public boolean deleteByDuration(long time) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_BLOCKED_SMS + " WHERE " + DURATION + "<=" + time);
//        return true;
//    }
//

//
//    // get Blocked SMS
//    public Cursor selectBlockedSMS(){
//        Cursor cursor = null;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + ID_BLOCKED_SMS
//                    + "," + ORIGINATING_ADDRESS_BLOCKED_SMS
//                    + "," + NAME_BLOCKED_SMS
//                    + "," + MESSAGE_BODY
//                    + "," + TIME_BLOCKED_SMS
//                    + " FROM " + TABLE_BLOCKED_SMS, null);
//        } catch(Exception e) {
//            Log.d("Blocked SMS selection "," error is " + e.getMessage());
//        }
//        return cursor;
//    }
//
//    // get individual Blocked SMS
//    public Cursor selectBlockedMessage(String id){
//        Cursor cursor = null;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + ID_BLOCKED_SMS + "," + ORIGINATING_ADDRESS_BLOCKED_SMS
//                    + "," + MESSAGE_BODY
//                    + "," + TIME_BLOCKED_SMS
//                    + "," + NAME_BLOCKED_SMS
//                    + " FROM " + TABLE_BLOCKED_SMS
//                    + " WHERE " + ID_BLOCKED_SMS + "=" + id, null);
//        } catch(Exception e) {
//            Log.d("Blocked SMS selection "," error is " + e.getMessage());
//        }
//        return cursor;
//    }
//
//    // Select all Blocked Numbers
//    public Cursor compareToBlockList(String originatingAddress){
//        Cursor cursor = null;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + ORIGINATING_ADDRESS_BLOCKlIST + " FROM " + TABLE_BLOCK_LIST + " WHERE " + ORIGINATING_ADDRESS_BLOCKlIST + "= '" + originatingAddress + "'", null);
//        } catch (Exception e){
//            Log.d("compare to Block list", " is" + e.getMessage());
//        }
//        return  cursor;
//    }
//
//    // update boolean value for enable or disable Block to unknown
//    public int enableBlockToUnknown(boolean check){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(BLOCK_TO_UNKNOWN, check);
//        return db.update(TABLE_PREFERENCES, contentValues, ID_PREFERENCES + "= ?", new String[]{Long.toString(1)});
//    }
//
//    // Check state whether checkbox is check or un-check
//    public Cursor checkState(){
//        Cursor cursor = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        cursor = db.rawQuery("SELECT " + BLOCK_TO_UNKNOWN + " FROM " + TABLE_PREFERENCES, null);
//        return cursor;
//    }
//
//    // get check for Block to Unknown feature
//    public Cursor blockToUnknown(){
//        Cursor cursor = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        cursor = db.rawQuery("SELECT " + BLOCK_TO_UNKNOWN + " FROM " + TABLE_PREFERENCES + " WHERE " + ID_PREFERENCES + "= 1", null);
//        return cursor;
//    }
//
//    // Delete data
//    public int delete(String originatingAddress){
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_BLOCK_LIST, ORIGINATING_ADDRESS_BLOCKlIST + " = ?", new String[]{String.valueOf(originatingAddress)});
//    }
//
//    // Update data
//    public int updateListById(long id, String originatingAddress){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ORIGINATING_ADDRESS_BLOCKlIST, originatingAddress);
//        return db.update(TABLE_BLOCK_LIST, contentValues, ID_BLOCK_LIST + "= ?", new String[]{Long.toString(id)});
//    }
//
//    public Cursor getListById(long id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.query(TABLE_BLOCK_LIST, new String[]{ID_BLOCK_LIST, ORIGINATING_ADDRESS_BLOCKlIST},ID_BLOCK_LIST +" = ?",new String[]{Long.toString(id)},null,null,null);
//        cursor.moveToFirst();
//        return cursor;
//    }
//
//    // delete all database data
//    public void deleteAllDB() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_BLOCK_LIST);
//    }
//
//    // sort by number
//    public Cursor sortByNumber() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT " + ID_BLOCK_LIST
//                + "," + ORIGINATING_ADDRESS_BLOCKlIST
//                + "," + NAME_BLOCK_LIST
//                + "," + TIME_BLOCK_LIST
//                + " FROM " + TABLE_BLOCK_LIST
//                + " ORDER BY " + ORIGINATING_ADDRESS_BLOCKlIST + " ASC", null);
//        return cursor;
//    }
//
//    // sort by date
//    public Cursor sortByDate() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT " + ID_BLOCK_LIST
//                + "," + ORIGINATING_ADDRESS_BLOCKlIST
//                + "," + NAME_BLOCK_LIST
//                + "," + TIME_BLOCK_LIST
//                + " FROM " + TABLE_BLOCK_LIST + " ORDER BY " + TIME_BLOCK_LIST + " DESC", null);
//        return cursor;
//    }
//
//    // sort by name - Block list
//    public Cursor sortByName() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT " + ID_BLOCK_LIST
//                + "," + ORIGINATING_ADDRESS_BLOCKlIST
//                + "," + NAME_BLOCK_LIST
//                + "," + TIME_BLOCK_LIST
//                + " FROM " + TABLE_BLOCK_LIST + " ORDER BY " + NAME_BLOCK_LIST + " ASC", null);
//        return cursor;
//    }
//
//
//    // sort by date
//    public Cursor sortByDate_blockedSMS() {
//        Cursor cursor = null;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            cursor = db.rawQuery("SELECT " + ID_BLOCKED_SMS
//                    + "," + ORIGINATING_ADDRESS_BLOCKED_SMS
//                    + "," + NAME_BLOCKED_SMS
//                    + "," + MESSAGE_BODY
//                    + "," + TIME_BLOCKED_SMS
//                    + " FROM " + TABLE_BLOCKED_SMS + " ORDER BY " + TIME_BLOCKED_SMS + " DESC", null);
//        } catch (Exception e) {
//            Log.d("blocked sms sort", " error " + e.getMessage());
//        }
//        return cursor;
//    }
//






    //            + "FOREIGN KEY(" + FK_ID_BLOCK_LIST + ") REFERENCES "
//            + TABLE_BLOCK_LIST + "(" + ID_BLOCK_LIST + ") ON DELETE CASCADE ON UPDATE CASCADE)";

//    cursor = db.rawQuery("SELECT " + ID_BLOCK_LIST + "," + ORIGINATING_ADDRESS + ","
////                    + TIME + "," + MESSAGE_BODY + " FROM table_block_list" +
////                    " LEFT OUTER JOIN " + TABLE_BLOCKED_SMS +
////                    " ON " + TABLE_BLOCK_LIST + "." + ID_BLOCK_LIST
////                    + "=" + TABLE_BLOCKED_SMS + "." + FK_ID_BLOCK_LIST, null);

}

