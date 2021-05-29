package dto.mailInfo;

import models.MailInfo;

public class MailInfoDTOConverter {

    public static MailInfo toEntity(MailInfoDTO mailInfoDTO) {
        return new MailInfo(
                mailInfoDTO.getMailServer(),
                mailInfoDTO.getPort(),
                mailInfoDTO.getEmailId(),
                mailInfoDTO.getPassword(),
                mailInfoDTO.getSslEnabled()
        );
    }

}
