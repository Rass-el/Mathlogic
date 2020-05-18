public class Negation extends Expression {
    public final String operation = "!";
    public Expression expression;

    public Negation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Negation) {
            Negation neg = (Negation) obj;
            return this.expression.equals(neg.expression);
        }
        return false;
    }
}
