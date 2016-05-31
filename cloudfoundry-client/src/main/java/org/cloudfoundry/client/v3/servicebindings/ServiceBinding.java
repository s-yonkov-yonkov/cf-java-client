/*
 * Copyright 2013-2016 the original author or authors.
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

package org.cloudfoundry.client.v3.servicebindings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cloudfoundry.Nullable;
import org.cloudfoundry.client.v3.Link;

import java.util.Map;

/**
 * Base class for responses that are service bindings
 */
public abstract class ServiceBinding {

    /**
     * The created at
     */
    @JsonProperty("created_at")
    public abstract String getCreatedAt();

    /**
     * The datas
     */
    @JsonProperty("data")
    public abstract Map<String, Object> getDatas();

    /**
     * The id
     */
    @JsonProperty("guid")
    public abstract String getId();

    /**
     * The links
     */
    @JsonProperty("links")
    public abstract Map<String, Link> getLinks();

    /**
     * The type
     */
    @JsonProperty("type")
    public abstract String getType();

    /**
     * The updated at
     */
    @JsonProperty("updated_at")
    @Nullable
    public abstract String getUpdatedAt();

}