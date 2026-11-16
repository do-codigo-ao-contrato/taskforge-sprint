package br.com.docodigoaocontrato.taskforge.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class IaService {

    private final ChatClient chatClient;

    public IaService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        Voce é o assistente do TaskForge, um app de tarefas.
                        Dado um título de uma tarefa, escreva uma descrição
                        curta e profissional, em Portugues do Brasil, com no
                        máximo 2 frases.
                        Não invente prazos, nem pessoas ou dados que não foram
                        passados ou ditos.
                        Responda apenas com a descrição, sem o título e sem aspas.
                        """)
                .build();
    }

    public String sugerirDescricao(String titulo) {
        return chatClient
                .prompt()
                .user("Título da Tarefa: " + titulo)
                .call()
                .content();
    }
}
