package org.openflexo.ta.opcua.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.openflexo.ta.opcua.model.nodes.OPCVariableType;

public class TypeConversion {

	private static final Map<NodeId, OPCVariableType> variableTypeMap = new HashMap<>();

	static {
		for (OPCVariableType variableType : OPCVariableType.values()) {
			variableTypeMap.put(getNodeId(variableType), variableType);
		}
	}

	static public NodeId getNodeId(OPCVariableType aVariableType) {
		switch (aVariableType) {
			case Boolean:
			case BooleanArray:
				return Identifiers.Boolean;
			case Byte:
			case ByteArray:
				return Identifiers.Byte;
			case SByte:
			case SByteArray:
				return Identifiers.SByte;
			case Integer:
				return Identifiers.Integer;
			case Int16:
			case Int16Array:
				return Identifiers.Int16;
			case Int32:
			case Int32Array:
				return Identifiers.Int32;
			case Int64:
			case Int64Array:
				return Identifiers.Int64;
			case UInteger:
				return Identifiers.UInteger;
			case UInt16:
			case UInt16Array:
				return Identifiers.UInt16;
			case UInt32:
			case UInt32Array:
				return Identifiers.UInt32;
			case UInt64:
			case UInt64Array:
				return Identifiers.UInt64;
			case Float:
			case FloatArray:
				return Identifiers.Float;
			case Double:
			case DoubleArray:
				return Identifiers.Double;
			case String:
			case StringArray:
				return Identifiers.String;
			case DateTime:
			case DateTimeArray:
				return Identifiers.DateTime;
			case Guid:
			case GuidArray:
				return Identifiers.Guid;
			case ByteString:
			case ByteStringArray:
				return Identifiers.ByteString;
			case XmlElement:
			case XmlElementArray:
				return Identifiers.XmlElement;
			case LocalizedText:
			case LocalizedTextArray:
				return Identifiers.LocalizedText;
			case QualifiedName:
			case QualifiedNameArray:
				return Identifiers.QualifiedName;
			case NodeId:
			case NodeIdArray:
				return Identifiers.NodeId;
			case Variant:
				return Identifiers.BaseDataType;
			case Duration:
				return Identifiers.Duration;
			case UtcTime:
				return Identifiers.UtcTime;
			default:
				return null;
		}
	}

	static public OPCVariableType getVariableType(NodeId aNodeId) {
		OPCVariableType variableType = variableTypeMap.get(aNodeId);
		return variableType;
	}

}
