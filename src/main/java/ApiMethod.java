public enum ApiMethod {

    SEND("send"),
    STATUS("status"),
    BALANCE("balance"),
    ;

    private final String method;

    ApiMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
