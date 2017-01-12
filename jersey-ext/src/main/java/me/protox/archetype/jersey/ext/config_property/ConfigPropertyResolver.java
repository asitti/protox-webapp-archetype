package me.protox.archetype.jersey.ext.config_property;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.OverrideCombiner;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;

public class ConfigPropertyResolver implements InjectionResolver<ConfigProperty> {

    static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropertyResolver.class);

    public static CombinedConfiguration config = new CombinedConfiguration(new OverrideCombiner());

    static {
        try {
            Configurations configs = new Configurations();
            File defaultConfigFile = new File(ConfigPropertyResolver.class.getResource("/config.properties").toURI());
            PropertiesConfiguration defaultConfiguration = configs.properties(defaultConfigFile);
            config.addConfiguration(new SystemConfiguration());
            config.addConfiguration(defaultConfiguration);
        } catch (URISyntaxException | ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        ConfigProperty configProperty = injectee.getParent().getAnnotation(ConfigProperty.class);
        assert configProperty != null;

        if (StringUtils.isBlank(configProperty.name())) {
            throw new IllegalArgumentException("@ConfigProperty shouldn't have blank name");
        }

        if (configProperty.mustPresent() && !config.containsKey(configProperty.name())) {
            throw new IllegalArgumentException(configProperty.name() + " must be present in config properties");
        }

        String propValue = config.getString(configProperty.name(), configProperty.defaultValue()).trim();

        if (!configProperty.allowBlank() && StringUtils.isBlank(propValue)) {
            throw new IllegalArgumentException(configProperty.name() + " doesn't allow blank name");
        }

        if (injectee.getRequiredType().equals(String.class)) {
            return propValue;
        } else if (injectee.getRequiredType().equals(Integer.TYPE)) {
            return Integer.valueOf(propValue);
        } else if (injectee.getRequiredType().equals(Long.TYPE)) {
            return Long.valueOf(propValue);
        } else if (injectee.getRequiredType().equals(Boolean.TYPE)) {
            return Boolean.valueOf(propValue);
        }

        throw new IllegalArgumentException("Not acceptable injectin type : ${injectee.requiredType.typeName}");

    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
