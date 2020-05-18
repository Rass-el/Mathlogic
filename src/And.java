public class And extends Expression {
    public final String operation = "&";
    public Expression leftExp;
    public Expression rightExp;

    public And(Expression left, Expression right) {
        this.leftExp = left;
        this.rightExp = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof And) {
            And and = (And) obj;
            return this.leftExp.equals(and.leftExp) && this.rightExp.equals(and.rightExp);
        }
        return false;
    }
}
