package app.src.main.java.packet;

public enum Opcode {
    
    LOGIN ((short)1);
    
    private short value;
    
    private Opcode(short value) {
        this.value = value;
    }
    
    public short value() {
        return value;
    }

    public static Opcode get(short value) throws Exception {
        switch (value) {
            case 1:
                return LOGIN;
            default:
                throw new IllegalArgumentException("Opcode is currently not supported.");
        }
    }
}

