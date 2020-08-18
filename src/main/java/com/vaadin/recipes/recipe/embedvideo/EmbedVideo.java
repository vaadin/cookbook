package com.vaadin.recipes.recipe.embedvideo;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("embed-video")
@Metadata(howdoI = "Add a Java API for Video component")
public class EmbedVideo extends Recipe {

    public EmbedVideo() {
        Video video = new Video("videos/vaadin-channel.mp4");
        video.setMaxWidth("500px");
        add(video);
    }
}