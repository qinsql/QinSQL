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
package org.lealone.p2p.server;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.drill.common.util.DrillVersionInfo;
import org.apache.drill.exec.coord.DistributedSemaphore;
import org.apache.drill.exec.coord.local.LocalClusterCoordinator;
import org.apache.drill.exec.coord.store.TransientStore;
import org.apache.drill.exec.coord.store.TransientStoreConfig;
import org.apache.drill.exec.proto.CoordinationProtos.DrillbitEndpoint;
import org.apache.drill.exec.proto.CoordinationProtos.DrillbitEndpoint.State;
import org.lealone.net.NetNode;
import org.lealone.p2p.gossip.Gossiper;

public class P2pClusterCoordinator extends LocalClusterCoordinator {

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void start(long millisToWait) throws Exception {
        super.start(millisToWait);
    }

    @Override
    public RegistrationHandle register(DrillbitEndpoint data) {
        return super.register(data);
    }

    @Override
    public void unregister(RegistrationHandle handle) {
        super.unregister(handle);
    }

    @Override
    public Collection<DrillbitEndpoint> getAvailableEndpoints() {
        return getOnlineEndPoints();
    }

    @Override
    public Collection<DrillbitEndpoint> getOnlineEndPoints() {
        ArrayList<DrillbitEndpoint> list = new ArrayList<>();
        for (NetNode node : Gossiper.instance.getLiveMembers()) {
            DrillbitEndpoint partialEndpoint = DrillbitEndpoint.newBuilder().setAddress(node.getHost())
                    .setVersion(DrillVersionInfo.getVersion()).setState(State.ONLINE)
                    .setControlPort(31011).setDataPort(31012).build();
            list.add(partialEndpoint);
        }
        return list;
    }

    @Override
    public RegistrationHandle update(RegistrationHandle handle, State state) {
        return super.update(handle, state);
    }

    @Override
    public DistributedSemaphore getSemaphore(String name, int maximumLeases) {
        return super.getSemaphore(name, maximumLeases);
    }

    @Override
    public <V> TransientStore<V> getOrCreateTransientStore(TransientStoreConfig<V> config) {
        return super.getOrCreateTransientStore(config);
    }

}
