package com.revature.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction {

    private String initiatedBy;
    private String receivingEmail;
    private String typeName;
    private int transactionId;
    private int requesterId;
    private int sendingId;
    private int receivingId;
    private Timestamp requestTime;
    private Timestamp resolveTime;

    private int statusId;
    private int descriptionId;
    private long amount;

    private String description;


    public Transaction(int transactionId, int requesterId, int sendingId, int receivingId,
                       Timestamp requestTime, Timestamp resolveTime,
                       int statusId, int descriptionId, long amount) {

        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.requestTime = requestTime;
        this.resolveTime = resolveTime;
        this.statusId = statusId;
        this.descriptionId = descriptionId;
        this.amount = amount;
    }

    public Transaction() {

    }

    public Transaction(int transactionId, int requesterId, int sendingId, int receivingId,
                       Timestamp reqTime, Timestamp resTime,
                       String receivingEmail, String initiatedBy, String typeName,
                       String description, long amount) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.requestTime = reqTime;
        this.resolveTime = resTime;
        this.typeName = typeName;
        this.receivingEmail = receivingEmail;
        this.initiatedBy = initiatedBy;
        this.description = description;
        this.amount = amount;
    }

    public Transaction(int requesterId, int sendingId, int receivingId,
                       String receivingEmail, long amount) {

        this.requesterId = requesterId;
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.receivingEmail = receivingEmail;
        this.amount = amount;
    }

    public Transaction(int transactionId, int requesterId, int sendingId, int receivingId, String receivingEmail,
                       String initiatedBy, String typeName, String description, int amount) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.receivingEmail = receivingEmail;
        this.typeName = typeName;
        this.initiatedBy = initiatedBy;
        this.description = description;
        this.amount = amount;
    }

    public Transaction(int transactionId, int requesterId, int sendingId, int receivingId, Timestamp reqTime,
                       String receivingEmail, String initiatedBy, String typeName, String description, long amount) {
        this.transactionId = transactionId;
        this.requesterId = requesterId;
        this.sendingId = sendingId;
        this.receivingId = receivingId;
        this.requestTime = reqTime;
        this.receivingEmail = receivingEmail;
        this.initiatedBy = initiatedBy;
        this.typeName = typeName;
        this.description = description;
        this.amount = amount;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getReceivingEmail() {
        return receivingEmail;
    }

    public void setReceivingEmail(String receivingEmail) {
        this.receivingEmail = receivingEmail;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public int getSendingId() {
        return sendingId;
    }

    public void setSendingId(int sendingId) {
        this.sendingId = sendingId;
    }

    public int getReceivingId() {
        return receivingId;
    }

    public void setReceivingId(int receivingId) {
        this.receivingId = receivingId;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Timestamp getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Timestamp resolveTime) {
        this.resolveTime = resolveTime;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getTransactionId() == that.getTransactionId() && getRequesterId() == that.getRequesterId() && getSendingId() == that.getSendingId() && getReceivingId() == that.getReceivingId() && getStatusId() == that.getStatusId() && getDescriptionId() == that.getDescriptionId() && getAmount() == that.getAmount() && getReceivingEmail().equals(that.getReceivingEmail()) && Objects.equals(getTypeName(), that.getTypeName()) && Objects.equals(getResolveTime(), that.getResolveTime()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInitiatedBy(), getReceivingEmail(), getTypeName(), getTransactionId(), getRequesterId(), getSendingId(), getReceivingId(), getRequestTime(), getResolveTime(), getStatusId(), getDescriptionId(), getAmount(), getDescription());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", receivingEmail='" + receivingEmail + '\'' +
                ", typeName='" + typeName + '\'' +
                ", transactionId=" + transactionId +
                ", requesterId=" + requesterId +
                ", sendingId=" + sendingId +
                ", receivingId=" + receivingId +
                ", requestTime=" + requestTime +
                ", resolveTime=" + resolveTime +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}