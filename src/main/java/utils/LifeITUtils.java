package utils;

import bean.PhoneCall;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import message.IMessage;
import message.Message;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class LifeITUtils {

    private static final XStream XSTREAM = new XStream(new DomDriver());
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private LifeITUtils() {
    }

    public static Map<String, PhoneCall> generateInitialCallXml(final int totalCalls) throws IOException {

        File tempDir = FileUtils.getTempDirectory();
        File xmlFolder = new File(tempDir.getAbsolutePath() + File.separator + "messages");
        if (!xmlFolder.exists()) {
            xmlFolder.mkdirs();
        }

        File[] allFiles = xmlFolder.listFiles();
        if (allFiles != null && allFiles.length != 0) {
            List<File> xmlFiles = Arrays.stream(allFiles).filter(file -> Files.getFileExtension(file.getName()).equals("xml")).collect(Collectors.toList());
            return extractMessagesFromFiles(xmlFiles, totalCalls);

        } else {
            return createXmlFileAndReturnMessages(xmlFolder, totalCalls);
        }

    }

    private static Map<String, PhoneCall> extractMessagesFromFiles(final List<File> files, final int filesNeeded) {
        Map<String, PhoneCall> resultMap = Maps.newHashMap();

        for (int i = 0; i < filesNeeded; i++) {
            if (files.size() - 1 < i) {
                break;
            }

            Object obj = XSTREAM.fromXML(files.get(i));
            if (obj instanceof Message) {
                String callerId = "callerId-" + (i + 1);
                resultMap.put(callerId, new PhoneCall(callerId, (Message) obj));
            }
        }

        return resultMap;
    }

    private static Map<String, PhoneCall> createXmlFileAndReturnMessages(final File parentFolder, final int nXmlFiles) throws IOException {
        Map<String, PhoneCall> resultMap = Maps.newHashMap();

        Random r = new Random();
        for (int i = 0; i < nXmlFiles; i++) {
            int randomTaxiDriverId = r.nextInt(11);
            String callerId = "callerId-" + (i + 1);
            resultMap.put(callerId, new PhoneCall(callerId, new Message(randomTaxiDriverId)));
            XSTREAM.toXML(new Message(randomTaxiDriverId), new FileWriter(new File(parentFolder.getAbsolutePath() + File.separator + "xml_" + i + ".xml")));
        }

        return resultMap;
    }

    /**
     * записывает сообщение в виде xml в файл в указанной папке.
     * @param folderName папка в которую записывается файл
     * @param message сохраняемый в виде xml объект
     * @throws IOException
     */
    public static void saveMessage(final String folderName, final IMessage message) throws IOException {

        File tempDir = FileUtils.getTempDirectory();
        File taxiDriverFolder = new File(tempDir + File.separator + "taxi_drivers");
        if (!taxiDriverFolder.exists()) {
            taxiDriverFolder.mkdirs();
        }

        File currentTaxiDriverFolder = new File(taxiDriverFolder + File.separator + folderName);
        if (!currentTaxiDriverFolder.exists()) {
            currentTaxiDriverFolder.mkdirs();
        }

        File currentMessageXml = new File(currentTaxiDriverFolder + File.separator + message.getDispatched() + ".xml");
        XSTREAM.toXML(message, new FileWriter(currentMessageXml));
    }

    public static void clearFolders() throws IOException {
        File tempDir = FileUtils.getTempDirectory();
        File taxiDriverFolder = new File(tempDir + File.separator + "taxi_drivers");
        FileUtils.cleanDirectory(taxiDriverFolder);
    }

    /**
     * одно логирование на всех.
     * @param level уровень логирования
     * @param message сообщения лога
     */
    public static void log(final Level level, final String message) {
        LOGGER.log(level, message);
    }
}