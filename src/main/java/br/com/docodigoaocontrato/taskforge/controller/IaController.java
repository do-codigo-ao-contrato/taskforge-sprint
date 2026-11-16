package br.com.docodigoaocontrato.taskforge.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IaController {

    private final ChatClient chatClient;

    public IaController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ia/pergunta")
    public String perguntar(@RequestParam String texto) {
        return chatClient
                .prompt()
                .user(texto)
                .call()
                .content();
    }
}
