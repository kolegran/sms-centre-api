public enum MessageFormat {

    SMS("sms=1"),
    VIBER("viber=1"),
    FLASH_ORDINARY("flash=0"),
    FLASH("flash=1"),
    PUSH_ORDINARY("push=0"),
    WAP_PUSH("push=1"),
    HLR_ORDINARY("hlr=0"),
    HLR("hlr=1"),
    BIN_ORDINARY("bin=0"),
    BIN("bin=1"),
    BIN_HEX("bin=2"),
    PING_ORDINARY("ping=0"),
    PING_SMS("ping=1"),
    MMS_ORDINARY("mms=0"),
    MMS("mms=1"),
    E_MAIL_ORDINARY("mail=0"),
    E_MAIL("mail=1"),
    CALL_ORDINARY("call=0"),
    CALL("call=1"),
    SOC_ORDINARY("soc=0"),
    SOC("soc=1"),
    WITHOUT_FORMAT("")
    ;

    private final String format;

    MessageFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
