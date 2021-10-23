import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.APIServerException;
import exception.CannotParseMessageException;
import exception.CannotSendMessageException;
import exception.CharsetEncodingException;
import exception.InterruptSendingException;
import exception.NoConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SMSCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSCService.class);
    private static final String API_DOCS_URL = "https://smsc.ua/api/http/#menu";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final int NUMBER_OF_PATTERN_APPLYING = 2;
    private static final String SPLIT_URL_REGEX = "\\?";
    private static final boolean SMSC_HTTPS = false;
    private static final boolean SMSC_POST = false;
    private static final int MAX_RETRIES_COUNT = 5;
    private static final int URL_MAX_LENGTH = 2000;
    private static final String EMPTY = "";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
     * @param phones          List of phones through comma or semicolon
     * @param message         The message to be send
     * @param transliteration Converting into transliteration (0, 1 or 2)
     * @param time            Required delivery time (DDMMYYhhmm, h1-h2, 0ts, +m)
     * @param id              Message ID
     * @param messageFormat   MessageFormat
     * @param sender          Sender name. To disable Sender ID pass an empty string or dot as the name
     * @param query           Additional request parameters
     * @return The resultant MailingCost object
     * <p>
     * (<id>, <amount of sms>, <cost>, <balance>) in case of successful sending
     * <p>
     * (<id>, <error code>) in case of error
     */
    public MailingCost send(String phones, String message, int transliteration, String time, String id, MessageFormat messageFormat, String sender, String query) {
        final String format = messageFormat.getFormat();
        final String formatToSend = format.isEmpty() ? EMPTY : "&" + format;

        return send(ApiMethod.SEND.getMethod(), MailingCost.class, "cost=1&phones=" + encode(phones)
            + "&mes=" + encode(message)
            + "&translit=" + transliteration + "&id=" + id + formatToSend
            + (sender.isEmpty() ? EMPTY : "&sender=" + encode(sender))
            + (time.isEmpty() ? EMPTY : "&time=" + encode(time))
            + (query.isEmpty() ? EMPTY : "&" + encode(query)));
    }

    /**
     * Get SMS cost
     *
     * @param phones          List of phones through comma or semicolon
     * @param message         The message to be send
     * @param transliteration Converting into transliteration (0, 1 or 2)
     * @param format          MessageFormat
     * @param sender          Sender name. To disable Sender ID pass an empty string or dot as the name
     * @param query           Additional request parameters
     * @return The resultant String array
     * <p>
     * (<cost>, <amount of sms>) in case of successful sending
     * <p>
     * (0, <error code>) in case of error
     */
    public String[] getSmsCost(String phones, String message, int transliteration, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1", "viber=1", "soc=1"};
        String[] m = {};

        m = send(ApiMethod.SEND.getMethod(), null,"cost=1&phones=" + encode(phones)
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
     * Get the status of a sent SMS or HLR request
     *
     * @param id    Message ID
     * @param phone Phone number
     * @param all   Additionally, the elements at the end of the array are returned:
     *              (<sending time>, <phone number>, <cost>, <sender id>, <status>, <massage text>)
     * @return The resultant String array
     * <p>
     * (<status>, <change time>, <sms error code>) for sent SMS
     * <p>
     * (<status>, <change time>, <sms error code>, <country code of registration>, <subscriber operator code>,
     * <country name of registration>, <subscriber operator name>, <roaming country name>, <roaming operator name>,
     * <SIM card IMSI code>, <service center number>) for HLR request
     * <p>
     * (0, <error code>) in case of error
     */
    public String[] getStatus(int id, String phone, int all) {
        String[] m = {};
        String tmp;

        m = send(ApiMethod.STATUS.getMethod(), null, "phone=" + encode(phone) + "&id=" + id + "&all=" + all);

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
     * Getting account balance
     *
     * @return The resultant String balance or empty line in case of error
     */
    public Balance getBalance() { return send(ApiMethod.BALANCE.getMethod(), Balance.class, "cur=true"); }

    /**
     * Building and sending a request
     *
     * @param apiMethod  Required command
     * @param args Additional arguments
     * @return The resultant Generic
     * @throws CharsetEncodingException may produce by SMSCService#encode(java.lang.String)
     */
    private <T> T send(String apiMethod, Class<T> classType, String args) {
        // TODO: check case with https
        final String protocol = SMSC_HTTPS ? "https" : "http";

        final String url = protocol + "://smsc.ua/sys/" + apiMethod + ".php?login=" + encode(smscLogin)
            + "&psw=" + encode(smscPassword)
            + "&fmt=3&charset=" + smscCharset + "&" + args;

        return send(url, classType, 0);
    }

    /**
     * Select URL to another server (ex. www2.smsc.ua) and then calling SMSCService#send(java.lang.String)
     *
     * @param url          API URL
     * @param retriesCount Count of retries
     * @return The resultant Generic
     */
    private <T> T send(String url, Class<T> classType, int retriesCount) {
        if (retriesCount == MAX_RETRIES_COUNT) {
            LOGGER.error("Cannot connect to any server. RetriesCount: {}. Url: {}", retriesCount, url);
            throw new NoConnectionException("Server is not responding. Try again later");
        }

        final String urlToSend = retriesCount > 0
            ? url.replace("://smsc.ua/", "://www" + retriesCount + ".smsc.ua/")
            : url;

        try {
            return send(urlToSend, classType);
        } catch (CannotSendMessageException | InterruptSendingException e) {
            LOGGER.info("Cannot connect to the next server: {}. Retrying...", urlToSend);
            return send(url, classType, retriesCount + 1);
        }
    }

    /**
     * Send Http Request
     *
     * @param url API URL
     * @return The resultant Generic
     */
    private <T> T send(String url, Class<T> classType) {
        try {
            final HttpResponse<String> response = httpClient.send(getHttpRequest(url), HttpResponse.BodyHandlers.ofString());
            final MessagingError messagingError = parseError(response.body());
            if (messagingError.isError()) {
                LOGGER.error("During message sending for URL: {} \n the next error occurred: {}.\n The error code is {}. For details see API docs reference: {}",
                    url,
                    messagingError.getError(),
                    messagingError.getErrorCode(),
                    API_DOCS_URL
                );
                throw new APIServerException(messagingError.getError());
            }
            return objectMapper.readValue(response.body(), classType);
        } catch (JacksonException exception) {
            LOGGER.error("Cannot parse response body");
            throw new CannotParseMessageException("Exception occurred during response parsing", exception);
        } catch (IOException exception) {
            LOGGER.error("Cannot send message to URL: {}", url);
            throw new CannotSendMessageException("Exception occurred during message sending for url: " + url, exception);
        } catch (InterruptedException exception) {
            LOGGER.error("Interrupted message sending to URL: {}", url);
            Thread.currentThread().interrupt();
            throw new InterruptSendingException("Message sending has been interrupted", exception);
        }
    }

    /**
     * Create HttpRequest. Select POST or GET method by checking SMSC_POST or length of URL
     *
     * @param url API URL
     * @return The resultant HttpRequest
     */
    private HttpRequest getHttpRequest(String url) {
        final HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
        if (isPost(url.length())) {
            // TODO: find use case for this way
            final String[] splittedUrl = url.split(SPLIT_URL_REGEX, NUMBER_OF_PATTERN_APPLYING);
            final String context = splittedUrl[0];
            final String requestParameters = splittedUrl[1];
            httpRequestBuilder
                .uri(URI.create(context))
                .POST(HttpRequest.BodyPublishers.ofString(requestParameters));
        } else {
            httpRequestBuilder
                .uri(URI.create(url))
                .GET();
        }
        return httpRequestBuilder
            .header("Content-Type", CONTENT_TYPE_VALUE)
            .build();
    }

    private boolean isPost(int urlLength) {
        return SMSC_POST || urlLength > URL_MAX_LENGTH;
    }

    private MessagingError parseError(String responseBody) throws JsonProcessingException {
        return objectMapper.readValue(responseBody, MessagingError.class);
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

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, smscCharset);
        } catch (UnsupportedEncodingException exception) {
            LOGGER.error("Cannot encode the next string {}. Unsupportable charset: {}", str, smscCharset);
            throw new CharsetEncodingException("Unsupportable charset: " + smscCharset, exception);
        }
    }
}