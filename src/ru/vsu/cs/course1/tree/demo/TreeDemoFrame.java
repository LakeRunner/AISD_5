package ru.vsu.cs.course1.tree.demo;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.vsu.cs.course1.tree.BinaryTreeAlgorithms;
import ru.vsu.cs.course1.tree.SimpleBinaryTree;
import ru.vsu.cs.course1.tree.BinaryTreePainter;
import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.util.SwingUtils;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Locale;

public class TreeDemoFrame extends JFrame {
    private JPanel panelMain;
    private JButton buttonPreOrderTraverse;
    private JButton buttonInOrderTraverse;
    private JButton buttonPostOrderTraverse;
    private JButton buttonByLevelTraverse;
    private JTextArea textAreaSystemOut;
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JSplitPane splitPaneMain;
    private JTextField textFieldColorCode;
    private JPanel panelPaintArea;
    private JButton buttonFindPares;
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label3;
    private JLabel l1;
    private JLabel l2;

    private final JPanel paintPanel;

    BinaryTree<Integer> tree = new SimpleBinaryTree<>();


    public TreeDemoFrame() {
        this.setTitle("Двоичные деревья");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPaneMain.setDividerLocation(0.5);
        splitPaneMain.setResizeWeight(1.0);
        splitPaneMain.setBorder(null);

        paintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };
        paintPanel.setBackground(new Color(36, 36, 36));
        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);

        buttonMakeTree.addActionListener(actionEvent -> {
            try {
                SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
        buttonPreOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
                    @Override
                    public void visit(Integer value, int level) {
                        System.out.println(value + " (уровень " + level + ")");
                    }
                }
                BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), new InnerVisitor());
            });
        });
        buttonInOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
                    @Override
                    public void visit(Integer value, int level) {
                        System.out.println(value + " (уровень " + level + ")");
                    }
                }
                BinaryTreeAlgorithms.inOrderVisit(tree.getRoot(), new InnerVisitor());
            });
        });
        buttonPostOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
                    @Override
                    public void visit(Integer value, int level) {
                        System.out.println(value + " (уровень " + level + ")");
                    }
                }
                BinaryTreeAlgorithms.postOrderVisit(tree.getRoot(), new InnerVisitor());
            });
        });
        buttonByLevelTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {
                System.out.println("Посетитель:");
                class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
                    @Override
                    public void visit(Integer value, int level) {
                        System.out.println(value + " (уровень " + level + ")");
                    }
                }
                BinaryTreeAlgorithms.byLevelVisit(tree.getRoot(), new InnerVisitor());
            });
        });
        buttonFindPares.addActionListener(actionEvent -> {
            List<BinaryTreeAlgorithms.PairOfNodes<Integer>> pairs = BinaryTreeAlgorithms.findPares(tree.getRoot());
            showSystemOut(() -> {
                System.out.println("Одинаковые поддеревья:\n");
                for (BinaryTreeAlgorithms.PairOfNodes<Integer> v : pairs) {
                    System.out.println(v.getFirst() + "(уровень " + v.getFirstLevel() + ") - " +
                            v.getSecond() + "(уровень " + v.getSecondLevel() + ")");
                }
            });
        });
        textFieldColorCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String num = textFieldColorCode.getText();
                switch (num) {
                    case "1":
                        textAreaSystemOut.setForeground(Color.RED);
                        break;
                    case "2":
                        textAreaSystemOut.setForeground(new Color(255, 140, 0));
                        break;
                    case "3":
                        textAreaSystemOut.setForeground(Color.YELLOW);
                        break;
                    case "4":
                        textAreaSystemOut.setForeground(Color.GREEN);
                        break;
                    case "5":
                        textAreaSystemOut.setForeground(Color.CYAN);
                        break;
                    case "6":
                        textAreaSystemOut.setForeground(Color.BLUE);
                        break;
                    case "7":
                        textAreaSystemOut.setForeground(new Color(170, 0, 255));
                        break;
                }
            }
        });
    }

    /**
     * Перерисовка дерева
     */
    public void repaintTree() {
        paintPanel.repaint();
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), 10, 10));
        panelMain.setBackground(new Color(-16777216));
        splitPaneMain = new JSplitPane();
        splitPaneMain.setBackground(new Color(-15724528));
        panelMain.add(splitPaneMain, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-16777216));
        splitPaneMain.setLeftComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-16777216));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Label2 = new JLabel();
        Label2.setEnabled(true);
        Font Label2Font = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, Label2.getFont());
        if (Label2Font != null) Label2.setFont(Label2Font);
        Label2.setForeground(new Color(-1));
        Label2.setText("Дерево в скобочной нотации:");
        panel2.add(Label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldBracketNotationTree = new JTextField();
        textFieldBracketNotationTree.setEnabled(true);
        Font textFieldBracketNotationTreeFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, textFieldBracketNotationTree.getFont());
        if (textFieldBracketNotationTreeFont != null)
            textFieldBracketNotationTree.setFont(textFieldBracketNotationTreeFont);
        textFieldBracketNotationTree.setForeground(new Color(-16777216));
        textFieldBracketNotationTree.setText("8 (6 (4 (5), 6), 6 (4 (5), 6))");
        panel2.add(textFieldBracketNotationTree, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonMakeTree = new JButton();
        buttonMakeTree.setFocusable(false);
        Font buttonMakeTreeFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonMakeTree.getFont());
        if (buttonMakeTreeFont != null) buttonMakeTree.setFont(buttonMakeTreeFont);
        buttonMakeTree.setText("Построить дерево");
        panel2.add(buttonMakeTree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-16777216));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Label1 = new JLabel();
        Font Label1Font = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, Label1.getFont());
        if (Label1Font != null) Label1.setFont(Label1Font);
        Label1.setForeground(new Color(-1));
        Label1.setText("Выберите цвет подсветки вывода (1-7):");
        panel3.add(Label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonFindPares = new JButton();
        buttonFindPares.setFocusable(false);
        Font buttonFindParesFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonFindPares.getFont());
        if (buttonFindParesFont != null) buttonFindPares.setFont(buttonFindParesFont);
        buttonFindPares.setText("Найти одинаковые поддеревья");
        panel3.add(buttonFindPares, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldColorCode = new JTextField();
        textFieldColorCode.setBackground(new Color(-14408668));
        textFieldColorCode.setEditable(true);
        textFieldColorCode.setEnabled(true);
        Font textFieldColorCodeFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 28, textFieldColorCode.getFont());
        if (textFieldColorCodeFont != null) textFieldColorCode.setFont(textFieldColorCodeFont);
        textFieldColorCode.setForeground(new Color(-1));
        textFieldColorCode.setText("4");
        panel3.add(textFieldColorCode, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panelPaintArea = new JPanel();
        panelPaintArea.setLayout(new BorderLayout(0, 0));
        panelPaintArea.setBackground(new Color(-14408668));
        Font panelPaintAreaFont = this.$$$getFont$$$("Arial Black", Font.BOLD, 24, panelPaintArea.getFont());
        if (panelPaintAreaFont != null) panelPaintArea.setFont(panelPaintAreaFont);
        panel1.add(panelPaintArea, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelPaintArea.add(spacer1, BorderLayout.CENTER);
        Label3 = new JLabel();
        Label3.setText("");
        panel1.add(Label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        l1 = new JLabel();
        Font l1Font = this.$$$getFont$$$("Monospaced", Font.BOLD, 48, l1.getFont());
        if (l1Font != null) l1.setFont(l1Font);
        l1.setForeground(new Color(-1792));
        l1.setHorizontalAlignment(0);
        l1.setHorizontalTextPosition(0);
        l1.setText("");
        l1.setVisible(false);
        panel1.add(l1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        l2 = new JLabel();
        Font l2Font = this.$$$getFont$$$("Monospaced", Font.BOLD, 48, l2.getFont());
        if (l2Font != null) l2.setFont(l2Font);
        l2.setForeground(new Color(-1792));
        l2.setText("");
        l2.setVisible(false);
        panel1.add(l2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setBackground(new Color(-16777216));
        Font panel4Font = this.$$$getFont$$$("Monospaced", Font.BOLD, 14, panel4.getFont());
        if (panel4Font != null) panel4.setFont(panel4Font);
        splitPaneMain.setRightComponent(panel4);
        buttonPreOrderTraverse = new JButton();
        buttonPreOrderTraverse.setFocusable(false);
        Font buttonPreOrderTraverseFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonPreOrderTraverse.getFont());
        if (buttonPreOrderTraverseFont != null) buttonPreOrderTraverse.setFont(buttonPreOrderTraverseFont);
        buttonPreOrderTraverse.setText("Прямой обход");
        panel4.add(buttonPreOrderTraverse, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonInOrderTraverse = new JButton();
        buttonInOrderTraverse.setFocusable(false);
        Font buttonInOrderTraverseFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonInOrderTraverse.getFont());
        if (buttonInOrderTraverseFont != null) buttonInOrderTraverse.setFont(buttonInOrderTraverseFont);
        buttonInOrderTraverse.setText("Симметричный обход");
        panel4.add(buttonInOrderTraverse, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPostOrderTraverse = new JButton();
        buttonPostOrderTraverse.setFocusable(false);
        Font buttonPostOrderTraverseFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonPostOrderTraverse.getFont());
        if (buttonPostOrderTraverseFont != null) buttonPostOrderTraverse.setFont(buttonPostOrderTraverseFont);
        buttonPostOrderTraverse.setText("Обратный обход");
        panel4.add(buttonPostOrderTraverse, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonByLevelTraverse = new JButton();
        buttonByLevelTraverse.setFocusable(false);
        Font buttonByLevelTraverseFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 24, buttonByLevelTraverse.getFont());
        if (buttonByLevelTraverseFont != null) buttonByLevelTraverse.setFont(buttonByLevelTraverseFont);
        buttonByLevelTraverse.setText("Обход в ширину");
        panel4.add(buttonByLevelTraverse, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(new Color(-14408668));
        scrollPane1.setForeground(new Color(-1));
        panel4.add(scrollPane1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaSystemOut = new JTextArea();
        textAreaSystemOut.setBackground(new Color(-14408668));
        textAreaSystemOut.setEditable(false);
        Font textAreaSystemOutFont = this.$$$getFont$$$("Monospaced", Font.BOLD, 22, textAreaSystemOut.getFont());
        if (textAreaSystemOutFont != null) textAreaSystemOut.setFont(textAreaSystemOutFont);
        textAreaSystemOut.setForeground(new Color(-16711927));
        textAreaSystemOut.setText("");
        scrollPane1.setViewportView(textAreaSystemOut);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
