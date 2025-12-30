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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));
        add(title, BorderLayout.NORTH);

        //creez sectiunea in care am informatii despre puncte si nr de jocuri
        JPanel infoPlayer = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPlayer.add(userLabel);
        infoPlayer.add(pointsLabel);
        infoPlayer.add(gamesLabel);
        add(infoPlayer, BorderLayout.CENTER);

        //creez sectiunea pentru butoane
        JButton newGame = new JButton("New Game");
        JButton continueGame = new JButton("Visualise or Continue Game");
        JButton logOut = new JButton("Logout");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(newGame);
        buttons.add(continueGame);
        buttons.add(logOut);
        add(buttons, BorderLayout.SOUTH);

        newGame.addActionListener(e -> startNewGame());
        continueGame.addActionListener(e -> contGame());
        logOut.addActionListener(e -> {
            app.logout();
            appFrame.showLogin();
        });
    }

//    public void createStyledButton(String title, String description, Color color)
//    {
//
//    }

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


    public void refresh()
    {
        User u = app.getCurrentUser();
        if (u == null)
        {
            userLabel.setText("User: -");
            pointsLabel.setText("Points: -");
            gamesLabel.setText("Active games: -");
            return;
        }
        userLabel.setText("User: " + u.getEmail());
        pointsLabel.setText("Points: " + u.getPoints());
        gamesLabel.setText("Active games: " + u.getActiveGames().size());
    }
}
