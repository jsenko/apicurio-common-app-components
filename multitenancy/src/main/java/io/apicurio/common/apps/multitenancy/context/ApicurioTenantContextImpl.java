package io.apicurio.common.apps.multitenancy.context;

import io.apicurio.common.apps.multitenancy.ApicurioTenantContext;
import io.apicurio.common.apps.multitenancy.limits.TenantLimitsConfiguration;
import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;

public class ApicurioTenantContextImpl implements ApicurioTenantContext {

    private final String tenantId;
    private final String tenantOwner;
    private final TenantLimitsConfiguration limitsConfiguration;
    private final TenantStatusValue status;
    private final String organizationId;

    public ApicurioTenantContextImpl(String tenantId, String tenantOwner, TenantLimitsConfiguration limitsConfiguration, TenantStatusValue status, String organizationId) {
        this.tenantId = tenantId;
        this.tenantOwner = tenantOwner;
        this.limitsConfiguration = limitsConfiguration;
        this.status = status;
        this.organizationId = organizationId;
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @return the limitsConfiguration
     */
    public TenantLimitsConfiguration getLimitsConfiguration() {
        return limitsConfiguration;
    }

    /**
     * @return the tenantOwner
     */
    public String getTenantOwner() {
        return tenantOwner;
    }

    public TenantStatusValue getStatus() {
        return status;
    }

    public String getOrganizationId() {
        return organizationId;
    }
}
