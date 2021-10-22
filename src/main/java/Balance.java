public class Balance {

    private final Double value;
    private final String currency;

    Balance(String value, String currency) {
        this.value = Double.parseDouble(value);
        this.currency = currency;
    }

    public double getValue() {
        return this.value;
    }

    public String getCurrency() {
        return this.currency;
    }
}
