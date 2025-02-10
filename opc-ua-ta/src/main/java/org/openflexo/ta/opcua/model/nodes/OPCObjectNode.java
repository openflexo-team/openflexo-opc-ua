package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;

@ModelEntity
@ImplementationClass(value = OPCObjectNode.OPCObjectNodeImpl.class)
public interface OPCObjectNode extends OPCInstanceNode<UaObjectNode> {

    public static abstract class OPCObjectNodeImpl extends OPCInstanceNodeImpl<UaObjectNode> implements OPCObjectNode {

        private UaObjectNode objectNode = null;

        @Override
        public UaObjectNode getNode() {
            if (objectNode != null) return objectNode;
            try {
                objectNode = getResourceData().getClient().getAddressSpace().getObjectNode(getNodeId());
                return objectNode;
            } catch (UaException e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

    }

}
