package net.aouchar.ebankaiagent.discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import net.aouchar.ebankaiagent.agents.EbankAiAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@DiscordController
public class EBankDiscordController {
    private EbankAiAgent ebankAiAgent;

    public EBankDiscordController(EbankAiAgent ebankAiAgent){
        this.ebankAiAgent = ebankAiAgent;
    }

    @DiscordMapping
    private void perform(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        String query = event.getMessage().getContentRaw();
        String response = ebankAiAgent.chat(query);
        event.getChannel().sendMessage(response).queue();

    }
}
