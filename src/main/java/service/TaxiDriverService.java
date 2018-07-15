package service;

import callable.TaxiThread;
import com.google.common.collect.Lists;
import message.IMessage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class TaxiDriverService {
    private static final List<ExecutorService> TAXI_DRIVERS = Lists.newArrayList();
    private static int totalTaxiDrivers;
    private static TaxiDriverService instance;

    private TaxiDriverService() {
    }

    public static TaxiDriverService get() {
        return instance;
    }

    /**
     * конфигурирует сервис.
     * @param taxiDrivers количество таксистов
     */
    public static void configureService(final int taxiDrivers) {
        if (instance == null) {
            totalTaxiDrivers = taxiDrivers;
            instance = new TaxiDriverService();

            init();
        }
    }

    /**
     * инициализирует список таксистов.
     */
    private static void init() {
        for (int i = 0; i < totalTaxiDrivers; i++) {
            final int num = i;
            TAXI_DRIVERS.add(Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "такси-" + num);
                }
            }));
        }
    }

    /**
     * Запускает на исполнение тред по указанному ID с указанным сообщением.
     * @param taxiDriverId номер сингл тред экзекутора
     * @param message сообщение для обработки
     */
    public void submitMessageToTaxiDriverById(final int taxiDriverId, final IMessage message) {
        if (taxiDriverId <= 0 || taxiDriverId > totalTaxiDrivers) {
            throw new IllegalArgumentException("Illegal taxi driver ID. It should be between 1 and " + totalTaxiDrivers);
        }

        TAXI_DRIVERS.get(taxiDriverId - 1).submit(new TaxiThread(message));
    }
}
