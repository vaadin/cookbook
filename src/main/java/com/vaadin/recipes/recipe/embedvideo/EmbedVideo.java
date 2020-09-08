package com.vaadin.recipes.recipe.embedvideo;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("embed-video")
@Metadata(
    howdoI = "Show a video",
    description = "Demonstrates how you can use the <video> element to show an mp4 video",
    sourceFiles = "Video.java"
)
public class EmbedVideo extends Recipe {

    public EmbedVideo() {
        Video video = new Video("videos/vaadin-channel.mp4");
        video.setMaxWidth("500px");
        add(video);
    }
}
