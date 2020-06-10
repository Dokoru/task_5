package TreeImplementation;

import java.util.function.Function;

public class Tree<T> {

    protected class SimpleTreeNode {
        public T value;
        public SimpleTreeNode left;
        public SimpleTreeNode right;
        private int gColor = -1;

        public SimpleTreeNode(T value, SimpleTreeNode left, SimpleTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public SimpleTreeNode(T value) {
            this(value, null, null);
        }

        public T getValue() {
            return value;
        }

        public SimpleTreeNode getLeft() {
            return left;
        }

        public SimpleTreeNode getRight() {
            return right;
        }

        public void setGColor(int color) {
            gColor = color;
        }

        public int getGColor() {
            return gColor;
        }
    }

    protected SimpleTreeNode root = null;

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public Tree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public Tree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, x -> x.toString());
    }

    public Tree() {
        this(null);
    }
    
    public SimpleTreeNode getRoot() {
        return root;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private class IndexWrapper {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                    quote && bracketStr.charAt(iw.index) != '"' ||
                    !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
               )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private SimpleTreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        SimpleTreeNode parentNode = new SimpleTreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                SimpleTreeNode leftNode = fromBracketStr(bracketStr, iw);
                parentNode.left = leftNode;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                SimpleTreeNode rightNode = fromBracketStr(bracketStr, iw);
                parentNode.right = rightNode;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        SimpleTreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }

    public int findTreeHeight() {
        class Inner {
            int findNodeHeight(SimpleTreeNode node, int level) {
                if (node == null) {
                    return 0;
                }
                int leftHeight = findNodeHeight(node.getLeft(), level + 1);
                int rightHeight = findNodeHeight(node.getRight(), level + 1);

                if (leftHeight > rightHeight) {
                    return leftHeight + 1;
                }
                else {
                    return rightHeight + 1;
                }
            }
        }
        return new Inner().findNodeHeight(root, 0);
    }

    public void setColor() {
        class Inner {
            void setColor(SimpleTreeNode node, int level) {
                if (node == null) {
                    return;
                }
                node.setGColor(255 - 255 / findTreeHeight() * level);
                setColor(node.getLeft(), level + 1);
                setColor(node.getRight(), level + 1);
            }
        }
        new Inner().setColor(root, 0);
    }
}
