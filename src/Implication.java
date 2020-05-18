public class Implication extends Expression {
    public final String operation = "->";
    public Expression leftExp;
    public Expression rightExp;

    public Implication(Expression left, Expression right) {
        this.leftExp = left;
        this.rightExp = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Implication) {
            Implication imp = (Implication) obj;
            return this.leftExp.equals(imp.leftExp) && this.rightExp.equals(imp.rightExp);
        }
        return false;
    }
}
