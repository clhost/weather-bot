package com.clhost.weatherbot.config;

import com.clhost.weatherbot.bot.WeatherBot;
import com.clhost.weatherbot.utils.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;


@Configuration
@PropertySource("classpath:application.properties")
public class BotProxyConfig {
    private static final String PROXY_HOST_PROPERTY_NAME = "telegram.proxy.host";
    private static final String PROXY_PORT_PROPERTY_NAME = "telegram.proxy.port";
    private static final String PROXY_LOGIN_PROPERTY_NAME = "telegram.proxy.login";
    private static final String PROXY_PASSWORD_PROPERTY_NAME = "telegram.proxy.password";

    @Autowired
    private Environment env;

    @Bean
    public WeatherBot weatherBot() {
        String proxyHost = env.getProperty(PROXY_HOST_PROPERTY_NAME);
        String proxyPort = env.getProperty(PROXY_PORT_PROPERTY_NAME);
        String login = env.getProperty(PROXY_LOGIN_PROPERTY_NAME);
        String password = env.getProperty(PROXY_PASSWORD_PROPERTY_NAME);

        if (isPropertyExists(proxyHost) && isPropertyExists(proxyPort) && StringUtils.isNumeric(proxyPort)) {
            HttpHost host = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            if (isPropertyExists(login) && isPropertyExists(password)) {
                // todo: log here
                System.out.println("\t bot -> with auth");
                return new WeatherBot(proxyWithAuth(host, login, password));
            }
            // todo: log here
            System.out.println("\t bot -> without auth");
            return new WeatherBot(proxyWithoutAuth(host));
        }

        // todo: log here
        System.out.println("\t bot -> simple");
        return new WeatherBot();
    }

    private static boolean isPropertyExists(String property) {
        return property != null && !property.equals("");
    }

    private static DefaultBotOptions proxyWithAuth(HttpHost host, String login, String password) {
        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host.getHostName(), host.getPort()),
                new UsernamePasswordCredentials(login, password));
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setProxy(host)
                .setAuthenticationEnabled(true)
                .build();
        defaultBotOptions.setRequestConfig(requestConfig);
        defaultBotOptions.setCredentialsProvider(credentialsProvider);
        defaultBotOptions.setHttpProxy(host);

        return defaultBotOptions;
    }

    private static DefaultBotOptions proxyWithoutAuth(HttpHost host) {
        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setProxy(host)
                .setAuthenticationEnabled(false)
                .build();
        defaultBotOptions.setRequestConfig(requestConfig);
        defaultBotOptions.setHttpProxy(host);

        return defaultBotOptions;
    }
}
