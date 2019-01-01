package garye.utils.jhy.common;

class JConst {
    private static final JConst ourInstance = new JConst();

    static JConst getInstance() {
        return ourInstance;
    }

    private JConst() {
    }
}
