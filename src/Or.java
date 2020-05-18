public class Or extends Expression {
    public final String operation = "|";
    public Expression leftExp;
    public Expression rightExp;

    public Or(Expression left, Expression right) {
        this.leftExp = left;
        this.rightExp = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Or) {
            Or or = (Or) obj;
            return this.leftExp.equals(or.leftExp) && this.rightExp.equals(or.rightExp);
        }
        return false;
    }
}
