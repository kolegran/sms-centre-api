public class SmsApplication {

    public static void main(String[] args) {
        SMSCService smscService = new SMSCService("OKukotin","82fb0d5563570d17d1f48d0d243604823c687243", "utf-8", true);
        smscService.getBalance();
    }
}
