package cc.diablo.manager.module;

import cc.diablo.Main;
import cc.diablo.module.Module;
import cc.diablo.module.impl.combat.*;
import cc.diablo.module.impl.movement.*;
import cc.diablo.module.impl.player.*;
import cc.diablo.module.impl.ghost.*;
import cc.diablo.module.impl.misc.*;
import cc.diablo.module.impl.exploit.*;
import cc.diablo.module.impl.render.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ModuleManager {
    public static ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        System.out.println("Loading modules...");
        this.addModules(
            new InventoryCleaner(),
            new Sprint(),
            new Fly(),
            new NoFall(),
            new HUD(),
            new AntiVoid(),
            new ClickGUI(),
            new Sneak(),
            new NoRotate(),
            new Timer(),
            new AimAssistance(),
            //new Reach(),
            new AutoClicker(),
            new InventoryMove(),
            new KillAura(),
            new Glow(),
            new AntiAim(),
            new FullBright(),
            new NoSlow(),
            new KeepSprint(),
            new AutoTool(),
            new Speed(),
            new Disabler(),
            new Velocity(),
            new Stealer(),
            new LongJump(),
            new AutoHypixel(),
            new Scoreboard(),
            new Criticals(),
            new AutoPot(),
            new Cape(),
            new Scaffold(),
            new Esp2D(),
            new Chams(),
            new ChestChams(),
            new SafeWalk(),
            new BowFly(),
            new Phase(),
            new MiddleClickFriend(),
            new Glint(),
            new TimeChanger(),
            new Blink(),
            new TargetStrafe(),
            new AutoBlocksMC(),
            new ClientSpoof(),
            new Jesus(),
            //new SlowHit(),
            new Animations(),
            new FOVChanger(),
            new LightningDetector(),
            new PlayerList(),
            new Highjump(),
            new CustomChat(),
            new ChestESP(),
            //new ItemOffset(),
            new AntiAtlas(),
            new Spammer(),
            new AutoDisable(),
            new ItemESP(),
            //new DamageParticles(),
            new ItemPhysics(),
            new XRay(),
            new HitSound(),
            new AntiVanish(),
            new FastBow(),
            new BowAimbot(),
            new StaffDetector(),
            new Regen(),
            new IRC(),
            new FastPlace(),
            new NameHider(),
            new Viewer(),
            new Crosshair(),
            new Crasher(),
            new Nametags(),
            new ShowName()
            //new Tracers();
        );

        for (Module m : modules) {
            System.out.println(m.getName() + " | " + m.getDescription());
        }
        System.out.println("Loaded " + modules.size() + " modules!");
    }

    public void addModules(Module... modulesAsList) {
        modules.addAll(Arrays.asList(modulesAsList));
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    public static ArrayList<Module> getModulesToggled() {
        ArrayList<Module> modulesToggled = null;
        for (Module module : getModules()) {
            if (module.isToggled()) {
                modulesToggled.add(module);
            }
        }
        return modulesToggled;

    }

    public static <T extends Module> Module getModule(Class<T> clas) {
        return getModules().stream().filter(module -> module.getClass() == clas).findFirst().orElse(null);
    }

    public static <T extends Module> Module getModuleByName(String module) {
        for (Module m : getModules()) {
            if (m.getName().equalsIgnoreCase(module)) {
                return m;
            }
        }
        return null;
    }
}
