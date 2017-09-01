package org.springframework.security.oauth2.provider.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anubandhans on 25/08/17.
 */
public class BlockChainClientDetailsService implements ClientDetailsService {

    private static final Log logger = LogFactory.getLog(BlockChainClientDetailsService.class);

    private BlockChainClientDetailsService(){};

    private static BlockChainClientDetailsService blockChainClientDetailsService;

    public static BlockChainClientDetailsService getInstance(){
        if (blockChainClientDetailsService==null){
            blockChainClientDetailsService = new BlockChainClientDetailsService();
            logger.info("BlockChainClientDetailsService :: initializing blockChainClientDetailsService");
        }
        return blockChainClientDetailsService;
    }

    //TODO: JDBC based code stil needs to be triggered.
    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        logger.info("BlockChainClientDetailsService:: loadClientByClientId method called with Client ID: " +clientId);
        ClientDetails details = clientDetailsStore.get(clientId);
        logger.info("BlockChainClientDetailsService:: loadClientByClientId Client Details: " +details);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return details;
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap<String, ClientDetails>(clientDetailsStore);
    }

}
