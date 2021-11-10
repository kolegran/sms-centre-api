package com.github.kpteam;

public enum ApiMethod {

    SEND("send"),
    STATUS("status"),
    BALANCE("balance"),
    // TODO: @Kolegran 2021/11/10 The logic for next API methods will implement in the next version
    MESSAGE_TEMPLATES("templates"),
    OPERATOR_TEMPLATES("op_templates"),
    EVENT_NOTIFICATIONS("ping"),
    ACTIONS_WITH_MAILINGS ("jobs"),
    ACTIONS_WITH_CONTACTS_AND_GROUPS("phones"),
    AVAILABLE_OPERATIONS_LIST("users"),
    CREATE_UPLOAD_OF_INVOICES_AND_ACTS("documents"),
    AVAILABLE_OPERATIONS_LIST_WITH_SENDERS("senders"),
    SENT_MESSAGES_HISTORY("get"),
    OPERATOR_INFO("info"),
    SHORT_URLS_INFO("tinyurls"),
    RECEIVE_PHONES("receive_phones"),
    CONFIRMING_PHONE_NUMBER_WITH_CALL("wait_call"),
    ;

    private final String method;

    ApiMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
