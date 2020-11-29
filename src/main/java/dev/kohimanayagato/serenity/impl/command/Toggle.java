package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.module.Module;

public class Toggle extends Command
{
    public Toggle(String name, String[] alias, String usage)
    {
        super(name, alias, usage);
    }


    @Override
    public void onTrigger(String arguments)
    {

        Module m = Client.moduleManager.getModule(arguments);
        if (m != null)
        {
            m.toggle();
            return;
        }

        printUsage();

    }

    @Override
    public void onRun(String arguments) {

    }
}