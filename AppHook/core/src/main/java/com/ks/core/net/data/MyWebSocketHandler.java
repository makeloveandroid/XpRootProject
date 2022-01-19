/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.ks.core.net.data;

import android.util.Base64;

import com.facebook.stetho.common.Utf8Charset;
import com.facebook.stetho.server.SocketLike;
import com.facebook.stetho.server.http.HttpHandler;
import com.facebook.stetho.server.http.HttpStatus;
import com.facebook.stetho.server.http.LightHttpBody;
import com.facebook.stetho.server.http.LightHttpMessage;
import com.facebook.stetho.server.http.LightHttpRequest;
import com.facebook.stetho.server.http.LightHttpResponse;
import com.facebook.stetho.server.http.LightHttpServer;
import com.facebook.stetho.websocket.SimpleEndpoint;
import com.ks.core.util.ReflectUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Crazy kludge to support upgrading to the WebSocket protocol while still using the
 * {@link HttpHandler} harness.
 * <p>
 * The way this works is that we pump the request directly into our WebSocket implementation and
 * force write the response out to the connection without returning.  Then, we extract the
 * remaining buffered input stream bytes from the socket and stitch them together with the
 * raw sockets input stream and pass everything onto the WebSocket engine which blocks
 * until WebSocket orderly shutdown.
 */
public class MyWebSocketHandler implements HttpHandler {
    private static final String HEADER_UPGRADE = "Upgrade".toLowerCase();
    private static final String HEADER_CONNECTION = "Connection".toLowerCase();
    private static final String HEADER_SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key".toLowerCase();
    private static final String HEADER_SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept".toLowerCase();
    private static final String HEADER_SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol".toLowerCase();
    private static final String HEADER_SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version".toLowerCase();

    private static final String HEADER_UPGRADE_WEBSOCKET = "websocket";
    private static final String HEADER_CONNECTION_UPGRADE = "Upgrade";
    private static final String HEADER_SEC_WEBSOCKET_VERSION_13 = "13";

    // Are you kidding me?  The WebSocket spec requires that we append this weird hardcoded String
    // to the key we receive from the client, SHA-1 that, and base64 encode it back to the client.
    // I'm guessing this is to prevent replay attacks of some kind but given that there's no actual
    // security context here, I can only imagine that this is just security through obscurity in
    // some fashion.
    private static final String SERVER_KEY_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private final SimpleEndpoint mEndpoint;

    public MyWebSocketHandler(SimpleEndpoint endpoint) {
        mEndpoint = endpoint;
    }

    @Override
    public boolean handleRequest(
            SocketLike socket,
            LightHttpRequest request,
            LightHttpResponse response) throws IOException {
        if (!isSupportableUpgradeRequest(request)) {
            response.code = HttpStatus.HTTP_NOT_IMPLEMENTED;
            response.reasonPhrase = "Not Implemented";
            response.body = LightHttpBody.create(
                    "Not a supported WebSocket upgrade request\n",
                    "text/plain");
            return true;
        }

        // This will not return on successful WebSocket upgrade, but rather block until the session is
        // shut down or a socket error occurs.
        doUpgrade(socket, request, response);
        return false;
    }

    private static boolean isSupportableUpgradeRequest(LightHttpRequest request) {
        return HEADER_UPGRADE_WEBSOCKET.equalsIgnoreCase(getFirstHeaderValue(request, HEADER_UPGRADE)) &&
                HEADER_CONNECTION_UPGRADE.equals(getFirstHeaderValue(request, HEADER_CONNECTION)) &&
                HEADER_SEC_WEBSOCKET_VERSION_13.equals(
                        getFirstHeaderValue(request, HEADER_SEC_WEBSOCKET_VERSION));
    }

    private void doUpgrade(
            SocketLike socketLike,
            LightHttpRequest request,
            LightHttpResponse response)
            throws IOException {
        response.code = HttpStatus.HTTP_SWITCHING_PROTOCOLS;
        response.reasonPhrase = "Switching Protocols";
        response.addHeader(HEADER_UPGRADE, HEADER_UPGRADE_WEBSOCKET);
        response.addHeader(HEADER_CONNECTION, HEADER_CONNECTION_UPGRADE);
        response.body = null;

        String clientKey = getFirstHeaderValue(request, HEADER_SEC_WEBSOCKET_KEY);
        if (clientKey != null) {
            response.addHeader(HEADER_SEC_WEBSOCKET_ACCEPT, generateServerKey(clientKey));
        }

        InputStream in = socketLike.getInput();
        OutputStream out = socketLike.getOutput();
        LightHttpServer.writeResponseMessage(
                response,
                new LightHttpServer.HttpMessageWriter(new BufferedOutputStream(out)));
        try {
//          WebSocketSession session = new WebSocketSession(in, out, mEndpoint);
//          session.handle();
            Class<?> webSocketSessionClass = getClass().getClassLoader().loadClass("com.facebook.stetho.websocket.WebSocketSession");
            Object session = ReflectUtils.invokeConstructor(webSocketSessionClass, new Class[]{InputStream.class, OutputStream.class, SimpleEndpoint.class}, in, out, mEndpoint);
            ReflectUtils.invokeMethod(session, "handle", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String generateServerKey(String clientKey) {
        try {
            String serverKey = clientKey + SERVER_KEY_GUID;
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(Utf8Charset.encodeUTF8(serverKey));
            return Base64.encodeToString(sha1.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFirstHeaderValue(LightHttpMessage message, String headerName) {
        return message.getFirstHeaderValue(headerName);
    }
}
