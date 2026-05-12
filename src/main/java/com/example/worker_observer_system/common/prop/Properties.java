package com.example.worker_observer_system.common.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties")
@Data
public class Properties {
   private String clientOriginUri;
}
