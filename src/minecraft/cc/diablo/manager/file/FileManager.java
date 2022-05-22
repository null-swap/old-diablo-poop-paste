package cc.diablo.manager.file;

import cc.diablo.Main;
import cc.diablo.helpers.TimerUtil;
import cc.diablo.manager.file.files.ConfigFile;
import cc.diablo.manager.file.files.KeyBindsFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
    public static ArrayList<CustomFile> Files;
    public static TimerUtil loadTimer;

    static {
        FileManager.Files = new ArrayList<CustomFile>();
    }

    public FileManager() {
        this.makeDirectories();
        loadTimer = new TimerUtil();
        //FileManager.Files.add(new ModulesFile("ModuleList", true, true));
        //FileManager.Files.add(new PrefixFile("CommandPrefix", false, true));
        //TODO: Fix this!, friend saving
        //FileManager.Files.add(new FriendsFile("Friends", true, true));
        FileManager.Files.add(new ConfigFile("Config", true, true));
        FileManager.Files.add(new KeyBindsFile("KeyBinds", true, true));
    }

    /**
     * public static void loadFriends() {
     * for (CustomFile f : FileManager.Files) {
     * if(f.equals(FriendsFile.class)){
     * try {
     * if (!f.loadOnStart()) {
     * continue;
     * }
     * f.loadFile();
     * }
     * catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * }
     * }
     * <p>
     * public static void saveFriends() {
     * for (CustomFile f : FileManager.Files) {
     * if(f.equals(FriendsFile.class)){
     * try {
     * f.saveFile();
     * }
     * catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * }
     * }
     **/

    public void loadFiles() {
        for (CustomFile f : FileManager.Files) {
            try {
                if (!f.loadOnStart()) {
                    continue;
                }
                f.loadFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFiles() {
        for (CustomFile f : FileManager.Files) {
            try {
                f.saveFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class<? extends CustomFile> clazz) {
        for (CustomFile file : FileManager.Files) {
            if (file.getClass() == clazz) {
                return file;
            }
        }
        return null;
    }

    public void makeDirectories() {
        try {
            if (!Main.fileDir.exists()) {
                if (Main.fileDir.mkdir()) {
                    System.out.println("Created diablo directory!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            } else {
                System.out.println("Directory already exists!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract static class CustomFile {
        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean Module, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(Main.fileDir, String.valueOf(name) + ".diablo");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public <T> T getValue(T value) {
            return null;
        }

        /*
        public void setValue(ValueManager.Value p, String value) {

            if (p instanceof ValueManager.DoubleValue) {
                ((ValueManager.DoubleValue)p).setValue(Double.valueOf(value));
            }
            if (p.value instanceof Integer) {
                p.value = Integer.valueOf(value);
            }
            else if (p.value instanceof Boolean) {
                p.value = Boolean.valueOf(value);
            }
            else if (p.value instanceof Double) {
                p.value = Double.valueOf(value);
            }
            else if (p.value instanceof Float) {
                p.value = Float.valueOf(value);
            }
            else if (p.value instanceof Long) {
                p.value = Long.valueOf(Long.parseLong(value));
            }
            else if (p.value instanceof String) {
            }
            else {
                p.reset();
            }
        }

         */

        public File getFile() {
            return this.file;
        }

        private boolean loadOnStart() {
            return this.load;
        }

        public String getName() {
            return this.name;
        }

        public abstract void loadFile() throws IOException;

        public abstract void saveFile() throws IOException;
    }

}
