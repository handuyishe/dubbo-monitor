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

import com.alibaba.dubbo.common.utils.NetUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SystemController
 *
 * @author Jinkai.Ma
 */
@Controller
@RequestMapping("/system")
public class SystemController {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    @RequestMapping(method = RequestMethod.GET)
    public String system(Model model) {
        List<String[]> rows = new ArrayList<String[]>();

        rows.add(new String[]{"Version", "2.8.4"});

        String address = NetUtils.getLocalHost();
        rows.add(new String[]{"Host", NetUtils.getHostName(address) + "/" + address});

        rows.add(new String[]{"OS", System.getProperty("os.name") + " " + System.getProperty("os.version")});

        rows.add(new String[]{"JVM", System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version") + ",<br/>" + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version") + " " + System.getProperty("java.vm.info", "")});

        rows.add(new String[]{"CPU", System.getProperty("os.arch", "") + ", " + String.valueOf(Runtime.getRuntime().availableProcessors()) + " cores"});

        rows.add(new String[]{"Locale", Locale.getDefault().toString() + "/" + System.getProperty("file.encoding")});

        rows.add(new String[]{"Uptime", formatUptime(ManagementFactory.getRuntimeMXBean().getUptime())});

        rows.add(new String[]{"Time", formatter.format(new Date())});

        model.addAttribute("rows", rows);
        return "system";
    }

    private static final long SECOND = 1000;

    private static final long MINUTE = 60 * SECOND;

    private static final long HOUR = 60 * MINUTE;

    private static final long DAY = 24 * HOUR;

    private String formatUptime(long uptime) {
        StringBuilder buf = new StringBuilder();
        if (uptime > DAY) {
            long days = (uptime - uptime % DAY) / DAY;
            buf.append(days);
            buf.append(" Days");
            uptime = uptime % DAY;
        }
        if (uptime > HOUR) {
            long hours = (uptime - uptime % HOUR) / HOUR;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(hours);
            buf.append(" Hours");
            uptime = uptime % HOUR;
        }
        if (uptime > MINUTE) {
            long minutes = (uptime - uptime % MINUTE) / MINUTE;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(minutes);
            buf.append(" Minutes");
            uptime = uptime % MINUTE;
        }
        if (uptime > SECOND) {
            long seconds = (uptime - uptime % SECOND) / SECOND;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(seconds);
            buf.append(" Seconds");
            uptime = uptime % SECOND;
        }
        if (uptime > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(uptime);
            buf.append(" Milliseconds");
        }
        return buf.toString();
    }

}
