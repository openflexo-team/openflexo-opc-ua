package org.openflexo.ta.opcua.model.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Node identifiers can be of different types that have to be known when serializing.
 *
 * This helps with just that.
 *
 * @author Luka
 */
public enum OPCIdentifierType {
    INTEGER,
    UUID,
    STRING,
    SHORT,
    BYTE_STRING;

    static public OPCIdentifierType fromInstance(Object identifier) {
        if (identifier instanceof Integer || identifier instanceof UInteger) return INTEGER;
        if (identifier instanceof java.util.UUID) return UUID;
        if (identifier instanceof Short) return SHORT;
        if (identifier instanceof String) return STRING;
        if (identifier instanceof ByteString) return BYTE_STRING;
        throw new IllegalArgumentException("Unrecognized identifier type: " + identifier + " of class " + identifier.getClass());
    }

    public Object parseString(String string) {
        switch (this) {
            case INTEGER: return Integer.parseInt(string);
            case UUID: return java.util.UUID.fromString(string);
            case SHORT: return Short.parseShort(string);
            case BYTE_STRING: return ByteString.of(string.getBytes());
            default: return string;
        }
    }

    public NodeId makeNodeId(int namespaceIndex, String string) {
        switch (this) {
            case INTEGER: return new NodeId(namespaceIndex, Integer.parseInt(string));
            case UUID: return new NodeId(namespaceIndex, java.util.UUID.fromString(string));
            case SHORT: return new NodeId(namespaceIndex, Short.parseShort(string));
            case BYTE_STRING: return new NodeId(namespaceIndex, ByteString.of(string.getBytes()));
            default: return new NodeId(namespaceIndex, string);
        }
    }


}
