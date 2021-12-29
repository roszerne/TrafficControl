package com.company.Fuzzy;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
public class LightController {

    public static double getNewLight(String fileName, int arriving_vehicle, int queuing_vehicle, int fog){
        try {
            FIS fis = FIS.load(fileName, false);

            // wyswietl wykresy funkcji fuzyfikacji i defuzyfikacji
           // JFuzzyChart.get().chart(fis);

            // zadaj wartosci wejsciowe
            fis.setVariable("arriving_vehicle", arriving_vehicle);
            fis.setVariable("queuing_vehicle", queuing_vehicle);
            fis.setVariable("fog", fog);
            // logika sterownika
            fis.evaluate();
            //TERM great_many := (25, 0) (28, 0.50) (31, 1) (34, 1);
            // graficzna prezentacja wyjscia
            Variable v = fis.getVariable("green_light");
            JFuzzyChart.get().chart(v, v.getDefuzzifier(), true);
            System.out.println("GREEN: " + v.getValue());
            return v.getValue();
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out
                    .println("Niepoprawna liczba parametrow. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (NumberFormatException ex) {
            System.out
                    .println("Niepoprawny parametr. Przyklad: java FuzzyExample string<plik_fcl> int<poziom natezenia> int<pora dnia>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 1.0;
    }
}
