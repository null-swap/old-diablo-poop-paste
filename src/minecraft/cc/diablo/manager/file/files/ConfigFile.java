package cc.diablo.manager.file.files;

import cc.diablo.Main;
import cc.diablo.manager.file.FileManager;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Module;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;

import java.io.*;
import java.util.Objects;

public class ConfigFile extends FileManager.CustomFile{
    public ConfigFile(final String name, final boolean Module, final boolean loadOnStart) {
        super(name, Module, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        final BufferedReader variable9 = new BufferedReader(new FileReader(this.getFile()));
        String line = null;
        while ((line = variable9.readLine()) != null) {
            if(line.startsWith("#")){
                Main.customName = line.substring(1);
            }
            if(!line.startsWith("/") && !line.startsWith("#")) {
                final String[] arguments = line.split(":");
                String module = arguments[0];
                String status = arguments[1];
                try {
                    Module m = ModuleManager.getModuleByName(module);
                    if(m != null) {
                        if (Objects.equals(status, "true")) {
                            m.setToggled(true);
                        } else if (Objects.equals(status, "false") && m.toggled) {
                            m.setToggled(false);
                        }
                    }
                    int val = 1;
                    assert m != null;
                    for (Setting s : m.getSettingList()){
                        val += 1;
                        if(s instanceof ModeSetting){
                            ((ModeSetting) s).setMode(arguments[val]);
                        }
                        if(s instanceof NumberSetting){
                            ((NumberSetting) s).setValue(Double.parseDouble(arguments[val]));
                        }
                        if(s instanceof BooleanSetting){
                            ((BooleanSetting) s).setChecked(Boolean.parseBoolean(arguments[val]));
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        variable9.close();
        System.out.println("Loaded " + this.getName() + "!");
    }

    @Override
    public void saveFile() throws IOException {
        final PrintWriter variable9 = new PrintWriter(new FileWriter(this.getFile()));
        variable9.println("// Diablo " + Main.version + " config (" + Main.buildType + ")");
        variable9.println("#"+Main.customName);
        for (Module m : ModuleManager.getModules()) {
            StringBuilder string = new StringBuilder(m.getName() + ":" + m.isToggled());
            for (Setting s : m.getSettingList()) {
                try {
                    if (s instanceof ModeSetting) {
                        string.append(":").append(((ModeSetting) s).getMode());
                    }
                } catch (Exception e) {
                    ((ModeSetting) s).setMode(((ModeSetting) s).getDefaultMode());
                }

                try {
                    if (s instanceof NumberSetting) {
                        string.append(":").append(((NumberSetting) s).getVal());
                    }
                } catch (Exception e) {
                    ((NumberSetting) s).setVal(((NumberSetting) s).getDef());
                }

                try {
                    if (s instanceof BooleanSetting) {
                        string.append(":").append(((BooleanSetting) s).isChecked());
                    }
                } catch (Exception e) {
                    ((BooleanSetting) s).setChecked(((BooleanSetting) s).getDefault());
                }
            }
            variable9.println(string);
        }
        variable9.close();
    }
}
