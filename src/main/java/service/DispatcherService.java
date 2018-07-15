package service;

import bean.PhoneCall;
import callable.DispatcherThread;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class DispatcherService {

    private static DispatcherService instance;
    private static int clientsThreshold;
    private static ExecutorService executor;

    private DispatcherService() {

    }

    public static void configureService(final int clientsThreshold) {
        if (instance == null) {
            DispatcherService.clientsThreshold = clientsThreshold;
            instance = new DispatcherService();
            init();
        }
    }

    public static DispatcherService get() {
        return instance;
    }

    private static void init() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("оператор-%d")
                .build();
        executor = Executors.newFixedThreadPool(clientsThreshold, threadFactory);
    }

    /**
     * Обработка телефонного звонка.
     *
     * @param phoneCall звонок клиента
     * @return id звонившего клиента
     */
    public static String processSinglePhoneCall(final PhoneCall phoneCall) {
        setCallerIdIntoMessage(phoneCall);
        executor.submit(new DispatcherThread(phoneCall));
        return phoneCall.getClientId();
    }

    private static void setCallerIdIntoMessage(final PhoneCall phoneCall) {
        //странно но ладно.
        phoneCall.getMessage().setDispatched(phoneCall.getClientId());
    }
}
