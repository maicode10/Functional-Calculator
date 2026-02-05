import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FunctionalCalculator extends JFrame {

    private static final String DIV = "\u00F7";
    private static final String MUL = "\u00D7";
    private static final String MINUS = "\u2212";
    private static final String PLUS = "+";

    private JLabel exprLabel;
    private JLabel valueLabel;

    private BigDecimal accumulator = BigDecimal.ZERO;
    private String pendingOp = "";
    private String currentInput = "0";
    private boolean startNewInput = true;

    public FunctionalCalculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(340, 480);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(45, 45, 45)); 
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        exprLabel = new JLabel(" ");
        exprLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        exprLabel.setForeground(Color.BLACK);
        exprLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel displayBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(210, 235, 200)); // Light green display
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        displayBox.setOpaque(false);
        displayBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        displayBox.setLayout(new BorderLayout());
        displayBox.add(exprLabel, BorderLayout.NORTH);
        displayBox.add(valueLabel, BorderLayout.SOUTH);

        RoundedGlossyButton eraseBtn = new RoundedGlossyButton("\u232B");
        eraseBtn.setPreferredSize(new Dimension(60, 55));
        eraseBtn.setEraseStyle(true);
        eraseBtn.addActionListener(e -> doBackspace());

        topPanel.add(displayBox, BorderLayout.CENTER);
        topPanel.add(eraseBtn, BorderLayout.EAST);

        root.add(topPanel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(4, 4, 8, 8));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 5, 5, 5));

        String[][] keys = {
                {"7", "8", "9", DIV},
                {"4", "5", "6", MUL},
                {"1", "2", "3", MINUS},
                {"0", ".", "=", PLUS}
        };

        for (String[] row : keys) {
            for (String text : row) {
                RoundedGlossyButton b = new RoundedGlossyButton(text);

                b.setFont(new Font("SansSerif", Font.BOLD, 32));

                if ("=".equals(text)) b.setAccent(true);
                if (text.equals(DIV) || text.equals(MUL) || text.equals(MINUS) || text.equals(PLUS))
                    b.setOperator(true);

                b.addActionListener(this::onButtonPress);
                grid.add(b);
            }
        }

        root.add(grid, BorderLayout.CENTER);

        setVisible(true);
    }

    private void onButtonPress(ActionEvent e) {
        String cmd = ((JButton) e.getSource()).getText();

        if ("0123456789".contains(cmd)) appendDigit(cmd);
        else if (cmd.equals(".")) appendDecimal();
        else if (cmd.equals(DIV) || cmd.equals(MUL) || cmd.equals(MINUS) || cmd.equals(PLUS))
            applyOperator(cmd);
        else if (cmd.equals("=")) computeEquals();
    }

    private void appendDigit(String d) {
        if (startNewInput) {
            currentInput = d;
            startNewInput = false;
        } else {
            if (currentInput.equals("0")) currentInput = d;
            else currentInput += d;
        }
        valueLabel.setText(currentInput);
    }

    private void appendDecimal() {
        if (startNewInput) {
            currentInput = "0.";
            startNewInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        valueLabel.setText(currentInput);
    }

    private void applyOperator(String op) {
        BigDecimal input = new BigDecimal(currentInput);

        if (!pendingOp.isEmpty())
            accumulator = compute(accumulator, input, pendingOp);
        else
            accumulator = input;

        pendingOp = op;
        startNewInput = true;

        exprLabel.setText(accumulator.stripTrailingZeros().toPlainString() + " " + op);
        valueLabel.setText(accumulator.stripTrailingZeros().toPlainString());
    }

    private void computeEquals() {
        if (pendingOp.isEmpty()) return;

        BigDecimal input = new BigDecimal(currentInput);
        BigDecimal result = compute(accumulator, input, pendingOp);

        exprLabel.setText(accumulator + " " + pendingOp + " " + input + " =");
        valueLabel.setText(result.stripTrailingZeros().toPlainString());

        pendingOp = "";
        accumulator = BigDecimal.ZERO;
        currentInput = result.toPlainString();
        startNewInput = true;
    }

    private BigDecimal compute(BigDecimal a, BigDecimal b, String op) {
        switch (op) {
            case DIV:
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero");
                    return BigDecimal.ZERO;
                }
                return a.divide(b, 12, RoundingMode.HALF_UP);
            case MUL: return a.multiply(b);
            case MINUS: return a.subtract(b);
            case PLUS: return a.add(b);
        }
        return b;
    }

    private void doBackspace() {
        if (startNewInput || currentInput.length() <= 1) {
            currentInput = "0";
            startNewInput = true;
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        valueLabel.setText(currentInput);
    }

    private static class RoundedGlossyButton extends JButton {
        private boolean operator = false;
        private boolean accent = false;
        private boolean eraseStyle = false;

        public RoundedGlossyButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
        }

        public void setOperator(boolean op) { operator = op; }
        public void setAccent(boolean a) { accent = a; }
        public void setEraseStyle(boolean e) { eraseStyle = e; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth(), h = getHeight();

            Color top, bottom;

            if (accent) {
                top = new Color(220, 40, 40);
                bottom = new Color(180, 20, 20);

            } else if (operator) {
                top = new Color(70, 180, 180);
                bottom = new Color(50, 160, 160);

            } else {
                top = new Color(70, 70, 70);
                bottom = new Color(50, 50, 50);
            }

            g2.setPaint(new GradientPaint(0, 0, top, 0, h, bottom));
            g2.fillRoundRect(0, 0, w, h, 16, 16);

            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(FunctionalCalculator::new);
    }
}
