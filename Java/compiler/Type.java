package compiler;

public enum Type {

    INT("I");

    private final String descriptor;

    Type(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return this.descriptor;
    }
}
