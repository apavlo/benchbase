/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.oltpbenchmark.benchmarks.tpch.procedures;

import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.tpch.TPCHUtil;
import com.oltpbenchmark.util.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Q13 extends GenericQuery {

    public final SQLStmt query_stmt = new SQLStmt(
            "select "
                    + "c_count, "
                    + "count(*) as custdist "
                    + "from "
                    + "( "
                    + "select "
                    + "c_custkey, "
                    + "count(o_orderkey) as c_count "
                    + "from "
                    + "customer left outer join orders on "
                    + "c_custkey = o_custkey "
                    + "and o_comment not like ? "
                    + "group by "
                    + "c_custkey "
                    + ") as c_orders "
                    + "group by "
                    + "c_count "
                    + "order by "
                    + "custdist desc, "
                    + "c_count desc"
    );

    @Override
    protected PreparedStatement getStatement(Connection conn, RandomGenerator rand) throws SQLException {
        // WORD1 is randomly selected from 4 possible values: special, pending, unusual, express
        String word1 = TPCHUtil.choice(new String[]{"special", "pending", "unusual", "express"}, rand);

        // WORD2 is randomly selected from 4 possible values: packages, requests, accounts, deposits
        String word2 = TPCHUtil.choice(new String[]{"packages", "requests", "accounts", "deposits"}, rand);

        String filter = "%" + word1 + "%" + word2 + "%";

        PreparedStatement stmt = this.getPreparedStatement(conn, query_stmt);
        stmt.setString(1, filter);
        return stmt;
    }
}
