package dev.kohimanayagato.serenity.api.gui.clickgui.button;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Module;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class SettingButton
{
	public final Minecraft mc = Minecraft.getMinecraft();
	private final int H;
	private Module module;
	private int X;
	private int Y;
	private int W;

	public SettingButton(Module module, int x, int y, int w, int h)
	{
		this.module = module;
		X = x;
		Y = y;
		W = w;
		H = h;
	}

	public void render(int mX, int mY)
	{
	}

	public void mouseDown(int mX, int mY, int mB)
	{
	}

	public void mouseUp(int mX, int mY)
	{
	}

	public void keyPress(int key)
	{
	}

	public void close()
	{
	}

	public void drawButton(int mX, int mY)
	{
		if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY))
		{
			switch (Client.settingManager.getSetting("ClickGUI", "Color").getEnumValue())
			{
				case "Purple":
					Client.clickGUI.drawGradient(X, Y, X + W, Y + H, new Color(140, 0, 180, 232).getRGB(), new Color(136, 0, 180, 232).getRGB());
					break;
				default:
					break;
			}
		}
		else
		{
			switch (Client.settingManager.getSetting("ClickGUI", "Color").getEnumValue())
			{
				case "Purple":
					Client.clickGUI.drawGradient(X, Y, X + W, Y + H, new Color(150, 0, 180, 232).getRGB(), new Color(146, 0, 180, 232).getRGB());
					break;
				default:
					break;
			}
		}
	}

	public Module getModule()
	{
		return module;
	}

	public void setModule(Module module)
	{
		this.module = module;
	}

	public int getX()
	{
		return X;
	}

	public void setX(int x)
	{
		X = x;
	}

	public int getY()
	{
		return Y;
	}

	public void setY(int y)
	{
		Y = y;
	}

	public int getW()
	{
		return W;
	}

	public int getH()
	{
		return H;
	}


	public boolean isHover(int X, int Y, int W, int H, int mX, int mY)
	{
		return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
	}
}