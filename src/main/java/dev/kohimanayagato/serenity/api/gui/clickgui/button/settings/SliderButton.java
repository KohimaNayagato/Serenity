package dev.kohimanayagato.serenity.api.gui.clickgui.button.settings;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.gui.clickgui.button.SettingButton;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.font.FontUtil;

import java.awt.*;
import java.text.DecimalFormat;

public class SliderButton extends SettingButton
{
	private final Setting setting;
	protected boolean dragging;
	protected int sliderWidth;

	SliderButton(Module module, Setting setting, int X, int Y, int W, int H)
	{
		super(module, X, Y, W, H);
		this.dragging = false;
		this.sliderWidth = 0;
		this.setting = setting;
	}

	protected void updateSlider(int mouseX)
	{
	}

	@Override
	public void render(int mX, int mY)
	{
		updateSlider(mX);

		if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY))
		{
			Client.clickGUI.drawGradient(getX() + (sliderWidth) + 6, getY(), getX() + getW(), getY() + getH(), new Color(220, 220, 220, 232).getRGB(), new Color(218, 218, 218, 232).getRGB());
			switch (Client.settingManager.getSetting("ClickGUI", "Color").getEnumValue())
			{
				case "Purple":
					Client.clickGUI.drawGradient(getX(), getY(), getX() + (sliderWidth) + 6, getY() + getH(), new Color(140, 0, 180, 232).getRGB(), new Color(136, 0, 180, 232).getRGB());
					break;
				default:
					break;
			}
		}
		else
		{
			Client.clickGUI.drawGradient(getX() + (sliderWidth) + 6, getY(), getX() + getW(), getY() + getH(), new Color(240, 240, 240, 232).getRGB(), new Color(238, 238, 238, 232).getRGB());
			switch (Client.settingManager.getSetting("ClickGUI", "Color").getEnumValue())
			{
				case "Purple":
					Client.clickGUI.drawGradient(getX(), getY(), getX() + (sliderWidth) + 6, getY() + getH(), new Color(150, 0, 180, 232).getRGB(), new Color(146, 0, 180, 232).getRGB());
					break;
				default:
					break;
			}
		}


		FontUtil.drawStringWithShadow(setting.getName(), (float) (getX() + 6), (float) (getY() + 4), new Color(255, 255, 255, 255).getRGB());
		FontUtil.drawStringWithShadow(String.valueOf(setting.getIntegerValue()), (float) ((getX() + getW() - 6) - FontUtil.getStringWidth(String.valueOf(setting.getIntegerValue()))), (float) (getY() + 4), new Color(255, 255, 255, 255).getRGB());
	}

	public void mouseDown(int mX, int mY, int mB)
	{
		if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY))
		{
			dragging = true;
		}
	}

	public void mouseUp(int mouseX, int mouseY)
	{
		dragging = false;
	}

	public void close()
	{
		dragging = false;
	}

	public static class IntSlider extends SliderButton
	{
		private final Setting intSetting;

		public IntSlider(Module module, Setting setting, int X, int Y, int W, int H)
		{
			super(module, setting, X, Y, W, H);
			intSetting = setting;
		}

		@Override
		protected void updateSlider(final int mouseX)
		{
			final double diff = Math.min(getW(), Math.max(0, mouseX - getX()));
			final double min = intSetting.getMinIntegerValue();
			final double max = intSetting.getMaxIntegerValue();
			sliderWidth = (int) ((getW() - 6) * (intSetting.getIntegerValue() - min) / (max - min));
			if (dragging)
			{
				if (diff == 0.0)
				{
					intSetting.setIntegerValue(intSetting.getIntegerValue());
				}
				else
				{
					final DecimalFormat format = new DecimalFormat("##");
					final String newValue = format.format(diff / getW() * (max - min) + min);
					intSetting.setIntegerValue(Integer.parseInt(newValue));
				}
			}
		}
	}
}
