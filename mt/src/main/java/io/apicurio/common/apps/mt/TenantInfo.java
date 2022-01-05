/*
 * Copyright 2021 Red Hat
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

package io.apicurio.common.apps.mt;

/**
 * @author eric.wittmann@gmail.com
 */
public class TenantInfo {

    private String id;
    private String owner;
    private String orgId;
    private TenantStatus status;
    private TenantLimits limits;

    /**
     * Constructor.
     * @param id the unique tenant ID
     * @param owner the owner of the tenant
     * @param orgId the unique ID of the organization the tenant is in
     * @param status the status of the tenant
     * @param limits information about the tenant's limits
     */
    public TenantInfo(String id, String owner, String orgId, TenantStatus status, TenantLimits limits) {
        this.id = id;
        this.owner = owner;
        this.orgId = orgId;
        this.status = status;
        this.limits = limits;
    }

    /**
     * Constructor.
     */
    public TenantInfo() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the status
     */
    public TenantStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TenantStatus status) {
        this.status = status;
    }

    /**
     * @return the limits
     */
    public TenantLimits getLimits() {
        return limits;
    }

    /**
     * @param limits the limits to set
     */
    public void setLimits(TenantLimits limits) {
        this.limits = limits;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TenantInfo [id=" + id + ", owner=" + owner + ", orgId=" + orgId + ", status=" + status + "]";
    }

}
