package com.yocto.yoclib.jsonrpc;

import org.json.JSONObject;

public class ResponseMessage extends Message{

    /**
     * @param value The object value
     */
    protected ResponseMessage(JSONObject value) {
        super(value);
    }

}