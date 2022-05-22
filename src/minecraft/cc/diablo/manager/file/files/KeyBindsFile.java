package cc.diablo.manager.file.files;

import cc.diablo.Main;
import cc.diablo.manager.file.FileManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Module;
import org.lwjgl.input.Keyboard;

public class KeyBindsFile extends FileManager.CustomFile
{
    public KeyBindsFile(final String name, final boolean Module, final boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        final BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = variable9.readLine()) != null) {
            final int i = line.indexOf(":");
            if (i >= 0) {
                final String module = line.substring(0, i).trim();
                final String key = line.substring(i + 1).trim();
                final Module m = ModuleManager.getModuleByName(module);
                if (key.isEmpty()) {
                    continue;
                }
                if (m == null) {
                    continue;
                }
                m.setKey(Keyboard.getKeyIndex(key.toUpperCase()));
            }
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + " File");
    }

    @Override
    public void saveFile() throws IOException {
        final PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        for (Module m : ModuleManager.modules) {
            variable9.println(String.valueOf(String.valueOf(m.getName())) + ":" + Keyboard.getKeyName(m.getKey()));
        }
        variable9.close();
    }
}