import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;

public class Main extends JFrame implements ActionListener {
    private JTextField display;
    private String numero1 = "", numero2 = "", operador = "";

    private int tamanhoFonte = 18;
    private final int TAMANHO_MIN = 12;
    private final int TAMANHO_MAX = 36;

    private List<JButton> botoes = new ArrayList<>();

    private final DecimalFormat formato = new DecimalFormat("#,##0.##", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public Main() {
        setTitle("Calculadora Simples");
        setSize(320, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza na tela

        // Campo de texto (display)
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        add(display, BorderLayout.NORTH);

        // Painel principal com os botões numéricos e operacionais
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));

        String[] botoesPrincipais = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", ".", "+"
        };

        for (String texto : botoesPrincipais) {
            JButton btn = new JButton(texto);
            btn.addActionListener(this);
            btn.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
            botoes.add(btn);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);

        // Painel inferior com A-, A+, e =
        JPanel painelInferior = new JPanel(new GridLayout(1, 3, 5, 5));

        JButton btnMenorFonte = new JButton("A-");
        JButton btnMaiorFonte = new JButton("A+");
        JButton btnIgual = new JButton("=");

        JButton[] especiais = {btnMenorFonte, btnMaiorFonte, btnIgual};

        for (JButton btn : especiais) {
            btn.addActionListener(this);
            btn.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
            botoes.add(btn);
            painelInferior.add(btn);
        }

        add(painelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("0123456789.".contains(cmd)) {
            if (operador.isEmpty()) {
                numero1 += cmd;
                display.setText(numero1);
            } else {
                numero2 += cmd;
                display.setText(numero1 + operador + numero2);
            }
        } else if ("+-*/".contains(cmd)) {
            if (numero1.isEmpty()) return;
            if (!numero2.isEmpty()) {
                double resultado = calcular();
                numero1 = Double.toString(resultado);
                numero2 = "";
            }
            operador = cmd;
            display.setText(numero1 + operador);
        } else if (cmd.equals("=")) {
            if (numero1.isEmpty() || operador.isEmpty() || numero2.isEmpty()) return;
            double resultado = calcular();
            String formatado = formato.format(resultado);
            display.setText(formatoParcial(numero1) + operador + formatoParcial(numero2) + "=" + formatado);
            numero1 = Double.toString(resultado);
            numero2 = "";
            operador = "";
        } else if (cmd.equals("C")) {
            numero1 = "";
            numero2 = "";
            operador = "";
            display.setText("");
        } else if (cmd.equals("A+")) {
            if (tamanhoFonte < TAMANHO_MAX) {
                tamanhoFonte += 2;
                atualizarFontes();
            }
        } else if (cmd.equals("A-")) {
            if (tamanhoFonte > TAMANHO_MIN) {
                tamanhoFonte -= 2;
                atualizarFontes();
            }
        }
    }

    private double calcular() {
        try {
            double n1 = Double.parseDouble(numero1.replace(",", "."));
            double n2 = Double.parseDouble(numero2.replace(",", "."));
            return switch (operador) {
                case "+" -> n1 + n2;
                case "-" -> n1 - n2;
                case "*" -> n1 * n2;
                case "/" -> n2 != 0 ? n1 / n2 : 0;
                default -> 0;
            };
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatoParcial(String valor) {
        try {
            double n = Double.parseDouble(valor.replace(",", "."));
            return formato.format(n);
        } catch (Exception e) {
            return valor;
        }
    }

    private void atualizarFontes() {
        Font novaFonte = new Font("Arial", Font.PLAIN, tamanhoFonte);
        display.setFont(novaFonte);
        for (JButton botao : botoes) {
            botao.setFont(novaFonte);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        new Main();
    }
}
