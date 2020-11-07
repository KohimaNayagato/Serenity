package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import org.lwjgl.input.Keyboard;

public class Bind extends Command
{
    public Bind(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments)
    {
        String[] split = arguments.split(" " );

        Module module = Client.getModuleManager().getModule(split[0]);

        if (module != null)
        {
            try
            {
                module.setBind(Keyboard.getKeyIndex(split[1].toUpperCase()));
                LoggerUtil.sendMessage(String.format("Bound %s to %s", module.getName(), split[1].toUpperCase()));
                return;
            }
            catch (Exception ignored)
            {
            }
        }

        printUsage();
    }

    public void onRun(String arguments) {

    }

}