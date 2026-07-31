package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.ta.opcua.utils.OPCConnectionException;

@ModelEntity
@ImplementationClass(value = OPCObjectNode.OPCObjectNodeImpl.class)
public interface OPCObjectNode extends OPCInstanceNode<UaObjectNode> {

    public static abstract class OPCObjectNodeImpl extends OPCInstanceNodeImpl<UaObjectNode> implements OPCObjectNode {

        private UaObjectNode objectNode = null;

        @Override
        public UaObjectNode getNode() {
            if (objectNode != null) return objectNode;
            final OpcUaClient client = getResourceData().getClient();
            if (client == null)
                throw new OPCConnectionException(getResourceData().getUri());
            try {
                objectNode = client.getAddressSpace().getObjectNode(getNodeId());
                return objectNode;
            } catch (UaException e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

    }

}
