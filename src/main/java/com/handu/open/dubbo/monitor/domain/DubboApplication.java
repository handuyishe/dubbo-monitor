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
package com.handu.open.dubbo.monitor.domain;

import java.io.Serializable;

/**
 * Dubbo Application Entity
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 15/6/27.
 */
public class DubboApplication implements Serializable {

    private String name;

    private String owner;

    private String organization;

    private int providerCount;

    private int consumerCount;

    private int efferentCount;

    private int afferentCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public int getProviderCount() {
        return providerCount;
    }

    public void setProviderCount(int providerCount) {
        this.providerCount = providerCount;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
    }

    public int getEfferentCount() {
        return efferentCount;
    }

    public void setEfferentCount(int efferentCount) {
        this.efferentCount = efferentCount;
    }

    public int getAfferentCount() {
        return afferentCount;
    }

    public void setAfferentCount(int afferentCount) {
        this.afferentCount = afferentCount;
    }
}
