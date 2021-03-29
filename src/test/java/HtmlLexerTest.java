public class HtmlLexerTest {
    public static void main(String[] args) {

        String html = "<html><head></head><body>abc.<div style='pandding: 1em;'>123</div><a></body></html>";

        HtmlLexer htmlLexer = new HtmlLexer();
        htmlLexer.tokenize(html);

    }
}
