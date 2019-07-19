/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill.exec.coord.p2p;

import java.util.Collection;

import org.apache.drill.exec.coord.ClusterCoordinator;
import org.apache.drill.exec.coord.DistributedSemaphore;
import org.apache.drill.exec.coord.store.TransientStore;
import org.apache.drill.exec.coord.store.TransientStoreConfig;
import org.apache.drill.exec.proto.CoordinationProtos.DrillbitEndpoint;
import org.apache.drill.exec.proto.CoordinationProtos.DrillbitEndpoint.State;

public class P2pClusterCoordinator extends ClusterCoordinator {

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void start(long millisToWait) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public RegistrationHandle register(DrillbitEndpoint data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unregister(RegistrationHandle handle) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<DrillbitEndpoint> getAvailableEndpoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<DrillbitEndpoint> getOnlineEndPoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RegistrationHandle update(RegistrationHandle handle, State state) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DistributedSemaphore getSemaphore(String name, int maximumLeases) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <V> TransientStore<V> getOrCreateTransientStore(TransientStoreConfig<V> config) {
        // TODO Auto-generated method stub
        return null;
    }

}
