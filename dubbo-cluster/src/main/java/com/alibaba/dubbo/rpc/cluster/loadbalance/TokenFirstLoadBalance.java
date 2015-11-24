/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.cluster.loadbalance;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * tokenfirst load balance.
 *
 * @author chenrui
 */
public class TokenFirstLoadBalance extends AbstractLoadBalance {

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String token = url.getParameter("token");
        List<Invoker<T>> filteredInvokers = new ArrayList<Invoker<T>>();
        if(!StringUtils.isBlank(token)) {
            for (Invoker<T> invoker : invokers) {
                if (token.equals(invoker.getUrl().getParameter("token"))) {
                    filteredInvokers.add(invoker);
                }
            }
        }
        if(filteredInvokers.isEmpty()) {
            filteredInvokers = invokers;
        }
        return ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getExtension(Constants.DEFAULT_LOADBALANCE)
                .select(filteredInvokers, url, invocation);
    }

}