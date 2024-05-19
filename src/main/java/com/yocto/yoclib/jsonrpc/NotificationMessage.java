package com.yocto.yoclib.jsonrpc;

import org.json.JSONObject;

public class NotificationMessage extends RequestMessage{

    /**
     * @param value The object value
     */
    protected NotificationMessage(JSONObject value) {
        super(value);
    }

}