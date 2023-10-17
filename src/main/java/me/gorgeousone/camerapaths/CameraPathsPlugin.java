package me.gorgeousone.camerapaths;

import me.gorgeousone.camerapaths.animation.AnimationHandler;
import me.gorgeousone.camerapaths.cmdframework.command.ParentCommand;
import me.gorgeousone.camerapaths.cmdframework.handler.CommandHandler;
import me.gorgeousone.camerapaths.commands.KeyframeCommand;
import me.gorgeousone.camerapaths.commands.StartCommand;
import me.gorgeousone.camerapaths.commands.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CameraPathsPlugin extends JavaPlugin {

    private SessionHandler sessionHandler;
    private AnimationHandler animationHandler;
    
    @Override
    public void onEnable() {
        sessionHandler = new SessionHandler(this);
        animationHandler = new AnimationHandler(this);
        registerCommands();
    }

    private void registerCommands() {
        ParentCommand cameraCmd = new ParentCommand("cam");
        cameraCmd.addChild(new KeyframeCommand(sessionHandler));
        cameraCmd.addChild(new StartCommand(sessionHandler, animationHandler));
        cameraCmd.addChild(new TestCommand(sessionHandler));
        
        CommandHandler cmdHandler = new CommandHandler(this);
        cmdHandler.registerCommand(cameraCmd);
    }
}
