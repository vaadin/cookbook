package com.vaadin.recipes.recipe.formattext;

import com.vaadin.flow.component.HasText;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("format-text")
@Metadata(howdoI = "show formatted text",
        description = "Different approaches for showing formatted text.",
        tags = {Tag.FLOW })
public class FormatText extends Recipe {
    public FormatText() {
        var lineBreaks = new Paragraph("Text\nwith\nline\nbreaks");
        lineBreaks.setWhiteSpace(HasText.WhiteSpace.PRE);

        var html = new Html(Jsoup.clean("<p>Formatted <b>text</b><br>as <i>HTML</i></p>", Safelist.basic()));

        var elements = new Paragraph();
        elements.getElement().appendChild(Element.createText("Formatted "), new Element("b").setText("text"),
                new Element("br"), Element.createText("as "), new Element("i").setText("elements"));

        add(new Paragraph(
                "To only preserve line breaks, you can use a component like Paragraph, Div or Span with a style to preserve line breaks."),
                lineBreaks,
                new Paragraph(
                        "For full formatting, you can use HTML either as a string or by assembling individual elements. "
                                + "When using an HTML string, you should be careful to not include any user-provided values that might lead to cross-site scripting vulnerabilities."),
                html, elements);
    }
}
