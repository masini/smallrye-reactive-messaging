package io.smallrye.reactive.messaging.kafka.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.smallrye.reactive.messaging.kafka.base.KafkaMapBasedConfig;
import io.vertx.core.json.JsonObject;

class JsonHelperTest {

    @Test
    public void test() {
        KafkaMapBasedConfig config = KafkaMapBasedConfig.builder()
                .put("bootstrap.servers", "not-important")
                .put("key", "value")
                .put("int", 10)
                .put("double", 23.4)
                .put("trueasstring", "true")
                .put("falseasstring", "false")
                .put("boolean.t", true)
                .put("boolean.f", false)
                .put("FOO_BAR", "value")
                .build();

        JsonObject object = JsonHelper.asJsonObject(config);
        assertThat(object.getString("key")).isEqualTo("value");
        assertThat(object.getInteger("int")).isEqualTo(10);
        assertThat(object.getDouble("double")).isEqualTo(23.4);
        assertThat(object.getBoolean("trueasstring")).isTrue();
        assertThat(object.getBoolean("falseasstring")).isFalse();
        assertThat(object.getString("foo.bar")).isEqualTo("value");
        assertThat(object.getString("bootstrap.servers")).isEqualTo("not-important");

        assertThat(object.getBoolean("boolean.t")).isTrue();
        assertThat(object.getBoolean("boolean.f")).isFalse();

    }

}
