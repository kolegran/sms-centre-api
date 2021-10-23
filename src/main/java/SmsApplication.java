import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmsApplication {

    public static void main(String[] args) {
        final SMSCService smscService = new SMSCService("OKukotin","82fb0d5563570d17d1f48d0d243604823c687243", "utf-8", true);
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
