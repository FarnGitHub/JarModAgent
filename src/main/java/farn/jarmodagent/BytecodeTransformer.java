package farn.jarmodagent;

public interface BytecodeTransformer {

    byte[] transform(String name, byte[] bytecode);
}
