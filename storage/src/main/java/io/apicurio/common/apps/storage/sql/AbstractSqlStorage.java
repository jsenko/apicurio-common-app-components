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

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import io.apicurio.common.apps.storage.sql.jdbi.Handle;
import io.apicurio.common.apps.storage.sql.jdbi.HandleFactory;

/**
 * @author eric.wittmann@gmail.com
 */
public abstract class AbstractSqlStorage<S extends CommonSqlStatements> {

    @Inject
    protected Logger log;

    @Inject
    protected HandleFactory handles;

    @Inject
    protected S sqlStatements;

    @ConfigProperty(name = "app.sql.init", defaultValue = "true")
    boolean initDB;

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    /**
     * @return the expected version of the DB
     */
    protected abstract int dbVersion();

    /**
     * Does any initialization logic for the SQL storage.  This might try to upgrade the DB or simply
     * verify that the DB is correct.
     */
    protected void doInitialize() {
        log.info("    JDBC URL: " + jdbcUrl);
        handles.withHandleNoException((handle) -> {
            if (initDB) {
                if (!isDatabaseInitialized(handle)) {
                    log.info("Database not initialized.");
                    initializeDatabase(handle);
                } else {
                    log.info("Database was already initialized, skipping.");
                }

                if (!isDatabaseCurrent(handle)) {
                    log.info("Old database version detected, upgrading.");
                    upgradeDatabase(handle);
                }
            } else {
                if (!isDatabaseInitialized(handle)) {
                    log.error("Database not initialized.  Please use the DDL scripts to initialize the database before starting the application.");
                    throw new RuntimeException("Database not initialized.");
                }

                if (!isDatabaseCurrent(handle)) {
                    log.error("Detected an old version of the database.  Please use the DDL upgrade scripts to bring your database up to date.");
                    throw new RuntimeException("Database not upgraded.");
                }
            }
            return null;
        });
    }

    /**
     * @return true if the database has already been initialized
     */
    private boolean isDatabaseInitialized(Handle handle) {
        log.info("Checking to see if the DB is initialized.");
        int count = handle.createQuery(this.sqlStatements.isDatabaseInitialized()).mapTo(Integer.class).one();
        return count > 0;
    }

    /**
     * @return true if the database has already been initialized
     */
    private boolean isDatabaseCurrent(Handle handle) {
        log.info("Checking to see if the DB is up-to-date.");
        log.info("Build's DB version is {}", dbVersion());
        int version = this.getDatabaseVersion(handle);
        return version == dbVersion();
    }

    private void initializeDatabase(Handle handle) {
        log.info("Initializing the Apicurio Registry database.");
        log.info("\tDatabase type: " + this.sqlStatements.dbType());

        final List<String> statements = this.sqlStatements.databaseInitialization();
        log.debug("---");

        statements.forEach( statement -> {
            log.debug(statement);
            handle.createUpdate(statement).execute();
        });
        log.debug("---");
    }

    /**
     * Upgrades the database by executing a number of DDL statements found in DB-specific
     * DDL upgrade scripts.
     */
    private void upgradeDatabase(Handle handle) {
        log.info("Upgrading the Apicurio Hub API database.");

        int fromVersion = this.getDatabaseVersion(handle);
        int toVersion = dbVersion();

        log.info("\tDatabase type: {}", this.sqlStatements.dbType());
        log.info("\tFrom Version:  {}", fromVersion);
        log.info("\tTo Version:    {}", toVersion);

        final List<String> statements = this.sqlStatements.databaseUpgrade(fromVersion, toVersion);
        log.debug("---");
        statements.forEach( statement -> {
            log.debug(statement);

            if (statement.startsWith("UPGRADER:")) {
                String cname = statement.substring(9).trim();
                applyUpgrader(handle, cname);
            } else {
                handle.createUpdate(statement).execute();
            }
        });
        log.debug("---");
    }

    /**
     * Instantiates an instance of the given upgrader class and then invokes it.  Used to perform
     * advanced upgrade logic when upgrading the DB (logic that cannot be handled in simple SQL
     * statements).
     * @param handle
     * @param cname
     */
    private void applyUpgrader(Handle handle, String cname) {
        try {
            @SuppressWarnings("unchecked")
            Class<IDbUpgrader> upgraderClass = (Class<IDbUpgrader>) Class.forName(cname);
            IDbUpgrader upgrader = upgraderClass.getConstructor().newInstance();
            upgrader.upgrade(handle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reuturns the current DB version by selecting the value in the 'apicurio' table.
     */
    private int getDatabaseVersion(Handle handle) {
        try {
            int version = handle.createQuery(this.sqlStatements.getDatabaseVersion())
                    .bind(0, "db_version")
                    .mapTo(Integer.class)
                    .one();
            return version;
        } catch (Exception e) {
            log.error("Error getting DB version.", e);
            return 0;
        }
    }

}
