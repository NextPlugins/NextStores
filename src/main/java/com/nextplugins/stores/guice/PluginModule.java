package com.nextplugins.stores.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.stores.NextStores;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */

@EqualsAndHashCode(callSuper = false)
@Data(staticConstructor = "from")
public class PluginModule extends AbstractModule {

    private final NextStores nextStores;

    @Override
    protected void configure() {

        bind(NextStores.class)
                .toInstance(nextStores);

        bind(Logger.class)
                .annotatedWith(Names.named("main"))
                .toInstance(nextStores.getLogger());

        bind(SQLExecutor.class)
                .toInstance(new SQLExecutor(nextStores.getSqlConnector()));

        bind(ConfigurationSection.class)
                .annotatedWith(Names.named("buttons"))
                .toInstance(nextStores.getMessagesConfig().getConfigurationSection("inventory"));

    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

}

