import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EventChart extends JPanel {
    private final List<Long> counts;
    private final List<String> labels;

    public EventChart(List<Long> counts, List<String> labels) {
        this.counts = counts;
        this.labels = labels;
    }

    public static EventChart fromEvents(List<Long>counts , List<String>labels) {

        List<FileHandler.Month> monthList = Arrays.stream(FileHandler.Month.values()).toList();

        return new EventChart(counts, labels);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (counts == null || counts.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int leftMargin = 50;
        int bottomMargin = 50;
        int topMargin = 40;

        int chartHeight = height - bottomMargin - topMargin;
        int chartWidth = width - leftMargin - 20;

        int barWidth = Math.max(1, chartWidth / counts.size() - 5);

        double maxCount = counts.stream().mapToDouble(Long::doubleValue).max().orElse(10.0);

        // Draw Y-axis
        g2.setColor(Color.BLACK);
        g2.drawLine(leftMargin, topMargin, leftMargin, height - bottomMargin);

        // Draw X-axis
        g2.drawLine(leftMargin, height - bottomMargin, width - 20, height - bottomMargin);

        // Draw Y-axis label
        g2.drawString("Events", leftMargin - 40, topMargin - 10);

        // Draw X-axis label
        g2.drawString("Months", width / 2 - 20, height - 10);

        // Draw horizontal grid lines
        g2.setColor(new Color(200, 200, 200));
        for (int i = 1; i <= 5; i++) {
            int y = topMargin + (chartHeight * i / 5);
            g2.drawLine(leftMargin, y, width - 20, y);
        }

        // Draw bars
        for (int i = 0; i < counts.size(); i++) {
            double count = counts.get(i);
            int barHeight = (int) ((count / maxCount) * chartHeight);

            int x = leftMargin + i * (barWidth + 5);
            int y = height - bottomMargin - barHeight;

            // Seasonal color
            g2.setColor(getSeasonColor(i + 1));
            g2.fillRect(x, y, barWidth, barHeight);

            // Value label
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(counts.get(i)), x, y - 5);

            // Month label
            if (i < labels.size()) {
                String shortName = labels.get(i).substring(0, 3);
                g2.drawString(shortName, x, height - bottomMargin + 20);
            }
        }
    }

    private Color getSeasonColor(int month) {
        return switch (month) {
            case 12, 1, 2 -> new Color(135, 206, 250); // Winter - Light Blue
            case 3, 4, 5 -> new Color(60, 179, 113);   // Spring - Green
            case 6, 7, 8 -> new Color(255, 165, 0);    // Summer - Orange
            case 9, 10, 11 -> new Color(205, 133, 63); // Autumn - Brown
            default -> Color.GRAY;
        };
    }
}
