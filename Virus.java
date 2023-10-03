import java.util.Random;

public class Virus {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Virus <Infektiösität> <Feldgröße> <Debug>");
            return;
        }

        double infektiositaet = Double.parseDouble(args[0]);
        int feldgroesse = Integer.parseInt(args[1]);
        int debug = Integer.parseInt(args[2]);

        int[][] welt = new int[feldgroesse][feldgroesse];
        int[][] verbleibendenTage = new int[feldgroesse][feldgroesse];

        Random random = new Random();

        /* Zufällige Platzierung des ersten infizierten Feldes */
        int startX = random.nextInt(feldgroesse);
        int startY = random.nextInt(feldgroesse);
        welt[startX][startY] = 1; /* Der Virus beginnt auf einem zufälligen Feld */
        verbleibendenTage[startX][startY] = random.nextInt(5) + 3; // 3 ile 7 gün arasında

        int vergangeneTage = 0;

        while (infektionMoeglich(welt)) { /* Solange es eine zelle gibt die nicht infiziert ist: */
            vergangeneTage++;

            if (vergangeneTage > 1) {
                for (int x = 0; x < feldgroesse; x++) {
                    for (int y = 0; y < feldgroesse; y++) {

                        // Die Zelle ist infiziert, aktualisiere die Infektionsdauer
                        if (welt[x][y] == 1) {
                            verbleibendenTage[x][y]--;
                            if (verbleibendenTage[x][y] >= 3 && verbleibendenTage[x][y] <= 7) {
                                welt[x][y] = -1; // Genesen
                            }
                            // Übertrage das Virus auf Nachbarfelder
                            /* Nach links von hier: */
                            if (x > 0 && welt[x - 1][y] == 0 && random.nextDouble() < infektiositaet) {
                                welt[x - 1][y] = 1;
                                verbleibendenTage[x - 1][y] = random.nextInt(5) + 3;
                            }
                            // Fügen Sie hier die Übertragung auf andere Nachbarfelder hinzu (rechts,
                            // unten,
                            // oben).
                            /* Nach rechts von hier: */
                            if (x < feldgroesse - 1 && welt[x + 1][y] == 0 && random.nextDouble() < infektiositaet) {
                                welt[x + 1][y] = 1;
                                verbleibendenTage[x + 1][y] = random.nextInt(5) + 3;
                            }
                            //
                            /* Nach oben von hier: */
                            if (y > 0 && welt[x][y - 1] == 0 && random.nextDouble() < infektiositaet) {
                                welt[x][y - 1] = 1;
                                verbleibendenTage[x][y - 1] = random.nextInt(5) + 3;
                            }
                            /* Nach unten von hier: */
                            if (y < feldgroesse - 1 && welt[x][y + 1] == 0 && random.nextDouble() < infektiositaet) {
                                welt[x][y + 1] = 1;
                                verbleibendenTage[x][y + 1] = random.nextInt(5) + 3;
                            }
                        }
                    }
                }

            }

            if (debug == 1) {
                printMap(welt, verbleibendenTage, vergangeneTage);
                printMapDebug(welt, verbleibendenTage, vergangeneTage);
            } else {
                printMap(welt, verbleibendenTage, vergangeneTage);
            }
        }
    }

    private static boolean infektionMoeglich(int[][] welt) {
        for (int[] zeile : welt) {
            for (int zell : zeile) {
                if (zell == 0) {
                    return true; /* Es wurde nicht infizierte Zelle gefunden */
                }
            }
        }
        return false; /* Alle Zellen wurden infiziert */
    }

    private static void printMap(int[][] welt, int[][] verbleibendenTage, int vergangeneTage) {
        System.out.println("--------- Map ---------");
        for (int i = 0; i < welt.length; i++) {
            for (int j = 0; j < welt[i].length; j++) {
                if (welt[i][j] == 1) {
                    System.out.print("* "); /* infektiös */
                } else if (verbleibendenTage[i][j] > 0) {
                    System.out.print("# "); /* genesen */
                } else {
                    System.out.print("- "); /* noch nicht infiziert */
                }
            }
            System.out.println();
        }
        System.out.println("--------- Informationen ---------");
        System.out.println("Taegliche Inzidenz: " + BerechneTaeglicheInzidenz(welt));
        System.out.println("Tag: " + vergangeneTage);

    }

    private static void printMapDebug(int[][] welt, int[][] verbleibendenTage, int vergangeneTage) {
        System.out.println("--------- Debug Modus ---------");
        for (int i = 0; i < welt.length; i++) {
            for (int j = 0; j < welt[i].length; j++) {
                System.out.print(verbleibendenTage[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("--------- Informationen ---------");
        System.out.println("Taegliche Inzidenz: " + BerechneTaeglicheInzidenz(welt));
        System.out.println("Tag: " + vergangeneTage);
    }

    private static double BerechneTaeglicheInzidenz(int[][] welt) {
        int totalInfektionen = 0;
        for (int[] zeile : welt) {
            for (int zell : zeile) {
                if (zell == 1) {
                    totalInfektionen++;
                }
            }
        }
        return totalInfektionen * 100000.0 / (welt.length * welt[0].length); /* Inzidenz pro 100.000 Menschen */
    }

}
