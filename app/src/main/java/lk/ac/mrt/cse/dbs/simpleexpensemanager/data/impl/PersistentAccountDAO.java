package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO   implements AccountDAO {

    public static final String ACCOUNT_TABLE = "Account";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    private final DataBaseHelper dataBaseHelper;

    public PersistentAccountDAO(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper=dataBaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String > accountNumbersList=new ArrayList<>();
        String queryString="SELECT "+COLUMN_ACCOUNT_NO+" FROM "+ACCOUNT_TABLE;
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                String accountNo=cursor.getString(0);
                accountNumbersList.add(accountNo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList=new ArrayList<>();
        String queryString="SELECT * FROM "+ACCOUNT_TABLE;
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                String accountNo=cursor.getString(0);
                String bankName=cursor.getString(1);
                String accountHolderName=cursor.getString(2);
                double balance=cursor.getDouble(3);
               Account account=new Account(accountNo,bankName,accountHolderName,balance);
               accountList.add(account);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return  accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(ACCOUNT_TABLE,new String[]{"*"},COLUMN_ACCOUNT_NO + "=?",new String[]{accountNo},null,null,null);//sqLiteDatabase.rawQuery(queryString,null);
        Account account;
        if(cursor.moveToFirst()) {
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);
            account=new Account(accountNo, bankName, accountHolderName, balance);
        }else {
            sqLiteDatabase.close();
            cursor.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        sqLiteDatabase.close();
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_ACCOUNT_NO,account.getAccountNo());
        contentValues.put(COLUMN_BANK_NAME,account.getBankName());
        contentValues.put(COLUMN_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(COLUMN_BALANCE,account.getBalance());
        sqLiteDatabase.insert(ACCOUNT_TABLE,null,contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getWritableDatabase();
        boolean result=sqLiteDatabase.delete(ACCOUNT_TABLE,COLUMN_ACCOUNT_NO+"=?",new String[]{accountNo})>0;
        sqLiteDatabase.close();
        if(!result){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account=getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        SQLiteDatabase sqLiteDatabase=this.dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BALANCE,account.getBalance());
        sqLiteDatabase.update(ACCOUNT_TABLE,contentValues,COLUMN_ACCOUNT_NO+"=?",new String[]{account.getAccountNo()});
        sqLiteDatabase.close();
    }
}
