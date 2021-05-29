package dto.operation;

public class OperationDTO {

    final private OperationTypeDTO operationType;
    final private ObjectTypeDTO objectType;
    final private Object object;
    final private long time;

    public OperationDTO(long time, OperationTypeDTO operationType, ObjectTypeDTO objectType, Object object) {
        this.time = time;
        this.object = object;
        this.objectType = objectType;
        this.operationType = operationType;
    }

    public OperationTypeDTO getOperationType() {
        return operationType;
    }

    public Object getObject() {
        return object;
    }

    public ObjectTypeDTO getObjectType() {
        return objectType;
    }

    public long getTime() {
        return time;
    }

}
