import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagingError {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_code")
    private Integer errorCode;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isError() {
        return nonNull(error) && nonNull(errorCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagingError that = (MessagingError) o;
        return Objects.equals(error, that.error) &&
            Objects.equals(errorCode, that.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errorCode);
    }

    @Override
    public String toString() {
        return "MessagingError{" +
            "error='" + error + '\'' +
            ", errorCode=" + errorCode +
            '}';
    }
}
