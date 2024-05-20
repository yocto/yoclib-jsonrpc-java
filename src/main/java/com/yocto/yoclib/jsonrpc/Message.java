package com.yocto.yoclib.jsonrpc;

import org.json.*;

public abstract class Message {

    private final JSONObject value;

    /**
     * @param value The object value
     */
    protected Message(JSONObject value) {
        this.value = value;
    }

    /**
     * @return The JSON-RPC version value
     */
    public String getJSONRPC() {
        return this.hasJSONRPC() ? this.value.getString("jsonrpc") : null;
    }

    /**
     * @return
     */
    public Object getId() {
        return this.getId(true);
    }

    /**
     * @param strictId
     * @return
     */
    public Object getId(boolean strictId) {
        return this.hasId(strictId) ? this.value.get("id") : null;
    }

    /**
     * @return
     */
    public String getMethod() {
        return this.hasMethod() ? this.value.getString("method") : null;
    }

    /**
     * @return
     */
    public Object getParams() {
        return this.hasParams() ? this.value.get("params") : null;
    }

    /**
     * @return
     */
    public Object getResult() {
        return this.hasResult() ? this.value.get("result") : null;
    }

    /**
     * @return
     */
    public Object getError() {
        return this.hasError() ? this.value.get("error") : null;
    }

