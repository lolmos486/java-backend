package com.revature.utility;

import com.revature.exception.InvalidParameterException;
import com.revature.model.Account;
import com.revature.model.Transaction;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Helpers {

    public static Boolean validateAccount(Account acc) throws InvalidParameterException {

        return true;
    }

    public static Boolean validateEmail(String email) throws InvalidParameterException {
        InvalidParameterException exceptions = new InvalidParameterException();
        if (email == null || email.isEmpty()) {
            exceptions.addMessage("Value for receivingEmail must exist.");
            throw exceptions;
        }
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            exceptions.addMessage("Value for receivingEmail does not match <username>@<domain> pattern");
            throw exceptions;
        }
        return true;
    }

    public static int validatePositiveInt(String s, String val) throws InvalidParameterException {
        InvalidParameterException exceptions = new InvalidParameterException();
        int n = 0;
        try {
            n = Integer.parseInt(s);
            if (n < 1) {
                exceptions.addMessage(val + " must be a non-zero positive number.");
            }
        } catch (NumberFormatException e) {
            exceptions.addMessage(val + " <" + s + "> is an invalid value. Numeric value is expected.");
        }
        if (exceptions.containsMessage()) {
            throw exceptions;
        }
        return n;
    }

    public static Long validatePositiveLong(String s, String val) throws InvalidParameterException {
        InvalidParameterException exceptions = new InvalidParameterException();
        long n = 0L;
        try {
            BigDecimal amt = BigDecimal.valueOf(Double.parseDouble(s)).movePointRight(2).divideToIntegralValue(BigDecimal.valueOf(1));
            n = (long) (int) (Float.parseFloat(amt.toString()) * 10) / 10;
            if (n < 1) {
                exceptions.addMessage(val + " must be a non-zero positive number.");
            }
        } catch (NumberFormatException e) {
            exceptions.addMessage(val + " <" + s + "> is an invalid value. Numeric value is expected.");
        }
        if (exceptions.containsMessage()) {
            throw exceptions;
        }
        return n;
    }


    public static Transaction validateTransactionParams(Map<String, String> trx) throws InvalidParameterException {

        Transaction t = new Transaction();
        String initiatedBy = trx.get("initiatedBy");
        if (initiatedBy != null) t.setInitiatedBy(initiatedBy);
        String requesterId = trx.get("requesterId");
        if (requesterId != null) t.setRequesterId(validatePositiveInt(requesterId, "User ID"));
        String transactionId = trx.get("transactionId");
        if (transactionId != null) t.setTransactionId(validatePositiveInt(transactionId, "Transaction ID"));
        String sendingId = trx.get("sendingId");
        if (sendingId != null) t.setSendingId(validatePositiveInt(sendingId, "Sending Account ID"));
        String receivingId = trx.get("receivingId");
        if (receivingId != null) t.setReceivingId(validatePositiveInt(receivingId, "Receiving Account ID"));
        String statusId = trx.get("statusId");
        if (statusId != null) t.setStatusId(validatePositiveInt(statusId, "Transaction Status ID"));
        String descriptionId = trx.get("descriptionId");
        if (descriptionId != null) t.setDescriptionId(validatePositiveInt(descriptionId, "Transaction Description ID"));
        String amount = trx.get("amount");
        if (amount != null) t.setAmount(validatePositiveLong(amount, "Transaction amount"));
        if (trx.get("receivingEmail") != null)
            if (validateEmail(trx.get("receivingEmail"))) t.setReceivingEmail(trx.get("receivingEmail"));
        return t;

    }


}
