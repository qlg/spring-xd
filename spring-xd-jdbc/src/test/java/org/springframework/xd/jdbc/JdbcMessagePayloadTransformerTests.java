/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.xd.jdbc;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JdbcMessagePayloadTransformerTests {

    private final JdbcMessagePayloadTransformer transformer = new JdbcMessagePayloadTransformer();

    @Test
    public void testTransformWithColumns() throws Exception {
        String payload = "{\"id\": 123, \"name\":\"Sven\", \"age\":22}";
        transformer.setColumnNames("name, age");
        Map<String, Object> results = transformer.transformPayload(payload);
        assertEquals(3, results.size());
        assertEquals(123, results.get("id"));
        assertEquals("Sven", results.get("name"));
        assertEquals(22, results.get("age"));
        assertEquals("name, age", transformer.getColumns());
        assertEquals(":payload[name], :payload[age]", transformer.getValues());
    }

    @Test
    public void testTransformPayload() throws Exception {
        String payload = "{\"id\": 123, \"name\":\"Sven\", \"age\":22}";
        transformer.setColumnNames("payload");
        Map<String, Object> results = transformer.transformPayload(payload);
        assertEquals(1, results.size());
        assertEquals(payload, results.get("payload"));
        assertEquals("payload", transformer.getColumns());
        assertEquals(":payload[payload]", transformer.getValues());
    }

    @Test
    public void testNoColumns() throws Exception {
        assertEquals("", transformer.getColumns());
        assertEquals("", transformer.getValues());
    }

    @Test
    public void testColumnsWithSpace() throws Exception {
        transformer.setColumnNames("name, age");
        assertEquals("name, age", transformer.getColumns());
        assertEquals(":payload[name], :payload[age]", transformer.getValues());
    }

    @Test
    public void testColumnsNoSpace() throws Exception {
        transformer.setColumnNames("name,age");
        assertEquals("name, age", transformer.getColumns());
        assertEquals(":payload[name], :payload[age]", transformer.getValues());
    }
}
