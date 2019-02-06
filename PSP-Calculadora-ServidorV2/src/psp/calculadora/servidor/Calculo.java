package psp.calculadora.servidor;

public class Calculo {

    String devResultado;

    public String calculo(String operacion) {

        String[] parts = operacion.split("n");
        float num1 = Float.parseFloat(parts[0]);

        float resultado = 0;

        if (parts[1].equalsIgnoreCase("√")) {
            resultado = (float) Math.sqrt(num1);
        } else {

            float num2 = Float.parseFloat(parts[2]);

            System.out.println("operacion" + operacion);

            switch (parts[1]) {
                case ("+"):
                    resultado = num1 + num2;
                    break;
                case ("-"):
                    resultado = num1 - num2;
                    break;

                case ("/"):
                    if (num2 == 0) {
                        resultado = Float.POSITIVE_INFINITY;
                    } else {
                        resultado = num1 / num2;
                    }

                    break;

                case ("*"):
                    resultado = num1 * num2;
                    break;

                default:
                    System.out.println("hola");
                    break;
            }

        }
        return devResultado = String.valueOf(resultado);

    }
}
