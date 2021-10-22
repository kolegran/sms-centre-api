public enum MessageFormat {

    SMS("sms"),
    FLASH("flash"),
    PUSH("push"),
    HLR("hlr"),
    BIN("bin"),
    PING("ping"),
    MMS("mms"),
    MAIL("mail"),
    CALL("call"),
    VIBER("viber"),
    SOC("soc"),
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
