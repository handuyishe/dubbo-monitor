/**
 * Copyright 2006-2015 handu.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.handu.open.dubbo.monitor.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
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