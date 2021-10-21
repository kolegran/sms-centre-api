public class SmsApplication {

    public static void main(String[] args) {
        SMSCSender acc = new SMSCSender("OKukotin","82fb0d5563570d17d1f48d0d243604823c687243", "utf-8", true);
        acc.getBalance();
    }
}
