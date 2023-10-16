package me.gorgeousone.camerapaths;

import me.gorgeousone.camerapaths.cmdframework.command.ParentCommand;
import me.gorgeousone.camerapaths.cmdframework.handler.CommandHandler;
import me.gorgeousone.camerapaths.commands.KeyframeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CameraPathsPlugin extends JavaPlugin {

    private SessionHandler sessionHandler;
    
    @Override
    public void onEnable() {
        sessionHandler = new SessionHandler(this);
        registerCommands();
    }

    private void registerCommands() {
        ParentCommand cameraCmd = new ParentCommand("cam");
        cameraCmd.addChild(new KeyframeCommand(sessionHandler));
        
        CommandHandler cmdHandler = new CommandHandler(this);
        cmdHandler.registerCommand(cameraCmd);
    }
}
