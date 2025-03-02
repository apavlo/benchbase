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


package com.oltpbenchmark.benchmarks.indexjungle;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.indexjungle.procedures.GetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IndexJungleBenchmark extends BenchmarkModule {

    private static final Logger LOG = LoggerFactory.getLogger(IndexJungleBenchmark.class);

    /**
     * The number of columns to consider in lookup txns
     */
    protected final int lookup_cols_set_size;

    protected final long num_records;

    public IndexJungleBenchmark(WorkloadConfiguration workConf) {
        super(workConf);
        this.lookup_cols_set_size = workConf.getXmlConfig().getInt("lookupColumnsSize", 3);
        this.num_records = (int) Math.round(IndexJungleConstants.NUM_RECORDS * workConf.getScaleFactor());
    }

    @Override
    protected Package getProcedurePackageImpl() {
        return GetRange.class.getPackage();
    }

    @Override
    protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
        List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
        for (int i = 0; i < workConf.getTerminals(); ++i) {
            workers.add(new IndexJungleWorker(this, i));
        }
        return workers;
    }

    @Override
    protected Loader<IndexJungleBenchmark> makeLoaderImpl() {
        return new IndexJungleLoader(this);
    }

}
