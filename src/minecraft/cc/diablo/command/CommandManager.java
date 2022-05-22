package cc.diablo.command;

import cc.diablo.command.impl.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
        public static ArrayList<Command> commands = new ArrayList<>();
        public CommandManager() {
            System.out.println("Loading commands...");
            this.addModules(
                new ToggleCommand(),
                new ModulesCommand(),
                new HelpCommand(),
                new CommandsCommand(),
                new VClipCommand(),
                new ClearChatCommand(),
                new BindCommand(),
                new FriendCommand(),
                new OpenFolderCommand(),
                new PanicCommand(),
                new ConfigCommand(),
                new ClientNameCommand(),
                new QuitCommand()
            );

            for(Command c : commands){
                System.out.println(c.getDisplayName() + " | " + c.getDescription());
            }

            System.out.println("Loaded " + commands.size() + " commands!");
        }

        public void addModules(Command... modulesAsList) {
            commands.addAll(Arrays.asList(modulesAsList));
        }

        public static ArrayList<Command> getModules() {
            return commands;
        }

        public static <T extends Command> Command getCommand(Class<T> clas) {
            return getModules().stream().filter(command -> command.getClass() == clas).findFirst().orElse(null);
        }
}
