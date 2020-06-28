package FroggUpdate.update_v0_1;

public enum OpCode_v0_1 {
    //SHARED FLOAT, INT ARITHMETIC FUNCTIONS
    IADD,
    IADDI,
    FADD,
    FADDI,
    ISUB,
    ISUBI,
    FSUB,
    FSUBI,
    IMUL,
    IMULI,
    FMUL,
    FMULI,
    IDIV,
    IDIVI,
    FDIV,
    FDIVI,
    ICPY,
    ICPYI,
    FCPY,
    FCPYI,
    //COMPARE FUNCTIONS
    IEQ,
    IEQI,
    FEQ,
    FEQI,
    INE,
    INEI,
    FNE,
    FNEI,
    ILT,
    ILTI,
    FLT,
    FLTI,
    IGT,
    IGTI,
    FGT,
    FGTI,
    //INT ONLY FUNCTIONS
    MOD,
    AND,
    ANDI,
    OR,
    ORI,
    NOT,
    XOR,
    XORI,
    //FLOAT ONLY FUNCTIONS
    SIN,
    COS,
    TAN,
    //BRANCH FUNCTIONS
    BRT,
    BRF,
    BRA,
    //END FUNCTION
    END,
}