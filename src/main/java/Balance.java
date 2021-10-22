public class Balance {

    private final double value;
    private final String currency;

    Balance(){
        this.value = 0.0;
        this.currency = "Conditional Unit";
    }

    Balance(String[] balance) {
        this.value = Double.parseDouble(balance[0]);
        this.currency = balance[1];
    }

    public double getValue() {
        return this.value;
    }

    public String getCurrency() {
        return this.currency;
    }
}
