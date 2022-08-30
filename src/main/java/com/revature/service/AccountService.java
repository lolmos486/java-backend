package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.exception.InvalidParameterException;
import com.revature.model.Account;
import com.revature.model.User;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AccountService {
    private AccountDao accountDao;
    private UserService userService;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.userService = new UserService();


    }

    public AccountService(AccountDao accountDao, UserService userService) {
        this.accountDao = accountDao;
        this.userService = userService;
    }

    public AccountService(AccountDao mockDao) {
        this.accountDao = mockDao;
    }

    public Account openAccount(Map<String, String> newAccount) throws InvalidParameterException {
        Account account = new Account();
        InvalidParameterException exceptions = new InvalidParameterException();
        String typeId = newAccount.get("typeId");

        if (typeId == null) {
            exceptions.addMessage("Must have an account type");

        } else {
            account.setTypeId(Integer.parseInt(typeId));
        }
        int balance = 0;
        account.setBalance(balance);

        if (exceptions.containsMessage()) {
            throw exceptions;
        }
//        System.out.println(account);
        return accountDao.openAccount(account);
    }

    public List<Account> getAccountsByEmail(String email) throws SQLException, InvalidParameterException {
        InvalidParameterException exceptions = new InvalidParameterException();

        if(userService.getUserEmailByEmail(email)) {
            return accountDao.getAccountsByEmail(email);
        }
        else {
            exceptions.addMessage("User under email was not found");
            throw exceptions;
        }
    }

    public Account getAccountByEmailAndAccountId(String email, int id) throws InvalidParameterException, SQLException {
        InvalidParameterException exceptions = new InvalidParameterException();
        List<Account> accountList = getAccountsByEmail(email);
        boolean isIn = false;
        if (accountList != null) {
            for (Account account : accountList) {
                if (id == (account.getAccountId())) {
                    isIn = true;
                }
            }
        }
        if(isIn) {
            return accountDao.getAccountByEmailAndAccountId(email, id);

        }
        else{
            exceptions.addMessage("The account was not found");
            throw exceptions;
        }
    }


    public String linkUserToAccount(int aId, String email) throws SQLException, InvalidParameterException {
        System.out.println(email);
        InvalidParameterException exceptions = new InvalidParameterException();
        List<String> owners = accountDao.obtainListOfAccountOwners(aId);
        User myUser = userService.getUserByEmail(email);
        System.out.println(myUser);
        if (userService.getUserByEmail(email) == null) {
            exceptions.addMessage("User not found");
        } else {
            String fullName = myUser.getFirstName() + " " + myUser.getLastName();
            if (owners.contains(fullName)) {
                exceptions.addMessage("User " + myUser.getUserId() + " already linked to account " + aId);
            }
        }
        if (!accountDao.getAccountById(aId)){
            exceptions.addMessage("The account does not exist");
        }
        if (exceptions.containsMessage()) {
            throw exceptions;
        }
        return accountDao.linkUserToAccount(aId, myUser.getUserId());
    }

    public String unlinkUserFromAccount(int aId, String email) throws SQLException, InvalidParameterException {
        List<String> owners = accountDao.obtainListOfAccountOwners(aId);
        User myUser = userService.getUserByEmail(email);
        InvalidParameterException exceptions = new InvalidParameterException();
        if (userService.getUserByEmail(email) == null) {
            exceptions.addMessage("User not found");
        } else {
            String fullName = myUser.getFirstName() + " " + myUser.getLastName();
            if (!owners.contains(fullName)) {
                exceptions.addMessage("User " + myUser.getUserId() + " not linked to account " + aId);
            }
        }
        if (exceptions.containsMessage()) {
            throw exceptions;
        }
        return accountDao.unlinkUserFromAccount(aId, myUser.getUserId());
    }

    public String deleteAccount(int aId) throws SQLException, InvalidParameterException {
        Account account = accountDao.getAccountsById(aId);
        List<String> accountOwners = accountDao.obtainListOfAccountOwners(aId);
        InvalidParameterException exception = new InvalidParameterException();
        if (account.getBalance() != 0) {
            exception.addMessage("Account balance must be 0!");
        }
        if (accountOwners.size() > 1) {
            exception.addMessage("An account with more than one linked user cannot be deleted!");
        }
        if (exception.containsMessage()) {
            throw exception;
        }
        return accountDao.deleteAccount(aId);
    }

    public List<String> obtainListOfAccountOwners(int aId) throws SQLException {
//        InvalidParameterException exception = new InvalidParameterException();
            accountDao.getAccountById(aId);
//        if(accountDao.getAccountById(aId)) {
            return accountDao.obtainListOfAccountOwners(aId);
        }

    public Boolean isOwnerOfAccount(int uId, int aId) {
        return accountDao.isOwnerOfAccount(uId, aId);
    }
}

