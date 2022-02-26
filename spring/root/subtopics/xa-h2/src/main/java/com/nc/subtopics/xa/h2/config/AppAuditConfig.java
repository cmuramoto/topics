package com.nc.subtopics.xa.h2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import me.snowdrop.boot.narayana.autoconfigure.NarayanaConfiguration;

@Import({ NarayanaConfiguration.class, XADatasourceConfig.class, PersistenceAuditConfig.class })
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.nc.services.audit" })
public class AppAuditConfig {

}
