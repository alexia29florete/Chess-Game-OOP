import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEndInfoPanel extends JPanel
{
    private final Main app;
    private final AppFrame appFrame;

    private final JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel scoreLabel  = new JLabel("", SwingConstants.CENTER);
    private final JLabel messageLabel  = new JLabel("", SwingConstants.CENTER);

    public GameEndInfoPanel(Main app, AppFrame appFrame)
    {
        this.app = app;
        this.appFrame = appFrame;
        setupUI();
    }

    public void setupUI()
    {
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 16, 26));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(600, 520));
        card.setMaximumSize(new Dimension(600, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(18, 28, 44));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 35), 1, true),
                BorderFactory.createEmptyBorder(28, 36, 28, 36)
        ));

        JLabel title = new JLabel("GAME ENDED", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 44));
        title.setForeground(new Color(210, 170, 70));

        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setFont(new Font("Apple Color Emoji", Font.BOLD, 52));
        resultLabel.setForeground(new Color(235, 240, 250));

        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        scoreLabel.setForeground(new Color(70, 225, 150));

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(180, 190, 205));

        JButton menuBtn = new JButton("Play Again");
        JButton exitBtn = new JButton("Exit Game");

        styleButton(menuBtn, new Color(45, 185, 105));
        styleButton(exitBtn, new Color(46, 58, 72));

        menuBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                appFrame.showMainMenu();
            }
        });
        exitBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        card.add(title);
        card.add(Box.createVerticalStrut(22));
        card.add(resultLabel);
        card.add(Box.createVerticalStrut(18));
        card.add(scoreLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(22));
        card.add(menuBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(exitBtn);

        add(card);
    }

    private void styleButton(JButton b, Color bg)
    {
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setMaximumSize(new Dimension(450, 56));
        b.setPreferredSize(new Dimension(450, 56));
        b.setBorder(BorderFactory.createEmptyBorder(12, 26, 12, 26));

        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void setResult(String endRes)
    {
        String type = "UNKNOWN";
        int scoredPoints = 0;
        String message = "";

        try
        {
            String[] parts = endRes.split("\\|", 4);
            if (parts.length >= 2)
            {
                type = parts[1];
            }
            if (parts.length >= 3)
            {
                scoredPoints = Integer.parseInt(parts[2]);
            }
            if (parts.length >= 4)
            {
                message = parts[3];
            }
        }
        catch (Exception ignored) {}

        resultLabel.setText(getResult(type, scoredPoints) + " " + buildResultTitle(type, scoredPoints, message));

        if(scoredPoints >= 0)
        {
            scoreLabel.setText("+" + scoredPoints);
            scoreLabel.setForeground(new Color(70, 225, 150));

            messageLabel.setText("Points earned this game");
            messageLabel.setForeground(new Color(180, 190, 205));
        }
        else
        {
            scoreLabel.setText(String.valueOf(scoredPoints));
            scoreLabel.setForeground(new Color(240, 95, 95));

            messageLabel.setText("Points lost this game");
            messageLabel.setForeground(new Color(180, 190, 205));
        }
    }

    private String getResult(String type, int scoredPoints)
    {
        if ("EQUALITY".equalsIgnoreCase(type))
        {
            return "\uD83E\uDD1D";
        }
        if ("CHECKMATE".equalsIgnoreCase(type) || "RESIGN".equalsIgnoreCase(type))
        {
            if(scoredPoints >= 0)
            {
                return "\uD83C\uDFC6";
            }
            else
            {
                return "\uD83D\uDE1E";
            }
        }
        return "";
    }

    private String buildResultTitle(String type, int scoredPoints, String message)
    {
        if ("EQUALITY".equals(type))
        {
            return "DRAW";
        }
        if ("CHECKMATE".equals(type) || "RESIGN".equals(type))
        {
            if(scoredPoints >= 0)
            {
                return "VICTORY";
            }
            else
            {
                return "DEFEAT";
            }
        }

        if(message.isEmpty())
        {
            return "GAME ENDED";
        }
        else
        {
            return message;
        }
    }
}