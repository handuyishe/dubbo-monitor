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
import com.handu.open.dubbo.monitor.domain.DubboApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Applications Controller
 *
 * @author Silence <me@chenzhiguo.cn>
 * Created on 15/6/26.
 */
@Controller
@RequestMapping("/applications")
public class ApplicationsController {

    @Autowired
    private RegistryContainer registryContainer;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        Set<String> applications = registryContainer.getApplications();
        List<DubboApplication> rows = new ArrayList<DubboApplication>();
        DubboApplication dubboApplication;
        if (applications != null && applications.size() > 0) {
            for (String application : applications) {
                dubboApplication = new DubboApplication();
                dubboApplication.setName(application);
                List<URL> providers = registryContainer.getProvidersByApplication(application);
                List<URL> consumers = registryContainer.getConsumersByApplication(application);

                if ((providers != null && providers.size() > 0) || (consumers != null && consumers.size() > 0)) {
                    URL url = (providers != null && providers.size() > 0) ? providers.iterator().next() : consumers.iterator().next();
                    dubboApplication.setName(application);
                    dubboApplication.setOwner(url.getParameter("owner", ""));
                    dubboApplication.setOrganization((url.hasParameter("organization") ? url.getParameter("organization") : ""));
                }

                dubboApplication.setProviderCount(providers == null ? 0 : providers.size());

                dubboApplication.setConsumerCount(consumers == null ? 0 : consumers.size());

                Set<String> efferents = registryContainer.getDependencies(application, false);
                dubboApplication.setEfferentCount(efferents == null ? 0 : efferents.size());

                Set<String> afferents = registryContainer.getDependencies(application, true);
                dubboApplication.setAfferentCount(afferents == null ? 0 : afferents.size());

                rows.add(dubboApplication);
            }
        }
        model.addAttribute("rows", rows);
        return "application/applications";
    }

    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public String providers(@RequestParam String application, Model model) {
        List<URL> providers = registryContainer.getProvidersByApplication(application);
        List<String> rows = new ArrayList<String>();
        if (providers != null && providers.size() > 0) {
            for (URL u : providers) {
                rows.add(u.toFullString());
            }
        }

        model.addAttribute("application", application);
        model.addAttribute("rows", rows);
        return "application/providers";
    }

    @RequestMapping(value = "/consumers", method = RequestMethod.GET)
    public String consumers(@RequestParam String application, Model model) {
        List<URL> consumers = registryContainer.getConsumersByApplication(application);
        List<String> rows = new ArrayList<String>();
        if (consumers != null && consumers.size() > 0) {
            for (URL u : consumers) {
                rows.add(u.toFullString());
            }
        }

        model.addAttribute("application", application);
        model.addAttribute("rows", rows);
        return "application/consumers";
    }

    @RequestMapping(value = "dependencies", method = RequestMethod.GET)
    public String dependencies(@RequestParam String application, @RequestParam(required = false) boolean reverse, Model model) {
        List<String> rows = new ArrayList<String>();

        Set<String> directly = registryContainer.getDependencies(application, reverse);
        Set<String> indirectly = new HashSet<String>();
        appendDependency(rows, reverse, application, 0, new HashSet<String>(), indirectly);

        model.addAttribute("application", application);
        model.addAttribute("reverse", reverse);
        model.addAttribute("rows", rows);
        return "application/dependencies";
    }

    private void appendDependency(List<String> rows, boolean reverse, String application, int level, Set<String> appended, Set<String> indirectly) {
        StringBuilder buf = new StringBuilder();
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                buf.append("<span style=\"margin-left:" + (level * 30) + "px;\"></span>");
            }
            buf.append(reverse ? "<i class=\"fa fa-level-down\" style=\"-webkit-transform: rotate(180deg);margin-right:5px;\"></i> " : "<i class=\"fa fa-level-up\" style=\"-webkit-transform: rotate(90deg);margin-right:5px;\"></i> ");
        }
        boolean end = false;
        if (level > 5) {
            buf.append(" <span class=\"badge bg-light-blue\">More...</span>");
            end = true;
        } else {
            buf.append(application);
            if (appended.contains(application)) {
                buf.append(" <span class=\"badge bg-red\">Cycle</span>");
                end = true;
            }
        }
        rows.add(buf.toString());
        if (end) {
            return;
        }

        appended.add(application);
        indirectly.add(application);
        Set<String> dependencies = registryContainer.getDependencies(application, reverse);
        if (dependencies != null && dependencies.size() > 0) {
            for (String dependency : dependencies) {
                appendDependency(rows, reverse, dependency, level + 1, appended, indirectly);
            }
        }
        appended.remove(application);
    }
}
