package packet;

public enum Opcode {
    
    LOGIN ((short)1),
    
    USER_PASS_VALIDATION ((short)30001);
    
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
            case 30001:
                return USER_PASS_VALIDATION;
            default:
                throw new IllegalArgumentException(String.format("Opcode %d is currently not supported.", value));
        }
    }
}

