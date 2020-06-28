package FroggUpdate.update_v0_1;

import java.util.*;

import FroggObj.FroggObj;
import FroggUpdate.UpdaterInterface;
import FroggUpdate.update_v0_1.UpdateInstructions_v0_1;

public class Updater_v0_1 implements UpdaterInterface {

    private Map<Reg_v0_1, Integer> intRegs = new EnumMap<>(Reg_v0_1.class);
    private Map<Reg_v0_1, Float> floatRegs = new EnumMap<>(Reg_v0_1.class);
    private int programCounter;

    public Updater_v0_1() {
        // initialize/reset values in run
    }

    public void run(FroggObj object, byte[] triggers, long time) {
        UpdateInstructions_v0_1 code = object.getInstructions();
        Instruction_v0_1 currentInstruction;

        // init
        init();

        // start execution
        currentInstruction = code.getLine(0);
        while (currentInstruction.GetOpCode() != OpCode_v0_1.END) {
            exec(currentInstruction);
            currentInstruction = code.getLine(this.programCounter);
        }

        // extract data when complete: use FroggObj setters

    }

    private void init() {
        this.programCounter = 0;
    }

    private void exec(Instruction_v0_1 instruction) {
        // this is cumbersome. maybe reduce number of instructions in future versions
        // (many of these could be made aliases of each other IF we make an assembler)
        switch (instruction.GetOpCode()) {
            case IADD:
            case IADDI:
            case FADD:
            case FADDI:
                ADD(instruction);
                break;
            case ISUB:
            case ISUBI:
            case FSUB:
            case FSUBI:
                SUB(instruction);
                break;
            case IMUL:
            case IMULI:
            case FMUL:
            case FMULI:
                MUL(instruction);
                break;
            case IDIV:
            case IDIVI:
            case FDIV:
            case FDIVI:
                DIV(instruction);
                break;
            case ICPY:
            case ICPYI:
            case FCPY:
            case FCPYI:
                CPY(instruction);
                break;
            case IEQ:
            case IEQI:
            case FEQ:
            case FEQI:
                EQ(instruction);
                break;
            case INE:
            case INEI:
            case FNE:
            case FNEI:
                NE(instruction);
                break;
            case ILT:
            case ILTI:
            case FLT:
            case FLTI:
                LT(instruction);
                break;
            case IGT:
            case IGTI:
            case FGT:
            case FGTI:
                GT(instruction);
                break;
            case MOD:
                MOD(instruction);
                break;
            case AND:
            case ANDI:
                AND(instruction);
                break;
            case OR:
            case ORI:
                OR(instruction);
                break;
            case NOT:
                NOT(instruction);
                break;
            case XOR:
            case XORI:
                XOR(instruction);
                break;
            case SIN:
                SIN(instruction);
                break;
            case COS:
                COS(instruction);
                break;
            case TAN:
                TAN(instruction);
                break;
            case BRT:
            case BRF:
            case BRA:
                BR(instruction);
                break;
            case END:
                break;
        }
        // increment program counter after all operations
        this.programCounter++;
        // to make the branch instruction jump as expected, it subtracts 1 internally
    }

    //
    private void ADD(Instruction_v0_1 instr) {
        int intAcc;
        float floatAcc;
        switch (instr.GetOpCode()) {
            case IADD:
                intAcc = this.intRegs.get(instr.getSrc1()) + this.intRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case IADDI:
                intAcc = this.intRegs.get(instr.getSrc1()) + instr.getIntImm();
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case FADD:
                floatAcc = this.floatRegs.get(instr.getSrc1()) + this.floatRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            case FADDI:
                floatAcc = this.floatRegs.get(instr.getSrc1()) + instr.getFloatImm();
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                break;
        }
    }

    private void SUB(Instruction_v0_1 instr) {
        int intAcc;
        float floatAcc;
        switch (instr.GetOpCode()) {
            case ISUB:
                intAcc = this.intRegs.get(instr.getSrc1()) - this.intRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case ISUBI:
                intAcc = this.intRegs.get(instr.getSrc1()) - instr.getIntImm();
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case FSUB:
                floatAcc = this.floatRegs.get(instr.getSrc1()) - this.floatRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            case FSUBI:
                floatAcc = this.floatRegs.get(instr.getSrc1()) - instr.getFloatImm();
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                break;
        }
    }

    private void MUL(Instruction_v0_1 instr) {
        int intAcc;
        float floatAcc;
        switch (instr.GetOpCode()) {
            case IMUL:
                intAcc = this.intRegs.get(instr.getSrc1()) * this.intRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case IMULI:
                intAcc = this.intRegs.get(instr.getSrc1()) * instr.getIntImm();
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case FMUL:
                floatAcc = this.floatRegs.get(instr.getSrc1()) * this.floatRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            case FMULI:
                floatAcc = this.floatRegs.get(instr.getSrc1()) * instr.getFloatImm();
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                break;
        }
    }

    private void DIV(Instruction_v0_1 instr) {
        int intAcc;
        float floatAcc;
        switch (instr.GetOpCode()) {
            case IDIV:
                intAcc = this.intRegs.get(instr.getSrc1()) / this.intRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case IDIVI:
                intAcc = this.intRegs.get(instr.getSrc1()) / instr.getIntImm();
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case FDIV:
                floatAcc = this.floatRegs.get(instr.getSrc1()) / this.floatRegs.get(instr.getSrc2());
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            case FDIVI:
                floatAcc = this.floatRegs.get(instr.getSrc1()) / instr.getFloatImm();
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                break;
        }
    }

