package io.apicurio.common.apps.multitenancy;

import io.apicurio.common.apps.multitenancy.limits.TenantLimitsConfiguration;
import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;

public interface ApicurioTenantContext {

    String getTenantId();

    TenantLimitsConfiguration getLimitsConfiguration();

    String getTenantOwner();

    TenantStatusValue getStatus();

    String getOrganizationId();

}
