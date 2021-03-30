package example.token;

public enum TokenType {
    StartingTag,
    ClosingTag,
    SelfClosingTag,
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
