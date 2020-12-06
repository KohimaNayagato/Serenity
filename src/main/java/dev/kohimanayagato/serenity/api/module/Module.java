package dev.kohimanayagato.serenity.api.module;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module
{
	private String name;
	private String description;
	private Category category;
	private int bind;
	private boolean enabled;
	public final Minecraft mc = Minecraft.getMinecraft();

	public Module(String name, Category category)
	{
		this.name = name;
		this.category = category;
	}

	public Module(String name, String description, Category category)
	{
		this.name = name;
		this.description = description;
		this.category = category;
	}


	public void onEnable() {}
	public void onDisable() {}

	public void enable()
	{
		setEnabled(true);
		onEnable();
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void disable()
	{
		setEnabled(false);
		onDisable();
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public void toggle()
	{
		if (isEnabled()) disable();
		else enable();
	}
	
	public boolean nullCheck()
	{
		return mc.player == null || mc.world == null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public int getBind()
	{
		return bind;
	}

	public void setBind(int bind)
	{
		this.bind = bind;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

}
