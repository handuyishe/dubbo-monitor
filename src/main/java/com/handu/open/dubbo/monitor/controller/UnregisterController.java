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

import com.alibaba.dubbo.common.URL;
import com.handu.open.dubbo.monitor.RegistryContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * UnregisterController
 *
 * @author Jinkai.Ma
 */
@Controller
@RequestMapping("/unregister")
public class UnregisterController {

    @Autowired
    private RegistryContainer registryContainer;

    @RequestMapping(method = RequestMethod.GET)
    public String unregister(@RequestParam String provider, HttpServletRequest request) {
        URL providerUrl = URL.valueOf(provider);
        registryContainer.getRegistry().unregister(providerUrl);

        String page;
        Map<String, String[]> params = request.getParameterMap();

        if (params.containsKey("service")) {
            page = "services/providers?service=" + request.getParameter("service");
        } else if (params.containsKey("host")) {
            page = "hosts/providers?host=" + request.getParameter("host");
        } else if (params.containsKey("application")) {
            page = "applications/providers?application=" + request.getParameter("application");
        } else {
            page = "services/providers?service=" + providerUrl.getServiceInterface();
        }

        return "redirect:" + page;
    }
}
