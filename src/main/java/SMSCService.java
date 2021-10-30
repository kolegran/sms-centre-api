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
import java.nio.charset.StandardCharsets;

public class SMSCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSCService.class);
    private static final String API_DOCS_URL = "https://smsc.ua/api/http/#menu";
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final int NUMBER_OF_PATTERN_APPLYING = 2;
    private static final String SPLIT_URL_REGEX = "\\?";
    private static final boolean DEFAULT_DEBUG = false;
    private static final boolean SMSC_POST = false;
    private static final int MAX_RETRIES_COUNT = 5;
    private static final int URL_MAX_LENGTH = 2000;
    private static final String EMPTY = "";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String login;
    private final String password;
    private final String charset;
    private final Boolean debug;
    private final Protocol protocol;

    private SMSCService(String login, String password, Protocol protocol, String charset, Boolean debug) {
        this.login = login;
        this.password = password;
        this.protocol = protocol;
        this.charset = charset;
        this.debug = debug;
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
     * @return The resultant SmsCost object
     * <p>
     * (<cost>, <amount of sms>) in case of successful sending
     * <p>
     * (0, <error code>) in case of error
     */
    public SmsCost getSmsCost(String phones, String message, int transliteration, MessageFormat format, String sender, String query) {

        SmsCost response = send(ApiMethod.SEND.getMethod(), SmsCost.class,"cost=1&phones=" + encode(phones)
            + "&mes=" + encode(message)
            + "&translit=" + transliteration + "&format=" + format.getFormat()
            + (sender.isEmpty() ? "" : "&sender=" + encode(sender))
            + (query.isEmpty() ? "" : "&" + query));

        if (response.getCost() == null) {
            LOGGER.error("Server is not responding");
            throw new NoConnectionException("Server is not responding. Try again later");
        }
        return response;
    }

     /**
     * Get the status of a sent SMS or HLR request
     *
     * @param id          Message ID
     * @param phone       Phone number in international format
     * @param all         Completeness of information in the status (0, 1 or 2)
     * @return The resultant Status object
     * <p>
     * Status object in case of successful sending
     * <p>
     * (<id>, <error code>) in case of error
     */
    public Status getStatus(int id, String phone, int all) {
        return send(ApiMethod.STATUS.getMethod(), Status.class, "phone=" + encode(phone) + "&id=" + id + "&all=" + all);
    }

    /**
     * Getting account balance
     *
     * @return The resultant String balance or empty line in case of error
     */
    public Balance getBalance() { return send(ApiMethod.BALANCE.getMethod(), Balance.class, "cur=true"); }

    /**
     * Create Builder for SMSCService
     *
     * @return The resultant Builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Building and sending a request
     *
     * @param apiMethod  Required command
     * @param args Additional arguments
     * @return The resultant Generic
     * @throws CharsetEncodingException may produce by SMSCService#encode(java.lang.String)
     */
    private <T> T send(String apiMethod, Class<T> classType, String args) {
        final String url = protocol.getName() + "://smsc.ua/sys/" + apiMethod + ".php?login=" + encode(login)
            + "&psw=" + encode(password)
            + "&fmt=3&charset=" + charset + "&" + args;

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

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException exception) {
            LOGGER.error("Cannot encode the next string {}. Unsupportable charset: {}", str, charset);
            throw new CharsetEncodingException("Unsupportable charset: " + charset, exception);
        }
    }

    public static class Builder {

        private String password;
        private String login;
        private String charset;
        private Protocol protocol;
        private Boolean debug;

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder debug(Boolean debug) {
            this.debug = debug;
            return this;
        }

        public SMSCService build() {
            return new SMSCService(this.login, this.password, this.protocol, this.charset, this.debug);
        }

        public SMSCService build(String login, String password) {
            return new SMSCService(login, password, Protocol.HTTP, DEFAULT_CHARSET, DEFAULT_DEBUG);
        }
    }
}