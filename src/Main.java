import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static List<Expression> axioms = new ArrayList<>();
    public static List<Expression> hypotheses = new ArrayList<>();
    public static List<Implication> implications = new ArrayList<>();
    public static List<Expression> proved = new ArrayList<>();
    public static List<Expression> answer = new ArrayList<>();

    public static int ID = 1;

    public static void buildAnswerTree(Expression expression) {
        if (!expression.proved) {
            expression.proved = true;
            answer.add(expression);
            if (expression.type == ProofType.MODUS_PONENS) {
                buildAnswerTree(expression.mp);
                buildAnswerTree(expression.mpLeft);
            }
        }
    }

    public static void printAnswer(Expression statement) {
        StringBuilder sb = new StringBuilder();
        Expression e = proved.get(proved.size() - 1);
        buildAnswerTree(e);

        for (int i = 0; i < hypotheses.size(); i++) {
            sb.append(resolve(hypotheses.get(i)));
            if (i != hypotheses.size() - 1) {
                sb.append(", ");
            } else {
                sb.append(" ");
            }
        }
        sb.append("|- " + resolve(statement) + '\n');

        Collections.sort(answer, new Comparator<Expression>() {
            @Override public int compare(Expression e1, Expression e2) {
                return e1.id - e2.id;
            }
        });

        for (int i = 0; i < answer.size(); i++) {
            answer.get(i).id = i + 1;
        }

        for (int i = 0; i < answer.size(); i++) {
            Expression ansExp = answer.get(i);
            sb.append("[" + ansExp.id + ". ");

            if (ansExp.type == ProofType.AXIOM) {
                sb.append("Ax. sch. " + ansExp.axiomNum + "] ");
            } else if (ansExp.type == ProofType.HYPO) {
                sb.append("Hypothesis " + ansExp.hypoNum + "] ");
            } else if (ansExp.type == ProofType.MODUS_PONENS) {
                sb.append("M.P. " + ansExp.mp.id + ", " + ansExp.mpLeft.id + "] ");
            } else {
                System.out.println("Штирлиц никогда не был так близок к провалу");
            }
            sb.append(resolve(ansExp) + '\n');
        }
        System.out.print(sb.toString());
    }

    public static String resolve(Expression exp) {
        if (exp instanceof Variable) {
            return ((Variable) exp).variable;
        } else if (exp instanceof Negation) {
            return "!" + resolve(((Negation) exp).expression);
        } else if (exp instanceof Implication) {
            Implication implication = (Implication) exp;
            return "(" + resolve(implication.leftExp) + " -> " + resolve(implication.rightExp) + ")";
        } else if (exp instanceof Or) {
            Or or = (Or) exp;
            return "(" + resolve(or.leftExp) + " | " + resolve(or.rightExp) + ")";
        } else if (exp instanceof And) {
            And and = (And) exp;
            return "(" + resolve(and.leftExp) + " & " + resolve(and.rightExp) + ")";
        } else {
            System.err.println("Этого никогда не должно было произойти");
            return "";
        }
    }

    public static boolean isModusPonens(Expression exp) {
        for (Implication imp : implications) {
            if (imp.rightExp.equals(exp) && proved.contains(imp.leftExp)) {
                for (int i = 0; i < proved.size(); i++) {
                    if (proved.get(i).equals(imp.leftExp)) {
                        exp.mpLeft = proved.get(i);
                    }
                }
                exp.mp = imp;
                exp.id = ID++;
                exp.type = ProofType.MODUS_PONENS;
                proved.add(exp);
                if (exp instanceof Implication) {
                    implications.add((Implication) exp);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isHypothesis(Expression exp) {
        for (int i = 0; i < hypotheses.size(); i++) {
            if (hypotheses.get(i).equals(exp)) {
                exp.id = ID++;
                exp.type = ProofType.HYPO; //todo тонкое место с гипотезой
                exp.hypoNum = i + 1;
                proved.add(exp);
                if (exp instanceof Implication) {
                    implications.add((Implication) exp);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isAxiom(Expression exp) {
        SchemeComparator sc = new SchemeComparator();
        for (int i = 0; i < axioms.size(); i++) {
            sc.refresh();
            if (sc.isScheme(exp, axioms.get(i))) {
                exp.id = ID++;
                exp.type = ProofType.AXIOM;
                exp.axiomNum = i + 1;
                proved.add(exp);
                if (exp instanceof Implication) {
                    implications.add((Implication) exp);
                }
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Parser parser = new Parser();

        Expression statement = null;

        axioms.add(0, parser.parse("(a)->((b)->(a))"));
        axioms.add(1, parser.parse("((a)->(b))->((a)->(b)->(c))->((a)->(c))"));
        axioms.add(2, parser.parse("(a)->(b)->(a)&(b)"));
        axioms.add(3, parser.parse("(a)&(b)->(a)"));
        axioms.add(4, parser.parse("(a)&(b)->(b)"));
        axioms.add(5, parser.parse("(a)->(a)|(b)"));
        axioms.add(6, parser.parse("(b)->(a)|(b)"));
        axioms.add(7, parser.parse("((a)->(c))->((b)->(c))->((a)|(b)->(c))"));
        axioms.add(8, parser.parse("((a)->(b))->((a)->!(b))->!(a)"));
        axioms.add(9, parser.parse("!!(a)->(a)"));

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line = in.readLine().replaceAll("\\s+","");
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '|' && line.charAt(i+1) == '-') {
                    String h = line.substring(0, i);
                    if (h.length() > 0) {
                        for (String s : h.split(",")) {
                            hypotheses.add(parser.parse(s));
                        }
                    }
                    statement = parser.parse(line.substring(i+2));   // доказываемое утверждение
                    break;
                }
            }

            line = in.readLine();
            while (line != null) {
                line = line.replaceAll("\\s+","");
                Expression exp = parser.parse(line);
                if (!proved.contains(exp)) {
                    if (isModusPonens(exp)) {

                    } else if (isHypothesis(exp)) {

                    } else if (isAxiom(exp)) {

                    } else {
                        System.out.println("Proof is incorrect");
                        return;
                    }
                }
                line = in.readLine();
            }

            if (!proved.get(proved.size() - 1).equals(statement)) {
                System.out.println("Proof is incorrect");
                return;
            }
            printAnswer(statement);
        } catch (IOException e) {
            System.err.println("Хьюстон, у нас проблемы");
            e.printStackTrace();
        }
    }

}
