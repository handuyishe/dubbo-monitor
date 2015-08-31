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
import java.util.List;

/**
 * Line Chart Series
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 15/6/30.
 */
public class LineChartSeries implements Serializable {

    private String name;

    private List<double[]> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<double[]> getData() {
        return data;
    }

    public void setData(List<double[]> data) {
        this.data = data;
    }

}
