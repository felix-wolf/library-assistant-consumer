package dto;

public class MailInfoDTO {

    private final String mailServer;
    private final Integer port;
    private final String emailID;
    private final String password;
    private final Boolean sslEnabled;

    public MailInfoDTO(String mailServer, Integer port, String emailID, String password, Boolean sslEnabled) {
        this.mailServer = mailServer;
        this.port = port;
        this.emailID = emailID;
        this.password = password;
        this.sslEnabled = sslEnabled;
    }

    public String getMailServer() {
        return mailServer;
    }

    public Integer getPort() {
        return port;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

}
