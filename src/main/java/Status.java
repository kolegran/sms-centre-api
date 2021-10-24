import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    @JsonProperty("status")
    private int status;

    @JsonProperty("last_date")
    //TODO: find the class for displaying timestamps

    @JsonProperty("last_timestamp")

    @JsonProperty("err")
    private int errorCode;

    @JsonProperty("send_date")

    @JsonProperty("send_timestamp")

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("cost")
    private int cost;

    @JsonProperty("sender_id")
    private int senderID;

    @JsonProperty("status_name")
    private String statusName;

    @JsonProperty("message")
    private String message;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("type")
    private int type;

    @JsonProperty("sms_cnt")
    private int sms_count;

    //TODO: write the methods and overrides
}
