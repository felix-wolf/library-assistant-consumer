package dto.operation;

import com.google.gson.*;
import dto.mailInfo.MailInfoDTO;
import dto.member.MemberDTO;

import java.lang.reflect.Type;

public class OperationDTODeserializer implements JsonDeserializer<OperationDTO> {

    @Override
    public OperationDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        OperationTypeDTO operationTypeDTO = OperationTypeDTO.valueOf(jsonObject.get("operationType").getAsString());
        ObjectTypeDTO objectTypeDTO = ObjectTypeDTO.valueOf(jsonObject.get("objectType").getAsString());
        JsonObject objectAsJson = jsonObject.get("object").getAsJsonObject();
        switch (objectTypeDTO) {
            case MEMBER:
                return new OperationDTO(
                        jsonObject.get("time").getAsLong(),
                        operationTypeDTO,
                        objectTypeDTO,
                        operationTypeDTO == OperationTypeDTO.DELETE ?
                                new MemberDTO(objectAsJson.get("id").getAsString())
                                :
                                new MemberDTO(
                                        objectAsJson.get("id").getAsString(),
                                        objectAsJson.get("name").getAsString(),
                                        objectAsJson.get("email").getAsString(),
                                        objectAsJson.get("mobile").getAsString()
                                )
                );

            case MAIL_SERVER_INFO:
                return new OperationDTO(
                        jsonObject.get("time").getAsLong(),
                        operationTypeDTO,
                        objectTypeDTO,
                        new MailInfoDTO(
                                objectAsJson.get("mailServer").getAsString(),
                                objectAsJson.get("port").getAsInt(),
                                objectAsJson.get("emailId").getAsString(),
                                objectAsJson.get("password").getAsString(),
                                objectAsJson.get("sslEnabled").getAsBoolean()
                        )
                );
        }
        return null;
    }
}