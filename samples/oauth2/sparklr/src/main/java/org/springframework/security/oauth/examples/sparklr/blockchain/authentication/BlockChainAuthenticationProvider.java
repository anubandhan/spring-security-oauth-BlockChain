package org.springframework.security.oauth.examples.sparklr.blockchain.authentication;


/**
 * The MIT License
 * Copyright (c) 2014-2017 Anubandhan Singh Sengar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("blockChainAuthenticationProvider")
public class BlockChainAuthenticationProvider implements AuthenticationProvider {

    static final Logger log = Logger.getLogger(BlockChainAuthenticationProvider.class);

    private static BlockChainAuthenticationProvider blockChainAuthenticationProvider;
    private BlockChainAuthenticationProvider(){
        log.info("blockChainAuthenticationProvider Blank constructor called");
    }

    public static BlockChainAuthenticationProvider getInstance(){
        if(blockChainAuthenticationProvider==null){
            blockChainAuthenticationProvider = new BlockChainAuthenticationProvider();
        }
        return blockChainAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String signature = authentication.getCredentials().toString();

        boolean authenticated = false;
        log.info("blockChainAuthenticationProvider: authentication method called..."+authentication);

        /**
         *
         * Here implements for signature validation will go. No hit to DB as
         * This functionality override will only be required if we are planning to have a central user database. Else its not required.
         * Lets take an OAuth example. OAuth doesnt mean that user will have access to User credentials from Authenticator, But it simply says that
         * Authenticator will validate that user is an Authentic user and user will authorise user of protected resource.
         *
         */
        authenticated = true;

        if (authenticated) {

            String usernameInDB = authentication.getName();



            /**
             * Here look for username in your database!
             *
             */
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();

            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(usernameInDB, signature ,grantedAuths);
            log.info("blockChainAuthenticationProvider: authentication method called..."+auth);
            return auth;
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return     authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
