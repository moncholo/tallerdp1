package ar.uba.fi.taller2.mensajerocliente.InfoCiudades;

/**
 * Created by matias on 19/11/15.
 */
public class ParisInfo {

    public static String[] getMuseos(){
        String[] museosArray = {"Museo del Louvre", "Museo de Orsay", "Centro Pompidou", "Museo du Quai Branly", "Museo Rodin"};
        return museosArray;
    }

    public static String[] getRestaurantes(){
        String[] restArray = {"Epicure", "Seb'on", "Paris Picnic", "Pur' - Jean-François Rouquette", "Le Cinq", "Merguez & Pastrami"};
        return restArray;
    }

    public static String[] getBares(){
        String[] baresArray ={"La Serena", "Lizard Lounge", "El Prado", "La Palette", "Harry's New York Bar", "Bar 8", "Brewberry Cave à Bières"};
        return baresArray;
    }

    public static String[] getMonumetos(){
        String[] monumetosArray ={"Torre Eiffel", "Catedral de Notre Dame", "Montmartre", "Arco de Triunfo de París", "Basílica del Sagrado Corazón", "Jardines de Luxemburgo"};
        return monumetosArray;
    }
}
