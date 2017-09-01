package org.springframework.security.oauth2.config.annotation.builders;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BlockChainClientDetailsService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anubandhans on 25/08/17.
 */
public class BlockChainClientDetailsServiceBuilder extends
        ClientDetailsServiceBuilder<BlockChainClientDetailsServiceBuilder> {

    //TODO: Currently we are saving Client details in a Hashmap.
    //TODO: Later we need to save it in a linked list that needs to be retrived from DB as well.
    //TODO: As per JdbcClientDetailsServiceBuilder we can just fetch client list from DB and store that in Linked lIst, adition will happen directly in list and DB.
    //TODO: Client List needs to be synced with Peers.

    private Map<String, ClientDetails> clientDetails = new HashMap<String, ClientDetails>();

    @Override
    protected void addClient(String clientId, ClientDetails value) {
        clientDetails.put(clientId, value);
    }

    @Override
    protected ClientDetailsService performBuild() {
        BlockChainClientDetailsService clientDetailsService = BlockChainClientDetailsService.getInstance();
        clientDetailsService.setClientDetailsStore(clientDetails);
        return clientDetailsService;
    }

}
