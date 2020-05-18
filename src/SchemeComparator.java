public class SchemeComparator {
    private Expression a = null;
    private Expression b = null;
    private Expression c = null;

    public void refresh() {
        a = null;
        b = null;
        c = null;
    }

    public boolean isScheme(Expression expression, Expression scheme) {
        if (expression == null || scheme == null) {
            return false;
        }
        if (scheme instanceof Variable) {
            Variable var = (Variable) scheme;
            switch (var.variable) {
                case "a":
                    if (a != null) {
                        return a.equals(expression);
                    } else {
                        a = expression;
                    }
                    break;
                case "b":
                    if (b != null) {
                        return b.equals(expression);
                    } else {
                        b = expression;
                    }
                    break;
                case "c":
                    if (c != null) {
                        return c.equals(expression);
                    } else {
                        c = expression;
                    }
                    break;
            }
            return true;
        } else if (expression instanceof Negation && scheme instanceof Negation) {
            Negation negExp = (Negation) expression;
            Negation negScheme = (Negation) scheme;
            return isScheme(negExp.expression, negScheme.expression);
        } else if (expression instanceof Implication && scheme instanceof Implication) {
            Implication impExp = (Implication) expression;
            Implication impScheme = (Implication) scheme;
            return isScheme(impExp.leftExp, impScheme.leftExp) && isScheme(impExp.rightExp, impScheme.rightExp);
        } else if (expression instanceof Or && scheme instanceof Or) {
            Or orExp = (Or) expression;
            Or orScheme = (Or) scheme;
            return isScheme(orExp.leftExp, orScheme.leftExp) && isScheme(orExp.rightExp, orScheme.rightExp);
        } else if (expression instanceof And && scheme instanceof And) {
            And andExp = (And) expression;
            And andScheme = (And) scheme;
            return isScheme(andExp.leftExp, andScheme.leftExp) && isScheme(andExp.rightExp, andScheme.rightExp);
        }
        return false;
    }
}
