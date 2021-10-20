// TODO: correct dangling comment
/**
 * SMSC.UA API (smsc.ua) version 1.4 (20/10/2020) SMSC's sms sender package
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SMSCSender {

    private static final boolean SMSC_HTTPS = false;
    private static final boolean SMSC_POST = false;

    private String smscLogin = "login";
    private String smscPassword = "password";
    private String smscCharset = "utf-8";
    private boolean smscDebug = false;

    public SMSCSender() {
    }

    public SMSCSender(String login, String password) {
        this.smscLogin = login;
        this.smscPassword = password;
    }

    /**
     * SMS Sending
     *
     * @param phones          - list of phones through comma or semicolon
     * @param message         - the message to be send
     * @param transliteration - converting into transliteration (0, 1 or 2)
     * @param time            - required delivery time (DDMMYYhhmm, h1-h2, 0ts, +m)
     * @param id              - message id
     * @param format          - message format (0 - common(classic) sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms, 7 - mms, 8 - mail, 9 - call, 10 - viber, 11 - soc)
     * @param sender          - sender name. To disable Sender ID pass an empty string or dot as the name
     * @param query           - additional request parameters ("valid=01:00&maxsms=3&tz=2")
     * @return array (<id>, <amount of sms>, <cost>, <account balance>) in case of successful sending
     * array (<id>, <error code>) in case of error
     */

    public String[] sendSms(String phones, String message, int transliteration, String time, String id, int format, String sender, String query) {
        final String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1", "viber=1", "soc=1"};
        String[] m = {};

        try {
            m = sendSMSCCommand("send", "cost=3&phones=" + URLEncoder.encode(phones, smscCharset)
                    + "&mes=" + URLEncoder.encode(message, smscCharset)
                    + "&translit=" + transliteration + "&id=" + id + (format > 0 ? "&" + formats[format] : "")
                    + (sender == "" ? "" : "&sender=" + URLEncoder.encode(sender, smscCharset))
                    + (time == "" ? "" : "&time=" + URLEncoder.encode(time, smscCharset))
                    + (query == "" ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {

        }

        if (m.length > 1) {
            if (smscDebug) {
                if (Integer.parseInt(m[1]) > 0) {
                    System.out.println("SMS send successful. ID: " + m[0] + ", amount of SMS: " + m[1] + ", cost: " + m[2] + ", balance: " + m[3]);
                } else {
                    System.out.print("Ошибка №" + Math.abs(Integer.parseInt(m[1])));
                    System.out.println(Integer.parseInt(m[0]) > 0 ? (", ID: " + m[0]) : "");
                }
            }
        } else {
            System.out.println("Server is not responding");
        }

        return m;
    }

    /**
     * Get SMS cost
     *
     * @param phones          - list of phones through comma or semicolon
     * @param message         - the message to be send
     * @param transliteration - converting into transliteration (0, 1 or 2)
     * @param format          - message format (0 - обычное sms, 1 - flash-sms, 2 - wap-push, 3 - hlr, 4 - bin, 5 - bin-hex, 6 - ping-sms, 7 - mms, 8 - mail, 9 - call, 10 - viber, 11 - soc)
     * @param sender          - sender name. To disable Sender ID pass an empty string or dot as the name
     * @param query           - additional request parameters ("list=79999999999:Ваш пароль: 123\n78888888888:Ваш пароль: 456")
     * @return array (<cost>, <amount of sms>) in case of successful sending
     * array (0, <error code>) in case of error
     */

    public String[] getSmsCost(String phones, String message, int transliteration, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1", "viber=1", "soc=1"};
        String[] m = {};

        try {
            m = sendSMSCCommand("send", "cost=1&phones=" + URLEncoder.encode(phones, smscCharset)
                    + "&mes=" + URLEncoder.encode(message, smscCharset)
                    + "&translit=" + transliteration + (format > 0 ? "&" + formats[format] : "")
                    + (sender == "" ? "" : "&sender=" + URLEncoder.encode(sender, smscCharset))
                    + (query == "" ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // (cost, cnt) or (0, -error)

        if (m.length > 1) {
            if (smscDebug) {
                if (Integer.parseInt(m[1]) > 0)
                    System.out.println("Cost of mailing: " + m[0] + ", Amount of SMS: " + m[1]);
            } else
                System.out.print("Error code " + Math.abs(Integer.parseInt(m[1])));
        } else {
            System.out.println("Server is not responding");
        }
        return m;
    }


    /**
     * Checking the status of a sent SMS or HLR request
     *
     * @param id    - message id
     * @param phone - phone number
     * @param all   - additionally, the elements at the end of the array are returned:
     *              (<sending time>, <phone number>, <cost>, <sender id>, <status>, <massage text>)
     * @return array
     * (<status>, <change time>, <sms error code>) for sent SMS
     * (<status>, <change time>, <sms error code>, <country code of registration>, <subscriber operator code>,
     * <country name of registration>, <subscriber operator name>, <roaming country name>, <roaming operator name>,
     * <SIM card IMSI code>, <service center number>) for HLR request
     * or array(0, <error code>) in case of error
     */

    public String[] getStatus(int id, String phone, int all) {
        String[] m = {};
        String tmp;

        try {
            m = sendSMSCCommand("status", "phone=" + URLEncoder.encode(phone, smscCharset) + "&id=" + id + "&all=" + all);

            if (m.length > 1) {
                if (smscDebug) {
                    if (m[1] != "" && Integer.parseInt(m[1]) >= 0) {
                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Integer.parseInt(m[1]));
                        System.out.println("SMS status: " + m[0]);
                    } else
                        System.out.println("Error code" + Math.abs(Integer.parseInt(m[1])));
                }

                if (all == 1 && m.length > 9 && (m.length < 14 || m[14] != "HLR")) {
                    tmp = implode(m, ",");
                    m = tmp.split(",", 9);
                }
            } else
                System.out.println("Server is not responding");
        } catch (UnsupportedEncodingException e) {

        }

        return m;
    }

    /**
     * Getting balance
     *
     * @return String balance or empty line in case of error
     */

    public String getBalance() {
        String[] m = sendSMSCCommand("balance", ""); // (balance) или (0, -error)

        if (m.length >= 1) {
            if (smscDebug) {
                if (m.length == 1) {
                    System.out.println("Balance : " + m[0]);
                } else {
                    System.out.println("Error: " + Math.abs(Integer.parseInt(m[1])));
                }
            } else {
                System.out.println("Server is not responding!");
            }
        }
        return m.length == 2 ? "" : m[0];
    }

    /**
     * Building and sending a request
     *
     * @param cmd - required command
     * @param arg - additional arguments
     */

    private String[] sendSMSCCommand(String cmd, String arg) {
        String ret = "";

        try {
            String _url = (SMSC_HTTPS ? "https" : "http") + "://smsc.ua/sys/" + cmd + ".php?login=" + URLEncoder.encode(smscLogin, smscCharset)
                    + "&psw=" + URLEncoder.encode(smscPassword, smscCharset)
                    + "&fmt=1&charset=" + smscCharset + "&" + arg;

            String url = _url;
            int i = 0;
            do {
                if (i++ > 0) {
                    url = _url;
                    url = url.replace("://smsc.ua/", "://www" + (i) + ".smsc.ua/");
                }
                ret = smscReadUrl(url);
            }
            while (ret == "" && i < 5);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret.split(",");
    }

    /**
     * URL Reading
     *
     * @param url - message ID
     * @return line - server response
     */
    private String smscReadUrl(String url) {

        String line = "", real_url = url;
        String[] param = {};
        boolean is_post = (SMSC_POST || url.length() > 2000);

        if (is_post) {
            param = url.split("\\?", 2);
            real_url = param[0];
        }

        try {
            URL u = new URL(real_url);
            InputStream is;

            if (is_post) {
                URLConnection conn = u.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), smscCharset);
                os.write(param[1]);
                os.flush();
                os.close();
                System.out.println("post");
                is = conn.getInputStream();
            } else {
                is = u.openStream();
            }

            InputStreamReader reader = new InputStreamReader(is, smscCharset);

            int ch;
            while ((ch = reader.read()) != -1) {
                line += (char) ch;
            }

            reader.close();
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }

        return line;
    }

    private String implode(String[] ary, String delim) {
        String output = "";

        for (int i = 0; i < ary.length; i++) {
            if (i != 0)
                output += delim;
            output += ary[i];
        }
        return output;
    }
}