package com.yocto.yoclib.jsonrpc;

import org.json.*;

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

    /**
     *
     * @param object The object
     * @return JSON string
     * @throws JSONRPCException An exception
     */
    public static String encodeJSON(Object object) throws JSONRPCException {
        try{
            if(object instanceof JSONObject){
                return ((JSONObject) object).toString(0);
            }
            if(object instanceof JSONArray){
                return ((JSONArray) object).toString(0);
            }
            return JSONObject.valueToString(object);
        }catch(JSONException e){
            throw new JSONRPCException("Failed to encode JSON.");
        }
    }

    /**
     *
     * @param json JSON string
     * @return The object
     * @throws JSONRPCException An exception
     */
    public static Object decodeJSON(String json) throws JSONRPCException {
        try{
            return new JSONTokener(json).nextValue();
        }catch(JSONException $e){
            throw new JSONRPCException("Failed to decode JSON.");
        }
    }

}