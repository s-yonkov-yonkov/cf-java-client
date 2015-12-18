/*
 * Copyright 2013-2015 the original author or authors.
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

package org.cloudfoundry.operations;

import org.cloudfoundry.client.v2.Resource.Metadata;
import org.cloudfoundry.client.v2.organizations.ListOrganizationSpaceQuotaDefinitionsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationSpaceQuotaDefinitionsResponse;
import org.cloudfoundry.client.v2.spacequotadefinitions.SpaceQuotaDefinitionEntity;
import org.cloudfoundry.client.v2.spacequotadefinitions.SpaceQuotaDefinitionResource;
import org.cloudfoundry.utils.test.TestSubscriber;
import org.junit.Before;
import org.reactivestreams.Publisher;
import reactor.Publishers;

import static org.mockito.Mockito.when;

public final class DefaultSpaceQuotasTest {

    private static ListOrganizationSpaceQuotaDefinitionsResponse getListOrganizationSpaceQuotaDefinitionsResponse(
            int page, int numPages) {

        return ListOrganizationSpaceQuotaDefinitionsResponse.builder()
                .resource(getSpaceQuotaDefinitionResource(page))
                .totalPages(numPages)
                .build();
    }

    private static SpaceQuota getSpaceQuota(int index) {
        return SpaceQuota.builder()
                .id("test-id-" + index)
                .instanceMemoryLimit(1024)
                .paidServicePlans(true)
                .totalMemoryLimit(2048)
                .totalRoutes(10)
                .name("test-name-" + index)
                .organizationId("test-org-id-" + index)
                .build();
    }

    private static SpaceQuotaDefinitionResource getSpaceQuotaDefinitionResource(int resourceIndex) {
        return SpaceQuotaDefinitionResource.builder()
                .metadata(Metadata.builder()
                        .id("test-id-" + resourceIndex)
                        .build())
                .entity(SpaceQuotaDefinitionEntity.builder()
                        .instanceMemoryLimit(1024)
                        .memoryLimit(2048)
                        .name("test-name-" + resourceIndex)
                        .nonBasicServicesAllowed(true)
                        .organizationId("test-org-id-" + resourceIndex)
                        .totalRoutes(10)
                        .build())
                .build();
    }

    public static final class List extends AbstractOperationsApiTest<SpaceQuota> {

        private final SpaceQuotas spaceQuotas = new DefaultSpaceQuotas(this.cloudFoundryClient, TEST_ORGANIZATION);

        @Before
        public void setUp() throws Exception {
            ListOrganizationSpaceQuotaDefinitionsResponse page1 =
                    getListOrganizationSpaceQuotaDefinitionsResponse(1, 2);
            ListOrganizationSpaceQuotaDefinitionsResponse page2 =
                    getListOrganizationSpaceQuotaDefinitionsResponse(2, 2);

            when(this.cloudFoundryClient.organizations()
                    .listSpaceQuotaDefinitions(ListOrganizationSpaceQuotaDefinitionsRequest.builder()
                            .id(TEST_ORGANIZATION)
                            .page(1)
                            .build()))
                    .thenReturn(Publishers.just(page1));

            when(this.cloudFoundryClient.organizations()
                    .listSpaceQuotaDefinitions(ListOrganizationSpaceQuotaDefinitionsRequest.builder()
                            .id(TEST_ORGANIZATION)
                            .page(2)
                            .build()))
                    .thenReturn(Publishers.just(page2));
        }

        @Override
        protected void assertions(TestSubscriber<SpaceQuota> testSubscriber) throws Exception {
            testSubscriber
                    .assertEquals(getSpaceQuota(1))
                    .assertEquals(getSpaceQuota(2));
        }

        @Override
        protected Publisher<SpaceQuota> invoke() {
            return this.spaceQuotas.list();
        }

    }

    public static final class ListNoSpace extends AbstractOperationsApiTest<SpaceQuota> {

        private final SpaceQuotas spaceQuotas = new DefaultSpaceQuotas(this.cloudFoundryClient, null);

        @Override
        protected void assertions(TestSubscriber<SpaceQuota> testSubscriber) throws Exception {
            testSubscriber
                    .assertError(IllegalStateException.class);
        }

        @Override
        protected Publisher<SpaceQuota> invoke() {
            return this.spaceQuotas.list();
        }

    }

}