    private void CPY(Instruction_v0_1 instr) {
        int intAcc;
        float floatAcc;
        switch (instr.GetOpCode()) {
            case ICPY:
                intAcc = this.intRegs.get(instr.getSrc1());
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case ICPYI:
                intAcc = instr.getIntImm();
                this.intRegs.keySet(instr.getDst(), intAcc);
                this.floatRegs.keySet(instr.getDst(), intAcc);
                break;
            case FCPY:
                floatAcc = this.floatRegs.get(instr.getSrc1());
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            case FCPYI:
                floatAcc = instr.getFloatImm();
                this.intRegs.keySet(instr.getDst(), floatAcc);
                this.floatRegs.keySet(instr.getDst(), floatAcc);
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                break;
        }
    }

    private void EQ(Instruction_v0_1 instr) {
        int intA, intB;
        float floatA, floatB;
        boolean acc;
        switch (instr.GetOpCode()) {
            case IEQ:
                intA = this.intRegs.get(instr.getSrc1());
                intB = this.intRegs.get(instr.getSrc2());
                acc = intA == intB;
                break;
            case IEQI:
                intA = this.intRegs.get(instr.getSrc1());
                intB = instr.getIntImm();
                acc = intA == intB;
                break;
            case FEQ:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = this.floatRegs.get(instr.getSrc2());
                acc = floatA == floatB;
                break;
            case FEQI:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = instr.getFloatImm();
                acc = floatA == floatB;
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                return;
        }
        // set the logical values
        if (acc) {
            this.intRegs.keySet(instr.getDst(), 1);
            this.floatRegs.keySet(instr.getDst(), 1);
        } else {
            this.intRegs.keySet(instr.getDst(), 0);
            this.floatRegs.keySet(instr.getDst(), 0);
        }
    }

    private void NE(Instruction_v0_1 instr) {
        int intA, intB;
        float floatA, floatB;
        boolean acc;
        switch (instr.GetOpCode()) {
            case INE:
                intA = this.intRegs.get(instr.getSrc1());
                intB = this.intRegs.get(instr.getSrc2());
                acc = intA != intB;
                break;
            case INEI:
                intA = this.intRegs.get(instr.getSrc1());
                intB = instr.getIntImm();
                acc = intA != intB;
                break;
            case FNE:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = this.floatRegs.get(instr.getSrc2());
                acc = floatA != floatB;
                break;
            case FNEI:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = instr.getFloatImm();
                acc = floatA != floatB;
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                return;
        }
        // set the logical values
        if (acc) {
            this.intRegs.keySet(instr.getDst(), 1);
            this.floatRegs.keySet(instr.getDst(), 1);
        } else {
            this.intRegs.keySet(instr.getDst(), 0);
            this.floatRegs.keySet(instr.getDst(), 0);
        }
    }

    private void LT(Instruction_v0_1 instr) {
        int intA, intB;
        float floatA, floatB;
        boolean acc;
        switch (instr.GetOpCode()) {
            case ILT:
                intA = this.intRegs.get(instr.getSrc1());
                intB = this.intRegs.get(instr.getSrc2());
                acc = intA < intB;
                break;
            case ILTI:
                intA = this.intRegs.get(instr.getSrc1());
                intB = instr.getIntImm();
                acc = intA < intB;
                break;
            case FLT:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = this.floatRegs.get(instr.getSrc2());
                acc = floatA < floatB;
                break;
            case FLTI:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = instr.getFloatImm();
                acc = floatA < floatB;
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                return;
        }
        // set the logical values
        if (acc) {
            this.intRegs.keySet(instr.getDst(), 1);
            this.floatRegs.keySet(instr.getDst(), 1);
        } else {
            this.intRegs.keySet(instr.getDst(), 0);
            this.floatRegs.keySet(instr.getDst(), 0);
        }
    }

    private void GT(Instruction_v0_1 instr) {
        int intA, intB;
        float floatA, floatB;
        boolean acc;
        switch (instr.GetOpCode()) {
            case IGT:
                intA = this.intRegs.get(instr.getSrc1());
                intB = this.intRegs.get(instr.getSrc2());
                acc = intA > intB;
                break;
            case IGTI:
                intA = this.intRegs.get(instr.getSrc1());
                intB = instr.getIntImm();
                acc = intA > intB;
                break;
            case FGT:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = this.floatRegs.get(instr.getSrc2());
                acc = floatA > floatB;
                break;
            case FGTI:
                floatA = this.floatRegs.get(instr.getSrc1());
                floatB = instr.getFloatImm();
                acc = floatA > floatB;
                break;
            default:
                // should never be reached: only ADD insructions should be passed
                return;
        }
        // set the logical values
        if (acc) {
            this.intRegs.keySet(instr.getDst(), 1);
            this.floatRegs.keySet(instr.getDst(), 1);
        } else {
            this.intRegs.keySet(instr.getDst(), 0);
            this.floatRegs.keySet(instr.getDst(), 0);
        }
    }

    private void MOD(Instruction_v0_1 instr) {
    }

    private void AND(Instruction_v0_1 instr) {
    }

    private void OR(Instruction_v0_1 instr) {
    }

    private void NOT(Instruction_v0_1 instr) {
    }

    private void XOR(Instruction_v0_1 instr) {
    }

    private void SIN(Instruction_v0_1 instr) {
    }

    private void COS(Instruction_v0_1 instr) {
    }

    private void TAN(Instruction_v0_1 instr) {
    }

    private void BR(Instruction_v0_1 instr) {
    }

}