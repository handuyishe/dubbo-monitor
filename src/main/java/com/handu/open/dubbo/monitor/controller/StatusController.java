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

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.status.Status;
import com.alibaba.dubbo.common.status.StatusChecker;
import com.handu.open.dubbo.monitor.domain.DubboStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * StatusController
 *
 * @author Jinkai.Ma
 */
@Controller
@RequestMapping("/status")
public class StatusController {

    @RequestMapping(method = RequestMethod.GET)
    public String status(Model model) {
        List<DubboStatus> rows = new ArrayList<DubboStatus>();
        Set<String> names = ExtensionLoader.getExtensionLoader(StatusChecker.class).getSupportedExtensions();
        DubboStatus dubboStatus;
        for (String name : names) {
            StatusChecker checker = ExtensionLoader.getExtensionLoader(StatusChecker.class).getExtension(name);
            Status status = checker.check();
            if (status != null && !Status.Level.UNKNOWN.equals(status.getLevel())) {
                dubboStatus = new DubboStatus();
                dubboStatus.setName(name);
                dubboStatus.setStatus(status);
                dubboStatus.setDescription(status.getMessage());
                rows.add(dubboStatus);
            }
        }
        model.addAttribute("rows", rows);
        return "status";
    }

}
