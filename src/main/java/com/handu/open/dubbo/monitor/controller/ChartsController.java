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
package com.handu.open.dubbo.monitor.controller;

import com.handu.open.dubbo.monitor.DubboMonitorService;
import com.handu.open.dubbo.monitor.domain.DubboInvoke;
import com.handu.open.dubbo.monitor.domain.DubboInvokeLineChart;
import com.handu.open.dubbo.monitor.domain.LineChartSeries;
import com.handu.open.dubbo.monitor.support.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Charts Controller
 *
 * @author Zhiguo.Chen <me@chenzhiguo.cn>
 *         Created on 15/6/30.
 */
@Controller
@RequestMapping("/services/charts")
public class ChartsController {

    @Autowired
    private DubboMonitorService dubboMonitorService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute DubboInvoke dubboInvoke, Model model) {
        //获取Service方法
        List<String> methods = dubboMonitorService.getMethodsByService(dubboInvoke);
        model.addAttribute("service", dubboInvoke.getService());
        model.addAttribute("rows", methods);
        return "service/charts";
    }

    @ResponseBody
    @RequestMapping(value = "loadChartsData")
    public CommonResponse loadChartsData(@ModelAttribute DubboInvoke dubboInvoke) {
        // 计算统计平均请求次数的时间粒度
        long timeParticle = dubboInvoke.getTimeParticle() / 1000;
        List<DubboInvokeLineChart> dubboInvokeLineChartList = new ArrayList<DubboInvokeLineChart>();
        DubboInvokeLineChart qpsLineChart;
        DubboInvokeLineChart artLineChart;
        List<LineChartSeries> qpsLineChartSeriesList;
        List<LineChartSeries> artLineChartSeriesList;
        LineChartSeries qpsLineChartSeries;
        LineChartSeries artLineChartSeries;
        List<double[]> qpsSeriesDatas;
        List<double[]> artSeriesDatas;
        List<String> methods = dubboMonitorService.getMethodsByService(dubboInvoke);
        for (String method : methods) {
            qpsLineChart = new DubboInvokeLineChart();
            artLineChart = new DubboInvokeLineChart();
            qpsLineChartSeriesList = new ArrayList<LineChartSeries>();
            artLineChartSeriesList = new ArrayList<LineChartSeries>();
            dubboInvoke.setMethod(method);
            // 组织Provider折线数据
            qpsLineChartSeries = new LineChartSeries();
            artLineChartSeries = new LineChartSeries();
            dubboInvoke.setType("provider");
            List<DubboInvoke> providerDubboInvokeDetails = dubboMonitorService.countDubboInvoke(dubboInvoke);
            qpsLineChartSeries.setName(dubboInvoke.getType());
            artLineChartSeries.setName(dubboInvoke.getType());
            qpsSeriesDatas = new ArrayList<double[]>();
            artSeriesDatas = new ArrayList<double[]>();
            double[] qpsProviderSeriesData;
            double[] artProviderSeriesData;
            for (DubboInvoke dubboInvokeDetail : providerDubboInvokeDetails) {
                qpsProviderSeriesData = new double[]{dubboInvokeDetail.getInvokeTime(), Double.valueOf(String.format("%.4f", dubboInvokeDetail.getSuccess() / timeParticle))};
                qpsSeriesDatas.add(qpsProviderSeriesData);
                artProviderSeriesData = new double[]{dubboInvokeDetail.getInvokeTime(), Double.valueOf(String.format("%.4f", dubboInvokeDetail.getElapsed()))};
                artSeriesDatas.add(artProviderSeriesData);
            }
            qpsLineChartSeries.setData(qpsSeriesDatas);
            qpsLineChartSeriesList.add(qpsLineChartSeries);
            artLineChartSeries.setData(artSeriesDatas);
            artLineChartSeriesList.add(artLineChartSeries);
            // 组织Consumer折线数据
            qpsLineChartSeries = new LineChartSeries();
            artLineChartSeries = new LineChartSeries();
            dubboInvoke.setType("consumer");
            List<DubboInvoke> consumerDubboInvokeDetails = dubboMonitorService.countDubboInvoke(dubboInvoke);
            qpsLineChartSeries.setName(dubboInvoke.getType());
            artLineChartSeries.setName(dubboInvoke.getType());
            qpsSeriesDatas = new ArrayList<double[]>();
            artSeriesDatas = new ArrayList<double[]>();
            double[] qpsConsumerSeriesData;
            double[] artConsumerSeriesData;
            for (DubboInvoke dubboInvokeDetail : consumerDubboInvokeDetails) {
                qpsConsumerSeriesData = new double[]{dubboInvokeDetail.getInvokeTime(), Double.valueOf(String.format("%.4f", dubboInvokeDetail.getSuccess() / timeParticle))};
                qpsSeriesDatas.add(qpsConsumerSeriesData);
                artConsumerSeriesData = new double[]{dubboInvokeDetail.getInvokeTime(), Double.valueOf(String.format("%.4f", dubboInvokeDetail.getElapsed()))};
                artSeriesDatas.add(artConsumerSeriesData);
            }
            qpsLineChartSeries.setData(qpsSeriesDatas);
            qpsLineChartSeriesList.add(qpsLineChartSeries);
            artLineChartSeries.setData(artSeriesDatas);
            artLineChartSeriesList.add(artLineChartSeries);

            //====================== 统计QPS ===========================
            qpsLineChart.setSeriesData(qpsLineChartSeriesList);
            qpsLineChart.setTitle("Requests per second (QPS)");
            qpsLineChart.setyAxisTitle("t/s");
            qpsLineChart.setMethod(method);
            qpsLineChart.setChartType("QPS");

            dubboInvokeLineChartList.add(qpsLineChart);

            //====================== 统计ART ===========================
            artLineChart.setSeriesData(artLineChartSeriesList);
            artLineChart.setTitle("Average response time (ms)");
            artLineChart.setyAxisTitle("ms/t");
            artLineChart.setMethod(method);
            artLineChart.setChartType("ART");

            dubboInvokeLineChartList.add(artLineChart);
        }

        CommonResponse commonResponse = CommonResponse.createCommonResponse();
        commonResponse.setData(dubboInvokeLineChartList);
        return commonResponse;
    }
}
