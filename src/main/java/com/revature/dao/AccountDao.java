package com.revature.dao;

import com.revature.model.Account;
import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public Account openAccount(Account newAccount) {
        try (Connection con = ConnectionUtility.createConnection()) {

            PreparedStatement ps = con.prepareStatement("INSERT INTO accounts" +
                    "(type_id, balance) VALUES (?, ?) RETURNING *");

            ps.setInt(1, newAccount.getTypeId());
            ps.setLong(2, newAccount.getBalance());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("id"), rs.getInt("type_id"), rs.getLong("balance"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getAccountsByEmail(String email) throws SQLException {

        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT act.type_name, a.type_id, a.balance as " +
                    "amount_in_dollars, a.id as acc_id, uwa.user_id " +
                    "FROM account_types act " +
                    "JOIN accounts a ON a.type_id = act.id " +
                    "JOIN users_with_accounts uwa ON a.id = uwa.account_id " +
                    "JOIN users u ON u.id = uwa.user_id " +
                    "WHERE u.email = ?");

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                int accountId = rs.getInt("acc_id");
                int typeId = rs.getInt("type_id");
                String typeName = rs.getString("type_name");
                long balance = rs.getLong("amount_in_dollars");
                Account account = new Account(accountId, typeId, typeName, balance);

                accounts.add(account);
            }
            return accounts;
        }
    }

    public Account getAccountByEmailAndAccountId(String email, int id) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT act.type_name, a.type_id, a.balance as " +
                    "amount_in_dollars, a.id as acc_id, uwa.user_id " +
                    "FROM account_types act " +
                    "JOIN accounts a ON a.type_id = act.id " +
                    "JOIN users_with_accounts uwa ON a.id = uwa.account_id " +
                    "JOIN users u ON u.id = uwa.user_id " +
                    "WHERE u.email = ? AND uwa.account_id = ?");

            ps.setString(1, email);
            ps.setInt(2, id);

            ResultSet rs = ps.executeQuery();
            Account account = null;
            while (rs.next()) {
                int accountId = rs.getInt("acc_id");
                int typeId = rs.getInt("type_id");
                String typeName = rs.getString("type_name");
                long balance = rs.getLong("amount_in_dollars");
                account = new Account(accountId, typeId, typeName, balance);
            }
            List<String> users = obtainListOfAccountOwners(id);
            account.setAccountOwners(users);
            return account;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String linkUserToAccount(int aId, int uId) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users_with_accounts " +
                    "(account_id, user_id) VALUES (?, ?) RETURNING *");
            ps.setInt(1, aId);
            ps.setInt(2, uId);

            ResultSet rs = ps.executeQuery();
            Account account = null;
            while (rs.next()) {
                int accountId = rs.getInt("account_id");
                int userId = rs.getInt("user_id");
                account = new Account(accountId, userId);
            }
            return "Account " + aId + " successfully linked to user " + uId + "!";
        }
    }

    public String unlinkUserFromAccount(int aId, int uId) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM users_with_accounts " +
                    "WHERE account_id = ? AND user_id = ? RETURNING *");
            ps.setInt(1, aId);
            ps.setInt(2, uId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Account " + aId + " successfully unlinked from user " + uId + "!";
            } else {
                return "Account " + aId + " is not associated with user " + uId + ".";
            }
        }
    }

    public List<String> obtainListOfAccountOwners(int aId) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT concat_ws(' ', u.first_name, u.last_name) as name " +
                    "FROM users u " +
                    "JOIN users_with_accounts uwa ON u.id = uwa.user_id " +
                    "JOIN accounts a ON uwa.account_id = a.id " +
                    "WHERE account_id = ?");
            ps.setInt(1, aId);
            ResultSet rs = ps.executeQuery();
            List<String> owners = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                owners.add(name);
            }
            return owners;
        }
    }

    public String deleteAccount(int aId) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM accounts " +
                    "WHERE id = ? RETURNING *");
            ps.setInt(1, aId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Account " + aId + " successfully deleted!";
            } else {
                return "Account " + aId + " could not be deleted!";
            }
        }
    }

    public Account getAccountsById(int aId) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, aId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(aId, rs.getInt("type_id"), rs.getLong("balance"));
            } else {
                return null;
            }
        }
    }

    public Boolean isOwnerOfAccount(int uId, int aId) {
        try (Connection con = ConnectionUtility.createConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT ? IN(\n" +
                    "\tSELECT uwa .account_id \n" +
                    "\t\tFROM users_with_accounts uwa\n" +
                    "\t\tWHERE uwa.user_id = ?\n" +
                    "\t\t) as owns_account;");
            ps.setInt(1, aId);
            ps.setInt(2, uId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("owns_account");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public int getPrimaryAccountById(int uId) {
        try (Connection con = ConnectionUtility.createConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT primary_acc FROM users  WHERE id = ?");
            ps.setInt(1, uId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("primary_acc");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int getPrimaryAccountByEmail(String receivingEmail) {
        try (Connection con = ConnectionUtility.createConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT primary_acc FROM users  WHERE email = ?");
            ps.setString(1, receivingEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("primary_acc");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean getAccountById(int id) {
        try (Connection con = ConnectionUtility.createConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int getBalanceofAccountById(int id) {
        try (Connection con = ConnectionUtility.createConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}