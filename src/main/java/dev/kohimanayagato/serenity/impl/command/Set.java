package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;

public class Set extends Command
{
    public Set(String name, String[] alias, String usage)
    {
        super(name, alias, usage);
    }


    @Override
    public void onTrigger(String arguments)
    {
        String[] args = arguments.split(" ");

        for (Module module : Client.moduleManager.getModules())
        {
            if (module.getName().equalsIgnoreCase(args[0]))
            {

                for (Setting setting : Client.settingManager.getSettings())
                {
                    if (setting.getModule().equals(module) && args[1].equalsIgnoreCase(setting.getName()))
                    {

                        if (setting.isInteger() && (setting.getMinIntegerValue() - 1) < Integer.parseInt(args[2].toLowerCase()) && (setting.getMaxIntegerValue() + 1) > Integer.parseInt(args[2].toLowerCase()))
                        {
                            setting.setIntegerValue(Integer.parseInt(args[2].toLowerCase()));
                            LoggerUtil.sendMessage("Set " + setting.getName() + " to " + setting.getIntegerValue());
                            return;
                        }
                        else if (setting.isBoolean())
                        {
                            setting.setBooleanValue(args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true"));
                            LoggerUtil.sendMessage("Set " + setting.getName() + " to " + setting.getBooleanValue());
                            return;
                        }
                        else if (setting.isEnum())
                        {
                            for (String string : setting.getEnumValues())
                            {
                                if (args[2].equalsIgnoreCase(string)) setting.setEnumValue(string);
                            }
                            LoggerUtil.sendMessage("Set " + setting.getName() + " to " + setting.getEnumValue());
                            return;
                        }
                    }
                }
            }
        }

        printUsage();
    }
    @Override
    public void onRun(String arguments) {

    }
}