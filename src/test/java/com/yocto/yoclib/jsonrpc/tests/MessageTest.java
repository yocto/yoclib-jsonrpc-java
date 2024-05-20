package com.yocto.yoclib.jsonrpc.tests;

import com.yocto.yoclib.jsonrpc.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest{

    @Test
    public void testDecodeEmptyJSON(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.decodeJSON(""));

        assertEquals("Failed to decode JSON.",t.getMessage());
    }

    @Test
    public void testDecodeJSONString() throws JSONRPCException {
        assertEquals("abc",Message.decodeJSON("\"abc\""));
    }

    @Test
    public void testDecodeJSONObject() throws JSONRPCException{
        assertTrue(new JSONObject().similar(Message.decodeJSON("{}")));
    }

    @Test
    public void testDecodeJSONArray() throws JSONRPCException{
        assertTrue(new JSONArray().similar(Message.decodeJSON("[]")));
    }

    @Test
    public void testEncodeJSONString() throws JSONRPCException{
        assertEquals("\"abc\"",Message.encodeJSON("abc"));
    }

    @Test
    public void testEncodeJSONObject() throws JSONRPCException{
        assertEquals("{}",Message.encodeJSON(new JSONObject()));
    }

    @Test
    public void testEncodeJSONArray() throws JSONRPCException{
        assertEquals("[]",Message.encodeJSON(new JSONArray()));
    }

    @Test
    public void testIsBatch(){
        assertTrue(Message.isBatch(new JSONArray()));

        assertFalse(Message.isBatch("abc"));
        assertFalse(Message.isBatch(true));
        assertFalse(Message.isBatch(false));
        assertFalse(Message.isBatch(123));
        assertFalse(Message.isBatch(123.456));
        assertFalse(Message.isBatch(new JSONObject()));
    }

    @Test
    public void testParseObjectString(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject("abc"));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseObjectTrue(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(true));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseObjectFalse(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(false));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseObjectInteger(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(123));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseObjectFloat(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(123.456));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseObjectArray(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONArray()));

        assertEquals("A message MUST be a JSON object.",t.getMessage());
    }

    @Test
    public void testParseEmptyObject(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject()));

        assertEquals("[V1] Unknown message type.",t.getMessage());
    }

    @Test
    public void testParseRequestV1WithMethod(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("method",JSONObject.NULL)));

        assertEquals("The \"method\" property in request MUST be a string.",t.getMessage());
    }

    @Test
    public void testParseRequestV2WithMethod(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("method",JSONObject.NULL)));

        assertEquals("The \"method\" property in request MUST be a string.",t.getMessage());
    }

    @Test
    public void testParseRequestV1WithMethodString(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("method","abc")));

        assertEquals("[V1] Missing \"params\" property in request.",t.getMessage());
    }

    @Test
    public void testParseRequestV2WithMethodString() throws JSONRPCException{
        assertInstanceOf(RequestMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("method","abc")));
    }

    @Test
    public void testParseRequestV1WithParams(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("params",JSONObject.NULL)));

        assertEquals("Missing \"method\" property in request.",t.getMessage());
    }

    @Test
    public void testParseRequestV2WithParams(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("params",JSONObject.NULL)));

        assertEquals("Missing \"method\" property in request.",t.getMessage());
    }

    @Test
    public void testParseRequestV1WithMethodStringAndParams(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("method","abc").put("params",JSONObject.NULL)));

        assertEquals("[V1] The \"params\" property in request MUST be an array.",t.getMessage());
    }

    @Test
    public void testParseRequestV2WithMethodStringAndParams(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("method","abc").put("params",JSONObject.NULL)));

        assertEquals("[V2] The \"params\" property MUST be an array or object if present.",t.getMessage());
    }

    @Test
    public void testParseRequestV2WithMethodStringAndParamsFalse(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("id",false).put("method","abc").put("params",new JSONArray())));

        assertEquals("[V2] The \"id\" property MUST be an string, number or null if present.",t.getMessage());
    }

    @Test
    public void testParseRequestV1WithMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("method","abc").put("params",new JSONArray())));
    }

    @Test
    public void testParseRequestV2WithMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("method","abc").put("params",new JSONArray())));
    }

    @Test
    public void testParseRequestV1WithIdNullAndMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("id",JSONObject.NULL).put("method","abc").put("params",new JSONArray())));
    }

    @Test
    public void testParseRequestV2WithIdNullAndMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("id",JSONObject.NULL).put("method","abc").put("params",new JSONArray())));
    }

    @Test
    public void testParseRequestV1WithIdFalsyAndMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(RequestMessage.class,Message.parseObject(new JSONObject().put("id",false).put("method","abc").put("params",new JSONArray())));
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("id",false).put("method","abc").put("params",new JSONArray()),false));
    }

    @Test
    public void testParseRequestV2WithIdFalsyAndMethodStringAndParamsArray() throws JSONRPCException{
        assertInstanceOf(RequestMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("id",0).put("method","abc").put("params",new JSONArray())));
        assertInstanceOf(NotificationMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("id",0).put("method","abc").put("params",new JSONArray()),false));
    }

    @Test
    public void testParseResponseV1WithResult(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("result",JSONObject.NULL)));

        assertEquals("[V1] Missing \"error\" property in request.",t.getMessage());
    }

    @Test
    public void testParseResponseV1WithError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("error",JSONObject.NULL)));

        assertEquals("[V1] Missing \"result\" property in request.",t.getMessage());
    }

    @Test
    public void testParseResponseV1WithResultAndError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("result","abc").put("error","def")));

        assertEquals("[V1] Only one property \"result\" or \"error\" can be non null.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultAndError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("result","abc").put("error","def")));

        assertEquals("[V2] Only one property \"result\" or \"error\" can be present.",t.getMessage());
    }

    @Test
    public void testParseResponseV1WithResultNullAndErrorNumber(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("result",JSONObject.NULL).put("error",12.34)));

        assertEquals("[V1] The \"error\" property in request MUST be an string, object or null.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObject(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("error",new JSONObject())));

        assertEquals("[V2] The error object MUST have a \"code\" property.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObjectCode(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("error",new JSONObject().put("code",JSONObject.NULL))));

        assertEquals("[V2] The error object MUST have a \"message\" property.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObjectCodeAndMessage(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("error",new JSONObject().put("code",JSONObject.NULL).put("message",JSONObject.NULL))));

        assertEquals("[V2] The \"code\" property of the error object MUST be an integer.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObjectCodeNumberAndMessage(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("error",new JSONObject().put("code",123).put("message",JSONObject.NULL))));

        assertEquals("[V2] The \"message\" property of the error object MUST be a string.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObjectCodeNumberAndMessageString(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("error",new JSONObject().put("code",123).put("message","Some error"))));

        assertEquals("[V2] Missing \"id\" property in response.",t.getMessage());
    }

    @Test
    public void testParseResponseV2WithResultNullAndErrorObjectCodeNumberAndMessageStringAndId() throws JSONRPCException{
        assertInstanceOf(ResponseMessage.class,Message.parseObject(new JSONObject().put("jsonrpc","2.0").put("id",123).put("error",new JSONObject().put("code",456).put("message","Some error"))));
    }

    @Test
    public void testParseResponseV1WithResultAndNullError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("result","abc").put("error",JSONObject.NULL)));

        assertEquals("[V1] Missing \"id\" property in response.",t.getMessage());
    }

    @Test
    public void testParseResponseV1WithIdAndResultAndNullError() throws JSONRPCException{
        assertInstanceOf(ResponseMessage.class,Message.parseObject(new JSONObject().put("id",123).put("result","abc").put("error",JSONObject.NULL)));
    }

    @Test
    public void testParseResponseV1WithIdNullAndResultAndNullError() throws JSONRPCException{
        assertInstanceOf(ResponseMessage.class,Message.parseObject(new JSONObject().put("id",JSONObject.NULL).put("result","abc").put("error",JSONObject.NULL)));
    }

    @Test
    public void testParseResponseV1WithIdFalsyAndResultAndNullError() throws JSONRPCException{
        assertInstanceOf(ResponseMessage.class,Message.parseObject(new JSONObject().put("id",false).put("result","abc").put("error",JSONObject.NULL)));
        assertInstanceOf(ResponseMessage.class,Message.parseObject(new JSONObject().put("id",false).put("result","abc").put("error",JSONObject.NULL),false));
    }

    @Test
    public void testParseVersion2(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","2.0")));

        assertEquals("[V2] Unknown message type.",t.getMessage());
    }

    @Test
    public void testParseUnknownVersion(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.parseObject(new JSONObject().put("jsonrpc","1.5")));

        assertEquals("Unknown version \"1.5\".",t.getMessage());
    }

    @Test
    public void testGetters() throws JSONRPCException{
        assertEquals(123,Message.createRequestMessageV1(123,"myMethod").getId());
        assertEquals("myMethod",Message.createRequestMessageV1(123,"myMethod").getMethod());
        assertTrue(new JSONArray().similar(Message.createRequestMessageV1(123,"myMethod").getParams()));

        assertNull(Message.createNotificationMessageV1("myMethod").getId());
        assertEquals("myMethod",Message.createNotificationMessageV1("myMethod").getMethod());
        assertTrue(new JSONArray().similar(Message.createNotificationMessageV1("myMethod").getParams()));

        assertEquals(123,Message.createResponseMessageV1(123,"myResult").getId());
        assertEquals("myResult",Message.createResponseMessageV1(123,"myResult").getResult());
        assertNull(Message.createResponseMessageV1(123,"myResult").getError());

        assertEquals(456,Message.createResponseMessageV1(123,null,new JSONObject().put("code",456).put("message","Some error text").put("data",true)).getErrorCode());
        assertEquals("Some error text",Message.createResponseMessageV1(123,null,new JSONObject().put("code",456).put("message","Some error text").put("data",true)).getErrorMessage());
        assertEquals(true,Message.createResponseMessageV1(123,null,new JSONObject().put("code",456).put("message","Some error text").put("data",true)).getErrorData());
    }

    @Test
    public void testIsRequest() throws JSONRPCException{
        assertTrue(Message.createRequestMessageV1(123,"myMethod").isRequest());
        assertTrue(Message.createNotificationMessageV1("myMethod").isRequest());
        assertFalse(Message.createResponseMessageV1(123,"myResult").isRequest());
    }

    @Test
    public void testIsNotification() throws JSONRPCException{
        assertFalse(Message.createRequestMessageV1(123,"myMethod").isNotification());
        assertTrue(Message.createNotificationMessageV1("myMethod").isNotification());
        assertFalse(Message.createResponseMessageV1(123,"myResult").isNotification());
    }

    @Test
    public void testIsResponse() throws JSONRPCException{
        assertFalse(Message.createRequestMessageV1(123,"myMethod").isResponse());
        assertFalse(Message.createNotificationMessageV1("myMethod").isResponse());
        assertTrue(Message.createResponseMessageV1(123,"myResult").isResponse());
    }

    @Test
    public void testIsVersion2() throws JSONRPCException{
        assertFalse(Message.createRequestMessageV1(123,"myMethod").isVersion2());
        assertTrue(Message.createRequestMessageV2(123,"myMethod").isVersion2());

        assertFalse(Message.createNotificationMessageV1("myMethod").isVersion2());
        assertTrue(Message.createNotificationMessageV2("myMethod").isVersion2());

        assertFalse(Message.createResponseMessageV1(123,"myResult").isVersion2());
        assertTrue(Message.createResponseMessageV2(123,"myResult").isVersion2());
    }

    @Test
    public void testCreateRequestMessageV2WithIdAndMethodAndParamsFalse(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createRequestMessageV2(123,"abc",false));

        assertEquals("[V2] The \"params\" property in request MUST be an object, array or null.",t.getMessage());
    }

    @Test
    public void testCreateNotificationMessageV2WithIdAndMethodAndParamsFalse(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createNotificationMessageV2("abc",false));

        assertEquals("[V2] The \"params\" property in request MUST be an object, array or null.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV1WithResultAndError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV1(123,"abc","def"));

        assertEquals("[V1] Only one property \"result\" or \"error\" can be non null.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultAndError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV2(123,"abc",new JSONObject()));

        assertEquals("[V2] Only one property \"result\" or \"error\" can be non null.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultNullAndError(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV2(123,null,new JSONObject()));

        assertEquals("[V2] The error object MUST have a \"code\" property.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultNullAndErrorCode(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV2(123,null,new JSONObject().put("code",JSONObject.NULL)));

        assertEquals("[V2] The error object MUST have a \"message\" property.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultNullAndErrorCodeAndMessage(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV2(123,null,new JSONObject().put("code",JSONObject.NULL).put("message",JSONObject.NULL)));

        assertEquals("[V2] The \"code\" property of the error object MUST be an integer.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultNullAndErrorCodeIntegerAndMessage(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV2(123,null,new JSONObject().put("code",123).put("message",JSONObject.NULL)));

        assertEquals("[V2] The \"message\" property of the error object MUST be a string.",t.getMessage());
    }

    @Test
    public void testCreateResponseMessageV2WithResultNullAndErrorCodeIntegerAndMessageString() throws JSONRPCException{
        assertInstanceOf(ResponseMessage.class,Message.createResponseMessageV2(123,null,new JSONObject().put("code",123).put("message","")));
    }

    @Test
    public void testCreateResponseMessageV1WithErrorFalse(){
        Throwable t = assertThrows(JSONRPCException.class,() -> Message.createResponseMessageV1(123,null,false));

        assertEquals("[V1] The \"error\" property in request MUST be an string, object or null.",t.getMessage());
    }

    @Test
    public void testCreateRequestOrNotificationV2() throws JSONRPCException{
        assertInstanceOf(RequestMessage.class,Message.createRequestMessageV2(123,"myMethod",new JSONArray()));
        assertInstanceOf(RequestMessage.class,Message.createRequestMessageV2(123,"myMethod",new JSONObject()));

        assertInstanceOf(NotificationMessage.class,Message.createNotificationMessageV2("myMethod",new JSONArray()));
        assertInstanceOf(NotificationMessage.class,Message.createNotificationMessageV2("myMethod",new JSONObject()));
    }

    @Test
    public void testToObject(){
        assertTrue(new JSONObject().put("id",123).put("method","getMethod").put("params",new JSONArray().put("param1").put("param2")).similar(Message.createRequestMessageV1(123,"getMethod",new JSONArray().put("param1").put("param2")).toObject()));
    }

}