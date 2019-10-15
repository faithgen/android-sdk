package net.faithgen.sdk.singletons;

import net.faithgen.sdk.SDK;
import net.faithgen.sdk.models.Config;
import net.faithgen.sdk.models.Ministry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MinistrySingleton {
    private static final MinistrySingleton ourInstance = new MinistrySingleton();
    private Ministry ministry;
    private Config config;

    public  static MinistrySingleton getInstance() {
        return ourInstance;
    }

    private MinistrySingleton() {
        BufferedReader reader = null;
        StringBuilder text = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(SDK.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        this.ministry = GSONSingleton.getInstance().getGson().fromJson(text.toString(), Ministry.class);
        this.config = GSONSingleton.getInstance().getGson().fromJson(text.toString(), Config.class);
    }

    public Ministry getMinistry() {
        return ministry;
    }

    public Config getConfig() {
        return config;
    }
}
