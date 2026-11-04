package net.aouchar.ebankaiagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

@Component
public class EbankAiAgent {
    private ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder chatClient, ToolCallbackProvider tools, ChatMemory chatMemory){
        this.chatClient = chatClient
                //.defaultSystem("""                       """)
                .defaultTools(tools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String query){
        return chatClient.prompt(query)
                .advisors(a-> a.param(ChatMemory.CONVERSATION_ID, "C1"))
                .call().content();
    }
}
