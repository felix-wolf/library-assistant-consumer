package dto.member;

import models.Member;

public class MemberDTOConverter {

    public static Member toEntity(MemberDTO memberDTO) {
        return new Member(
                Integer.parseInt(memberDTO.getId()),
                memberDTO.getName(),
                memberDTO.getEmail(),
                memberDTO.getMobile()
        );
    }
}
