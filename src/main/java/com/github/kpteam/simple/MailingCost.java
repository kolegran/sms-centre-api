package com.github.kpteam.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MailingCost {

    @JsonProperty("id")
    private String id;

    @JsonProperty("cost")
    private Double cost;

    @JsonProperty("cnt")
    private Integer messagesCount;

    @JsonProperty("balance")
    private Double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(Integer messagesCount) {
        this.messagesCount = messagesCount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailingCost that = (MailingCost) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(messagesCount, that.messagesCount) &&
            Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, messagesCount, balance);
    }

    @Override
    public String toString() {
        return "com.github.kpteam.simple.MailingCost{" +
            "id='" + id + '\'' +
            ", cost=" + cost +
            ", messagesCount=" + messagesCount +
            ", balance=" + balance +
            '}';
    }
}
