/**
 * Copyright 2006-2015 handu.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.handu.open.dubbo.monitor.domain;

import java.io.Serializable;

/**
 * Dubbo Statistics Entity
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 15/7/2.
 */
public class DubboStatistics implements Serializable {

    private String method;

    private double consumerSuccess;

    private double providerSuccess;

    private double consumerFailure;

    private double providerFailure;

    private double consumerAvgElapsed;

    private double providerAvgElapsed;

    private double consumerMaxElapsed;

    private double providerMaxElapsed;

    private double consumerMaxConcurrent;

    private double providerMaxConcurrent;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getConsumerSuccess() {
        return consumerSuccess;
    }

    public void setConsumerSuccess(double consumerSuccess) {
        this.consumerSuccess = consumerSuccess;
    }

    public double getProviderSuccess() {
        return providerSuccess;
    }

    public void setProviderSuccess(double providerSuccess) {
        this.providerSuccess = providerSuccess;
    }

    public double getConsumerFailure() {
        return consumerFailure;
    }

    public void setConsumerFailure(double consumerFailure) {
        this.consumerFailure = consumerFailure;
    }

    public double getProviderFailure() {
        return providerFailure;
    }

    public void setProviderFailure(double providerFailure) {
        this.providerFailure = providerFailure;
    }

    public double getConsumerAvgElapsed() {
        return consumerAvgElapsed;
    }

    public void setConsumerAvgElapsed(double consumerAvgElapsed) {
        this.consumerAvgElapsed = consumerAvgElapsed;
    }

    public double getProviderAvgElapsed() {
        return providerAvgElapsed;
    }

    public void setProviderAvgElapsed(double providerAvgElapsed) {
        this.providerAvgElapsed = providerAvgElapsed;
    }

    public double getConsumerMaxElapsed() {
        return consumerMaxElapsed;
    }

    public void setConsumerMaxElapsed(double consumerMaxElapsed) {
        this.consumerMaxElapsed = consumerMaxElapsed;
    }

    public double getProviderMaxElapsed() {
        return providerMaxElapsed;
    }

    public void setProviderMaxElapsed(double providerMaxElapsed) {
        this.providerMaxElapsed = providerMaxElapsed;
    }

    public double getConsumerMaxConcurrent() {
        return consumerMaxConcurrent;
    }

    public void setConsumerMaxConcurrent(double consumerMaxConcurrent) {
        this.consumerMaxConcurrent = consumerMaxConcurrent;
    }

    public double getProviderMaxConcurrent() {
        return providerMaxConcurrent;
    }

    public void setProviderMaxConcurrent(double providerMaxConcurrent) {
        this.providerMaxConcurrent = providerMaxConcurrent;
    }
}
