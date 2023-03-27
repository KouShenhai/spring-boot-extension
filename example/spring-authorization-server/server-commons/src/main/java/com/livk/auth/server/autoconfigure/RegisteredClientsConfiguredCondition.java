package com.livk.auth.server.autoconfigure;

import com.livk.commons.spring.util.SpringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author livk
 */
class RegisteredClientsConfiguredCondition extends SpringBootCondition {

    private static final Bindable<Map<String, OAuth2AuthorizationServerProperties.Client>> STRING_CLIENT_MAP = Bindable
            .mapOf(String.class, OAuth2AuthorizationServerProperties.Client.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage
                .forCondition("OAuth2 Registered Clients Configured Condition");
        Map<String, OAuth2AuthorizationServerProperties.Client> registrations = getRegistrations(
                context.getEnvironment());
        if (!registrations.isEmpty()) {
            return ConditionOutcome.match(message.foundExactly("registered clients " + registrations.values()
                    .stream()
                    .map(OAuth2AuthorizationServerProperties.Client::getRegistration)
                    .map(OAuth2AuthorizationServerProperties.Registration::getClientId)
                    .collect(Collectors.joining(", "))));
        }
        return ConditionOutcome.noMatch(message.notAvailable("registered clients"));
    }

    private Map<String, OAuth2AuthorizationServerProperties.Client> getRegistrations(Environment environment) {
        return SpringUtils.bind(environment, "spring.security.oauth2.authorizationserver.client", STRING_CLIENT_MAP)
                .orElse(Collections.emptyMap());
    }

}
