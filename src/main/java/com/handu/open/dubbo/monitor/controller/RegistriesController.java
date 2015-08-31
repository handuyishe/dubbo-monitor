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
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.support.AbstractRegistry;
import com.alibaba.dubbo.registry.support.AbstractRegistryFactory;
import com.handu.open.dubbo.monitor.domain.DubboRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * RegistiesController
 *
 * @author Jinkai.Ma
 */
@Controller
@RequestMapping(value = "/registries")
public class RegistriesController {

    @RequestMapping(method = RequestMethod.GET)
    public String registries(Model model) {
        List<DubboRegistry> rows = new ArrayList<DubboRegistry>();
        Collection<Registry> registries = AbstractRegistryFactory.getRegistries();
        if (registries.size() > 0) {
            DubboRegistry dubboRegistry;
            for (Registry registry : registries) {
                dubboRegistry = new DubboRegistry();
                dubboRegistry.setServer(registry.getUrl().getAddress());
                dubboRegistry.setHostname(NetUtils.getHostName(dubboRegistry.getServer()));
                dubboRegistry.setAvailable(registry.isAvailable());
                if (registry instanceof AbstractRegistry) {
                    dubboRegistry.setRegisteredCount(((AbstractRegistry) registry).getRegistered().size());
                    dubboRegistry.setSubscribedCount(((AbstractRegistry) registry).getSubscribed().size());
                }
                rows.add(dubboRegistry);
            }
        }
        model.addAttribute("rows", rows);
        return "registry/registries";
    }

    @RequestMapping(value = "/registered", method = RequestMethod.GET)
    public String registered(@RequestParam String registry, Model model) {
        Collection<Registry> registries = AbstractRegistryFactory.getRegistries();
        Registry reg = null;
        if (registries.size() > 0) {
            for (Registry r : registries) {
                String sp = r.getUrl().getAddress();
                if (((registry == null || registry.length() == 0) && reg == null) || sp.equals(registry)) {
                    reg = r;
                }
            }
        }

        List<String> rows = new ArrayList<String>();

        if (reg instanceof AbstractRegistry) {
            Set<URL> services = ((AbstractRegistry) reg).getRegistered();
            if (services != null && services.size() > 0) {
                for (URL u : services) {
                    rows.add(u.toFullString());
                }
            }
        }

        model.addAttribute("registry", registry);
        model.addAttribute("rows", rows);
        return "registry/registered";
    }

    @RequestMapping(value = "/subscribed", method = RequestMethod.GET)
    public String subscribed(@RequestParam String registry, Model model) {
        Collection<Registry> registries = AbstractRegistryFactory.getRegistries();
        Registry reg = null;
        if (registries.size() > 0) {
            for (Registry r : registries) {
                String sp = r.getUrl().getAddress();
                if (((registry == null || registry.length() == 0) && reg == null) || sp.equals(registry)) {
                    reg = r;
                }
            }
        }

        List<String> rows = new ArrayList<String>();

        if (reg instanceof AbstractRegistry) {
            Set<URL> services = ((AbstractRegistry) reg).getSubscribed().keySet();
            if (services.size() > 0) {
                for (URL u : services) {
                    rows.add(u.toFullString());
                }
            }
        }

        model.addAttribute("registry", registry);
        model.addAttribute("rows", rows);
        return "registry/subscribed";
    }

}
