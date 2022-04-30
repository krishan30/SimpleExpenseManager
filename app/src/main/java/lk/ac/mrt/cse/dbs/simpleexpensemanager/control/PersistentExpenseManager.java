package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        setup(context);
    }
    @Override
    public void setup(Context context) throws ExpenseManagerException {
        /*** Begin generating dummy data for In-Memory implementation ***/
        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);

        TransactionDAO persistentTransactionDAO= new PersistentTransactionDAO(dataBaseHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dataBaseHelper);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        //Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        //Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        //getAccountsDAO().addAccount(dummyAcct1);
        //getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/

    }
}
