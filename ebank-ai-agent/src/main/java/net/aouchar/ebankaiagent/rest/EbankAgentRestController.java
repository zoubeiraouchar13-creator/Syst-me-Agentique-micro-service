package net.aouchar.ebankaiagent.rest;

import net.aouchar.ebankaiagent.agents.EbankAiAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankAgentRestController {
    private EbankAiAgent ebankAiAgent;

    public EbankAgentRestController(EbankAiAgent ebankAiAgent){

        this.ebankAiAgent = ebankAiAgent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(String query){
        return ebankAiAgent.chat(query);
    }
}
