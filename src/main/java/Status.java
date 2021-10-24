import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public String getLastDate() { return lastDate; }

    public void setLastDate(String lastDate) { this.lastDate = lastDate; }

    public String getLastTimestamp() { return lastTimestamp; }

    public void setLastTimestamp(String lastTimestamp) { this.lastTimestamp = lastTimestamp; }

    public Integer getErrorCode() { return errorCode; }

    public void setErrorCode(Integer errorCode) { this.errorCode = errorCode; }

    public String getSendDate() { return sendDate; }

    public void setSendDate(String sendDate) { this.sendDate = sendDate; }

    public String getSendTimestamp() { return sendTimestamp; }

    public void setSendTimestamp(String sendTimestamp) { this.sendTimestamp = sendTimestamp; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Integer getCost() { return cost; }

    public void setCost(Integer cost) { this.cost = cost; }

    public Integer getSenderId() { return senderId; }

    public void setSenderId(Integer senderId) { this.senderId = senderId; }

    public String getStatusName() { return statusName; }

    public void setStatusName(String statusName) { this.statusName = statusName; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public Integer getType() { return type; }

    public void setType(Integer type) { this.type = type; }

    public Integer getSmsCount() { return smsCount; }

    public void setSmsCount(Integer smsCount) { this.smsCount = smsCount; }

    @Override
    public String toString() {
        return "Status{" +
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
