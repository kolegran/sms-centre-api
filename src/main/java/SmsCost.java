import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SmsCost {

    @JsonProperty("cost")
    private Double cost;

    @JsonProperty("cnt")
    private Double count;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsCost smsCost = (SmsCost) o;
        return Objects.equals(cost, smsCost.cost) && Objects.equals(count, smsCost.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, count);
    }

    @Override
    public String toString() {
        return "SmsCost{" +
            "cost=" + cost +
            ", count=" + count +
            '}';
    }
}
