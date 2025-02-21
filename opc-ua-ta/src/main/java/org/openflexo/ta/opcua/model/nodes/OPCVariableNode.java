package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.openflexo.pamela.annotations.*;
import org.openflexo.ta.opcua.model.types.OPCDataType;

@ModelEntity
@ImplementationClass(value = OPCVariableNode.OPCVariableNodeImpl.class)
public interface OPCVariableNode extends OPCInstanceNode<UaVariableNode> {

	public OPCDataType getValueType();

	/**
	 * Returns an int that tells the dimensionality of the data.
	 *
	 * <ul>
	 *   <li>-2 means a complex structure</li>
	 *   <li>-1 means a scalar</li>
	 *   <li>0 means any dimension (scalar or array)</li>
	 *   <li>1 means a 1D array</li>
	 *   <li>2 means a 2D array</li>
	 *   <li>...</li>
	 * </ul>
	 */
	public int getValueDimensionality();

	/**
	 * Returns a String computed from getValueType() and getValueDimensionality(). Some examples:
	 *
	 * <ul>
	 *     <li>'Uint32' for a scalar</li>
	 *     <li>'Uint32[]' for a 1D array</li>
	 *     <li>'Uint32[][] for a 2D array</li>
	 *     <li>'Uint32?' if dimensionality is unknown</li>
	 *     <li>'Uint32[complex]' for anything complex</li>
	 * </ul>
	 *
	 * Note that unsupported types are displayed as (for example) 'Id862' with 862 being the unhandled identifier.
	 */
	public String getValueTypeString();

	// TODO : we want to subscribe to a variable, how? Discuss ;)

	public static abstract class OPCVariableNodeImpl extends OPCInstanceNodeImpl<UaVariableNode> implements OPCVariableNode {

		private UaVariableNode variableNode;

		@Override
		public UaVariableNode getNode() {
			if (variableNode != null) return variableNode;
			try {
				variableNode = getResourceData().getClient().getAddressSpace().getVariableNode(getNodeId());
				return variableNode;
			} catch (UaException e) {
				System.err.println(e.getMessage());
			}
			return null;
		}

		@Override
		public OPCDataType getValueType() {
			return OPCDataType.fromNodeId(getNode().getDataType());
		}

		@Override
		public int getValueDimensionality() {
			return getNode().getValueRank();
		}

		@Override
		public String getValueTypeString() {
			final String name = getValueType() == OPCDataType.Unsupported ? "Id" + getNode().getDataType().getIdentifier() : getValueType().getName();
			final int valueDimensionality = getValueDimensionality();
			switch (valueDimensionality) {
				case -2: return name + "[complex]";
				case -1: return name;
				case 0: return name + "?";
				default: {
					StringBuilder builder = new StringBuilder(name);
					for (int i = 0; i < valueDimensionality; i++) builder.append("[]");
					return builder.toString();
				}
			}
		}

	}
}
