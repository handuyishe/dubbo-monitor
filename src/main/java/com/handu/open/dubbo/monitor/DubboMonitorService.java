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
package com.handu.open.dubbo.monitor;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import com.google.common.collect.Maps;
import com.handu.open.dubbo.monitor.domain.DubboInvoke;
import com.handu.open.dubbo.monitor.support.Dao;
import com.handu.open.dubbo.monitor.support.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MonitorService
 *
 * @author Jinkai.Ma
 */
@Service(delay = -1)
public class DubboMonitorService implements MonitorService {

    private static final Logger logger = LoggerFactory.getLogger(DubboMonitorService.class);

    public static final String CLASSNAME = DubboMonitorService.class.getName() + ".";

//    private static final String[] types = {SUCCESS, FAILURE, ELAPSED, CONCURRENT, MAX_ELAPSED, MAX_CONCURRENT};

    private static final String POISON_PROTOCOL = "poison";

    private static final String TIMESTAMP = "timestamp";

    private volatile boolean running = true;

    private Thread writeThread;

    private BlockingQueue<URL> queue;

    @Autowired
    private RegistryContainer registryContainer;

    @Autowired
    private Dao dao;

    @PostConstruct
    private void init() {
        queue = new LinkedBlockingQueue<URL>(Integer.parseInt(ConfigUtils.getProperty("dubbo.monitor.queue", "100000")));
        writeThread = new Thread(new Runnable() {
            public void run() {
                while (running) {
                    try {
                        writeToDataBase(); // 记录统计日志
                    } catch (Throwable t) { // 防御性容错
                        logger.error("Unexpected error occur at write stat log, cause: " + t.getMessage(), t);
                        try {
                            Thread.sleep(5000); // 失败延迟
                        } catch (Throwable t2) {
                        }
                    }
                }
            }
        });
        writeThread.setDaemon(true);
        writeThread.setName("DubboMonitorAsyncWriteLogThread");
        writeThread.start();
    }

    /**
     * Dubbo调用信息数据写入DB
     *
     * @throws Exception
     */
    private void writeToDataBase() throws Exception {
        URL statistics = queue.take();
        if (POISON_PROTOCOL.equals(statistics.getProtocol())) {
            return;
        }
        String timestamp = statistics.getParameter(Constants.TIMESTAMP_KEY);
        Date now;
        if (timestamp == null || timestamp.length() == 0) {
            now = new Date();
        } else if (timestamp.length() == "yyyyMMddHHmmss".length()) {
            now = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
        } else {
            now = new Date(Long.parseLong(timestamp));
        }
        DubboInvoke dubboInvoke = new DubboInvoke();

        dubboInvoke.setId(UuidUtil.createUUID());
        try {
            if (statistics.hasParameter(PROVIDER)) {
                dubboInvoke.setType(CONSUMER);
                dubboInvoke.setConsumer(statistics.getHost());
                dubboInvoke.setProvider(statistics.getParameter(PROVIDER));
                int i = dubboInvoke.getProvider().indexOf(':');
                if (i > 0) {
                    dubboInvoke.setProvider(dubboInvoke.getProvider().substring(0, i));
                }
            } else {
                dubboInvoke.setType(PROVIDER);
                dubboInvoke.setConsumer(statistics.getParameter(CONSUMER));
                int i = dubboInvoke.getConsumer().indexOf(':');
                if (i > 0) {
                    dubboInvoke.setConsumer(dubboInvoke.getConsumer().substring(0, i));
                }
                dubboInvoke.setProvider(statistics.getHost());
            }
            dubboInvoke.setInvokeDate(now);
            dubboInvoke.setService(statistics.getServiceInterface());
            dubboInvoke.setMethod(statistics.getParameter(METHOD));
            dubboInvoke.setInvokeTime(statistics.getParameter(TIMESTAMP, System.currentTimeMillis()));
            dubboInvoke.setSuccess(statistics.getParameter(SUCCESS, 0));
            dubboInvoke.setFailure(statistics.getParameter(FAILURE, 0));
            dubboInvoke.setElapsed(statistics.getParameter(ELAPSED, 0));
            dubboInvoke.setConcurrent(statistics.getParameter(CONCURRENT, 0));
            dubboInvoke.setMaxElapsed(statistics.getParameter(MAX_ELAPSED, 0));
            dubboInvoke.setMaxConcurrent(statistics.getParameter(MAX_CONCURRENT, 0));
            if (dubboInvoke.getSuccess() == 0 && dubboInvoke.getFailure() == 0 && dubboInvoke.getElapsed() == 0
                    && dubboInvoke.getConcurrent() == 0 && dubboInvoke.getMaxElapsed() == 0 && dubboInvoke.getMaxConcurrent() == 0) {
                return;
            }
            dao.insert(CLASSNAME, "addDubboInvoke", dubboInvoke);

        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }

    public void collect(URL statistics) {
        queue.offer(statistics);
        if (logger.isInfoEnabled()) {
            logger.info("collect statistics: " + statistics);
        }

    }

    public List<URL> lookup(URL query) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 统计调用数据用于图表展示
     *
     * @param dubboInvoke
     */
    public List<DubboInvoke> countDubboInvoke(DubboInvoke dubboInvoke) {
        if (StringUtils.isEmpty(dubboInvoke.getService())) {
            logger.error("统计查询缺少必要参数！");
            throw new RuntimeException("统计查询缺少必要参数！");
        }
        return dao.getList(CLASSNAME, "countDubboInvoke", dubboInvoke);
    }

    public List<String> getMethodsByService(DubboInvoke dubboInvoke) {
        return dao.getList(CLASSNAME, "getMethodsByService", dubboInvoke);
    }

    /**
     * 统计各方法调用信息
     *
     * @param dubboInvoke
     * @return
     */
    public List<DubboInvoke> countDubboInvokeInfo(DubboInvoke dubboInvoke) {
        if (StringUtils.isEmpty(dubboInvoke.getService()) || StringUtils.isEmpty(dubboInvoke.getMethod())
                || StringUtils.isEmpty(dubboInvoke.getType())) {
            logger.error("统计查询缺少必要参数！");
            throw new RuntimeException("统计查询缺少必要参数！");
        }
        return dao.getList(CLASSNAME, "countDubboInvokeInfo", dubboInvoke);
    }

    /**
     * 统计系统方法调用排序信息
     *
     * @param dubboInvoke
     * @return
     */
    public Map<String, List> countDubboInvokeTopTen(DubboInvoke dubboInvoke) {
        Map<String, List> result = Maps.newHashMap();
        result.put("success", dao.getList(CLASSNAME, "countDubboInvokeSuccessTopTen", dubboInvoke));
        result.put("failure", dao.getList(CLASSNAME, "countDubboInvokeFailureTopTen", dubboInvoke));
        return result;
    }
}