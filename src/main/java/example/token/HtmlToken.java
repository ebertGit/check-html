package example.token;


public class HtmlToken {
    private String name = "";

    private TokenType type;

    private boolean isCss = false;

    private boolean isScript = false;

    private String text = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public boolean isCss() {
        return isCss;
    }

    public void setCssFlag(boolean flag) {
        isCss = flag;
    }

    public boolean isScript() {
        return isScript;
    }

    public void setScriptFlag(boolean flag) {
        isScript = flag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
