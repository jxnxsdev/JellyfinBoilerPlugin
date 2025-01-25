package de.jxnxsdev.sources.client;

import com.google.gson.JsonObject;
import net.somewhatcity.boiler.api.CreateArgument;
import net.somewhatcity.boiler.api.CreateCommandArguments;
import net.somewhatcity.boiler.api.IBoilerSource;
import net.somewhatcity.boiler.api.SourceConfig;
import net.somewhatcity.boiler.api.display.IBoilerDisplay;
import net.somewhatcity.boiler.api.util.CommandArgumentType;
import net.somewhatcity.boiler.api.util.SourceType;
import org.bytedeco.javacpp.Loader;

import java.awt.*;
import java.io.IOException;

@SourceConfig(sourceType = SourceType.CLIENT)
@CreateCommandArguments(arguments = {
        @CreateArgument(name = "videourl", type = CommandArgumentType.GREEDY_STRING)
})
public class JellyfinPlayer implements IBoilerSource {

    private IBoilerDisplay display;
    private Thread thread;
    private Process ffmpegProcess;

    @Override
    public void load(IBoilerDisplay display, JsonObject jsonObject) {
        this.display = display;
        String streamOut = display.rtspPublishUrl();
        String sourceUrl = jsonObject.get("videourl").getAsString();

        thread = new Thread(() -> {
            String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);

            String[] args = {
                    ffmpeg, "-re", "-loglevel", "0", "-i", sourceUrl, "-preset", "ultrafast",
                    "-maxrate", "3000k", "-b:v", "2500k", "-bufsize", "600k", "-f", "rtsp", streamOut
            };

            System.out.println(String.join(" ", args));

            ProcessBuilder pb = new ProcessBuilder(args);

            try {
                pb.inheritIO();
                ffmpegProcess = pb.start();
                int exitCode = ffmpegProcess.waitFor();
                System.out.println("FFmpeg process finished with exit code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @Override
    public void unload() {
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
        }

        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void draw(Graphics2D g2, Rectangle viewport) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, viewport.width, viewport.height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Boiler client mod required to view this source", 10, 30);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("rootMapId: " + display.mapDisplay().frameAt(0, 0).mapId(0), 10, 80);
        g2.drawString("source: " + display.sourceName(), 10, 100);
    }
}
