package com.github.bunnyi116.bedrockminer.command;

import com.github.bunnyi116.bedrockminer.BedrockMiner;
import com.github.bunnyi116.bedrockminer.Test;
import com.github.bunnyi116.bedrockminer.command.commands.BehaviorCommand;
import com.github.bunnyi116.bedrockminer.command.commands.DebugCommand;
import com.github.bunnyi116.bedrockminer.command.commands.DisableCommand;
import com.github.bunnyi116.bedrockminer.command.commands.TaskCommand;
import com.github.bunnyi116.bedrockminer.task.TaskManager;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.ArrayList;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandManager {
    private static final ArrayList<CommandBase> commands;

    static {
        commands = new ArrayList<>();
        commands.add(new BehaviorCommand());
        commands.add(new DebugCommand());
        commands.add(new TaskCommand());
        commands.add(new DisableCommand());
    }

    private static String getCommandPrefix() {
        return BedrockMiner.COMMAND_PREFIX;
    }

    public static void init() {
        // 开始注册
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // 子命令
            for (var command : commands) {
                command.register(dispatcher, registryAccess);
            }
            // 主命令执行
            var root = literal(getCommandPrefix()).executes(CommandManager::executes);
            // 测试命令
            if (BedrockMiner.TEST) {
                Test.register(root);
            }
            dispatcher.register(root);
        });
    }

    private static int executes(CommandContext<FabricClientCommandSource> context) {
        TaskManager.setWorking(!TaskManager.isWorking());
        return 0;
    }


}
