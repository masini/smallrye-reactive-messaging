package io.smallrye.reactive.messaging.kafka.base;

import java.util.*;

import org.eclipse.microprofile.config.Config;

import io.smallrye.reactive.messaging.kafka.KafkaConnector;
import io.smallrye.reactive.messaging.test.common.config.MapBasedConfig;

/**
 * An implementation of {@link Config} based on a simple {@link Map}.
 * This class is just use to mock real configuration, so should only be used for tests.
 * <p>
 * Note that this implementation does not do any conversion, so you must pass the expected object instances.
 */
public class KafkaMapBasedConfig extends MapBasedConfig {

    public KafkaMapBasedConfig(Map<String, Object> map) {
        super(map);
    }

    public KafkaMapBasedConfig() {
        super();
    }

    public static Builder builder() {
        return builder("");
    }

    public static Builder builder(String prefix) {
        return builder(prefix, false);
    }

    public static Builder builder(String prefix, boolean tracing) {
        return new KafkaMapBasedConfig.Builder(prefix, tracing);
    }

    public static class Builder {
        private final String prefix;
        private final Boolean withTracing;
        private final Map<String, Object> configValues = new HashMap<>();

        private Builder(String prefix, Boolean withTracing) {
            this.prefix = prefix;
            this.withTracing = withTracing;
        }

        public Builder put(String key, Object value) {
            configValues.put(key, value);
            return this;
        }

        public Builder put(Object... keyOrValue) {
            String k = null;
            for (Object o : keyOrValue) {
                if (k == null) {
                    if (o instanceof String) {
                        k = o.toString();
                    } else {
                        throw new IllegalArgumentException("Expected " + o + " to be a String");
                    }
                } else {
                    put(k, o);
                    k = null;
                }
            }
            if (k != null) {
                throw new IllegalArgumentException("Invalid number of parameters, last key " + k + " has no value");
            }
            return this;
        }

        private String getFullKey(String shortKey) {
            if (prefix.length() > 0) {
                return prefix + "." + shortKey;
            } else {
                return shortKey;
            }
        }

        public KafkaMapBasedConfig build() {
            Map<String, Object> inner = new HashMap<>();

            if (!configValues.containsKey("connector")) {
                inner.put(getFullKey("connector"), KafkaConnector.CONNECTOR_NAME);
            }

            if (!configValues.containsKey("graceful-shutdown")) {
                inner.put(getFullKey("graceful-shutdown"), false);
            }

            if (!withTracing && !configValues.containsKey("tracing-enabled")) {
                inner.put(getFullKey("tracing-enabled"), false);
            }
            if (!configValues.containsKey("bootstrap.servers")) {
                inner.put(getFullKey("bootstrap.servers"), KafkaTestBase.getBootstrapServers());
            }
            configValues.forEach((key, value) -> inner.put(getFullKey(key), value));
            return new KafkaMapBasedConfig(inner);
        }
    }
}
