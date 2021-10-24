import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmsApplication {

    public static void main(String[] args) {
        final SMSCService smscService = SMSCService.newBuilder()
            .login("OKukotin")
            .password("82fb0d5563570d17d1f48d0d243604823c687243")
            .protocol(Protocol.HTTP)
            .charset(StandardCharsets.UTF_8.name())
            .debug(true)
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
    }
}
