package dev.kohimanayagato.serenity.api.gui.clickgui;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Category;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;


public class ClickGUI extends GuiScreen
{
	private final ArrayList<Window> windows = new ArrayList<>();

	public ClickGUI()
	{
		int xOffset = 3;

		for (Category category : Category.values())
		{
			if (category == Category.HIDDEN) continue;
			Window window = new Window(category, xOffset, 3, 105, 15);
			windows.add(window);
			xOffset += 110;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		doScroll();
		for (Window window : windows)
		{
			window.render(mouseX, mouseY);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		for (Window window : windows)
		{
			window.mouseDown(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		for (Window window : windows)
		{
			window.mouseUp(mouseX, mouseY);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode)
	{
		for (Window window : windows)
		{
			window.keyPress(keyCode);
		}

		if (keyCode == Keyboard.KEY_ESCAPE)
		{
			mc.displayGuiScreen(null);

			if (mc.currentScreen == null)
			{
				mc.setIngameFocus();
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor)
	{
		drawGradientRect(left, top, right, bottom, startColor, endColor);
	}

	@Override
	public void onGuiClosed()
	{
		for (Window window : windows)
		{
			window.close();
		}

		Client.moduleManager.getModule("ClickGUI").disable();
	}

	private void doScroll()
	{
		int w = Mouse.getDWheel();
		if (w < 0)
		{
			for (Window window : windows)
			{
				window.setY(window.getY() - 8);
			}
		}
		else if (w > 0)
		{
			for (Window window : windows)
			{
				window.setY(window.getY() + 8);
			}
		}
	}
}