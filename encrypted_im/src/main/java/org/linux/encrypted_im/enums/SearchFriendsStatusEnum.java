package org.linux.encrypted_im.enums;

public enum  SearchFriendsStatusEnum {

    SUCCESS(0, "OK"),
    USER_NOT_EXIST(1, "查无此人"),
    NOT_YOURSELF(2, "这是你自己啊"),
    ALREADY_FRIENDS(3, "该用户已经是你的好友啦");

    public final Integer status;
    public final String msg;

    SearchFriendsStatusEnum(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public static String getMsgByKey(Integer status) {
        for (SearchFriendsStatusEnum type : SearchFriendsStatusEnum.values()) {
            if (type.getStatus() == status) {
                return type.msg;
            }
        }
        return null;
    }
}
