package org.openflexo.ta.opcua.model.nodes;

import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;

@ModelEntity
public interface OPCVariableNode extends OPCInstanceNode<UaVariableNode> {

	@PropertyIdentifier(type = OPCVariableType.class)
	public static final String TYPE_KEY = "type";

	@Getter(value = TYPE_KEY)
	public OPCVariableType getType();

	@Setter(value = TYPE_KEY)
	public void setType(OPCVariableType aType);

	@PropertyIdentifier(type = Number.class)
	public static final String VALUE_KEY = "value";

	@Setter(VALUE_KEY)
	public void setValue(Number aValue);

	@Getter(value = VALUE_KEY)
	public Number getValue();

	// TODO : we want to subscribe to a variable, how? Discuss ;)

}
