package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO  implements TransactionDAO {

    public static final String TRANSACTION_TABLE = "TransactionLog";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_EXPENSETYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    private final DataBaseHelper dataBaseHelper;
    public PersistentTransactionDAO(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper=dataBaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_DATE,transaction.getDate().toString());
        contentValues.put(COLUMN_ACCOUNT_NO,transaction.getAccountNo());
        contentValues.put(COLUMN_EXPENSETYPE,transaction.getExpenseType().toString());
        contentValues.put(COLUMN_AMOUNT,transaction.getAmount());
        sqLiteDatabase.insert(TRANSACTION_TABLE,null,contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList=new ArrayList<>();
        String queryString="SELECT * FROM "+TRANSACTION_TABLE;
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                Date date=new Date(cursor.getString(1));
                String accountNo=cursor.getString(2);
                ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(3));
                double amount=cursor.getDouble(4);
                Transaction transaction=new Transaction(date,accountNo,expenseType,amount);
                transactionList.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return  transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> paginatedTransactionList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TRANSACTION_TABLE,new String[]{"*"},null,null,null,null,COLUMN_ID+" DESC", String.valueOf(limit));
        if(cursor.moveToFirst()){
            do{
                Date date=new Date(cursor.getString(1));
                String accountNo=cursor.getString(2);
                ExpenseType expenseType=ExpenseType.valueOf(cursor.getString(3));
                double amount=cursor.getDouble(4);
                Transaction transaction=new Transaction(date,accountNo,expenseType,amount);
                paginatedTransactionList.add(0,transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return  paginatedTransactionList;
    }

}
