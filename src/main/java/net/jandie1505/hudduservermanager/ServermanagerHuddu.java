package net.jandie1505.hudduservermanager;

import com.huddu.ApiClient;
import net.jandie1505.servermanager.plugins.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class ServermanagerHuddu extends Plugin {

    private ApiClient hudduClient;

    @Override
    public void onEnable() {
        if (!this.getPluginHandler().getPluginDirectory().isDirectory()) {
            this.getPluginHandler().getPluginDirectory().mkdir();
        }

        File file = new File(this.getPluginHandler().getPluginDirectory(), "config.json");

        JSONObject config = new JSONObject();
        config.put("projectId", "");
        config.put("streamId", "");
        config.put("token", "");

        try {
            if (!file.exists()) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write(config.toString(4));
                writer.flush();
                writer.close();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }

                String out = sb.toString();

                JSONObject configFile = new JSONObject(out);

                try {
                    config.put("projectId", configFile.getString("projectId"));
                } catch (JSONException ignored) {
                    // ignored
                }

                try {
                    config.put("streamId", configFile.getString("streamId"));
                } catch (JSONException ignored) {
                    // ignored
                }

                try {
                    config.put("token", configFile.getString("token"));
                } catch (JSONException ignored) {
                    // ignored
                }
            }
        } catch (IOException e) {
            this.getPluginHandler().logWarning("Cannot read/write config file");
            this.getPluginHandler().disablePlugin();
            return;
        }

        if (!config.has("projectId") || config.getString("projectId").equalsIgnoreCase("") || !config.has("streamId") || config.getString("streamId").equalsIgnoreCase("")) {
            this.getPluginHandler().disablePlugin();
            return;
        }

        try {
            if (config.has("token") && !config.getString("token").equalsIgnoreCase("")) {
                this.hudduClient = new ApiClient(config.getString("projectId"), config.getString("streamId"), config.getString("token"));
            } else {
                this.hudduClient = new ApiClient(config.getString("projectId"), config.getString("streamId"));
            }
        } catch (Exception e) {
            this.getPluginHandler().logError("Error while creating ApiClient");
            this.getPluginHandler().disablePlugin();
            return;
        }

        this.getPluginHandler().addSMEventListener(new EventListener(this));

        this.getPluginHandler().logInfo("Successfully enabled Servermanager-Huddu");
    }

    public ApiClient getHudduClient() {
        return this.hudduClient;
    }
}
