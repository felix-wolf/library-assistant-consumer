package dto.member;

public class MemberDTO {

    private final long id;
    private final String name;
    private final String email;
    private final String mobile;

    public MemberDTO(long id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
