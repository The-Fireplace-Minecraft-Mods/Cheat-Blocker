package the_fireplace.cheatblocker.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import the_fireplace.cheatblocker.CheatBlocker;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {
    private static HashMap<UUID, PlayerData> playerData = Maps.newHashMap();
    private static final File playerDataLocation = new File(CheatBlocker.getMinecraftHelper().getServer().getWorld(0).getSaveHandler().getWorldDirectory(), "cheatblocker/player");

    //region getters
    //public static boolean getIgnoresCensor(UUID player) {
    //    return getPlayerData(player).ignoresCensor;
    //}
    //endregion

    //region saved data setters
    //public static void setIgnoresCensor(UUID player, boolean ignoresCensor) {
    //    getPlayerData(player).setIgnoresCensor(ignoresCensor);
    //}
    //endregion

    //region cached data setters
    public static void setShouldDisposeReferences(UUID player, boolean shouldDisposeReferences) {
        getPlayerData(player).shouldDisposeReferences = shouldDisposeReferences;
    }
    //endregion

    //region getPlayerData
    private static PlayerData getPlayerData(UUID player) {
        if(!playerData.containsKey(player))
            playerData.put(player, new PlayerData(player));
        return playerData.get(player);
    }
    //endregion

    //region save
    public static void save() {
        for(Map.Entry<UUID, PlayerData> entry : Sets.newHashSet(playerData.entrySet())) {
            entry.getValue().save();
            if(entry.getValue().shouldDisposeReferences)
                playerData.remove(entry.getKey());
        }
    }
    //endregion

    private static class PlayerData {
        //region Internal variables
        private File playerDataFile;
        private boolean isChanged, saving, shouldDisposeReferences = false;
        //endregion

        //region Saved variables
        //TODO
        //endregion

        //region Constructor
        private PlayerData(UUID playerId) {
            playerDataFile = new File(playerDataLocation, playerId.toString()+".json");
            load(playerId);
        }
        //endregion

        //region load
        private void load(UUID playerId) {
            if(!playerDataLocation.exists()) {
                playerDataLocation.mkdirs();
                return;
            }

            JsonParser jsonParser = new JsonParser();
            try {
                Object obj = jsonParser.parse(new FileReader(playerDataFile));
                if(obj instanceof JsonObject) {
                    JsonObject jsonObject = (JsonObject) obj;
                    //ignoresCensor = jsonObject.has("ignoresCensor") && jsonObject.getAsJsonPrimitive("ignoresCensor").getAsBoolean();
                    //TODO
                }
            } catch (FileNotFoundException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //endregion

        //region save
        private void save() {
            if(!isChanged || saving)
                return;
            saving = true;
            new Thread(() -> {
                JsonObject obj = new JsonObject();
                //obj.addProperty("ignoresCensor", ignoresCensor);
                //TODO

                try {
                    FileWriter file = new FileWriter(playerDataFile);
                    file.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saving = isChanged = false;
            }).start();
        }
        //endregion

        //region setters
        //TODO
        //public void setIgnoresCensor(boolean ignoresCensor) {
        //    if(this.ignoresCensor != ignoresCensor) {
        //        this.ignoresCensor = ignoresCensor;
        //        isChanged = true;
        //    }
        //}
        //endregion
    }
}
