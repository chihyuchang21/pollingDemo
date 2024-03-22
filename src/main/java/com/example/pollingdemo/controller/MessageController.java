package com.example.pollingdemo.controller;

import com.example.pollingdemo.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

        @MessageMapping("/chat.sendMessage")
        @SendTo("/topic/public")
        public Message sendMessage(Message message) {
            return message;
        }
    }

    //@MessageMapping是一個用於處理STOMP消息的註解
    ///chat.sendMessage 是STOMP訊息的destination，可以自行設定