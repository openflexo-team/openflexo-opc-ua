package org.openflexo.ta.opcua.model.types;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;

public enum OPCDataType {
    Boolean,
    Byte,
    SByte,
    Integer,
    Int16,
    Int32,
    Int64,
    UInteger,
    UInt16,
    UInt32,
    UInt64,
    Float,
    Double,
    String,
    DateTime,
    Guid,
    ByteString,
    XmlElement,
    LocalizedText,
    QualifiedName,
    NodeId,
    Variant,
    Duration,
    UtcTime,
    Unsupported;

    static private final Map<NodeId, OPCDataType> REVERSE_MAP = new HashMap<>();

    static {
        for (OPCDataType dataType : OPCDataType.values()) {
            if (dataType == Unsupported) continue;
            REVERSE_MAP.put(dataType.getNodeId(), dataType);
        }
    }

    static public OPCDataType fromNodeId(NodeId nodeId) {
        OPCDataType returned = REVERSE_MAP.get(nodeId);
        return returned != null ? returned : OPCDataType.Unsupported;
    }
    public String getName() {
        return this.toString();
    }

    public NodeId getNodeId() {
        switch (this) {
            case Boolean: return Identifiers.Boolean;
            case Byte:  return Identifiers.Byte;
            case SByte: return Identifiers.SByte;
            case Integer: return Identifiers.Integer;
            case Int16: return Identifiers.Int16;
            case Int32: return Identifiers.Int32;
            case Int64: return Identifiers.Int64;
            case UInteger: return Identifiers.UInteger;
            case UInt16: return Identifiers.UInt16;
            case UInt32: return Identifiers.UInt32;
            case UInt64: return Identifiers.UInt64;
            case Float: return Identifiers.Float;
            case Double: return Identifiers.Double;
            case String: return Identifiers.String;
            case DateTime: return Identifiers.DateTime;
            case Guid: return Identifiers.Guid;
            case ByteString: return Identifiers.ByteString;
            case XmlElement: return Identifiers.XmlElement;
            case LocalizedText: return Identifiers.LocalizedText;
            case QualifiedName: return Identifiers.QualifiedName;
            case NodeId: return Identifiers.NodeId;
            case Variant: return Identifiers.BaseDataType;
            case Duration: return Identifiers.Duration;
            case UtcTime: return Identifiers.UtcTime;
            case Unsupported: return Identifiers.Structure;
            default: throw new RuntimeException("NodeId: Unhandled data type " + this);
        }
    }

    public Object getExampleValue() {
        switch (this) {
            case Boolean: return false;
            case Byte: return ubyte(0x00);
            case SByte: return (byte) 0x00;
            case Integer: return 32;
            case Int16: return (short) 16;
            case Int32: return 32;
            case Int64: return 64L;
            case UInteger: return uint(32);
            case UInt16: return ushort(16);
            case UInt32: return uint(32);
            case UInt64: return ulong(64L);
            case Float: return 3.14f;
            case Double: return 3.14d;
            case String: return "string value";
            case DateTime: return org.eclipse.milo.opcua.stack.core.types.builtin.DateTime.now();
            case Guid: return UUID.randomUUID();
            case ByteString: return  new ByteString(new byte[]{0x01, 0x02, 0x03, 0x04});
            case XmlElement: return new XmlElement("<a>hello</a>");
            case LocalizedText: return org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText.english("localized text");
            case QualifiedName: return new QualifiedName(1234, "defg");
            case NodeId: return new NodeId(1234, "abcd");
            case Variant: return 32;
            case Duration: return 1.0;
            case UtcTime: return org.eclipse.milo.opcua.stack.core.types.builtin.DateTime.now();
            case Unsupported: return false;
            default: throw new RuntimeException("ExampleValue: Unhandled data type " + this);
        }
    }

    public Variant getExampleVariant() {
        return new Variant(getExampleValue());
    }

}
