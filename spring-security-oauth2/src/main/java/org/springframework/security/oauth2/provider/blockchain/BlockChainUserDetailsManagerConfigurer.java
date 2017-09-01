package org.springframework.security.oauth2.provider.blockchain;

import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.provisioning.UserDetailsManager;


import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by anubandhans on 25/08/17.
 */

public class BlockChainUserDetailsManagerConfigurer<B extends ProviderManagerBuilder<B>> extends UserDetailsManagerConfigurer<B, BlockChainUserDetailsManagerConfigurer<B>> {

    static final Logger log = Logger.getLogger("BlockChainUserDetailsManagerConfigurer");

    public BlockChainUserDetailsManagerConfigurer(UserDetailsManager userDetailsManager) {
        super(userDetailsManager);
        log.info(" ------------------------ Inside BlockChainUserDetailsManagerConfigurer Upper Setting up New User");
    }
    public BlockChainUserDetailsManagerConfigurer() {
        super(new BlockChainUserDetailsManager(new ArrayList()));
        log.info(" ------------------------ Inside BlockChainUserDetailsManagerConfigurer Lower Setting up New User");
    }
}
