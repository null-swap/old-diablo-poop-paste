package net.minecraft.client;

import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.exploit.Disabler;

public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        if(Disabler.mode.isMode("AGC v2") && ModuleManager.getModuleByName("Disabler").isToggled())
            return "\\";
        return "vanilla";
    }
}
