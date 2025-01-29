package org.openflexo.ta.opcua.model.nodes;

public enum OPCVariableType {
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
	BooleanArray,
	ByteArray,
	SByteArray,
	Int16Array,
	Int32Array,
	Int64Array,
	UInt16Array,
	UInt32Array,
	UInt64Array,
	FloatArray,
	DoubleArray,
	StringArray,
	DateTimeArray,
	GuidArray,
	ByteStringArray,
	XmlElementArray,
	LocalizedTextArray,
	QualifiedNameArray,
	NodeIdArray;

	public boolean isArray() {
		switch (this) {
			case Boolean:
			case Byte:
			case SByte:
			case Integer:
			case Int16:
			case Int32:
			case Int64:
			case UInteger:
			case UInt16:
			case UInt32:
			case UInt64:
			case Float:
			case Double:
			case String:
			case DateTime:
			case Guid:
			case ByteString:
			case XmlElement:
			case LocalizedText:
			case QualifiedName:
			case NodeId:
			case Variant:
			case Duration:
			case UtcTime:
				return false;
			case BooleanArray:
			case ByteArray:
			case SByteArray:
			case Int16Array:
			case Int32Array:
			case Int64Array:
			case UInt16Array:
			case UInt32Array:
			case UInt64Array:
			case FloatArray:
			case DoubleArray:
			case StringArray:
			case DateTimeArray:
			case GuidArray:
			case ByteStringArray:
			case XmlElementArray:
			case LocalizedTextArray:
			case QualifiedNameArray:
			case NodeIdArray:
				return true;
			default:
				throw new RuntimeException("isArray: Unhandled data type " + this);
		}
	}

	public String getName() {
		return this.toString();
	}

}
