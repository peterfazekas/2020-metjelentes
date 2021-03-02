package hu.met;

import hu.met.contoller.ReportService;
import hu.met.model.service.*;

import java.util.Scanner;

public class App {

    private final ReportService service;
    private final Console console;

    private App() {
        DataApi dataApi = new DataApi(new FileReader(), new DataParser());
        service = new ReportService(dataApi.getData("tavirathu13.txt"),
                new FileWriter());
        console = new Console(new Scanner(System.in));
    }

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        System.out.println("2. feladat");
        System.out.print("Adja meg egy település kódját! Település: ");
        String settlement = console.read();
        System.out.println("Az utolsó mérési adat a megadott településről " +
                service.getLastReportTimeFromSettlement(settlement) +
                "-kor érkezett.");
        System.out.println("3. feladat");
        System.out.println("A legalacsonyabb hőmérséklet: " +
                service.getLowestTemperatureReport());
        System.out.println("A legmagasabb hőmérséklet: " +
                service.getHighestTemperatureReport());
        System.out.println("4. feladat");
        System.out.println(service.getCalmReportDetails());
        System.out.println("5. feladat");
        System.out.println(service.getTemperaturesBySettlement());
        System.out.println("6. feladat");
        System.out.println(service.writeWindReportsBySettlements());
    }
}
