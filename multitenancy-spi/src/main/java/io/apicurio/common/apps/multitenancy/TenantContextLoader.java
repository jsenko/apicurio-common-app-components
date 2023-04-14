package io.apicurio.common.apps.multitenancy;

public interface TenantContextLoader {

    ApicurioTenantContext loadBatchJobContext(String tenantId);

    ApicurioTenantContext loadRequestContext(String tenantId);

    ApicurioTenantContext defaultTenantContext();

    void invalidateTenantInCache(String tenantId);

    void invalidateTenantCache();
}
