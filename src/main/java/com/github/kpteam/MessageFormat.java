package com.github.kpteam;

/**
 * Enumeration used with com.github.kpteam.simple.SMSCService class for choice
 * format of message that will be sent to recipient
 */
public enum MessageFormat {

    /**
     * The argument in HTTP request which means choice SMS messaging format
     */
    SMS("sms=1"),

    /**
     * The argument in HTTP request which means sending a message via Viber
     */
    VIBER("viber=1"),

    /**
     * The default value of "flash" flag which means sending simple message
     */
    FLASH_ORDINARY("flash=0"),

    /**
     * The argument in HTTP request which means sending
     * a flash message that appears on the recipient's phone screen
     */
    FLASH("flash=1"),

    /**
     * The default value of "push" flag which means sending simple message
     */
    PUSH_ORDINARY("push=0"),    // TODO: what a simple message. Check this case

    /**
     * The argument in HTTP request which means sending a push-notification
     */
    WAP_PUSH("push=1"),

    /**
     * The default value of "hlr" flag which means sending simple message
     */
    HLR_ORDINARY("hlr=0"),

    /**
     * The argument in HTTP request which means sending a HLR request
     */
    HLR("hlr=1"),

    /**
     * The default value of "bin" flag which means sending simple message
     */
    BIN_ORDINARY("bin=0"),

    /**
     * The argument in HTTP request which means sending a binary message
     */
    BIN("bin=1"),

    /**
     * The argument in HTTP request which means sending a binary message in hex format
     */
    BIN_HEX("bin=2"),

    /**
     * The default value of "ping" flag which means sending simple message
     */
    PING_ORDINARY("ping=0"),

    /**
     * The argument in HTTP request which means sending
     * a ping-SMS that does not appear on the recipient's phone
     */
    PING_SMS("ping=1"),

    /**
     * The default value of "mms" flag which means sending simple message
     */
    MMS_ORDINARY("mms=0"),

    /**
     * The argument in HTTP request which means sending a message with attachment
     */
    MMS("mms=1"),

    /**
     * The default value of "mail" flag which means sending simple message
     */
    E_MAIL_ORDINARY("mail=0"),

    /**
     * The argument in HTTP request which means sending an email
     */
    E_MAIL("mail=1"),

    /**
     * The default value of "call" flag which means sending simple message
     */
    CALL_ORDINARY("call=0"),

    /**
     * The argument in HTTP request which means sending
     * a voice-generated message or audio file
     */
    CALL("call=1"),

    /**
     * The default value of "soc" flag which means sending simple message
     */
    SOC_ORDINARY("soc=0"),

    /**
     * The argument in HTTP request which means sending a social network message
     */
    SOC("soc=1"),

    /**
     * The value that uses in HTTP request for use messages without format
     */
    WITHOUT_FORMAT("")  // TODO: check this case
    ;

    private final String format;

    MessageFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
