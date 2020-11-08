package dev.kohimanayagato.serenity.api.discord;

public class RPCBuilder
{
    private final String id;
    private String details;
    private String state;
    private String largeImageKey;
    private String largeImageText;
    private String smallImageKey;
    private String smallImageText;

    public RPCBuilder(String id)
    {
        this.id = id;
    }

    public RPCBuilder withDetails(String details)
    {
        this.details = details;
        return this;
    }

    public RPCBuilder withState(String state)
    {
        this.state = state;
        return this;
    }

    public RPCBuilder withLargeImageKey(String largeImageKey)
    {
        this.largeImageKey = largeImageKey;
        return this;
    }

    public RPCBuilder withLargeImageText(String largeImageText)
    {
        this.largeImageText = largeImageText;
        return this;
    }

    public RPCBuilder withSmallImageKey(String smallImageKey)
    {
        this.smallImageKey = smallImageKey;
        return this;
    }

    public RPCBuilder withSmallImageText(String smallImageText)
    {
        this.smallImageText = smallImageText;
        return this;
    }

    public Discord build()
    {
        return new Discord(this);
    }

    public String getId()
    {
        return id;
    }

    public String getDetails()
    {
        return details;
    }

    public String getState()
    {
        return state;
    }

    public String getLargeImageKey()
    {
        return largeImageKey;
    }

    public String getLargeImageText()
    {
        return largeImageText;
    }

    public String getSmallImageKey()
    {
        return smallImageKey;
    }

    public String getSmallImageText()
    {
        return smallImageText;
    }
}