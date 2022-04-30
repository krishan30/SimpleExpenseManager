package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context) {
        super(context, "190046E.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement= "CREATE TABLE Account(accountNo TEXT PRIMARY KEY,bankName TEXT,accountHolderName TEXT,balance REAL)";
        sqLiteDatabase.execSQL(createAccountTableStatement);
        String createTransactionLogTableStatement= "CREATE TABLE TransactionLog(ID INTEGER PRIMARY KEY AUTOINCREMENT,date TEXT,accountNo TEXT,expenseType TEXT,amount REAL)";
        sqLiteDatabase.execSQL(createTransactionLogTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
