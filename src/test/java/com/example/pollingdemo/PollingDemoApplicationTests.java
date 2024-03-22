package com.example.pollingdemo;


import com.example.pollingdemo.controller.dispose.EchoWebSocketClient;
import com.example.pollingdemo.controller.dispose.WebSocketClient;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PollingDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        WebSocketClient client = new WebSocketClient();
        try {
            client.startConnection("127.0.0.1", 6666);
            String response = client.sendMessage("hello server");
            assertEquals("hello client", response);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to communicate with the server: " + e.getMessage());
        } finally {
            client.stopConnection();
        }
    }

    @Before
    public void setup() {
        EchoWebSocketClient client = new EchoWebSocketClient();
        client.startConnection("127.0.0.1", 4444);
    }

    @After
    public void tearDown() {
        EchoWebSocketClient client = new EchoWebSocketClient();
        client.stopConnection();
    }

    @Test
    public void givenClient_whenServerEchosMessage_thenCorrect() {
        EchoWebSocketClient client = new EchoWebSocketClient();
        try {
            client.startConnection("127.0.0.1", 6666); // 假设服务器运行在 localhost 的 6666 端口
            String resp1 = client.sendMessage("hello");
            String resp2 = client.sendMessage("world");
            String resp3 = client.sendMessage("!");
//            String resp4 = client.sendMessage(".");

            assertEquals("hello", resp1);
            assertEquals("world", resp2);
            assertEquals("!", resp3);
//            assertEquals("good bye", resp4);

        } catch (IOException e) {
            e.printStackTrace();
             fail("An IOException was thrown: " + e.getMessage());
        } finally {
            client.stopConnection();
        }
    }
}


