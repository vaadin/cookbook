package com.vaadin.recipes.recipe.embedaudio;

import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("embed-audio")
@Metadata(howdoI = "Embed an audio player", description = "Shows how you can embed an <audio> tag to allow the user to play audio")
public class EmbedAudio extends Recipe {

    public EmbedAudio() {
        Audio audio = new Audio("audio/sound.mp3");
        add(audio);
    }
}
