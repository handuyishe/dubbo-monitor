package com.handu.open.dubbo.monitor.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * MongoDB Config
 *
 * Created by ZhiGuo.Chen on 2015/7/14.
 */
@Configuration
@ComponentScan
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private Environment env;

    private static final String DB_URL = "db.url";

    private static final String DB_PORT = "db.port";

    private List<Converter<?, ?>> converters = Lists.newArrayList();

    @Override
    protected String getDatabaseName() {
        return "dubbo_monitor";
    }


    @Override
    @Bean
    public Mongo mongo() throws Exception {
        final String url = Preconditions.checkNotNull(env.getProperty(DB_URL));
        final int port = Integer.parseInt(env.getProperty(DB_PORT, "27017"));
        return new MongoClient(singletonList(new ServerAddress(url, port)));

//        return new MongoClient(singletonList(new ServerAddress(url, port)),
//                singletonList(MongoCredential.createCredential("name", "db", "pwd".toCharArray())));
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(converters);
    }

}