    /**
     * @return
     */
    public Integer getErrorCode() {
        Object error = this.getError();
        if (error instanceof JSONObject) {
            JSONObject errorObj = (JSONObject) error;
            if (errorObj.has("code")) {
                return errorObj.getInt("code");
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String getErrorMessage() {
        Object error = this.getError();
        if (error instanceof JSONObject) {
            JSONObject errorObj = (JSONObject) error;
            if (errorObj.has("message")) {
                return errorObj.getString("message");
            }
        }
        return null;
    }

    /**
     * @return
     */
    public Object getErrorData() {
        Object error = this.getError();
        if (error instanceof JSONObject) {
            JSONObject errorObj = (JSONObject) error;
            if (errorObj.has("data")) {
                return errorObj.get("data");
            }
        }
        return null;
    }

    /**
     * @return
     */
    public boolean hasJSONRPC() {
        return this.value.has("jsonrpc") && this.value.get("jsonrpc") != null;
    }

    /**
     * @return
     */
    public boolean hasId() {
        return this.hasId(true);
    }

    /**
     * @param strictId
     * @return
     */
    public boolean hasId(boolean strictId) {
        return this.value.has("id") && (strictId?(!this.value.isNull("id")):(!Message.isFalsy(this.value.get("id"))));
    }

    /**
     * @return
     */
    public boolean hasMethod() {
        return this.value.has("method") && !this.value.isNull("method");
    }

    /**
     * @return
     */
    public boolean hasParams() {
        return this.value.has("params") && !this.value.isNull("params");
    }

    /**
     * @return
     */
    public boolean hasResult() {
        return this.value.has("result") && !this.value.isNull("result");
    }

    /**
     * @return
     */
    public boolean hasError() {
        return this.value.has("error") && !this.value.isNull("error");
    }

    /**
     * @return
     */
    public boolean isRequest() {
        return this.value.has("method") || this.value.has("params");
    }

    /**
     * @return
     */
    public boolean isNotification() {
        return this.isNotification(true);
    }

    /**
     * @param strictId
     * @return
     */
    public boolean isNotification(boolean strictId) {
        return this.isRequest() && (!this.value.has("id") || !(strictId?(!this.value.isNull("id")):(!Message.isFalsy(this.value.get("id")))));
    }

    /**
     * @return True if message is a response, false if not.
     */
    public boolean isResponse() {
        return this.value.has("result") || this.value.has("error");
    }

    /**
     * @return True if message is version 2.0, false if not.
     */
    public boolean isVersion2() {
        return "2.0".equals(this.getJSONRPC());
    }

    /**
     * @return The object value
     */
    public JSONObject toObject() {
        return this.value;
    }

    public static RequestMessage createRequestMessageV1(Object id, String method) {
        return Message.createRequestMessageV1(id, method, new JSONArray());
    }

    /**
     * @param id
     * @param method
     * @param params
     * @return
     */
    public static RequestMessage createRequestMessageV1(Object id, String method, JSONArray params) {
        return new RequestMessage(new JSONObject().put("id", id).put("method", method).put("params", params));
    }

    public static RequestMessage createRequestMessageV2(Object id, String method) throws JSONRPCException {
        return Message.createRequestMessageV2(id, method, null);
    }

    /**
     * @param id
     * @param method
     * @param params
     * @return
     */
    public static RequestMessage createRequestMessageV2(Object id, String method, Object params) throws JSONRPCException {
        JSONObject obj = new JSONObject().put("jsonrpc", "2.0").put("id", id).put("method", method);

        if (params instanceof JSONObject || params instanceof JSONArray) {
            obj.put("params", params);
        } else if (params != null) {
            throw new JSONRPCException("[V2] The \"params\" property in request MUST be an object, array or null.");

        }
        return new RequestMessage(obj);
    }

    /**
     * @param method
     * @return
     */
    public static NotificationMessage createNotificationMessageV1(String method) {
        return Message.createNotificationMessageV1(method, new JSONArray());
    }

    /**
     * @param method
     * @param params
     * @return
     */
    public static NotificationMessage createNotificationMessageV1(String method, JSONArray params) {
        return new NotificationMessage(new JSONObject().put("id", JSONObject.NULL).put("method", method).put("params", params));
    }

    /**
     * @param method
     * @return
     */
    public static NotificationMessage createNotificationMessageV2(String method) throws JSONRPCException {
        return Message.createNotificationMessageV2(method, null);
    }

    /**
     * @param method
     * @param params
     * @return
     */
    public static NotificationMessage createNotificationMessageV2(String method, Object params) throws JSONRPCException {
        JSONObject obj = new JSONObject().put("jsonrpc", "2.0").put("method", method);

        if (params instanceof JSONObject || params instanceof JSONArray) {
            obj.put("params", params);
        } else if (params != null) {
            throw new JSONRPCException("[V2] The \"params\" property in request MUST be an object, array or null.");
        }

        return new NotificationMessage(obj);
    }

    /**
     * @param id
     * @return
     */
    public static ResponseMessage createResponseMessageV1(Object id) throws JSONRPCException {
        return Message.createResponseMessageV1(id, null);
    }

    /**
     * @param id
     * @param result
     * @return
     */
    public static ResponseMessage createResponseMessageV1(Object id, Object result) throws JSONRPCException {
        return Message.createResponseMessageV1(id, result, null);
    }

    /**
     * @param id
     * @param result
     * @param error
     * @return
     */
    public static ResponseMessage createResponseMessageV1(Object id, Object result, Object error) throws JSONRPCException {
        if (result != null && error != null) {
            throw new JSONRPCException("[V1] Only one property \"result\" or \"error\" can be non null.");
        }
        if (!(error instanceof JSONObject) && !(error instanceof String) && error != null) {
            throw new JSONRPCException("[V1] The \"error\" property in request MUST be an string, object or null.");
        }
        return new ResponseMessage(new JSONObject().put("id", id).put("result", result).put("error", error));
    }

    /**
     * @param id
     * @return
     */
    public static ResponseMessage createResponseMessageV2(Object id) throws JSONRPCException {
        return Message.createResponseMessageV2(id, null);
    }

    /**
     * @param id
     * @param result
     * @return
     */
    public static ResponseMessage createResponseMessageV2(Object id, Object result) throws JSONRPCException {
        return Message.createResponseMessageV2(id, result, null);
    }

    /**
     * @param id
     * @param result
     * @param error
     * @return
     */
    public static ResponseMessage createResponseMessageV2(Object id, Object result, JSONObject error) throws JSONRPCException {
        if (result != null && error != null) {
            throw new JSONRPCException("[V2] Only one property \"result\" or \"error\" can be non null.");
        }
        JSONObject obj = new JSONObject().put("jsonrpc", "2.0").put("id", id);
        if (error != null) {
            if (!error.has("code")) {
                throw new JSONRPCException("[V2] The error object MUST have a \"code\" property.");
            }
            if (!error.has("message")) {
                throw new JSONRPCException("[V2] The error object MUST have a \"message\" property.");
            }
            if (!(error.get("code") instanceof Integer)) {
                throw new JSONRPCException("[V2] The \"code\" property of the error object MUST be an integer.");
            }
            if (!(error.get("message") instanceof String)) {
                throw new JSONRPCException("[V2] The \"message\" property of the error object MUST be a string.");
            }
            obj.put("error", error);
        } else {
            obj.put("result", result);
        }
        return new ResponseMessage(obj);
    }

    /**
     * @param object
     * @return
     */
    public static boolean isBatch(Object object) {
        return object instanceof JSONArray;
    }

    /**
     * @param object The object
     * @return JSON string
     * @throws JSONRPCException An exception
     */
    public static String encodeJSON(Object object) throws JSONRPCException {
        try {
            if (object instanceof JSONObject) {
                return ((JSONObject) object).toString(0);
            }
            if (object instanceof JSONArray) {
                return ((JSONArray) object).toString(0);
            }
            return JSONObject.valueToString(object);
        } catch (JSONException e) {
            throw new JSONRPCException("Failed to encode JSON.");
        }
    }

    /**
     * @param json JSON string
     * @return The object
     * @throws JSONRPCException An exception
     */
    public static Object decodeJSON(String json) throws JSONRPCException {
        try {
            return new JSONTokener(json).nextValue();
        } catch (JSONException $e) {
            throw new JSONRPCException("Failed to decode JSON.");
        }
    }

    /**
     * @param object
     * @return
     */
    public static Message parseObject(Object object) throws JSONRPCException {
        return Message.parseObject(object, true);
    }

    /**
     * @param object
     * @param strictId
     * @return
     */
    public static Message parseObject(Object object, boolean strictId) throws JSONRPCException {
        if (object instanceof JSONObject){
            return Message.handleMessage((JSONObject) object,strictId);
        }
        throw new JSONRPCException("A message MUST be a JSON object.");
    }

    /**
     * @param message
     * @param strictId
     * @return
     * @throws JSONRPCException
     */
    private static Message handleMessage(JSONObject message, boolean strictId) throws JSONRPCException {
        if (message.has("jsonrpc")) {
            if ("2.0".equals(message.get("jsonrpc"))) {
                return Message.handleMessageV2(message, strictId);
            }
            throw new JSONRPCException("Unknown version \"" + (message.get("jsonrpc")) + "\".");
        } else {
            return Message.handleMessageV1(message, strictId);
        }
    }

    /**
     *
     * @param message
     * @param strictId
     * @return
     */
    private static Message handleMessageV2(JSONObject message,boolean strictId) throws JSONRPCException{
        if (Message.isRequestMessage(message)) {
            Message.validateMethodProperty(message);
            if (message.has("params") && !(message.get("params") instanceof JSONArray) && !(message.get("params") instanceof JSONObject)) {
                throw new JSONRPCException("[V2] The \"params\" property MUST be an array or object if present.");
            }
            if (message.has("id") && !(message.get("id") instanceof String) && !(message.get("id") instanceof Number) && !message.isNull("id")) {
                throw new JSONRPCException("[V2] The \"id\" property MUST be an string, number or null if present.");
            }
            if (message.has("id") && (strictId?(!message.isNull("id")):(!Message.isFalsy(message.get("id"))))){
                return new RequestMessage(message);
            } else {
                return new NotificationMessage(message);
            }
        }else if(Message.isResponseMessage(message)){
            if (message.has("result") && message.has("error")){
                throw new JSONRPCException("[V2] Only one property \"result\" or \"error\" can be present.");
            }
            if(message.has("error")){
                JSONObject error = message.getJSONObject("error");
                if (!error.has("code")) {
                    throw new JSONRPCException("[V2] The error object MUST have a \"code\" property.");
                }
                if (!error.has("message")) {
                    throw new JSONRPCException("[V2] The error object MUST have a \"message\" property.");
                }
                if (!(error.get("code") instanceof Integer)) {
                    throw new JSONRPCException("[V2] The \"code\" property of the error object MUST be an integer.");
                }
                if (!(error.get("message") instanceof String)) {
                    throw new JSONRPCException("[V2] The \"message\" property of the error object MUST be a string.");
                }
            }
            if(message.has("id")){
                return new ResponseMessage(message);
            } else {
                throw new JSONRPCException("[V2] Missing \"id\" property in response.");
            }
        }else{
            throw new JSONRPCException("[V2] Unknown message type.");
        }
    }


    /**
     * @param message
     * @return
     */
    private static boolean isRequestMessage(JSONObject message) {
        return message.has("method") || message.has("params");
    }

    /**
     * @param message
     * @return
     */
    private static boolean isResponseMessage(JSONObject message) {
        return message.has("result") || message.has("error");
    }

    /**
     * @param message
     * @throws JSONRPCException
     */
    private static void validateMethodProperty(JSONObject message) throws JSONRPCException {
        if (!message.has("method")) {
            throw new JSONRPCException("Missing \"method\" property in request.");
        }
        if (!(message.get("method") instanceof String)) {
            throw new JSONRPCException("The \"method\" property in request MUST be a string.");
        }
    }

    /**
     * @param message
     * @throws JSONRPCException
     */
    private static void validateParamsPropertyV1(JSONObject message) throws JSONRPCException {
        if (!message.has("params")) {
            throw new JSONRPCException("[V1] Missing \"params\" property in request.");
        }
        if (!(message.get("params") instanceof JSONArray)) {
            throw new JSONRPCException("[V1] The \"params\" property in request MUST be an array.");
        }
    }

    /**
     * @param message
     * @throws JSONRPCException
     */
    private static void validateResultPropertyV1(JSONObject message) throws JSONRPCException {
        if (!message.has("result")) {
            throw new JSONRPCException("[V1] Missing \"result\" property in request.");
        }
    }

    /**
     *
     * @param message
     * @throws JSONRPCException
     */
    private static void validateErrorPropertyV1(JSONObject message) throws JSONRPCException {
        if (!message.has("error")) {
            throw new JSONRPCException("[V1] Missing \"error\" property in request.");
        }
        if (!(message.get("error") instanceof JSONObject) && !(message.get("error") instanceof String) && !message.isNull("error")) {
            throw new JSONRPCException("[V1] The \"error\" property in request MUST be an string, object or null.");
        }
    }

    /**
     *
     * @param message
     * @param strictId
     * @return
     */
    private static Message handleMessageV1(JSONObject message,boolean strictId) throws JSONRPCException{
        if(Message.isRequestMessage(message)){
            Message.validateMethodProperty(message);
            Message.validateParamsPropertyV1(message);

            if(message.has("id") && (strictId?(!message.isNull("id")):(!Message.isFalsy(message.get("id"))))){
                return new RequestMessage(message);
            } else {
                return new NotificationMessage(message);
            }
        }else if(Message.isResponseMessage(message)){
            Message.validateResultPropertyV1(message);
            Message.validateErrorPropertyV1(message);
            if(!message.isNull("result") && !message.isNull("error")){
                throw new JSONRPCException("[V1] Only one property \"result\" or \"error\" can be non null.");
            }

            if(message.has("id")){
                return new ResponseMessage(message);
            }else{
                throw new JSONRPCException("[V1] Missing \"id\" property in response.");
            }
        }else{
            throw new JSONRPCException("[V1] Unknown message type.");
        }
    }

    private static boolean isFalsy(Object value){
        if(value==null){
            return true;
        }
        if(JSONObject.NULL.equals(value)){
            return true;
        }
        if(value instanceof Boolean){
            boolean b = (boolean) value;
            return !b;
        }
        if(value instanceof Number){
            Number n = (Number) value;
            if(n.byteValue()==0){
                return true;
            }
            if(n.shortValue()==0){
                return true;
            }
            if(n.intValue()==0){
                return true;
            }
            if(n.longValue()==0){
                return true;
            }
            if(n.floatValue()==0.0f){
                return true;
            }
            if(n.doubleValue()==0.0f){
                return true;
            }
            return false;
        }
        if(value instanceof String){
            String s = (String) value;
            return s.isEmpty();
        }
        return false;
    }

}