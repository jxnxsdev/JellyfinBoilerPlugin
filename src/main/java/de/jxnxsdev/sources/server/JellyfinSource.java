package de.jxnxsdev.sources.server;

import com.google.gson.JsonObject;
import net.somewhatcity.boiler.api.CreateArgument;
import net.somewhatcity.boiler.api.CreateCommandArguments;
import net.somewhatcity.boiler.api.IBoilerSource;
import net.somewhatcity.boiler.api.SourceConfig;
import net.somewhatcity.boiler.api.display.IBoilerDisplay;
import net.somewhatcity.boiler.api.util.CommandArgumentType;
import net.somewhatcity.boiler.api.util.SourceType;

import java.awt.*;

@SourceConfig(
    sourceType = SourceType.SERVER
)
@CreateCommandArguments(arguments = {
    @CreateArgument(name = "url", type = CommandArgumentType.STRING)
})

public class JellyfinSource implements IBoilerSource {
    @Override
    public void load(IBoilerDisplay display, JsonObject jsonObject) {
        String url = jsonObject.get("url").getAsString();
    }

    @Override
    public void unload() {

    }

    @Override
    public void draw(Graphics2D g2, Rectangle viewport) {g2.setColor(Color.BLACK);
        g2.drawRect(viewport.x + 10, viewport.y + 10, viewport.width - 20, viewport.height - 20);
        g2.setColor(Color.WHITE);
        g2.fillRect(viewport.x + 20, viewport.y + 20, viewport.width - 40, viewport.height - 40);
        g2.setColor(Color.BLACK);
        g2.drawString("Hello, World!", viewport.x + viewport.width / 2 - 40, viewport.y + viewport.height / 2 + 5);
        g2.setColor(Color.GRAY);
        g2.drawString("Hello, World!", viewport.x + viewport.width / 2 - 41, viewport.y + viewport.height / 2 + 6);
    }
}