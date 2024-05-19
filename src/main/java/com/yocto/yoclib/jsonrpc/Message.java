package com.yocto.yoclib.jsonrpc;

import org.json.JSONObject;

public abstract class Message{

    private final JSONObject value;

    /**
     *
     * @param value The object value
     */
    protected Message(JSONObject value){
        this.value = value;
    }

    /**
     *
     * @return The JSON-RPC version value
     */
    public String getJSONRPC(){
        return this.hasJSONRPC()?this.value.getString("jsonrpc"):null;
    }

    /**
     * @return bool
     */
    public boolean hasJSONRPC(){
        return this.value.has("jsonrpc") && this.value.get("jsonrpc")!=null;
    }

    /**
     *
     * @return True if message is a request, false if not.
     */
    public boolean isRequest(){
        return this.value.has("method") || this.value.has("params");
    }

    /**
     *
     * @return True if message is a response, false if not.
     */
    public boolean isResponse(){
        return this.value.has("result") || this.value.has("error");
    }

    /**
     *
     * @return True if message is version 2.0, false if not.
     */
    public boolean isVersion2(){
        return "2.0".equals(this.getJSONRPC());
    }

    /**
     *
     * @return The object value
     */
    public JSONObject toObject(){
        return this.value;
    }

}