package com.yocto.yoclib.jsonrpc.tests;

import com.yocto.yoclib.jsonrpc.JSONRPCException;
import com.yocto.yoclib.jsonrpc.Message;

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

}