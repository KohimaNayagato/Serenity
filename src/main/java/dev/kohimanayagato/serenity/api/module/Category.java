package dev.kohimanayagato.serenity.api.module;

public enum Category
{
	COMBAT("Combat"),
	EXPLOIT("Exploit"),
	RENDER("Render"),
	MOVEMENT("Movement"),
	CHAT("Chat"),
	MISC("Miscellaneous"),
	HIDDEN("Hidden");

	private String name;

	Category(String name)
	{
		setName(name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
