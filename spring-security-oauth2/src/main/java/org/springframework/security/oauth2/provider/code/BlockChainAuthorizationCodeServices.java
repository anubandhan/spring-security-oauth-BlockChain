package org.springframework.security.oauth2.provider.code;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anubandhans on 25/08/17.
 */
public class BlockChainAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final Log logger = LogFactory.getLog(BlockChainAuthorizationCodeServices.class);

    //TODO: needs to be connected to DB.
    protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore = new ConcurrentHashMap<String, OAuth2Authentication>();

    @Override
    protected void store(String code, OAuth2Authentication authentication) {

        logger.info("BlockChainAuthorizationCodeServices Called to store Auth Code :"+ authentication );
        this.authorizationCodeStore.put(code, authentication);
    }

    @Override
    public OAuth2Authentication remove(String code) {
        OAuth2Authentication auth = this.authorizationCodeStore.remove(code);
        return auth;
    }


}


