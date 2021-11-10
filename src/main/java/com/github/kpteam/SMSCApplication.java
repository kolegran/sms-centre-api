package com.github.kpteam;

import com.github.kpteam.simple.MailingCost;
import com.github.kpteam.simple.SMSCService;
import com.github.kpteam.simple.SmsCost;
import com.github.kpteam.simple.Status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SMSCApplication {

    public static void main(String[] args) {
        final SMSCService smscService = SMSCService.newBuilder()
            .login("OKukotin")
            .password("82fb0d5563570d17d1f48d0d243604823c687243")
            .protocol(Protocol.HTTP)
            .charset(StandardCharsets.UTF_8.name())
            .build();

        System.out.println(smscService.getBalance());

        final MailingCost mailingCost = smscService.send(
            "+380984327826, +380931010800",
            "Test message",
            0,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyhhmm")),
            "Message0",
            MessageFormat.SMS,
            "Kolegran",
            ""
        );
        System.out.println(mailingCost);

        final Status status = smscService.getStatus(1, "+380633660341", 1);
        System.out.println(status.toString());

        final SmsCost smsCost = smscService.getSmsCost(
            "+380984327826, +380931010800",
            "Something",
            0,
            MessageFormat.WITHOUT_FORMAT,
            "+380633660341",
            ""
        );
        System.out.println(smsCost.toString());
    }
}
