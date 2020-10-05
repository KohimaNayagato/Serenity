package dev.kohimanayagato.serenity.api.setting;

import dev.kohimanayagato.serenity.api.module.Module;

import java.util.ArrayList;

public class Setting
{
	private String name;
	private Module module;
	private SettingType type;
	private boolean booleanValue;
	private int integerValue;
	private int minIntegerValue;
	private int maxIntegerValue;
	private String enumValue;
	private ArrayList<String> enumValues;

	public static class Builder
	{
		private String name;
		private Module module;
		private final SettingType type;
		private boolean booleanValue;
		private int integerValue;
		private int minIntegerValue;
		private int maxIntegerValue;
		private String enumValue;
		private ArrayList<String> enumValues;

		public Builder(SettingType type)
		{
			this.type = type;
		}

		public Builder setName(String name)
		{
			this.name = name;
			return this;
		}

		public Builder setModule(Module module)
		{
			this.module = module;
			return this;
		}

		public Builder setBooleanValue(boolean booleanValue)
		{
			this.booleanValue = booleanValue;
			return this;
		}

		public Builder setIntegerValue(int integerValue)
		{
			this.integerValue = integerValue;
			return this;
		}

		public Builder setMinIntegerValue(int minIntegerValue)
		{
			this.minIntegerValue = minIntegerValue;
			return this;
		}

		public Builder setMaxIntegerValue(int maxIntegerValue)
		{
			this.maxIntegerValue = maxIntegerValue;
			return this;
		}

		public Builder setEnumValue(String enumValue)
		{
			this.enumValue = enumValue;
			return this;
		}

		public Builder setEnumValues(ArrayList<String> enumValues)
		{
			this.enumValues = enumValues;
			return this;
		}

		public Builder addEnumValue(String enumValue)
		{
			if (enumValues == null) enumValues = new ArrayList<>();
			enumValues.add(enumValue);
			return this;
		}

		public Setting build()
		{
			switch (type)
			{
				case BOOLEAN:
					return new Setting(name, module, booleanValue);
				case INTEGER:
					return new Setting(name, module, integerValue, minIntegerValue, maxIntegerValue);
				case ENUM:
					return new Setting(name, module, enumValue, enumValues);
				default:
					return null;
			}
		}
	}

	private Setting(String name, Module module, int intValue, int intMinValue, int intMaxValue)
	{
		setName(name);
		setModule(module);
		setIntegerValue(intValue);
		setMinIntegerValue(intMinValue);
		setMaxIntegerValue(intMaxValue);
		setType(SettingType.INTEGER);
	}

	private Setting(String name, Module module, boolean boolValue)
	{
		setName(name);
		setModule(module);
		setBooleanValue(boolValue);
		setType(SettingType.BOOLEAN);
	}

	private Setting(String name, Module module, String enumValue, ArrayList<String> enumValues)
	{
		setName(name);
		setModule(module);
		setEnumValue(enumValue);
		setEnumValues(enumValues);
		setType(SettingType.ENUM);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Module getModule()
	{
		return module;
	}

	public void setModule(Module module)
	{
		this.module = module;
	}

	public SettingType getType()
	{
		return type;
	}

	public void setType(SettingType type)
	{
		this.type = type;
	}

	public boolean getBooleanValue()
	{
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue)
	{
		this.booleanValue = booleanValue;
	}

	public int getIntegerValue()
	{
		return integerValue;
	}

	public void setIntegerValue(int integerValue)
	{
		this.integerValue = integerValue;
	}

	public int getMinIntegerValue()
	{
		return minIntegerValue;
	}

	public void setMinIntegerValue(int minIntegerValue)
	{
		this.minIntegerValue = minIntegerValue;
	}

	public int getMaxIntegerValue()
	{
		return maxIntegerValue;
	}

	public void setMaxIntegerValue(int maxIntegerValue)
	{
		this.maxIntegerValue = maxIntegerValue;
	}

	public String getEnumValue()
	{
		return enumValue;
	}

	public void setEnumValue(String enumValue)
	{
		this.enumValue = enumValue;
	}

	public ArrayList<String> getEnumValues()
	{
		return enumValues;
	}

	public void setEnumValues(ArrayList<String> enumValues)
	{
		this.enumValues = enumValues;
	}
}
