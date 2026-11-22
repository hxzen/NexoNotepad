package br.nexo.util;

import java.util.Random;

/**
 * Utilitário de cores em formato hexadecimal ("#RRGGBB").
 * Não usa JavaFX, então pode ser chamado pelo service e pelo banco também.
 *
 *  - gerarAleatoria(): sorteia uma cor viva (nem clara demais, nem escura demais).
 *  - corDoTexto(fundo): devolve "#FFFFFF" ou "#000000", a que der MAIS contraste
 *    com o fundo, para o texto da etiqueta ficar sempre legível.
 */
public class Cores {

    /** Cor usada quando não há categoria (ou a categoria ainda não tem cor). */
    public static final String CINZA = "#9E9E9E";

    private static final Random ALEATORIO = new Random();

    /** Sorteia uma cor viva: matiz qualquer, saturação 55–90%, luminosidade 35–70%. */
    public static String gerarAleatoria() {
        double matiz = ALEATORIO.nextDouble() * 360;
        double saturacao = 0.55 + ALEATORIO.nextDouble() * 0.35;
        double luminosidade = 0.35 + ALEATORIO.nextDouble() * 0.35;
        return hslParaHex(matiz, saturacao, luminosidade);
    }

    /** Devolve branco ou preto, o que tiver melhor contraste com a cor de fundo. */
    public static String corDoTexto(String fundoHex) {
        int[] rgb = hexParaRgb(fundoHex);
        double l = luminanciaRelativa(rgb[0], rgb[1], rgb[2]);

        double contrasteComBranco = 1.05 / (l + 0.05);
        double contrasteComPreto = (l + 0.05) / 0.05;
        return contrasteComBranco >= contrasteComPreto ? "#FFFFFF" : "#000000";
    }

    // ------------------------------------------------------------------

    private static int[] hexParaRgb(String hex) {
        try {
            String h = hex.startsWith("#") ? hex.substring(1) : hex;
            return new int[]{
                    Integer.parseInt(h.substring(0, 2), 16),
                    Integer.parseInt(h.substring(2, 4), 16),
                    Integer.parseInt(h.substring(4, 6), 16)
            };
        } catch (RuntimeException e) {
            return new int[]{158, 158, 158}; // cor inválida: trata como cinza
        }
    }

    /** Luminância relativa (fórmula do WCAG): 0 = preto, 1 = branco. */
    private static double luminanciaRelativa(int r, int g, int b) {
        return 0.2126 * linear(r) + 0.7152 * linear(g) + 0.0722 * linear(b);
    }

    private static double linear(int valor) {
        double c = valor / 255.0;
        return c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    private static String hslParaHex(double h, double s, double l) {
        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = l - c / 2;

        double r, g, b;
        if (h < 60)       { r = c; g = x; b = 0; }
        else if (h < 120) { r = x; g = c; b = 0; }
        else if (h < 180) { r = 0; g = c; b = x; }
        else if (h < 240) { r = 0; g = x; b = c; }
        else if (h < 300) { r = x; g = 0; b = c; }
        else              { r = c; g = 0; b = x; }

        return String.format("#%02X%02X%02X",
                Math.round((r + m) * 255),
                Math.round((g + m) * 255),
                Math.round((b + m) * 255));
    }
}