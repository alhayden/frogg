package FroggUpdate.update_v0_1;

public class Instruction_v0_1 {
    private Reg_v0_1 dstReg;
    private OpCode_v0_1 opCode;
    private Reg_v0_1 srcReg1;
    private Reg_v0_1 srcReg2;
    private int intImmediate;
    private float floatImmediate;

    public Instruction_v0_1()
    {

    }

    public OpCode_v0_1 GetOpCode()
    {
        return this.opCode;
    }
}