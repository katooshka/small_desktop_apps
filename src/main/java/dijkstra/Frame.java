package dijkstra;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

/**
 * Author: katooshka
 * Date: 11/2/15.
 */

//TODO: оптимизировать MouseListeners
//TODO: сделать возможным вводить только название станции без ветки
//TODO: разобраться с ошибкой при работе с TextAreas

public class Frame {

    public static void main(String[] args) throws IOException {
        Graph graph = GraphReader.readGraph("dijkstra/edges.txt", "dijkstra/vertices.txt");
        drawFrame(graph);
    }

    public static void drawFrame(Graph graph) throws IOException {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(800, 900));

        MetroMapPanel metroMapPanel = new MetroMapPanel(graph);

        JPanel wayInfo = new JPanel();
        wayInfo.setLayout(new GridLayout(1, 3, 10, 10));
        JTextArea stationFromText = new JTextArea();
        JTextArea stationToText = new JTextArea();
        JTextArea time = new JTextArea();

        stationFromText.setEditable(false);
        stationToText.setEditable(false);
        time.setEditable(false);

        stationFromText.setBackground(metroMapPanel.getBackground());
        stationToText.setBackground(metroMapPanel.getBackground());
        time.setBackground(metroMapPanel.getBackground());

        setTextAreaBorders(stationFromText, "Начальная станция");
        setTextAreaBorders(stationToText, "Конечная станция");
        setTextAreaBorders(time, "Время в пути");

        wayInfo.add(stationFromText);
        wayInfo.add(stationToText);
        wayInfo.add(time);

        metroMapPanel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                metroMapPanel.onMouseWheelMoved(e.getPreciseWheelRotation());
            }
        });
        metroMapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                metroMapPanel.onMousePressed(e.getX(), e.getY());
            }
        });
        metroMapPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                metroMapPanel.onMouseDragged(e.getX(), e.getY());
            }
        });


        metroMapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                metroMapPanel.onMouseClicked(e.getX(), e.getY());
                stationFromText.setText(metroMapPanel.stationFrom.split(":")[1]);
                if (metroMapPanel.stationTo != null) {
                    stationToText.setText(metroMapPanel.stationTo.split(":")[1]);
                    time.setText(String.valueOf(graph.getMinWayLength() / 60));
                } else {
                    stationToText.setText("");
                    time.setText("");
                }
            }
        });

        JPanel allElements = new JPanel();
        allElements.setLayout(new BorderLayout(10, 0));
        allElements.add(metroMapPanel, BorderLayout.CENTER);
        allElements.add(wayInfo, BorderLayout.SOUTH);

        frame.setContentPane(allElements);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static void setTextAreaBorders(JTextArea panel, String name) {
        panel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), name, TitledBorder.CENTER, TitledBorder.TOP));
    }

}

