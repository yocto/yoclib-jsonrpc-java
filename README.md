# yocLib - JSON-RPC (Java)

This yocLibrary enables your project to encode and decode JSON-RPC messages in Java.

## Status

[![Java CI with Maven](https://github.com/yocto/yoclib-jsonrpc-java/actions/workflows/maven.yml/badge.svg)](https://github.com/yocto/yoclib-jsonrpc-java/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/yocto/yoclib-jsonrpc-java/graph/badge.svg?token=08Yu14FDIF)](https://codecov.io/gh/yocto/yoclib-jsonrpc-java)

## Installation

```xml
<dependency>
	<groupId>com.yocto</groupId>
	<artifactId>yoclib-jsonrpc</artifactId>
</dependency>
```

## Use

### Serialization

```java
import com.yocto.yoclib.jsonrpc.JSONRPCException;
import com.yocto.yoclib.jsonrpc.Message;

import org.json.JSONArray;
import org.json.JSONObject;

Message message = Message.createRequestMessageV1(123,"getInfo",new JSONArray().put("payments")); // Create request (version 1.0)
Message message = Message.createNotificationMessageV1("notificationEvent",new JSONArray().put("payed")); // Create notification (version 1.0)
Message message = Message.createResponseMessageV1(123,new JSONObject().put("payments",new JSONArray().put("$10.12").put("$23.45").put("$12.34"))); // Create response (version 1.0)

Object object = message.toObject();

try{
    String json = Message.encodeJSON(object);
}catch(JSONRPCException e){
    //Handle encoding exception
}
```

### Deserialization

```java
import com.yocto.yoclib.jsonrpc.JSONRPCException;
import com.yocto.yoclib.jsonrpc.Message;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

Scanner scanner = new Scanner(System.in);

String json = scanner.nextLine(); // Get request body

try{
    Object object = Message.decodeJSON(json);
}catch(JSONRPCException e){
    //Handle decoding exception
}

if(Message.isBatch(object)){
    JSONArray arr = (JSONArray) object;
    for(Object element : arr.toList()){
        try{
            Message message = Message.parse(element);
        }catch(JSONRPCException e){
            //Handle message exception
        }
    }
}else{
    try{
        Message message = Message.parse(object);
    }catch(JSONRPCException e){
        //Handle message exception
    }
}
```