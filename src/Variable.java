public class Variable extends Expression{
    public final String operation = "";
    public String variable;

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Variable) {
            Variable var = (Variable) obj;
            return this.variable.equals(var.variable);
        }
        return false;
    }
}
