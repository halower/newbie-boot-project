
package org.springframework.cloud.alibaba.dubbo.metadata;

import org.apache.dubbo.config.ProtocolConfig;

import org.springframework.beans.factory.ObjectProvider;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

import static org.apache.dubbo.common.Constants.DEFAULT_PROTOCOL;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Dubbo's {@link ProtocolConfig} {@link Supplier}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboProtocolConfigSupplier implements Supplier<ProtocolConfig> {

    private final ObjectProvider<Collection<ProtocolConfig>> protocols;

    public DubboProtocolConfigSupplier(ObjectProvider<Collection<ProtocolConfig>> protocols) {
        this.protocols = protocols;
    }

    @Override
    public ProtocolConfig get() {
        ProtocolConfig protocolConfig = null;
        Collection<ProtocolConfig> protocols = this.protocols.getIfAvailable();

        if (!isEmpty(protocols)) {
            for (ProtocolConfig protocol : protocols) {
                String protocolName = protocol.getName();
                if (DEFAULT_PROTOCOL.equals(protocolName)) {
                    protocolConfig = protocol;
                    break;
                }
            }

            if (protocolConfig == null) { // If The ProtocolConfig bean named "dubbo" is absent, take first one of them
                Iterator<ProtocolConfig> iterator = protocols.iterator();
                protocolConfig = iterator.hasNext() ? iterator.next() : null;
            }
        }

        if (protocolConfig == null) {
            protocolConfig = new ProtocolConfig();
            protocolConfig.setName(DEFAULT_PROTOCOL);
            protocolConfig.setPort(-1);
        }

        return protocolConfig;
    }
}
