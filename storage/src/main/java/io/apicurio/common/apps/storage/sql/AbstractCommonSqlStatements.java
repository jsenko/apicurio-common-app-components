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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.apicurio.common.apps.storage.exceptions.StorageException;
import io.apicurio.common.apps.storage.sql.jdbi.parse.DdlParser;

/**
 * Shared base class for all sql statements.
 * @author eric.wittmann@gmail.com
 */
public abstract class AbstractCommonSqlStatements implements CommonSqlStatements {

    /**
     * Constructor.
     */
    public AbstractCommonSqlStatements() {
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#databaseInitialization()
     */
    @Override
    public List<String> databaseInitialization() {
        DdlParser parser = new DdlParser();
        try (InputStream input = getClass().getResourceAsStream(dbType() + ".ddl")) {
            if (input == null) {
                throw new RuntimeException("DDL not found for dbtype: " + dbType());
            }
            return parser.parse(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#databaseUpgrade(int, int)
     */
    @Override
    public List<String> databaseUpgrade(int fromVersion, int toVersion) {
        List<String> statements = new ArrayList<>();
        DdlParser parser = new DdlParser();

        for (int version = fromVersion + 1; version <= toVersion; version++) {
            try (InputStream input = getClass().getResourceAsStream("upgrades/" + version + "/" + dbType() + ".upgrade.ddl")) {
                statements.addAll(parser.parse(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return statements;
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#getDatabaseVersion()
     */
    @Override
    public String getDatabaseVersion() {
        return "SELECT a.prop_value FROM apicurio a WHERE a.prop_name = ?";
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#isPrimaryKeyViolation(java.lang.Exception)
     */
    @Override
    public boolean isPrimaryKeyViolation(Exception error) {
        if ("postgresql".equals(dbType())) {
            return error.getMessage().contains("violates unique constraint");
        } else if ("h2".equals(dbType()) ) {
            return error.getMessage() != null && error.getMessage().contains("primary key violation");
        } else {
            throw new StorageException("Unsupported DB type: " + dbType());
        }
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#isForeignKeyViolation(java.lang.Exception)
     */
    @Override
    public boolean isForeignKeyViolation(Exception error) {
        if ("postgresql".equals(dbType())) {
            return error.getMessage().contains("violates foreign key constraint");
        } else if ("h2".equals(dbType()) ) {
            return error.getMessage() != null && error.getMessage().contains("Referential integrity constraint violation");
        } else {
            throw new StorageException("Unsupported DB type: " + dbType());
        }
    }

    /**
     * @see io.apicurio.common.apps.storage.sql.CommonSqlStatements#isDatabaseInitialized()
     */
    @Override
    public String isDatabaseInitialized() {
        if ("postgresql".equals(dbType())) {
            return "SELECT count(*) AS count FROM information_schema.tables WHERE table_name = 'apicurio'";
        } else if ("h2".equals(dbType()) ) {
            return "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = 'APICURIO'";
        } else {
            throw new StorageException("Unsupported DB type: " + dbType());
        }
    }

}
