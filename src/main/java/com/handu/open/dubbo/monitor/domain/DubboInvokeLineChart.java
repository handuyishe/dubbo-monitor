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
 * LineChart
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 15/6/30.
 */
public class DubboInvokeLineChart implements Serializable {

    private String method;

    private String chartType;

    private String title;

    private String subtitle;

    private List<String> xAxisCategories;

    private String yAxisTitle;

    private List<LineChartSeries> seriesData;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getxAxisCategories() {
        return xAxisCategories;
    }

    public void setxAxisCategories(List<String> xAxisCategories) {
        this.xAxisCategories = xAxisCategories;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public List<LineChartSeries> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<LineChartSeries> seriesData) {
        this.seriesData = seriesData;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}
