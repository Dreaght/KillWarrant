package org.dreaght.killwarrant.utils;

import org.dreaght.killwarrant.config.ConfigManager;
import org.dreaght.killwarrant.config.SettingsConfig;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EcoTransactionsTest {
    @Mock
    private ConfigManager configManagerMock;

    @Test
    public void testCalculateFinalAward() {
        ConfigManager configManagerMock = mock(ConfigManager.class);
        SettingsConfig settingsConfigMock = mock(SettingsConfig.class);
        when(configManagerMock.getSettingsConfig()).thenReturn(settingsConfigMock);
        when(settingsConfigMock.getMaxOrderTime()).thenReturn(60L);

        LocalDateTime dateTime1 = LocalDateTime.of(2023, 11, 25, 10, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 11, 25, 10, 30);

        double actualResult = EcoTransactions.calculateFinalAward(dateTime1, dateTime2, 100, configManagerMock);
        assertEquals(50, actualResult);

        verify(configManagerMock).getSettingsConfig();
        verify(settingsConfigMock).getMaxOrderTime();
    }
}