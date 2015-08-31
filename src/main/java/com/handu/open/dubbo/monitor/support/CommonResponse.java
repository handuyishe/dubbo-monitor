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
package com.handu.open.dubbo.monitor.support;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 公用应答对象
 *
 * @author Zhiguo.Chen on 30/6/15.
 */
public class CommonResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造方法
     */
    public CommonResponse() {
        super();
        this.put("success", false);
    }

    /**
     * 设置为成功应答
     */
    public void success() {
        this.put("success", true);
    }

    /**
     * 设置带指定消息的成功应答
     * @param message 成功提示消息
     */
    public void success(String message) {
        this.put("success", true);
        this.put("message", message);
    }

    /**
     * 返回一个失败消息
     * @param message 失败的提示消息
     */
    public void fail(String message) {
        this.put("success", false);
        this.put("message", message);
    }

    /**
     * 返回一个包含字段错误信息的错误消息
     * @param message
     * @param errors
     */
    public void fail(String message, List<FieldError> errors) {
        this.put("success", false);
        StringBuilder messageBuilder = new StringBuilder(message);
        messageBuilder.append("<ul>");
        for (FieldError fieldError : errors) {
            messageBuilder.append("<li>").append(fieldError.getDefaultMessage()).append("</li>");
        }
        messageBuilder.append("</ul>");
        this.put("message", messageBuilder.toString());
    }

    /**
     * 重定向地址
     * @param url 要重定向到的URL
     */
    public void redirect(String url) {
        this.put("redirect", url);
    }

    /**
     * 向通用应答内设置一项数据对象
     * @param data
     */
    public void setData(Object data) {
        Collection collection;
        if (!containsKey("data") || get("data") == null) {
            collection = new ArrayList();
            put("data", collection);
        } else {
            collection = (Collection) get("data");
        }
        collection.add(data);
    }

    /**
     * 向通用应答内设置一个集合对象
     * @param collection
     */
    public void setData(Collection collection) {
        this.put("data", collection);
    }

    /**
     * 快速创建一个成功应答对象
     * @return
     */
    public static CommonResponse createCommonResponse() {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        return commonResponse;
    }

    /**
     * 快速创建一个应答对象, 可传入一个数据对象, 并置为success
     * @param data
     * @return
     */
    public static CommonResponse createCommonResponse(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        commonResponse.setData(data);
        return commonResponse;
    }
}