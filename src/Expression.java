public class Expression {
    public int id = 0;
    public boolean proved = false;
    public ProofType type = null;

    public int hypoNum;

    public int axiomNum;

    public int mpLeftNum;
    public int mpNum;
    public Expression mpLeft = null;
    public Expression mp = null;
}
