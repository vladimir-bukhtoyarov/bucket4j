/*
 *
 * Copyright 2015-2020 Vladimir Bukhtoyarov
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package io.github.bucket4j.distributed.proxy.optimizers.batch.sync;

import io.github.bucket4j.distributed.proxy.CommandExecutor;
import io.github.bucket4j.distributed.proxy.RequestOptimizer;
import io.github.bucket4j.distributed.proxy.optimizers.batch.sync.BatchingExecutor;

public class BatchingOptimizer implements RequestOptimizer {

    @Override
    public CommandExecutor optimize(CommandExecutor originalExecutor) {
        return new BatchingExecutor(originalExecutor);
    }

}