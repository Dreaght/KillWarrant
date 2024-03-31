package org.dreaght.killwarrant.utils;

import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.config.SettingsConfig;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParseValueTest {
    @Test
    public void objectTesting() {
        Order order = mock(Order.class);
        when(order.getAward()).thenReturn(100.0);

        ConfigManager configManager = mock(ConfigManager.class);

        SettingsConfig settingsConfig = mock(SettingsConfig.class);
        when(settingsConfig.getDecimalAwardFormat()).thenReturn("0");

        when(configManager.getSettingsConfig()).thenReturn(settingsConfig);

        DecimalFormat decimalAwardFormat = new DecimalFormat(configManager.getSettingsConfig().getDecimalAwardFormat());

        assertEquals("§7Award: §6100",
                ParseValue.parseWithBraces(
                "§7Award: §6%AWARD%",
                new String[]{"AWARD"},
                new Object[]{decimalAwardFormat.format(order.getAward())}
                ));
    }

    @Test
    public void stringTesting() {
        Order order = mock(Order.class);
        when(order.getClientName()).thenReturn("TestPlayer");

        assertEquals("§7Client: §aTestPlayer",
                ParseValue.parseWithBraces(
                        "§7Client: §a%CLIENT_NAME%",
                        new String[]{"CLIENT_NAME"},
                        new String[]{order.getClientName()}
                ));
    }
}
