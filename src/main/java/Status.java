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
                ", senderID=" + senderID +
                ", statusName='" + statusName + '\'' +
                ", message='" + message + '\'' +
                ", comment='" + comment + '\'' +
                ", type=" + type +
                ", sms_count=" + sms_count +
                '}';
    }
}
