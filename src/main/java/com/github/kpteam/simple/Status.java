package com.github.kpteam.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("last_date")
    private String lastDate;

    @JsonProperty("last_timestamp")
    private String lastTimestamp;

    @JsonProperty("err")
    private Integer errorCode;

    @JsonProperty("send_date")
    private String sendDate;

    @JsonProperty("send_timestamp")
    private String sendTimestamp;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("cost")
    private Integer cost;

    @JsonProperty("sender_id")
    private Integer senderId;

    @JsonProperty("status_name")
    private String statusName;

    @JsonProperty("message")
    private String message;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("sms_cnt")
    private Integer smsCount;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(String sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status1 = (Status) o;
        return Objects.equals(status, status1.status) &&
            Objects.equals(lastDate, status1.lastDate) &&
            Objects.equals(lastTimestamp, status1.lastTimestamp) &&
            Objects.equals(errorCode, status1.errorCode) &&
            Objects.equals(sendDate, status1.sendDate) &&
            Objects.equals(sendTimestamp, status1.sendTimestamp) &&
            Objects.equals(phone, status1.phone) &&
            Objects.equals(cost, status1.cost) &&
            Objects.equals(senderId, status1.senderId) &&
            Objects.equals(statusName, status1.statusName) &&
            Objects.equals(message, status1.message) &&
            Objects.equals(comment, status1.comment) &&
            Objects.equals(type, status1.type) &&
            Objects.equals(smsCount, status1.smsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, lastDate, lastTimestamp, errorCode, sendDate, sendTimestamp, phone, cost, senderId, statusName, message, comment, type, smsCount);
    }

    @Override
    public String toString() {
        return "com.github.kpteam.simple.Status{" +
            "status=" + status +
            ", lastDate='" + lastDate + '\'' +
            ", lastTimestamp='" + lastTimestamp + '\'' +
            ", errorCode=" + errorCode +
            ", sendDate='" + sendDate + '\'' +
            ", sendTimestamp='" + sendTimestamp + '\'' +
            ", phone='" + phone + '\'' +
            ", cost=" + cost +
            ", senderId=" + senderId +
            ", statusName='" + statusName + '\'' +
            ", message='" + message + '\'' +
            ", comment='" + comment + '\'' +
            ", type=" + type +
            ", smsCount=" + smsCount +
            '}';
    }
}
