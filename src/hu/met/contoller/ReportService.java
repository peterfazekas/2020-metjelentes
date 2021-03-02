package hu.met.contoller;

import hu.met.model.domain.Report;
import hu.met.model.domain.ReportTime;
import hu.met.model.service.FileWriter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {

    private final List<Report> reports;
    private final FileWriter fileWriter;

    public ReportService(List<Report> reports, FileWriter fileWriter) {
        this.reports = reports;
        this.fileWriter = fileWriter;
    }

    /**
     * 2. feladat: Kérje be a felhasználótól egy város kódját!
     * Adja meg, hogy az adott városból mikor érkezett az utolsó mérési adat!
     * A kiírásban az időpontot óó:pp formátumban jelenítse meg!
     */
    public String getLastReportTimeFromSettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .max(Comparator.comparing(Report::getReportTime))
                .map(Report::getReportTime)
                .map(ReportTime::toString)
                .get();
    }

    /**
     * 3. feladat: Határozza meg, hogy a nap során mikor mérték a
     * legalacsonyabb és a legmagasabb hőmérsékletet! Jelenítse meg a
     * méréshez kapcsolódó település nevét, az időpontot és  a hőmérsékletet!
     * Amennyiben több legnagyobb vagy legkisebb érték van, akkor elég
     * az egyiket kiírnia.
     */
    public String getLowestTemperatureReport() {
        return reports.stream()
                .min(Comparator.comparing(Report::getTemperature))
                .map(Report::toString)
                .get();
    }

    public String getHighestTemperatureReport() {
        return reports.stream()
                .max(Comparator.comparing(Report::getTemperature))
                .map(Report::toString)
                .get();
    }

    /**
     * 4. feladat: Határozza meg, azokat a településeket és időpontokat,
     * ahol és amikor a mérések idején szélcsend volt!
     * (A szélcsendet a táviratban 00000 kóddal jelölik.) Ha nem volt ilyen,
     * akkor a „Nem volt szélcsend a mérések idején.” szöveget írja ki!
     * A kiírásnál a település kódját és az időpontot jelenítse meg.
     */
    public String getCalmReportDetails() {
        List<Report> calmReports = getCalmReports();
        return calmReports.isEmpty()
                ? "Nem volt szélcsend a mérések idején."
                : printCalmReports(calmReports);
    }

    private String printCalmReports(List<Report> calmReports) {
        return calmReports.stream()
                .map(Report::getSettlementWithReportTime)
                .collect(Collectors.joining("\n"));
    }

    private List<Report> getCalmReports() {
        return reports.stream()
                .filter(Report::isCalm)
                .collect(Collectors.toList());
    }

    /**
     * 5. feladat: Határozza meg a települések napi középhőmérsékleti adatát
     * és a hőmérséklet-ingadozását! A kiírásnál a település kódja szerepeljen
     * a sor elején a minta szerint! A kiírásnál csak a megoldott
     * feladatrészre vonatkozó szöveget és értékeket írja ki!
     */
    public String getTemperaturesBySettlement() {
        return getSettlements().stream()
                .map(this::getTemperatureBySettlement)
                .collect(Collectors.joining("\n"));
    }

    private String getTemperatureBySettlement(String settlement) {
        return String.format("%s %s; %s",
                settlement,
                getAverageTemperatureBySettlement(settlement),
                getTemperatureFluctuationBySettlement(settlement));
    }
    /**
     * 5. a feladat: A középhőmérséklet azon hőmérsékleti adatok átlaga,
     * amikor a méréshez tartozó óra értéke 1., 7., 13., 19.
     * Ha egy településen a felsorolt órák valamelyikén nem volt mérés,
     * akkor a kiírásnál az „NA” szót jelenítse meg! Az adott órákhoz tartozó
     * összes adat átlagaként határozza meg a középhőmérsékletet, azaz minden
     * értéket azonos súllyal vegyen figyelembe!
     * A középhőmérsékletet egészre kerekítve jelenítse meg!
     */
    private String getAverageTemperatureBySettlement(String settlement) {
        return hasAllReportsTemperatureBySettlement(settlement)
                ? String.format("Középhőmérséklet: %d",
                    countAverageTemperatureBySettlement(settlement))
                : "NA";
    }

    private boolean hasAllReportsTemperatureBySettlement(String settlement) {
        return ReportTime.REPORT_HOURS.size() ==
                getReportsBySettlement(settlement).stream()
                        .map(Report::getReportTime)
                        .map(ReportTime::getHour)
                        .distinct()
                        .count();
    }

    private long countAverageTemperatureBySettlement(String settlement) {
        return Math.round(getReportsBySettlement(settlement).stream()
                .mapToInt(Report::getTemperature)
                .average()
                .getAsDouble());
    }


    private List<Report> getReportsBySettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .filter(Report::isReportHour)
                .collect(Collectors.toList());
    }

    /**
     * 5.b feladat: A hőmérséklet-ingadozás számításhoz az adott településen
     * a napi legmagasabb és legalacsonyabb hőmérséklet különbségét kell
     * kiszámítania!
     */
    private String getTemperatureFluctuationBySettlement(String settlement) {
        int temperatureFluctuation =
                getHighestTemperatureBySettlement(settlement) -
                        getLowestTemperatureBySettlement(settlement);
        return String.format("Hőmérséklet-ingadozás: %d",
                temperatureFluctuation);
    }


    private int getLowestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .mapToInt(Report::getTemperature)
                .min()
                .getAsInt();
    }

    private int getHighestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .mapToInt(Report::getTemperature)
                .max()
                .getAsInt();
    }

    /**
     * 6. feladat: Hozzon létre településenként egy szöveges állományt,
     * amely első sorában a település kódját tartalmazza!
     * A további sorokban a mérési időpontok és a hozzá tartozó szélerősségek
     * jelenjenek meg! A szélerősséget a minta szerint a számértéknek
     * megfelelő számú kettőskereszttel (#) adja meg!
     * A fájlban az időpontokat és a szélerősséget megjelenítő
     * kettőskereszteket szóközzel válassza el egymástól!
     * A fájl neve X.txt legyen, ahol az X helyére a település kódja kerüljön!
     */
    public String writeWindReportsBySettlements() {
        getSettlements().forEach(settlement -> {
            List<String> report = getWindReportDetailsBySettlement(settlement);
            report.add(0, settlement);
            fileWriter.write(createFilename(settlement), report);
        });
        return "A fájlok elkészültek.";
    }

    private List<String> getWindReportDetailsBySettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .map(Report::getWindForceByTime)
                .collect(Collectors.toList());
    }

    private String createFilename(String settlement) {
        return settlement + ".txt";
    }

    private List<String> getSettlements() {
        return reports.stream()
                .map(Report::getSettlement)
                .distinct()
                .collect(Collectors.toList());
    }
}
