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

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.handu.open.dubbo.monitor.RegistryContainer;
import com.handu.open.dubbo.monitor.domain.DubboHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * HostsController
 *
 * @author Jinkai.Ma
 */
@Controller
@RequestMapping("/hosts")
public class HostsController {

    @Autowired
    private RegistryContainer registryContainer;

    @RequestMapping(method = RequestMethod.GET)
    public String hosts(Model model) {
        List<DubboHost> rows = new ArrayList<DubboHost>();
        Set<String> hosts = registryContainer.getHosts();

        if (hosts != null && hosts.size() > 0) {
            DubboHost dubboHost;
            for (String host : hosts) {
                dubboHost = new DubboHost();

                dubboHost.setHost(host);
                dubboHost.setHostname(NetUtils.getHostName(host));

                List<URL> providers = registryContainer.getProvidersByHost(host);
                List<URL> consumers = registryContainer.getConsumersByHost(host);

                if ((providers != null && providers.size() > 0) || (consumers != null && consumers.size() > 0)) {
                    URL url = (providers != null && providers.size() > 0) ? providers.iterator().next() : consumers.iterator().next();
                    dubboHost.setApplication(url.getParameter(Constants.APPLICATION_KEY, ""));
                    dubboHost.setOwner(url.getParameter("owner", ""));
                    dubboHost.setOrganization((url.hasParameter("organization") ? url.getParameter("organization") : ""));
                }

                int providerSize = providers == null ? 0 : providers.size();
                dubboHost.setProviderCount(providerSize);

                int consumerSize = consumers == null ? 0 : consumers.size();
                dubboHost.setConsumerCount(consumerSize);

                rows.add(dubboHost);
            }
        }

        model.addAttribute("rows", rows);
        return "host/hosts";
    }

    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public String providers(@RequestParam String host, Model model) {
        List<URL> providers = registryContainer.getProvidersByHost(host);
        List<String> rows = new ArrayList<String>();
        if (providers != null && providers.size() > 0) {
            for (URL u : providers) {
                rows.add(u.toFullString());
            }
        }

        model.addAttribute("host", host);
        model.addAttribute("address", NetUtils.getHostName(host) + "/" + host);
        model.addAttribute("rows", rows);
        return "host/providers";
    }

    @RequestMapping(value = "/consumers", method = RequestMethod.GET)
    public String consumers(@RequestParam String host, Model model) {
        List<URL> consumers = registryContainer.getConsumersByHost(host);
        List<String> rows = new ArrayList<String>();
        if (consumers != null && consumers.size() > 0) {
            for (URL u : consumers) {
                rows.add(u.toFullString());
            }
        }

        model.addAttribute("host", host);
        model.addAttribute("address", NetUtils.getHostName(host) + "/" + host);
        model.addAttribute("rows", rows);
        return "host/consumers";
    }

}
