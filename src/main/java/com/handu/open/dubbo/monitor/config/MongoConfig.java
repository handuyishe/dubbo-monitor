package com.handu.open.dubbo.monitor.config;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * MongoDB Config
 *
 * Created by ZhiGuo on 2015/7/14.
 */
@Configuration
@ComponentScan
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private List<Converter<?, ?>> converters;

    @Override
    protected String getDatabaseName() {
        return "dubbo-invoke";
    }

//    @Override
//    public Mongo mongo() throws Exception {
//        Mongo mongo = new Mongo("127.0.0.1", 27017);
//        mongo.setWriteConcern(WriteConcern.SAFE);
//        return mongo;
//    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(singletonList(new ServerAddress("127.0.0.1", 27017)),
                singletonList(MongoCredential.createCredential("name", "db", "pwd".toCharArray())));
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(converters);
    }

}