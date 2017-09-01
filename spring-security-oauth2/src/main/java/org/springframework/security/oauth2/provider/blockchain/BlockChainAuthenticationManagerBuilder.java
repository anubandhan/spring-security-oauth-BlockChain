package org.springframework.security.oauth2.provider.blockchain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by anubandhans on 25/08/17.
 */
@Component("blockChainAuthenticationManagerBuilder")
@Order(10)
public class BlockChainAuthenticationManagerBuilder extends AuthenticationManagerBuilder {

    protected final Log logger = LogFactory.getLog(getClass());

    public BlockChainAuthenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    public BlockChainUserDetailsManagerConfigurer<AuthenticationManagerBuilder> blockChainAuthentication() throws Exception {
        logger.info("BlockChainAuthenticationManagerBuilder Called : ");
        return (BlockChainUserDetailsManagerConfigurer)this.apply(new BlockChainUserDetailsManagerConfigurer());
    }


}
