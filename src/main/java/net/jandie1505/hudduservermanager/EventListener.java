package net.jandie1505.hudduservermanager;

import net.jandie1505.servermanager.events.EasyListener;
import net.jandie1505.servermanager.events.events.LogEvent;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;

public class EventListener extends EasyListener {
    private ServermanagerHuddu plugin;

    public EventListener(ServermanagerHuddu plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLog(LogEvent event) {
        try {
            JSONObject logEntry = new JSONObject();

            logEntry.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(event.getTimestamp()));
            logEntry.put("content", event.getContent());

            this.plugin.getHudduClient().report(logEntry);
        } catch (Exception e) {
            this.plugin.getPluginHandler().logError("Error while logging");
            this.plugin.getPluginHandler().disablePlugin();
        }
    }
}
