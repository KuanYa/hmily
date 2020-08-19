/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hmily.config.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hmily.config.api.exception.ConfigException;

/**
 * The type Config env.
 *
 * @author xiaoyu
 */
public final class ConfigEnv {
    
    private final Map<Class<?>, Config> configBeans = new ConcurrentHashMap<>();
    
    private static final ConfigEnv INST = new ConfigEnv();
    
    /**
     * Save some custom configuration information.
     */
    private ConfigEnv() {
        if (INST != null) {
            throw new ConfigException("repeated configEnv object.");
        }
    }
    
    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ConfigEnv getInstance() {
        return INST;
    }
    
    
    /**
     * Register config.
     *
     * @param config the config
     */
    public void registerConfig(final Config config) {
        if (config.getClass().getSuperclass().isAssignableFrom(AbstractConfig.class)) {
            putBean(config);
        }
    }
    
    /**
     * Gets config.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the config
     */
    @SuppressWarnings("unchecked")
    public <T extends Config> T getConfig(final Class<T> clazz) {
        return (T) configBeans.get(clazz);
    }
    
    /**
     * Register an object that needs to interpret configuration information .
     *
     * @param parent parent.
     */
    public void putBean(final Config parent) {
        if (parent != null && StringUtils.isNotBlank(parent.prefix())) {
            if (configBeans.containsKey(parent.getClass())) {
                return;
            }
            configBeans.put(parent.getClass(), parent);
        }
    }
    
    /**
     * Gets all loaded configuration information.
     *
     * @return stream. stream
     */
    public Stream<Config> stream() {
        return configBeans.values().stream().filter(e -> !e.isLoad());
    }
}
