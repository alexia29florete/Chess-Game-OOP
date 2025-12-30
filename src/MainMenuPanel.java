import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainMenuPanel extends JPanel
{
    private final Main app;
    private final AppFrame appFrame;

    private final JLabel userLabel = new JLabel();
    private final JLabel pointsLabel = new JLabel();
    private final JLabel gamesLabel = new JLabel();

    public MainMenuPanel(Main app, AppFrame appFrame)
    {
        this.app = app;
        this.appFrame = appFrame;
        setupUI();
    }

    public void setupUI()
    {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(10, 16, 26));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel mainMenuLabel = new JLabel("Main Menu");
        mainMenuLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainMenuLabel.setForeground(new Color(180, 190, 205));

        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(new Color(235, 240, 250));
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(mainMenuLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        add(center, BorderLayout.CENTER);

        //partea principala din centru cu info si butoane
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(720, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(18, 28, 44));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 35), 1, true),
                BorderFactory.createEmptyBorder(26, 30, 26, 30)
        ));

        JLabel title = new JLabel("Chess Master");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(235, 240, 250));

        //creez sectiune de puncte si jocuri
        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        statusPanel.setOpaque(false);
        statusPanel.setMaximumSize(new Dimension(660, 95));
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pointsCard = createStatCard(pointsLabel);
        JPanel gamesCard = createStatCard(gamesLabel);
        pointsLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        gamesLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));

        statusPanel.add(pointsCard);
        statusPanel.add(gamesCard);

        //creez cele 3 butoane
        JButton newGame = new JButton("New Game");
        JButton continueGame = new JButton("Visualise or Continue");
        JButton logOut = new JButton("Logout");

        newGame.addActionListener(e -> startNewGame());
        continueGame.addActionListener(e -> contGame());
        logOut.addActionListener(e -> {
            app.logout();
            appFrame.showLogin();
        });

        //listare verticala a butoanelor
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setAlignmentX(Component.CENTER_ALIGNMENT);

        list.add(createStyleButton(newGame, new Color(45, 185, 105)));
        list.add(Box.createVerticalStrut(12));
        list.add(createStyleButton(continueGame, new Color(52, 86, 140)));
        list.add(Box.createVerticalStrut(12));
        list.add(createStyleButton(logOut, new Color(220, 70, 70)));

        card.add(title);
        card.add(Box.createVerticalStrut(18));
        card.add(statusPanel);
        card.add(Box.createVerticalStrut(22));
        card.add(list);

        center.add(card);

    }

    private JPanel createStyleButton(JButton b, Color accent)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(new Color(16, 24, 38));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 28), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 14)
        ));
        row.setMaximumSize(new Dimension(520, 62));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new Dimension(6, 1));
        row.add(strip, BorderLayout.WEST);

        //stil buton
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setForeground(new Color(235, 240, 250));
        b.setBackground(new Color(16, 24, 38));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel arrow = new JLabel("\u203A");
        arrow.setForeground(new Color(180, 190, 205));
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 22));

        row.add(b, BorderLayout.CENTER);
        row.add(arrow, BorderLayout.EAST);

        return row;
    }

    private JPanel createStatCard(JLabel label)
    {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(14, 22, 36));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 25), 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        label.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(label);
        return p;
    }

    public void startNewGame()
    {
        String alias = JOptionPane.showInputDialog(this, "Choose an alias: ", "New Game", JOptionPane.QUESTION_MESSAGE);
        if(alias == null)
        {
            return;
        }
        alias = alias.trim();
        if(alias.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Alias cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = {"White", "Black"};
        int choice = JOptionPane.showOptionDialog(this, "Choose your color:", "New Game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        Colors userColor;
        if(choice == 0)
        {
            userColor = Colors.WHITE;
        }
        else
        {
            userColor = Colors.BLACK;
        }
        Game game = app.startNewGameUI(alias, userColor);
        if (game == null)
        {
            JOptionPane.showMessageDialog(this, "Could not start a new game.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        app.write();
        refresh();

        appFrame.showGame(game);
    }

    public void contGame()
    {
        User u = app.getCurrentUser();
        List<Game> active = u.getActiveGames();

        //daca utilizatorul nu are jocuri pe care sa le continue in cont
        if (active == null || active.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Sorry, there aren't any games to continue", "Continue Game", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //creez o noua pagina in interiorul paginii curente
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Select a game", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Your active games", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        root.add(title, BorderLayout.NORTH);

        //in listPanel o sa tin butoanele pt jocuri
        JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        //iau fiecare joc al user-ului
        for (Game g : u.getActiveGames())
        {
            JButton btn = new JButton("Game #" + g.getId());
            //daca apas pe un buton, atunci imi deschide pagina cu informatii despre joc
            btn.addActionListener(e ->
            {
                dialog.dispose();
                showGameInfoDialog(g);
            });
            listPanel.add(btn);
        }

        //aici sa pot da scroll daca am mai multe jocuri
        JScrollPane scroll = new JScrollPane(listPanel);
        root.add(scroll, BorderLayout.CENTER);

        //am un buton de close ca sa ajung la main menu daca nu vreau sa mai vad lista de jocuri
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(closeBtn);
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private void showGameInfoDialog(Game g)
    {
        if (g == null)
        {
            return;
        }

        app.prepareSelectedGameForCurrentUser(g);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Game Information", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Game #" + g.getId(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        //afisez jucatori si ce culori a avut fiecare
        JPanel playersPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        playersPanel.setBorder(BorderFactory.createTitledBorder("Players"));

        for (Player p : g.getPlayers())
        {
            playersPanel.add(new JLabel(p.getName() + " (" + p.getColor() + ")"));
        }

        //afisez istoricul mutarilor
        DefaultListModel<String> movesModel = new DefaultListModel<>();
        int idx = 1;
        for (Move m : g.getMoves())
        {
            String bulinaCuloare;
            if(m.getPlayerColor() == Colors.WHITE)
            {
                bulinaCuloare ="\u26AA";
            }
            else
            {
                bulinaCuloare ="\u26AB";
            }
            String line = idx + ". "  + bulinaCuloare + " " + m.getFrom() + " -> " + m.getTo();
            if (m.getCapturedPiece() != null)
            {
                line += " (captured " + m.getCapturedPiece().type() + ")";
            }
            movesModel.addElement(line);
            idx++;
        }

        JList<String> movesList = new JList<>(movesModel);
        JScrollPane movesScroll = new JScrollPane(movesList);
        movesScroll.setBorder(BorderFactory.createTitledBorder("Move History (" + g.getMoves().size() + " moves)"));

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(playersPanel, BorderLayout.NORTH);
        center.add(movesScroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // butoane de navigare
        JButton backBtn = new JButton("Back");
        JButton deleteBtn = new JButton("Delete");
        JButton continueBtn = new JButton("Continue");

        backBtn.addActionListener(e -> dialog.dispose());

        deleteBtn.addActionListener(e ->
        {
            //in momentul in care apas pe butonul de a sterge un joc, sunt intrebat daca vreau sa sterg
            int ok = JOptionPane.showConfirmDialog(dialog, "Delete Game #" + g.getId() + "?", "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION)
            {
                //daca spun ca vreau sa il sterg, sterg jocul care contine id-ul selectat
                boolean deleted = app.deleteGame(g.getId());
                if (deleted)
                {
                    app.write();
                    refresh();
                    dialog.dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(dialog, "Could not delete the game.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        continueBtn.addActionListener(e ->
        {
            dialog.dispose();
            appFrame.showGame(g);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backBtn);
        bottom.add(deleteBtn);
        bottom.add(continueBtn);
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }


    public void refresh() {
        User u = app.getCurrentUser();
        if (u == null) {
            userLabel.setText("Guest");

            pointsLabel.setText("<html><div style='text-align:center; color:#EBF0FA; font-size:22px;'><b>\u2605 0</b></div>"
                    + "<div style='text-align:center; color:#B4BECD; font-size:11px;'>Total Points</div></html>");

            gamesLabel.setText("<html><div style='text-align:center; color:#EBF0FA; font-size:22px;'><b>\u265F 0</b></div>"
                    + "<div style='text-align:center; color:#B4BECD; font-size:11px;'>Active Games</div></html>");
            return;
        }

        userLabel.setText(u.getEmail());

        pointsLabel.setText("<html><div style='text-align:center; color:#f8f82f; font-size:22px;'><b>\u2605 "
                + u.getPoints()
                + "</b></div><div style='text-align:center; color:#B4BECD; font-size:11px;'>Total Points</div></html>");

        gamesLabel.setText("<html><div style='text-align:center; color:#05ad2f; font-size:22px;'><b>\u265F "
                + u.getActiveGames().size()
                + "</b></div><div style='text-align:center; color:#B4BECD; font-size:11px;'>Active Games</div></html>");
    }
}
