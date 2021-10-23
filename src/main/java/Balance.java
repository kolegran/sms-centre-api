import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("currency")
    private String currency;

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance that = (Balance) o;
        return Objects.equals(balance, that.balance) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, currency);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
