package dto.operation;

import dto.member.MemberDTO;
import dto.member.MemberDTOConverter;
import models.ObjectType;
import models.Operation;
import models.OperationType;

public class OperationDTOConverter {

    public static Operation toEntity(OperationDTO operationDTO) {
        switch (operationDTO.getObjectType()) {
            case MEMBER:
                MemberDTO memberDTO = (MemberDTO) operationDTO.getObject();
                return new Operation(
                        operationDTO.getTime(),
                        OperationType.valueOf(operationDTO.getOperationType().toString()),
                        ObjectType.valueOf(operationDTO.getObjectType().toString()),
                        MemberDTOConverter.toEntity(memberDTO)
                );
            case ISSUE:
            case BOOK:
            case MAIL_SERVER_INFO:
        }
        return null;

    }

}
