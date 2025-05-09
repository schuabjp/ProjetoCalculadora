import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Classe principal da calculadora simples
public class Main extends JFrame implements ActionListener {
    private JTextField display;
    private String numero1 = "", numero2 = "", operador = "";

    public Main() {
        // Configuração da janela principal (frame)
        setTitle("Calculadora Simples");
        setSize(300, 400); // largura x altura
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cria campo de texto (display)
        display = new JTextField();
        display.setEditable(false); // somente leitura
        display.setHorizontalAlignment(JTextField.RIGHT); // alinha texto à direita
        add(display, BorderLayout.NORTH);

        // Cria painel para botões (grid 4x4)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5)); // 4 linhas, 4 colunas, espaçamento 5px

        // Lista de rótulos dos botões (0-9, ., C, operadores)
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", ".", "+"
        };

        // Adiciona botões numerais e operadores ao painel
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.addActionListener(this); // registra a ação no próprio frame
            panel.add(btn);
        }

        // Botão "=" é um botão especial que será colocado abaixo
        JButton botaoIgual = new JButton("=");
        botaoIgual.addActionListener(this);

        // Adiciona painel de botões no centro e botão "=" na parte inferior
        add(panel, BorderLayout.CENTER);
        add(botaoIgual, BorderLayout.SOUTH);

        setVisible(true); // torna a janela visível
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // Se for um número ou ponto decimal, atualiza o primeiro ou segundo número
        if ("0123456789.".contains(cmd)) {
            // Se ainda não foi escolhida uma operação, concatena no primeiro número
            if (operador.isEmpty()) {
                numero1 += cmd;
                display.setText(numero1);
            } else {
                // Se já existe um operador, concatena no segundo número
                numero2 += cmd;
                display.setText(numero1 + operador + numero2);
            }
        }
        // Se for um operador (exceto "=")
        else if ("+-*/".contains(cmd)) {
            // Se ainda não há primeiro número, ignora
            if (numero1.isEmpty()) return;
            // Se já há um segundo número, calcula o resultado anterior
            if (!numero2.isEmpty()) {
                double result = calcular();
                numero1 = Double.toString(result);
                numero2 = "";
            }
            operador = cmd; // define o operador atual
            display.setText(numero1 + operador);
        }
        // Se clicou em "C", limpa todos os campos
        else if (cmd.equals("C")) {
            numero1 = "";
            numero2 = "";
            operador = "";
            display.setText("");
        }
        // Se clicou em "=", calcula o resultado final
        else if (cmd.equals("=")) {
            // Verifica se há algo para calcular
            if (numero1.isEmpty() || operador.isEmpty() || numero2.isEmpty()) {
                return;
            }
            double result = calcular();
            display.setText(numero1 + operador + numero2 + "=" + result);
            // Prepara para próxima operação a partir do resultado
            numero1 = Double.toString(result);
            numero2 = "";
            operador = "";
        }
    }

    private double calcular() {
        double n1 = Double.parseDouble(numero1);
        double n2 = Double.parseDouble(numero2);
        switch (operador) {
            case "+": return n1 + n2;
            case "-": return n1 - n2;
            case "*": return n1 * n2;
            case "/": return n1 / n2;
        }
        return 0; // não deve acontecer
    }

    public static void main(String[] args) {
        // Usa o visual do sistema operacional, se possível
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se falhar, ignora
        }
        // Cria e mostra a calculadora
        new Main();
    }
}
