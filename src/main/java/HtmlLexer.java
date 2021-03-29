
import example.enums.DfaState;
import example.token.HtmlToken;
import example.token.TokenType;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Html 字句解析器(Lexical Analyzer)
 * <p>Html 例：</p>
 * <pre>{@code
 * <!DOCTYPE html>
 * <html lang="en">
 *  <head>
 *   <title>Sample styled page</title>
 *   <style>
 *    body { background: navy; color: yellow; }
 *   </style>
 *  </head>
 *  <body>
 *   <h1>Sample styled page</h1>
 *   <p>This page is just a demo.</p>
 *   <!-- This is comment. -->
 *   <form name="main">
 *    Result: <output name="result"></output>
 *    <br />
 *    New line.
 *    <br/>
 *   <script>
 *    document.forms.main.elements.result.value = 'Hello World';
 *   </script>
 *   </form>
 *  </body>
 * </html>
 * }</pre>
 */
public class HtmlLexer {

    private StringBuffer tokenText;
    private HtmlToken token;
//    private ArrayList<HtmlToken> tokens;

    private Stack<HtmlToken> htmlTagStack = new Stack<>();

    /**
     * 文字列を解析する。
     */
    public void tokenize(String code) {
//        tokens = new ArrayList<>();
        CharArrayReader reader = new CharArrayReader(code.toCharArray());
        tokenText = new StringBuffer();
        token = new HtmlToken();
        int ich = 0;
        char ch = 0;
        DfaState state = DfaState.Initial;

        try {
            while ((ich = reader.read()) != -1) {
                ch = (char) ich;
                switch (state) {
                    case Initial:
                        state = initToken(ch);
                        break;
                    case Document_1:
                        if (ch == '!') {
                            state = DfaState.Document_2;
                            tokenText.append(ch);
                        } else if (ch == '/') {
                            state = DfaState.ClosingTag_1;
                            token.setType(TokenType.ClosingTag); // 閉じるタグの場合
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            token.setType(TokenType.UnknownItem); // 例：「<」のみの場合
                            state = initToken(ch);
                        }
                        break;
                    case Document_2:
                        if (ch == 'D' || ch == 'd') {
                            state = DfaState.Document_3;
                            tokenText.append(ch);
                        } else if (ch == '-') {
                            state = DfaState.Comment_3;
                            token.setType(TokenType.Comment); // コメントの場合
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            token.setType(TokenType.UnknownItem); // 例：「<!」のみの場合
                            state = initToken(ch);
                        }
                        break;
                    case Document_3:
                        if (ch == 'O' || ch == 'o') {
                            state = DfaState.Document_4;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_4:
                        if (ch == 'C' || ch == 'c') {
                            state = DfaState.Document_5;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_5:
                        if (ch == 'T' || ch == 't') {
                            state = DfaState.Document_6;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_6:
                        if (ch == 'Y' || ch == 'y') {
                            state = DfaState.Document_7;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_7:
                        if (ch == 'P' || ch == 'p') {
                            state = DfaState.Document_8;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_8:
                        if (ch == 'E' || ch == 'e') {
                            state = DfaState.Document_9;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.Element;
                            token.setType(TokenType.StartingTag); // タグの場合
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Document_9:
                        if (isBlank(ch)) {
                            state = DfaState.Document;
                            token.setType(TokenType.Document);  // 「<!DOCTYPE 」の場合
                        } else {
                            token.setType(TokenType.UnknownItem);
                        }
                        tokenText.append(ch);
                        break;
                    case Document:
                        if (ch == '>') {
                            state = initToken(ch);
                        } else {
                            tokenText.append(ch);
                        }
                        break;
                    case Comment_3:
                        if (ch == '-') {
                            state = DfaState.Comment_4;
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.UnknownItem;
                            token.setType(TokenType.UnknownItem);
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Comment_4:
                        state = DfaState.CommentContent;
                        tokenText.append(ch);
                        break;
                    case CommentContent:
                        if (ch == '-') {
                            state = DfaState.CommentClosing_1;  // 「-->」かのチェックを開始
                        }
                        tokenText.append(ch);
                        break;
                    case CommentClosing_1:
                        if (ch == '-') {
                            state = DfaState.CommentClosing_2;
                        } else {
                            state = DfaState.CommentContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CommentClosing_2:
                        if (ch == '>') {
                            state = initToken(ch);
                        } else {
                            state = DfaState.CommentContent;
                            tokenText.append(ch);
                        }
                        break;
                    case Element:
                        if (ch == '>') {
                            tokenText.append(ch);
                            state = initToken(ch);
                        } else if (!isBlank(ch)) {
                            tokenText.append(ch);   // 空白或いは、「>」まで続く
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case ClosingTag:
                    case UnknownItem:
                        if (ch == '>') {
                            tokenText.append(ch);
                            state = initToken(ch);
                        } else if (!isBlank(ch)) {
                            tokenText.append(ch);   // 空白或いは、「>」まで続く
                        } else {
                            token.setType(TokenType.UnknownItem);
                            state = initToken(ch);
                        }
                        break;
                    case ClosingTag_1:
                        // TODO: 他の違法文字も判断できるように、メソッド化する。
                        if (ch == '/') {
                            state = DfaState.UnknownItem;
                            token.setType(TokenType.UnknownItem);
                            tokenText.append(ch);
                        } else if (!isBlank(ch)) {
                            state = DfaState.ClosingTag;
                            token.setType(TokenType.ClosingTag);
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Attribute:
                        // TODO: クォーテーション関連の判断処理を追加
                        if (ch == '>') {
                            state = initToken(ch);
                        } else if (ch == '/') {
                            state = DfaState.ClosingSelf;
                        } else {
                            tokenText.append(ch);
                        }
                        break;
                    case ClosingSelf:
                        if (ch == '>') {
                            state = initToken(ch);
                        } else {
                            state = DfaState.Attribute;
                            tokenText.append(ch);
                        }
                        break;
                    case Value:
                        // TODO: 属性の値
                        break;
                    case InnerText:
                        if (ch == '<') {
                            state = initToken(ch);
                        } else {
                            tokenText.append(ch);
                        }
                    case CssContent:
                        if (ch == '<') {
                            // TODO: 今回は、開始タグと終了タグだけをチェックするため、それ以外の内容をクリアしても問題ない。
                            tokenText = new StringBuffer();
                            state = DfaState.CssClosing_1;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_1:
                        if (ch == '/') {
                            state = DfaState.CssClosing_2;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_2:
                        if (ch == 'S' || ch == 's') {
                            state = DfaState.CssClosing_3;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_3:
                        if (ch == 'T' || ch == 't') {
                            state = DfaState.CssClosing_4;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_4:
                        if (ch == 'Y' || ch == 'y') {
                            state = DfaState.CssClosing_5;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_5:
                        if (ch == 'L' || ch == 'l') {
                            state = DfaState.CssClosing_6;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_6:
                        if (ch == 'E' || ch == 'e') {
                            state = DfaState.CssClosing_7;
                        } else {
                            state = DfaState.CssContent;
                        }
                        tokenText.append(ch);
                        break;
                    case CssClosing_7:
                        if (ch == '>') {
                            tokenText.append(ch);
                            token.setType(TokenType.ClosingTag);
                            token.setCssFlag(true);
                            state = initToken(ch);
                        } else {
                            state = DfaState.CssContent;
                            tokenText.append(ch);
                        }
                        break;
                    case JsContent:
                        if (ch == '<') {
                            // TODO: 今回は、開始タグと終了タグだけをチェックするため、それ以外の内容をクリアしても問題ない。
                            tokenText = new StringBuffer();
                            state = DfaState.JsClosing_1;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_1:
                        if (ch == '/') {
                            state = DfaState.JsClosing_2;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_2:
                        if (ch == 'S' || ch == 's') {
                            state = DfaState.JsClosing_3;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_3:
                        if (ch == 'C' || ch == 'c') {
                            state = DfaState.JsClosing_4;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_4:
                        if (ch == 'R' || ch == 'r') {
                            state = DfaState.JsClosing_5;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_5:
                        if (ch == 'I' || ch == 'i') {
                            state = DfaState.JsClosing_6;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_6:
                        if (ch == 'P' || ch == 'p') {
                            state = DfaState.JsClosing_7;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_7:
                        if (ch == 'T' || ch == 't') {
                            state = DfaState.JsClosing_8;
                        } else {
                            state = DfaState.JsContent;
                        }
                        tokenText.append(ch);
                        break;
                    case JsClosing_8:
                        if (ch == '>') {
                            tokenText.append(ch);
                            token.setType(TokenType.ClosingTag);
                            token.setScriptFlag(true);
                            state = initToken(ch);
                        } else {
                            state = DfaState.JsContent;
                            tokenText.append(ch);
                        }
                        break;
                    default:
                }
            }
            // 最後の token をリストに入れる。
            if (tokenText.length() > 0) {
                initToken(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ペアしていないタグ有無を確認
        while (htmlTagStack.size() > 0) {
            // ログに出力する開始タグを見やすいように、後ろに「>」をつけて出力する。
            String startTag = htmlTagStack.pop().getText();
            startTag = startTag.endsWith(">") ? startTag : startTag + ">";
            System.out.printf("該開始タグとペアする閉じるタグが存在しません:「%s」\n", startTag);
        }
        System.out.println("チェック完了");
    }

    /**
     * 文字より状態を判定する。
     *
     * @param ch
     * @return
     */
    private DfaState initToken(char ch) {
        if (tokenText.length() > 0) {
            String text = tokenText.toString();
            token.setText(text);
            // タグ名を取得する。
            if (token.getType() == TokenType.StartingTag) {
                token.setName(text.substring(1, text.endsWith(">") ? text.length() - 1 : text.length()));
                // styleタグか, scriptタグかを判断する。
                if ("style".equalsIgnoreCase(token.getName())) {
                    token.setCssFlag(true);
                } else if ("script".equalsIgnoreCase(token.getName())) {
                    token.setScriptFlag(true);
                }
            } else if (token.getType() == TokenType.ClosingTag) {
                token.setName(text.substring(2, text.length() - 1));
            }
//            tokens.add(token);

            // タグをチェックする。
            if (token.getType() == TokenType.StartingTag) {
                htmlTagStack.push(token);
            } else if (token.getType() == TokenType.ClosingTag) {
                if (token.getName().equals(htmlTagStack.peek().getName())) {
                    // ログに出力する開始タグを見やすいように、後ろに「>」をつけて出力する。
                    String startTag = htmlTagStack.pop().getText();
                    startTag = startTag.endsWith(">") ? startTag : startTag + ">";
                    System.out.printf("ペア成功したタグ：%s - %s\n", startTag, token.getText());
                } else {
                    System.out.printf("該閉じるタグとペアする開始タグが存在しません:「%s」\n", token.getText());
                }
            } else if (token.getType() == TokenType.UnknownItem) {
                System.out.printf("未知項目：「%s」\n", token.getText());
            }

            // 初期化する。
            tokenText = new StringBuffer();
            token = new HtmlToken();
        }

        DfaState newState = DfaState.Initial;
        if (ch != '>' && htmlTagStack.size() > 0 && !htmlTagStack.peek().getText().endsWith(">") && htmlTagStack.peek().getType() == TokenType.StartingTag) {
            // タグの属性
            newState = DfaState.Attribute;
            htmlTagStack.peek().setText(htmlTagStack.peek().getText() + ">");
        } else if (ch == '<') {
            newState = DfaState.Document_1;
            token.setType(TokenType.Document);
            tokenText.append(ch);
//        } else if (ch == '\'') {
//
//        } else if (ch == '"') {
//
//        } else if (isBlank(ch)) {
//
//        } else if (ch == '>') {
//
//        } else {
//            newState = DfaState.Initial;
        }

        return newState;
    }


//TODO: utilクラスに移動

    /**
     * 英字かを判断する。
     *
     * @param ch
     * @return
     */
    private boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    /**
     * 数字かを判断する。
     *
     * @param ch
     * @return
     */
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 空白文字かを判断する。
     *
     * @param ch
     * @return
     */
    private boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

}
