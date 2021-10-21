import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SMSCService {

    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String EMPTY_RESPONSE = "";
    private static final boolean SMSC_HTTPS = false;
    private static final boolean SMSC_POST = false;
    private static final int MAX_RETRIES_COUNT = 5;
    private static final String EMPTY = "";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private String smscLogin = "login";
    private String smscPassword = "password";
    private String smscCharset = "utf-8";
    private boolean smscDebug = false;

    public SMSCService() {
    }

    public SMSCService(String login, String password) {
        this.smscLogin = login;
        this.smscPassword = password;
    }

    public SMSCService(String login, String password, String charset) {
        this.smscLogin = login;
        this.smscPassword = password;
        this.smscCharset = charset;
    }

    public SMSCService(String login, String password, String charset, boolean debug) {
        this.smscLogin = login;
        this.smscPassword = password;
        this.smscCharset = charset;
        this.smscDebug = debug;
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
        final String[] formats = {"", "sms=1", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1", "viber=1", "soc=1"};
        String[] m = {};

        m = send("send", "cost=0&phones=" + encode(phones)
            + "&mes=" + encode(message)
            + "&translit=" + transliteration + "&id=" + id + (format > 0 ? "&" + formats[format] : "")
            + (sender == "" ? "" : "&sender=" + encode(sender))
            + (time == "" ? "" : "&time=" + encode(time))
            + (query == "" ? "" : "&" + query));

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

        m = send("send", "cost=1&phones=" + encode(phones)
            + "&mes=" + encode(message)
            + "&translit=" + transliteration + (format > 0 ? "&" + formats[format] : "")
            + (sender == "" ? "" : "&sender=" + encode(sender))
            + (query == "" ? "" : "&" + query));

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

        m = send("status", "phone=" + encode(phone) + "&id=" + id + "&all=" + all);

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

        return m;
    }

    /**
     * Getting balance
     *
     * @return String balance or empty line in case of error
     */
    public String getBalance() {
        String[] m = send("balance", "");

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
     * @param cmd
     *        Required command
     * @param args
     *        Additional arguments
     *
     * @return The resultant String array
     *
     * @exception CharsetEncodingException may produce encode(String) method
     */
    private String[] send(String cmd, String args) {
        final String protocol = SMSC_HTTPS ? "https" : "http";

        final String url = protocol + "://smsc.ua/sys/" + cmd + ".php?login=" + encode(smscLogin)
            + "&psw=" + encode(smscPassword)
            + "&fmt=1&charset=" + smscCharset + "&" + args;

        return send(url, 0).split(",");
    }

    private String send(String url, int retriesCount) {
        if (retriesCount == MAX_RETRIES_COUNT) {
            return EMPTY_RESPONSE;
        }

        final String urlToSend = retriesCount > 0
            ? url.replace("://smsc.ua/", "://www" + retriesCount + ".smsc.ua/") // select URL to another server, ex. www2.smsc.ua
            : url;

        final String response = smscReadUrl(urlToSend);
        if (EMPTY.equals(response)) {
            return send(url, retriesCount + 1);
        }
        return response;
    }

    /**
     * URL Reading
     *
     * @param url
     *        Message ID
     *
     * @return The resultant String
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
        String out = "";

        for (int i = 0; i < ary.length; i++) {
            if (i != 0)
                out += delim;
            out += ary[i];
        }

        return out;
    }

    private String send(String url) {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", CONTENT_TYPE_VALUE)
            .build();
        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException exception) {
            throw new CannotSendMessageException("Exception occurred during message sending for url: " + url, exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new InterruptSendingException("Message sending has been interrupted", exception);
        }
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, smscCharset);
        } catch (UnsupportedEncodingException exception) {
            throw new CharsetEncodingException("Unsupportable SMSC Charset", exception);
        }
    }

    private static final class CharsetEncodingException extends RuntimeException {

        public CharsetEncodingException(String message, Exception exception) {
            super(message, exception);
        }
    }

    private static final class CannotSendMessageException extends RuntimeException {

        public CannotSendMessageException(String message, Exception exception) {
            super(message, exception);
        }
    }

    private static final class InterruptSendingException extends RuntimeException {

        public InterruptSendingException(String message, Exception exception) {
            super(message, exception);
        }
    }
}