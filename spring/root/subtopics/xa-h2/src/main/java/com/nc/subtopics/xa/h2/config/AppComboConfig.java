package com.nc.subtopics.xa.h2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({ AppAuditConfig.class, AppInternalConfig.class })
@ComponentScan(basePackages = { "com.nc.subtopics.xa.ha.services" })
public class AppComboConfig {

}
