package org.openflexo.ta.opcua.model;

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;

@ModelEntity
public interface OPCVariableNode extends OPCNode {

    // TODO : Do we need something similar to OPC Variant class to hold values of different types?

    @PropertyIdentifier(type = Number.class)
    public static final String OPC_VALUE = "value";

    @Setter(OPC_VALUE)
    public void setValue(Number aValue);

    @Getter(value = OPC_VALUE)
    public Number getValue();

    // TODO : we want to subscribe to a variable, how? Discuss ;)

}
