package compiler;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassFile {

    private final String name;

    private final ConstantPool constantPool;

    private final Map<String, Field> fields = new HashMap<>();

    private final Map<String, Method> methods = new HashMap<>();

    public ClassFile(String className) {
        this.name = className;
        this.constantPool = new ConstantPool(className);
    }

    public void addField(Type type, String name) {

        if (fields.containsKey(name)) {
            throw new RuntimeException();
        }
        int nameIndex = constantPool.putUtf8(name);
        int descriptorIndex = constantPool.putUtf8(type.getDescriptor());
        int referenceIndex = constantPool.putFieldRef(nameIndex, descriptorIndex);
        Field f = new Field(type, nameIndex, descriptorIndex, referenceIndex);
        this.fields.put(name, f);
    }

    public void addMethod(Type returnType, String name, List<LocalVariable> arguments, Block block) {

        String argumentDescriptor = "(" +
                arguments.stream().map(a -> a.getType().getDescriptor()).collect(Collectors.joining()) +
                ")";

        if (methods.containsKey(argumentDescriptor)) {
            throw new RuntimeException();
        }
        String descriptor = argumentDescriptor + returnType.getDescriptor();
        int descriptorIndex = constantPool.putUtf8(descriptor);
        int nameIndex = constantPool.putUtf8(name);
        short referenceIndex = constantPool.putMethodRef(nameIndex, descriptorIndex);
        int attributeNameIndex = constantPool.putUtf8("Code");
        Method m = new Method(returnType, referenceIndex, nameIndex, descriptorIndex, attributeNameIndex, block);
        this.methods.put(name + argumentDescriptor, m);
    }

    public int addInteger(int value) {
        return this.constantPool.putInteger(value);
    }

    public void evaluate() throws Exception {
        try (DataOutputStream classfile = new DataOutputStream(new FileOutputStream(this.name + ".class"))) {
            classfile.writeInt(0xCAFEBABE);
            classfile.writeShort(0x00);
            classfile.writeShort(0x34);
            constantPool.emitCode(classfile);
            classfile.writeShort(0x20);
            classfile.writeShort(constantPool.getClassIndex());
            classfile.writeShort(constantPool.getSuperClassIndex());
            classfile.writeShort(0);
            emitCodeOfField(classfile);
            emitCodeOfMethod(classfile);
            classfile.writeShort(0);
        }
    }

    private void emitCodeOfField(DataOutputStream outputStream) throws IOException {
        int size = this.fields.size();
        if (size > 65535) {
            throw new RuntimeException("Too Big Classfile");
        }

        Collection<Field> values = this.fields.values();
        outputStream.writeShort(size);
        for (Field f : values) {
            f.emitCode(outputStream);
        }
    }

    private void emitCodeOfMethod(DataOutputStream outputStream) throws IOException {
        int size = this.methods.size();
        if (size > 65535) {
            throw new RuntimeException("Too Big Classfile");
        }

        Collection<Method> values = this.methods.values();
        outputStream.writeShort(size);
        for (Method m : values) {
            m.emitCode(outputStream);
        }
    }

    public boolean hasField(String name) {
        return this.fields.containsKey(name);
    }

    public GlobalVariable createFieldReference(String name) {
        if (!this.fields.containsKey(name)) {
            throw new RuntimeException();
        }
        return this.fields.get(name).createReference();
    }

    public Method getMethod(String identifier) {
        if (!this.methods.containsKey(identifier)) {
            throw new RuntimeException();
        }
        return this.methods.get(identifier);
    }
}
