package io.apicurio.common.apps.multitenancy.limits;

import io.apicurio.tenantmanager.api.datamodel.ApicurioTenant;

public interface TenantLimitsConfigurationService {

    TenantLimitsConfiguration fromTenantMetadata(ApicurioTenant tenantMetadata);

    TenantLimitsConfiguration defaultConfigurationTenant();

    boolean isConfigured();
}
