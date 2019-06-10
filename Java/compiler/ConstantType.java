package compiler;

public enum ConstantType {

    CLASS((byte) 7),
    FIELDREF((byte) 9),
    METHODREF((byte) 10),
    INTEGER((byte) 3),
    NAMEANDTYPE((byte) 12),
    UTF8((byte) 1);

    private byte value;

    ConstantType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
