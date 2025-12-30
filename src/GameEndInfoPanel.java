import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GameEndInfoPanel extends JPanel
{
    private final Main app;
    private final AppFrame appFrame;

    private final JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel deltaLabel  = new JLabel("", SwingConstants.CENTER);
    private final JLabel totalLabel  = new JLabel("", SwingConstants.CENTER);

    public GameEndInfoPanel(Main app, AppFrame appFrame)
    {
        this.app = app;
        this.appFrame = appFrame;
        setupUI();
    }

    public void setupUI()
    {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Game finished", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 18f));
        deltaLabel.setFont(deltaLabel.getFont().deriveFont(16f));
        totalLabel.setFont(totalLabel.getFont().deriveFont(16f));

        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deltaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalStrut(20));
        center.add(resultLabel);
        center.add(Box.createVerticalStrut(14));
        center.add(deltaLabel);
        center.add(Box.createVerticalStrut(8));
        center.add(totalLabel);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);

        JButton menuBtn = new JButton("Play Again");
        JButton exitBtn = new JButton("Exit");

        menuBtn.addActionListener(e -> appFrame.showMainMenu());
        exitBtn.addActionListener(e -> System.exit(0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.add(menuBtn);
        bottom.add(exitBtn);

        add(bottom, BorderLayout.SOUTH);

        // default (dacă ajungi accidental aici)
        setResult("END|UNKNOWN|0|");
    }

    public void setResult(String endRes)
    {
        // Parse: END|TYPE|delta|msg
        String type = "UNKNOWN";
        int delta = 0;
        String msg = "";

        try
        {
            String[] parts = endRes.split("\\|", 4);
            if (parts.length >= 2)
            {
                type = parts[1];
            }
            if (parts.length >= 3)
            {
                delta = Integer.parseInt(parts[2]);
            }
            if (parts.length >= 4)
            {
                msg = parts[3];
            }
        }
        catch (Exception ignored)
        {
            msg = endRes;
        }

        int total = 0;
        if (app != null && app.getCurrentUser() != null)
        {
            total = app.getCurrentUser().getPoints();
        }

        resultLabel.setText(buildResultTitle(type, delta, msg));
        deltaLabel.setText("Points this earned game: " + (delta >= 0 ? "+" : "") + delta);
    }

    private String buildResultTitle(String type, int delta, String msg)
    {
        if ("EQUALITY".equalsIgnoreCase(type))
        {
            return "Egalitate";
        }
        if ("CHECKMATE".equalsIgnoreCase(type))
        {
            return (delta >= 0) ? "Victory by checkmate" : "Defeated! You were checkmated by the computer";
        }
        if ("RESIGN".equalsIgnoreCase(type))
        {
            return (delta >= 0) ? "Computer has resigned - Victory" : "You have resigned - Defeat";
        }

        if (msg != null && !msg.trim().isEmpty())
        {
            return msg;
        }
        return "Game ended";
    }
}
