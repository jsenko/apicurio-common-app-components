package io.apicurio.common.apps.multitenancy;

import io.apicurio.common.apps.multitenancy.limits.TenantLimitsConfiguration;
import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;

public interface TenantContext {

    String DEFAULT_TENANT_ID = "_";

    /**
     * Get tenant ID.
     */
    String tenantId();

    ApicurioTenantContext currentContext();

    String tenantOwner();

    default String getTenantIdOrElse(String alternative) {
        return isLoaded() ? tenantId() : alternative;
    }

    TenantLimitsConfiguration limitsConfig();

    void setContext(ApicurioTenantContext ctx);

    void clearContext();

    boolean isLoaded();

    TenantStatusValue getTenantStatus();
}
