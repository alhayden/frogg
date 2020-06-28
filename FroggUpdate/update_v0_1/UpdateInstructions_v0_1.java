package FroggUpdate.update_v0_1.UpdateInstructions_v0_1;

public class UpdateInstructions_v0_1 implements FroggUpdate.UpdateInstructions{

    private Instruction_v0_1 instructions[];

    private boolean validated;

    public UpdateInstructions_v0_1(int length, byte[] instructions)
    {
        this.instructions = new Instruction_v0_1[length];

        //init from bytestream
        deSerialize();

        //validate
        this.validated = validate();
    }

    public void getLine(int line)
    {


    }

    private void deSerialize()
    {

    }

    public boolean validate()
    {
        return false;
    }
}