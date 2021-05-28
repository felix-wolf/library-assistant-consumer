package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mailInfo")
public class MailInfo {

    @Id
    @GeneratedValue
    private long id;
    private String mailServer;
    private Integer port;
    private String emailId;
    private String password;
    private Boolean sslEnabled;

    public MailInfo() { }

    public MailInfo(String mailServer, Integer port, String emailId, String password, Boolean sslEnabled) {
        this.mailServer = mailServer;
        this.port = port;
        this.emailId = emailId;
        this.password = password;
        this.sslEnabled = sslEnabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }
}
