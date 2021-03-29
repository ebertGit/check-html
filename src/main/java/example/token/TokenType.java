package example.token;

public enum TokenType {
    StartingTag,
    ClosingTag,
    Document,
    Attribute,
    Value,
    InnerText,
    Comment,
    CssTag,
    JsTag,
    CssContent,
    JsContent,
    UnknownItem,
}
