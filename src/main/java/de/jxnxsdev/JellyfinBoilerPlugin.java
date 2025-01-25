package de.jxnxsdev;

import de.jxnxsdev.sources.client.JellyfinPlayer;
import de.jxnxsdev.sources.server.JellyfinSource;
import net.somewhatcity.boiler.api.BoilerApi;
import net.somewhatcity.boiler.api.ISourceManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JellyfinBoilerPlugin extends JavaPlugin {

    private BoilerApi boilerApi;

    @Override
    public void onEnable() {
        boilerApi = Bukkit.getServicesManager().getRegistration(BoilerApi.class).getProvider();
        ISourceManager sourceManager = boilerApi.sourceManager();
        sourceManager.register("jellyfin", JellyfinSource.class);
        sourceManager.register("jellyfinplayer", JellyfinPlayer.class);
    }
}