package org.openflexo.ta.opcua.model.nodes;

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;

@ModelEntity
public interface OPCVariableNode extends OPCInstanceNode {

    @PropertyIdentifier(type = OPCVariableType.class)
    public static final String TYPE_KEY = "type";

    @Getter(value = TYPE_KEY)
    public OPCVariableType getType();

    @Setter(value = TYPE_KEY)
    public void setType(OPCVariableType aType);

    @PropertyIdentifier(type = Number.class)
    public static final String OPC_VALUE = "value";

    @Setter(OPC_VALUE)
    public void setValue(Number aValue);

    @Getter(value = OPC_VALUE)
    public Number getValue();

    // TODO : we want to subscribe to a variable, how? Discuss ;)

}
