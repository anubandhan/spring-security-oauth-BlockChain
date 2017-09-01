package org.springframework.security.oauth2.provider.blockchain;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by anubandhans on 25/08/17.
 */
interface MutableUserDetails extends UserDetails {

    void setPassword(String password);

}