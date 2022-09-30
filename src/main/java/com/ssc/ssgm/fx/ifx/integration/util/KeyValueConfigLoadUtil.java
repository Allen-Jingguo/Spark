package com.ssc.ssgm.fx.ifx.integration.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class KeyValueConfigLoadUtil {

    public static Map<String, String> loadConfig(String text) {

        final Map<String, String> configMap = new HashMap<>();
        try {
            final val properties = new Properties();
            try {
                properties.load(new StringReader(text));
            } catch (IOException e) {
                log.error("IOException::", e);
            }

            properties.forEach((key, value) -> {
                configMap.put(key.toString(), value.toString());
            });
        } catch (Exception e) {
           log.error("Exception::",e);
        }

        return configMap;
    }
}
