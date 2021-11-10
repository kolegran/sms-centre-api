package com.github.kpteam.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsCost {

    @JsonProperty("cost")
    private Double cost;

    @JsonProperty("cnt")
    private Double messagesCount;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(Double messagesCount) {
        this.messagesCount = messagesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsCost smsCost = (SmsCost) o;
        return Objects.equals(cost, smsCost.cost) &&
            Objects.equals(messagesCount, smsCost.messagesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, messagesCount);
    }

    @Override
    public String toString() {
        return "com.github.kpteam.simple.SmsCost{" +
            "cost=" + cost +
            ", count=" + messagesCount +
            '}';
    }
}
