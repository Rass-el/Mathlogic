public class Parser {
    private int currentIndex;

    private boolean valid(char c) {
        return ('A' <= c && c <= 'Z') ||
                ('a' <= c && c <= 'z') ||
                ('0' <= c && c <= '9') ||
                (c == '\'') || (c == '’');
    }

    public Expression parse(String s) {
        currentIndex = 0;                        // текущий индекс символа с строке s
        return parseImplication(s);
    }

    private Expression parseImplication(String s) {
        Expression expression = parseOr(s);
        while (currentIndex < s.length()) {
            if (s.charAt(currentIndex) == '-') {
                currentIndex += 2;
                expression = new Implication(expression, parseImplication(s));
            } else {
                break;
            }
        }
        return expression;
    }

    private Expression parseOr(String s) {
        Expression expression = parseAnd(s);
        while (currentIndex < s.length()) {
            if (s.charAt(currentIndex) == '|') {
                currentIndex++;
                expression = new Or(expression, parseAnd(s));
            } else {
                break;
            }
        }
        return expression;
    }

    private Expression parseAnd(String s) {
        Expression expression = parseVariable(s);
        while (currentIndex < s.length()) {
            if (s.charAt(currentIndex) == '&') {
                currentIndex++;
                expression = new And(expression, parseVariable(s));
            } else {
                break;
            }
        }
        return expression;
    }

    private Expression parseVariable(String s) {
        Expression expression;
        switch (s.charAt(currentIndex)) {
            case '(':
                currentIndex++;
                expression = parseImplication(s);
                currentIndex++;
                break;
            case '!':
                currentIndex++;
                expression = new Negation(parseVariable(s));
                break;
            default:
                StringBuilder var = new StringBuilder();
                while (currentIndex < s.length() && valid(s.charAt(currentIndex))) {
                    var.append(s.charAt(currentIndex++));
                }
                expression = new Variable(var.toString());
        }
        return expression;
    }
}
