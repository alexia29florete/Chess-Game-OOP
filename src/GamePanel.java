import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.io.File;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel
{
    private final Main app;
    private final AppFrame appFrame;

    private Game game;

    private final JLabel playersTurnLabel = new JLabel(" ");
    private final JLabel scoreLabel = new JLabel(" ");
    private final JLabel messageLabel = new JLabel(" ");

    private final JTextArea historyMoves = new JTextArea(12, 18);
    private final JTextArea capturedPiecesUser = new JTextArea(8, 18);
    private final JTextArea capturedPiecesComputer = new JTextArea(8, 18);

    private final JButton[][] tabla = new JButton[8][8];

    private Position selectedFrom = null;
    private final List<Position> mutariPosibile = new ArrayList<>();

    private static final int ICON_SIZE = 52;
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    public GamePanel(Main app, AppFrame appFrame)
    {
        this.app = app;
        this.appFrame = appFrame;
        setupUI();
    }

    private void setupUI()
    {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(25, 35, 50));

        //creez partea de sus cu mesaje
        JPanel top = new JPanel(new GridLayout(1, 3, 10, 0));
        top.setBackground(new Color(35, 45, 65));
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 55, 75), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        playersTurnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playersTurnLabel.setForeground(new Color(220, 225, 235));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(255, 200, 100));
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        top.add(playersTurnLabel);
        top.add(messageLabel);
        top.add(scoreLabel);
        add(top, BorderLayout.NORTH);

        JPanel boardWrapper = new JPanel(new BorderLayout());
        boardWrapper.setBackground(new Color(25, 35, 50));
        boardWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 55, 75), 4),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        //in centru o sa fie tabla de 8x8
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        boardPanel.setBackground(new Color(25, 35, 50));

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                JButton b = new JButton();
                b.setContentAreaFilled(true);
                b.setMargin(new Insets(0,0,0,0));
                b.setFont(new Font("SansSerif", Font.BOLD, 24));
                b.setFocusPainted(false);
                b.setOpaque(true);
                b.setBorderPainted(true);
                b.setPreferredSize(new Dimension(70, 70));

                int rr = i, cc = j;
                b.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        onSquareClicked(rr, cc);
                    }
                });

                tabla[i][j] = b;
                boardPanel.add(b);
            }
        }
        boardWrapper.add(boardPanel, BorderLayout.CENTER);
        add(boardWrapper, BorderLayout.CENTER);

        //in stanga o sa fie move history
        historyMoves.setEditable(false); //nu vreau ca utilizatorul sa il poata edita
        historyMoves.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        historyMoves.setBackground(new Color(35, 45, 65));
        historyMoves.setForeground(new Color(220, 225, 235));
        historyMoves.setCaretColor(new Color(220, 225, 235));
        historyMoves.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(wrap("Move History", styledScroll(historyMoves, new Color(35, 45, 65))), BorderLayout.WEST);

        //in dreapta o sa am capturile fiecaruia si butoane
        capturedPiecesUser.setEditable(false);
        capturedPiecesComputer.setEditable(false);
        capturedPiecesUser.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        capturedPiecesComputer.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        capturedPiecesUser.setBackground(new Color(35, 45, 65));
        capturedPiecesUser.setForeground(new Color(255, 200, 100));
        capturedPiecesUser.setCaretColor(new Color(220, 225, 235));
        capturedPiecesUser.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        capturedPiecesComputer.setBackground(new Color(35, 45, 65));
        capturedPiecesComputer.setForeground(new Color(255, 200, 100));
        capturedPiecesComputer.setCaretColor(new Color(220, 225, 235));
        capturedPiecesComputer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel captures = new JPanel(new GridLayout(2, 1, 8, 8));
        captures.setBackground(new Color(25, 35, 50));
        captures.add(wrap("Your captures", new JScrollPane(capturedPiecesUser)));
        captures.add(wrap("Computer's captures", new JScrollPane(capturedPiecesComputer)));

        JButton resignBtn = createStyledButton("Resign", new Color(220, 53, 69), new Color(200, 35, 51));

        JButton saveExitBtn = createStyledButton("Save & Exit", new Color(255, 193, 7), new Color(235, 173, 0));

        JButton backBtn = createStyledButton("Back to Menu", new Color(108, 117, 125), new Color(90, 98, 104));

        resignBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doResign();
            }
        });
        saveExitBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSaveAndExit();
            }
        });
        backBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (game == null)
                {
                    appFrame.showMainMenu();
                    return;
                }

                boolean deleted = app.deleteGame(game.getId());
                if (deleted)
                {
                    app.write();
                }
                game = null;
                selectedFrom = null;
                mutariPosibile.clear();
                appFrame.showMainMenu();
            }
        });

        JPanel buttons = new JPanel(new GridLayout(3, 1, 8, 8));
        buttons.setBackground(new Color(25, 35, 50));
        buttons.add(resignBtn);
        buttons.add(saveExitBtn);
        buttons.add(backBtn);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBackground(new Color(25, 35, 50));
        right.add(captures, BorderLayout.CENTER);
        right.add(buttons, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Efect hover
        btn.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private JScrollPane styledScroll(JComponent view, Color bg)
    {
        JScrollPane sp = new JScrollPane(view);

        sp.setBackground(bg);
        sp.getViewport().setBackground(bg);

        sp.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 75), 1));

        sp.getVerticalScrollBar().setBackground(bg);
        sp.getHorizontalScrollBar().setBackground(bg);

        return sp;
    }

    private JPanel wrap(String title, JComponent comp)
    {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(new Color(40, 40, 45));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(new Color(200, 200, 210));
        p.add(t, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    //utilizata atunci cand incep sau continui un joc
    public void setGame(Game game)
    {
        this.game = game;
        app.prepareSelectedGameForCurrentUser(this.game);

        this.game.resume();
        this.game.getScoreByCapturedPieces();

        selectedFrom = null;
        mutariPosibile.clear();
        refreshAll();

        //daca computer e alb, il las sa mute
        if (game.getPlayer() != game.getUser())
        {
            doComputerMoveSoon();
        }
    }

    //aceasta metoda ma ajuta sa "redesenez" grafica la fiecare mutarre, captura, etc
    private void refreshAll()
    {
        if (game == null)
        {
            return;
        }

        setBoard();
        renderSide();
        renderHistory();
        refreshStatus();
    }

    private Position uiToPos(int row, int col, Colors userColor)
    {
        char x;
        int y;

        if (userColor == Colors.WHITE)
        {
            x = (char) ('A' + col);
            y = 8 - row;
        }
        else
        {
            x = (char) ('H' - col);
            y = row + 1;
        }
        return new Position(x, y);
    }

    private Point posToUi(Position pos, Colors userColor)
    {
        int row, col;
        if (userColor == Colors.WHITE)
        {
            row = 8 - pos.getY();
            col = pos.getX() - 'A';
        }
        else
        {
            row = pos.getY() - 1;
            col = 'H' - pos.getX();
        }
        return new Point(row, col);
    }

    private void setBoard()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                //pentru fiecare patrat de pe tala, convertesc pozitia in A1-H8
                JButton casetuta = tabla[i][j];
                Position pos = uiToPos(i, j, game.getUser().getColor());
                //vad ce elemnte sunt pe tabla
                Piece piece = game.getBoard().getPieceAt(pos);

                //pun piesa pe tabla
                punePiesaPeTabla(casetuta, piece);
                //setez alternanta de culori
                if((i + j) % 2 == 0)
                {
                    casetuta.setBackground(new Color(240, 217, 181));
                }
                else
                {
                    casetuta.setBackground(new Color(181, 136, 99));
                }
            }
        }

        //daca selectez o piesa de pe tabla, sa imi arate mutarile posibile
        if (selectedFrom != null)
        {
            //colorez piesa selectata
            highlight(selectedFrom, new Color(246, 246, 120));
            for (Position t : mutariPosibile)
            {
                //selectez pozitiile posibile
                highlight(t, new Color(186, 202, 120));
            }
        }
    }
    private void highlight(Position pos, Color color)
    {
        Point uiCoord = posToUi(pos, game.getUser().getColor());
        tabla[uiCoord.x][uiCoord.y].setBackground(color);
    }

    private void punePiesaPeTabla(JButton casetuta, Piece piece)
    {
        //daca nu am piesa pe caseta respectiva, nu pun nimic
        if (piece == null)
        {
            casetuta.setIcon(null);
            casetuta.setText("");
            return;
        }

        ImageIcon icon = getIconForPiece(piece);
        if (icon != null)
        {
            casetuta.setIcon(icon);
            casetuta.setText("");
            casetuta.setContentAreaFilled(true);
        }
    }

    private ImageIcon getIconForPiece(Piece piece)
    {

        if (piece == null)
        {
            return null;
        }

        //construiesc numele fsierului
        String prefix;
        if(piece.getColor() == Colors.WHITE)
        {
            prefix = "w";
        }
        else
        {
            prefix = "b";
        }
        char type = Character.toUpperCase(piece.type());

        String name = null;
        if (piece.type() == 'K')
        {
            name =  prefix + "king";
        }
        if (piece.type() == 'Q')
        {
            name = prefix + "queen";
        }
        if (piece.type() == 'R')
        {
            name = prefix + "rook";
        }
        if (piece.type() == 'B')
        {
            name = prefix + "bishop";
        }
        if (piece.type() == 'N')
        {
            name = prefix + "knight";
        }
        if (piece.type() == 'P')
        {
            name = prefix + "pawn";
        }
        if(name == null)
        {
            return null;
        }

        //daca exista in cache, return
        if (iconCache.containsKey(name))
        {
            return iconCache.get(name);
        }

        //caut imaginea
        ImageIcon icon = null;
        File f = new File("../img/" + name + ".png");
        if(f.exists())
        {
            icon = new ImageIcon(f.getAbsolutePath());
        }

        //scalez iconita
        if(icon != null)
        {
            Image img = icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        }

        //il pun in cache
        iconCache.put(name, icon);
        return icon;
    }

    private void refreshStatus()
    {
        if (game == null)
        {
            return;
        }

        //reactualizez bara de sus cu mesaje, al cui rand este, cate puncte are user, daca e computer in check sau daca e user in check
        Player currentPlayer = game.getPlayer();
        playersTurnLabel.setText("It's: " + currentPlayer.getName() + "'s turn (" + currentPlayer.getColor() + ")");
        scoreLabel.setText("Game points: " + game.getUser().getPoints());

        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setText(" ");

        //afisez daca jucatorul curent e in sah
        if (game.getBoard().esteKingInCheck(currentPlayer.getColor()))
        {
            messageLabel.setForeground(new Color(255, 100, 100));
            if (currentPlayer == game.getUser())
            {
                messageLabel.setText("You are in check!");
            }
            else
            {
                messageLabel.setText("Computer is in check!");
            }
        }
    }

    //dupa fiecare mutare trebuie reactualizata lista de mutari
    private void renderHistory()
    {
        StringBuilder sb = new StringBuilder();
        List<Move> moves = game.getMoves();
        for (int i = 0; i < moves.size(); i++)
        {
            Move m = moves.get(i);
            String bulinaCuloare;
            if(m.getPlayerColor() == Colors.WHITE)
            {
                bulinaCuloare = "\u26AA";
            }
            else
            {
                bulinaCuloare = "\u26AB";
            }
            if(i < 10)
            {
                sb.append(i + 1).append(".    ").append(bulinaCuloare).append(": ").append(m.getFrom()).append(" -> ").append(m.getTo());
            }
            else if(i >= 10 && i < 100)
            {
                sb.append(i + 1).append(".   ").append(bulinaCuloare).append(": ").append(m.getFrom()).append(" -> ").append(m.getTo());
            }
            else
            {
                sb.append(i + 1).append(".  ").append(bulinaCuloare).append(": ").append(m.getFrom()).append(" -> ").append(m.getTo());
            }
            sb.append("\n");
        }
        historyMoves.setText(sb.toString());
        historyMoves.setCaretPosition(historyMoves.getDocument().getLength());
    }

    private String pieceToUnicode(Piece piece)
    {
        if(piece == null)
        {
            return "";
        }

        if(piece.getColor() == Colors.WHITE)
        {
            if(piece.type() == 'K')
            {
                return "\u2654";
            }
            if (piece.type() == 'Q')
            {
                return "\u2655";
            }
            if (piece.type() == 'R')
            {
                return "\u2656";
            }
            if (piece.type() == 'B')
            {
                return "\u2657";
            }
            if (piece.type() == 'N')
            {
                return "\u2658";
            }
            if (piece.type() == 'P')
            {
                return "\u2659";
            }
        }
        else
        {
            if(piece.type() == 'K')
            {
                return "\u265A";
            }
            if (piece.type() == 'Q')
            {
                return "\u265B";
            }
            if (piece.type() == 'R')
            {
                return "\u265C";
            }
            if (piece.type() == 'B')
            {
                return "\u265D";
            }
            if (piece.type() == 'N')
            {
                return "\u265E";
            }
            if (piece.type() == 'P')
            {
                return "\u265F";
            }
        }
        return "";
    }

    private String listPieces(List<Piece> pieces)
    {
        if (pieces == null || pieces.isEmpty())
        {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        int cnt = 6;
        for(int i = 0; i < pieces.size(); i++)
        {
            sb.append(pieceToUnicode(pieces.get(i)));
            if(i < pieces.size() - 1)
            {
                sb.append(" ");
            }

            //dupa fiecare 6 piese, trec la un nou rand
            if((i + 1) % cnt == 0 && i < pieces.size() - 1)
            {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void renderSide()
    {
        capturedPiecesUser.setText(listPieces(game.getUser().getCapturedPieces()));
        capturedPiecesComputer.setText(listPieces(game.getOpponent().getCapturedPieces()));
    }

    private void setMessage(String msg, boolean error)
    {
        if(msg == null)
        {
            messageLabel.setText("");
        }
        else
        {
            messageLabel.setText(msg);
        }

        if(error)
        {
            messageLabel.setForeground(new Color(255, 100, 100));
        }
        else
        {
            messageLabel.setForeground(new Color(100, 255, 150));
        }
    }

    //fac logica atunci cand apas pe o patratica
    private void onSquareClicked(int r, int c)
    {
        if (game == null)
        {
            return;
        }

        //user poate muta doar atunci cand ii vine randul
        if (game.getPlayer() != game.getUser())
        {
            return;
        }

        Position clickedPosition = uiToPos(r, c, game.getUser().getColor());
        Colors userColor = game.getUser().getColor();
        Piece clickedPiece = game.getBoard().getPieceAt(clickedPosition);

        //nu am nimic selectat => incerc sa selectez o piesa
        if(selectedFrom == null)
        {
            //daca nu am piesa
            if(clickedPiece == null)
            {
                return;
            }

            //daca incerc sa selectez o piesa care nu imi apartine, atentionez user-ul
            if(clickedPiece.getColor() != userColor)
            {
                setMessage("That piece is not yours!", true);
                return;
            }
            //acum inseamna ca piesa pe care am selectat-o este a mea
            selectedFrom = clickedPosition;
            computePossibleMoves(selectedFrom);
            refreshAll();
            return;
        }

        //daca am selectat deja ceva
        //deselectez daca apas pe aceeasi caseta
        if(selectedFrom.equals(clickedPosition))
        {
            selectedFrom = null;
            mutariPosibile.clear();
            refreshAll();
            return;
        }

        //daca eu am selectat o piesa, dar acum dau click pe o casuta care nu este inclusa printre mutarile posibile, imi deselecteaza
        if(!mutariPosibile.contains(clickedPosition))
        {
            selectedFrom = null;
            mutariPosibile.clear();
            if(clickedPiece != null && clickedPiece.getColor() == userColor)
            {
                //daca apas pe o alta piesa de a mea, o selectez
                selectedFrom = clickedPosition;
                computePossibleMoves(selectedFrom);
            }
            refreshAll();
            return;
        }

        //am selectat o piesa si fac o mutare
        char promo = 'Q';
        Piece movingPiece = game.getBoard().getPieceAt(selectedFrom);
        //daca piesa este pion,trebuie sa vad daca am ajuns la capatul tablei pentru a o promova
        if(movingPiece instanceof Pawn)
        {
            if(movingPiece.getColor() == Colors.WHITE && clickedPosition.getY() == 8)
            {
                promo = askPromotionChoice();
            }
            if(movingPiece.getColor() == Colors.BLACK && clickedPosition.getY() == 1)
            {
                promo = askPromotionChoice();
            }
        }

        String res = app.userMoveUI(game, selectedFrom, clickedPosition, promo);
        //sterg selectia
        selectedFrom = null;
        mutariPosibile.clear();

        handleResult(res);
        //daca mutarea e ok, "redesenez" tabla
        if (res.startsWith("OK|"))
        {
            refreshAll();

            if (game != null && game.getPlayer() != game.getUser())
            {
                doComputerMoveSoon();
            }
        }
    }

    //iau mutarile posibile ale piesei pe care am selectat-o si daca e o mutare valida, o adaug in lista
    private void computePossibleMoves(Position from)
    {
        mutariPosibile.clear();
        Piece p = game.getBoard().getPieceAt(from);
        if (p == null)
        {
            return;
        }

        for (Position to : p.getPossibleMoves(game.getBoard()))
        {
            try
            {
                if (game.getBoard().isValidMove(from, to))
                {
                    mutariPosibile.add(to);
                }
            }
            catch (Exception ignored)
            {

            }
        }
    }

    private char askPromotionChoice()
    {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pawn Promotion", true);
        dialog.setLayout(new GridLayout(1, 4, 15, 15));
        ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);

        final char[] result = {'Q'};

        String[] pieces = {"\u2655", "\u2656", "\u2657", "\u2658"};
        char[] codes = {'Q', 'R', 'B', 'N'};

        for (int i = 0; i < pieces.length; i++)
        {
            JButton btn = new JButton("<html><font size='60'>" + pieces[i] + "</font></html>");
            int index = i;

            btn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    result[0] = codes[index];
                    dialog.dispose();
                }
            });

            btn.setFocusPainted(false);
            //btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBackground(new Color(240, 240, 240));
            dialog.add(btn);
        }

        dialog.setVisible(true);
        return result[0];
    }

    private void doComputerMoveSoon()
    {
        Timer t = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Timer src = (Timer) e.getSource();
                src.stop();
                doComputerMove();
            }
        });
        t.setRepeats(false);
        t.start();
    }

    private void doComputerMove()
    {
        if (game == null)
        {
            return;
        }
        if (game.getPlayer() == game.getUser())
        {
            return;
        }

        String res = app.computerMoveUI(game);
        handleResult(res);

        if (res.startsWith("OK|"))
        {
            refreshAll();
        }
    }

    private void handleResult(String res)
    {
        if (res == null)
        {
            return;
        }

        String msg;
        if(res.contains("|"))
        {
            msg = res.substring(res.indexOf('|') + 1);
        }
        else
        {
            msg = res;
        }

        if (res.startsWith("ERR|"))
        {
            setMessage(msg, true);
            return;
        }

        if (res.startsWith("OK|"))
        {
            setMessage(msg, false);
            return;
        }

        if (res.startsWith("END|"))
        {
            // jocul a fost scos/salvat
            //JOptionPane.showMessageDialog(this, msg);
            game = null;
            appFrame.showGameEnd(res);
            //appFrame.showMainMenu();
        }
    }

    private void doResign()
    {
        if (game == null)
        {
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Are you sure you want to resign?", "Resign", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION)
        {
            return;
        }
        String res = app.resignUI(game);
        handleResult(res);
    }

    private void doSaveAndExit()
    {
        if (game != null) {
            app.saveGame(game);
        }
        appFrame.showMainMenu();
    }
}
