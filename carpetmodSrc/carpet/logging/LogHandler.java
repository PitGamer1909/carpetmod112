package carpet.logging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import carpet.CarpetServer;
import carpet.utils.HUDController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;

public abstract class LogHandler
{
    
    public static final LogHandler CHAT = new LogHandler()
    {
        @Override
        public void handle(EntityPlayerMP player, ITextComponent[] message, Object... commandParams)
        {
            Arrays.stream(message).forEach(player::sendMessage);
        }
    };
    public static final LogHandler HUD = new LogHandler()
    {
        @Override
        public void handle(EntityPlayerMP player, ITextComponent[] message, Object... commandParams)
        {
            for (ITextComponent m : message)
                HUDController.addMessage(player, m);
        }

        @Override
        public void onRemovePlayer(String playerName)
        {
            EntityPlayerMP player = CarpetServer.minecraft_server.getPlayerList().getPlayerByUsername(playerName);
            if (player != null)
                HUDController.clear_player(player);
        }
    };

    private static final Map<String, LogHandler> REGISTRY = new HashMap<>();
    
    static
    {
        register("chat", CHAT);
        register("hud", HUD);
    }
    
    private static void register(String name, LogHandler handler)
    {
        REGISTRY.put(name, handler);
    }
    
    public static LogHandler getHandlerByName(String name)
    {
        return REGISTRY.get(name);
    }
    
    public static List<String> getHandlerNames()
    {
        return REGISTRY.keySet().stream().sorted().collect(Collectors.toList());
    }
    
    public abstract void handle(EntityPlayerMP player, ITextComponent[] message, Object... commandParams);
    
    public void onAddPlayer(String playerName) {}
    
    public void onRemovePlayer(String playerName) {}
    
}
