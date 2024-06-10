package br.com.evandro.todoList.domains.user;

public enum UserStatusEnum {

    BLOCKED ("B", "BLOCKED"),
    ACTIVE ("A", "ACTIVE"),
    PENDENT("P", "PENDENT");

    private String status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    UserStatusEnum(String status, String description) {
        setStatus(status);
        setDescription(description);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static UserStatusEnum valueOfStatus(String status){
        for(UserStatusEnum value : UserStatusEnum.values()){
            if(status.equals(value.getStatus()))
                return value;
        }
        throw new IllegalArgumentException("Illegal argument status of user");
    }
}
