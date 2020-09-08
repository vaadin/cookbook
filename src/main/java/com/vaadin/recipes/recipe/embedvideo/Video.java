package com.vaadin.recipes.recipe.embedvideo;

import com.vaadin.flow.component.*;

@Tag("video")
public class Video extends HtmlContainer {
    private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors.attributeWithDefault(
        "src",
        ""
    );

    public Video() {
        super();
        getElement().setProperty("controls", true);
    }

    public Video(String src) {
        setSrc(src);
        getElement().setProperty("controls", true);
    }

    public String getSrc() {
        return get(srcDescriptor);
    }

    public void setSrc(String src) {
        set(srcDescriptor, src);
    }
}
