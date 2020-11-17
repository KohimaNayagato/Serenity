package dev.kohimanayagato.serenity.impl.module.misc;

import dev.kohimanayagato.serenity.api.discord.Discord;
import dev.kohimanayagato.serenity.api.discord.RPCBuilder;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;

public class RPC extends Module {

    public RPC(String name, String description, Category category)
    {
        super(name, description, category);
    }

    public static Discord discordRPC = new RPCBuilder("753692319290491013").withDetails("Version Alpha 0.6-DEV").withState("").withLargeImageKey("logo").withLargeImageText("Serenity Alpha").build();

    @Override
    public void onEnable()
    {
        discordRPC.start();
    }

    @Override
    public void onDisable()
    {
        discordRPC.stop();
    }

}
