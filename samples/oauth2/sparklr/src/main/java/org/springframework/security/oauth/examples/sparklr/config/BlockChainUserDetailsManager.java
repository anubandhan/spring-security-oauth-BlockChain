package org.springframework.security.oauth.examples.sparklr.config;


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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;


//TODO: Create User details in Table.
@Component
public class BlockChainUserDetailsManager implements UserDetailsManager {

    static final Logger log = Logger.getLogger(BlockChainUserDetailsManager.class);

    private static BlockChainUserDetailsManager blockChainUserDetailsManager;
    private BlockChainUserDetailsManager(){
        log.info("BlockChainUserDetailsManager Blank constructor called");
    }

    public static BlockChainUserDetailsManager getInstance(){
        if(blockChainUserDetailsManager==null){
            blockChainUserDetailsManager = new BlockChainUserDetailsManager();
        }
        return blockChainUserDetailsManager;
    }

    private final Map<String, MutableUserDetails> users = new HashMap<String,MutableUserDetails>();

    private AuthenticationManager authenticationManager;

    public BlockChainUserDetailsManager(Collection<UserDetails> users) {
        log.info("BlockChainUserDetailsManager Users Collection constructor called");
        for (UserDetails user : users) {
            createUser(user);
        }
    }


    public BlockChainUserDetailsManager(Properties users) {
        log.info("BlockChainUserDetailsManager Properties constructor called");
        Enumeration<?> names = users.propertyNames();
        UserAttributeEditor editor = new UserAttributeEditor();

        while(names.hasMoreElements()) {
            String name = (String) names.nextElement();
            editor.setAsText(users.getProperty(name));
            UserAttribute attr = (UserAttribute) editor.getValue();
            UserDetails user = new User(name, attr.getPassword(), attr.isEnabled(), true, true, true,
                    attr.getAuthorities());
            createUser(user);
        }
    }

    public void createUser(UserDetails user) {
        log.info("Inside ");
        Assert.isTrue(!userExists(user.getUsername()));

        users.put(user.getUsername().toLowerCase(), new MutableUser(user));
    }

    public void deleteUser(String username) {
        users.remove(username.toLowerCase());
    }

    public void updateUser(UserDetails user) {
        Assert.isTrue(userExists(user.getUsername()));

        users.put(user.getUsername().toLowerCase(), new MutableUser(user));
    }

    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current user.");
        }

        String username = currentUser.getName();

        log.debug("Changing password for user '"+ username + "'");

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (authenticationManager != null) {
            log.debug("Reauthenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            log.debug("No authentication manager set. Password won't be re-checked.");
        }

        MutableUserDetails user = users.get(username);

        if (user == null) {
            throw new IllegalStateException("Current user doesn't exist in database.");
        }

        user.setPassword(newPassword);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
