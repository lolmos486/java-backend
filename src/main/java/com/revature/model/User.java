package com.revature.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String userRole;


    private List<Account> accounts;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }


    public User() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.phoneNumber = "";
        this.userRole = "";
        this.accounts = new ArrayList<>();

    }

    public User(String firstName) {
        this.firstName = firstName;
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.phoneNumber = "";
        this.userRole = "";
        this.accounts = new ArrayList<>();

    }

    public User(int userId, String firstName, String lastName, String email, String pass, String phone, String userRole) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = pass;
        this.phoneNumber = phone;
        this.userRole = userRole;
        this.accounts = new ArrayList<>();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(userRole, user.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email, password, phoneNumber, userRole, accounts);


    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + password + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", userRole='" + userRole + '\'' +
                ", accounts=" + accounts +
                '}';
    }


}
