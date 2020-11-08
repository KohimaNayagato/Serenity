package dev.kohimanayagato.serenity.api.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Discord
{
    private final DiscordRichPresence presence;
    private final DiscordRPC rpc;
    private final String id;
    private String details;
    private String state;
    private final String largeImageKey;
    private String largeImageText;
    private final String smallImageKey;
    private String smallImageText;
    private boolean connected;

    public Discord(RPCBuilder builder)
    {
        presence = new DiscordRichPresence();
        rpc = DiscordRPC.INSTANCE;
        connected = false;

        this.id = builder.getId();

        if (builder.getDetails() == null) details = "";
        else details = builder.getDetails();
        if (builder.getState() == null) state = "";
        else state = builder.getState();

        if (builder.getLargeImageKey() == null) largeImageKey = "";
        else largeImageKey = builder.getLargeImageKey();
        if (builder.getLargeImageText() == null) largeImageText = "";
        else largeImageText = builder.getLargeImageText();

        if (builder.getSmallImageKey() == null) smallImageKey = "";
        else smallImageKey = builder.getSmallImageKey();
        if (builder.getSmallImageText() == null) smallImageText = "";
        else smallImageText = builder.getSmallImageText();
    }

    public void start()
    {
        if (connected) return;
        presence.startTimestamp = System.currentTimeMillis() / 1000L;

        DiscordEventHandlers handlers = new DiscordEventHandlers();

        rpc.Discord_Initialize(id, handlers, true, "");
        rpc.Discord_UpdatePresence(presence);

        presence.details = details;
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = largeImageText;
        presence.smallImageKey = smallImageKey;
        presence.smallImageText = smallImageText;
        rpc.Discord_UpdatePresence(presence);

        connected = true;
        new Thread(this::startThread).start();
    }

    public void stop()
    {
        if (!connected) return;
        connected = false;
        rpc.Discord_Shutdown();
    }

    private void startThread()
    {
        while (connected && !Thread.currentThread().isInterrupted())
        {
            updateRPC();

            try { Thread.sleep(3000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void updateRPC()
    {
        presence.details = details;
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = largeImageText;
        presence.smallImageKey = smallImageKey;
        presence.smallImageText = smallImageText;
        rpc.Discord_UpdatePresence(presence);
    }

    public String getId()
    {
        return id;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getLargeImageKey()
    {
        return largeImageKey;
    }

    public String getLargeImageText()
    {
        return largeImageText;
    }

    public void setLargeImageText(String largeImageText)
    {
        this.largeImageText = largeImageText;
    }

    public String getSmallImageKey()
    {
        return smallImageKey;
    }

    public String getSmallImageText()
    {
        return smallImageText;
    }

    public void setSmallImageText(String smallImageText)
    {
        this.smallImageText = smallImageText;
    }
}