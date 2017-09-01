package org.springframework.security.config.annotation.authentication.builders;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsAwareConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by anubandhans on 25/08/17.
 */
public class AuthenticationManagerBuilder {/*extends AbstractConfiguredSecurityBuilder<AuthenticationManager, AuthenticationManagerBuilder> implements ProviderManagerBuilder<AuthenticationManagerBuilder> {
    private final Log logger = LogFactory.getLog(getClass());

    private AuthenticationManager parentAuthenticationManager;
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
    private UserDetailsService defaultUserDetailsService;
    private Boolean eraseCredentials;
    private AuthenticationEventPublisher eventPublisher;

    *//**
     * Creates a new instance
     * @param  {@link ObjectPostProcessor} instance to use.
     *//*
    public AuthenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor,true);
        logger.info("Custom AuthenticationManagerBuilder  Called : ");
    }

    *//**
     * Allows providing a parent {@link AuthenticationManager} that will be
     * tried if this {@link AuthenticationManager} was unable to attempt to
     * authenticate the provided {@link Authentication}.
     *
     * @param authenticationManager
     *            the {@link AuthenticationManager} that should be used if the
     *            current {@link AuthenticationManager} was unable to attempt to
     *            authenticate the provided {@link Authentication}.
     * @return the {@link AuthenticationManagerBuilder} for further adding types
     *         of authentication
     *//*
    public AuthenticationManagerBuilder parentAuthenticationManager(
            AuthenticationManager authenticationManager) {
        if(authenticationManager instanceof ProviderManager) {
            eraseCredentials(((ProviderManager) authenticationManager).isEraseCredentialsAfterAuthentication());
        }
        this.parentAuthenticationManager = authenticationManager;
        return this;
    }

    *//**
     * Sets the {@link AuthenticationEventPublisher}
     *
     * @param eventPublisher
     *            the {@link AuthenticationEventPublisher} to use
     * @return the {@link AuthenticationManagerBuilder} for further
     *         customizations
     *//*
    public AuthenticationManagerBuilder authenticationEventPublisher(AuthenticationEventPublisher eventPublisher) {
        Assert.notNull(eventPublisher, "AuthenticationEventPublisher cannot be null");
        this.eventPublisher = eventPublisher;
        return this;
    }

    *//**
     *
     *
     * @param eraseCredentials
     *            true if {@link AuthenticationManager} should clear the
     *            credentials from the {@link Authentication} object after
     *            authenticating
     * @return the {@link AuthenticationManagerBuilder} for further customizations
     *//*
    public AuthenticationManagerBuilder eraseCredentials(boolean eraseCredentials) {
        this.eraseCredentials = eraseCredentials;
        return this;
    }


    *//**
     * Add in memory authentication to the {@link AuthenticationManagerBuilder}
     * and return a {@link InMemoryUserDetailsManagerConfigurer} to
     * allow customization of the in memory authentication.
     *
     * <p>
     * This method also ensure that a {@link UserDetailsService} is available
     * for the {@link #getDefaultUserDetailsService()} method. Note that
     * additional {@link UserDetailsService}'s may override this
     * {@link UserDetailsService} as the default.
     * </p>
     *
     * @return a {@link InMemoryUserDetailsManagerConfigurer} to allow
     *         customization of the in memory authentication
     * @throws Exception
     *             if an error occurs when adding the in memory authentication
     *//*
    public InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication()
            throws Exception {
        return apply(new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>());
    }


    *//**
     * Add in memory authentication to the {@link AuthenticationManagerBuilder}
     * and return a {@link InMemoryUserDetailsManagerConfigurer} to
     * allow customization of the in memory authentication.
     *
     * <p>
     * This method also ensure that a {@link UserDetailsService} is available
     * for the {@link #getDefaultUserDetailsService()} method. Note that
     * additional {@link UserDetailsService}'s may override this
     * {@link UserDetailsService} as the default.
     * </p>
     *
     * @return a {@link InMemoryUserDetailsManagerConfigurer} to allow
     *         customization of the in memory authentication
     * @throws Exception
     *             if an error occurs when adding the in memory authentication
     *//*
    public BlockChainUserDetailsManagerConfigurer<AuthenticationManagerBuilder> blockChainAuthentication() throws Exception {
        logger.info("BlockChainAuthenticationManagerBuilder Called : ");
        return (BlockChainUserDetailsManagerConfigurer)this.apply(new BlockChainUserDetailsManagerConfigurer());
    }


    *//**
     * Add JDBC authentication to the {@link AuthenticationManagerBuilder} and
     * return a {@link JdbcUserDetailsManagerConfigurer} to allow customization
     * of the JDBC authentication.
     *
     * <p>
     * When using with a persistent data store, it is best to add users external
     * of configuration using something like <a
     * href="http://flywaydb.org/">Flyway</a> or <a
     * href="http://www.liquibase.org/">Liquibase</a> to create the schema and
     * adding users to ensure these steps are only done once and that the
     * optimal SQL is used.
     * </p>
     *
     * <p>
     * This method also ensure that a {@link UserDetailsService} is available
     * for the {@link #getDefaultUserDetailsService()} method. Note that
     * additional {@link UserDetailsService}'s may override this
     * {@link UserDetailsService} as the default. See the <a href=
     * "http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#user-schema"
     * >User Schema</a> section of the reference for the default schema.
     * </p>
     *
     * @return a {@link JdbcUserDetailsManagerConfigurer} to allow customization
     *         of the JDBC authentication
     * @throws Exception
     *             if an error occurs when adding the JDBC authentication
     *//*
    public JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> jdbcAuthentication()
            throws Exception {
        return apply(new JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder>());
    }

    *//**
     * Add authentication based upon the custom {@link UserDetailsService} that
     * is passed in. It then returns a {@link DaoAuthenticationConfigurer} to
     * allow customization of the authentication.
     *
     * <p>
     * This method also ensure that the {@link UserDetailsService} is available
     * for the {@link #getDefaultUserDetailsService()} method. Note that
     * additional {@link UserDetailsService}'s may override this
     * {@link UserDetailsService} as the default.
     * </p>
     *
     * @return a {@link DaoAuthenticationConfigurer} to allow customization
     *         of the DAO authentication
     * @throws Exception
     *             if an error occurs when adding the {@link UserDetailsService}
     *             based authentication
     *//*
    public <T extends UserDetailsService> DaoAuthenticationConfigurer<AuthenticationManagerBuilder, T> userDetailsService(
            T userDetailsService) throws Exception {
        this.defaultUserDetailsService = userDetailsService;
        return apply(new DaoAuthenticationConfigurer<AuthenticationManagerBuilder,T>(userDetailsService));
    }

    *//**
     * Add LDAP authentication to the {@link AuthenticationManagerBuilder} and
     * return a {@link LdapAuthenticationProviderConfigurer} to allow
     * customization of the LDAP authentication.
     *
     * <p>
     * This method <b>does NOT</b> ensure that a {@link UserDetailsService} is
     * available for the {@link #getDefaultUserDetailsService()} method.
     * </p>
     *
     * @return a {@link LdapAuthenticationProviderConfigurer} to allow
     *         customization of the LDAP authentication
     * @throws Exception
     *             if an error occurs when adding the LDAP authentication
     *//*
    public LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuthentication()
            throws Exception {
        return apply(new LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder>());
    }

    *//**
     * Add authentication based upon the custom {@link AuthenticationProvider}
     * that is passed in. Since the {@link AuthenticationProvider}
     * implementation is unknown, all customizations must be done externally and
     * the {@link AuthenticationManagerBuilder} is returned immediately.
     *
     * <p>
     * This method <b>does NOT</b> ensure that the {@link UserDetailsService} is
     * available for the {@link #getDefaultUserDetailsService()} method.
     * </p>
     *
     * @return a {@link AuthenticationManagerBuilder} to allow further authentication
     *         to be provided to the {@link AuthenticationManagerBuilder}
     * @throws Exception
     *             if an error occurs when adding the {@link AuthenticationProvider}
     *//*
    public AuthenticationManagerBuilder authenticationProvider(
            AuthenticationProvider authenticationProvider) {
        this.authenticationProviders.add(authenticationProvider);
        return this;
    }

    @Override
    protected ProviderManager performBuild() throws Exception {
        if(!isConfigured()) {
            logger.debug("No authenticationProviders and no parentAuthenticationManager defined. Returning null.");
            return null;
        }
        ProviderManager providerManager = new ProviderManager(authenticationProviders, parentAuthenticationManager);
        if(eraseCredentials != null) {
            providerManager.setEraseCredentialsAfterAuthentication(eraseCredentials);
        }
        if(eventPublisher != null) {
            providerManager.setAuthenticationEventPublisher(eventPublisher);
        }
        providerManager = postProcess(providerManager);
        return providerManager;
    }

    *//**
     * Determines if the {@link AuthenticationManagerBuilder} is configured to
     * build a non null {@link AuthenticationManager}. This means that either a
     * non-null parent is specified or at least one
     * {@link AuthenticationProvider} has been specified.
     *
     * <p>
     * When using {@link SecurityConfigurer} instances, the
     * {@link AuthenticationManagerBuilder} will not be configured until the
     * {@link SecurityConfigurer#configure(SecurityBuilder)} methods. This means
     * a {@link SecurityConfigurer} that is last could check this method and
     * provide a default configuration in the
     * {@link SecurityConfigurer#configure(SecurityBuilder)} method.
     *
     * @return
     *//*
    public boolean isConfigured() {
        return !authenticationProviders.isEmpty() || parentAuthenticationManager != null;
    }

    *//**
     * Gets the default {@link UserDetailsService} for the
     * {@link AuthenticationManagerBuilder}. The result may be null in some
     * circumstances.
     *
     * @return the default {@link UserDetailsService} for the
     * {@link AuthenticationManagerBuilder}
     *//*
    public UserDetailsService getDefaultUserDetailsService() {
        return this.defaultUserDetailsService;
    }

    *//**
     * Captures the {@link UserDetailsService} from any {@link UserDetailsAwareConfigurer}.
     *
     * @param configurer the {@link UserDetailsAwareConfigurer} to capture the {@link UserDetailsService} from.
     * @return the {@link UserDetailsAwareConfigurer} for further customizations
     * @throws Exception if an error occurs
     *//*
    private <C extends UserDetailsAwareConfigurer<AuthenticationManagerBuilder,? extends UserDetailsService>> C apply(C configurer) throws Exception {
        this.defaultUserDetailsService = configurer.getUserDetailsService();
        return (C) super.apply(configurer);
    }*/
}