package com.yocto.yoclib.jsonrpc;

import org.json.JSONObject;

public class RequestMessage extends Message{

    /**
     * @param value The object value
     */
    protected RequestMessage(JSONObject value) {
        super(value);
    }

}