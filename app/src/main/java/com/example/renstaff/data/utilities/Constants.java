package com.example.renstaff.data.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_STATUS = "status";
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "renPreference";
    public static final String KEY_USER_AUTH_ID = "userAuthId";
    public static final String KEY_USER_AUTH_TOKEN = "userToken";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SIGNED_IN = "signedIn";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timeStamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_SENDER_LAST_NAME = "senderLastName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_RECEIVER_LAST_NAME = "receiverLastName";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTEXT_TYPE = "ContextType";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAABz6tSRc:APA91bHaSft68EOGzHMqMnyq4fhW03uceX165WugcPbXvAKBB01-G9t7ppZYJRUI0OQ66KQV36ULc_winP_vNbHFIMoQ5upAeVqJJI9u_HSzgxZgrGM11dDb51C7NOseSiyO9zjUkm4J");
            remoteMsgHeaders.put(REMOTE_MSG_CONTEXT_TYPE, "authorization/json");
        }
        return remoteMsgHeaders;
    }

}
