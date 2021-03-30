package example;

import example.HtmlLexer;

public class HtmlLexerTest {
    public static void main(String[] args) {

        String html = "" +
                "<!DOCTYPE html>" +
                "<html>" +
                "  <head></head>" +
                "  <body>" +
                "    <div>" +
                "      abc.<br>" +
                "      123 <br/>" +
                "      123 <br />" +
                "      <div style='pandding: 1em;'>" +
                "        123" +
                "      </div>" +
                "      <a>" +
                "      <img src='' />" +
                "    </div>" +
                "      </a>" +
                "  </body>" +
                "</html>";

        HtmlLexer htmlLexer = new HtmlLexer();
        htmlLexer.tokenize(html);

    }
}
