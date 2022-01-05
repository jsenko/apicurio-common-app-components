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

package io.apicurio.common.apps.storage.sql;

import java.util.List;

/**
 * Returns SQL statements used by the SQL storage implementations.
 * @author eric.wittmann@gmail.com
 */
public interface CommonSqlStatements {

    /**
     * @return the database type associated with these statements.
     */
    public String dbType();

    /**
     * Returns true if the given exception represents a primary key violation.
     * @param error the underlying error/stack
     * @return true if the given error represents a primary key violation
     */
    public boolean isPrimaryKeyViolation(Exception error);

    /**
     * Returns true if the given exception represents a foreign key violation.
     * @param error the underlying error/stack
     * @return true if the given error represents a foreign key violation
     */
    public boolean isForeignKeyViolation(Exception error);

    /**
     * @return A statement that returns 'true' if the database has already been initialized.
     */
    public String isDatabaseInitialized();

    /**
     * @return A sequence of statements needed to initialize the database.
     */
    public List<String> databaseInitialization();

    /**
     * @return A sequence of statements needed to upgrade the DB from one version to another.
     *
     * @param fromVersion the version being upgraded from
     * @param toVersion the version being upgraded to
     */
    public List<String> databaseUpgrade(int fromVersion, int toVersion);

    /**
     * @return A statement that returns the current DB version (pulled from the "apicurio" attribute table).
     */
    public String getDatabaseVersion();


    /*
     * The next few statements support config properties.
     */

    public String selectConfigProperties();

    public String deleteConfigProperty();

    public String insertConfigProperty();

    public String deleteAllConfigProperties();

    public String selectConfigPropertyByName();

    public String selectTenantIdsByConfigModifiedOn();


}
