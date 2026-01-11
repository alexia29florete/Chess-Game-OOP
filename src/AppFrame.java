import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppFrame extends JFrame
{
    private final Main app;
    //CardLayout ma ajuta sa schimb intre pagini
    private final CardLayout cardLayout = new CardLayout();
    //aici tin toate paginile intr-o "cutie"
    private final JPanel cutie = new JPanel(cardLayout);
    private final LoginPanel loginPanel;
    private final MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private final GameEndInfoPanel endPanel;
    public AppFrame(Main app)
    {
        this.app = app;
        setTitle("Chess Game");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));

        //fac app.write atunci cand inchid
//        addWindowListener(new WindowAdapter()
//        {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                app.write();
//            }
//        });

        loginPanel = new LoginPanel(app, this);
        mainMenuPanel = new MainMenuPanel(app, this);
        gamePanel = new GamePanel(app, this);
        endPanel = new GameEndInfoPanel(app, this);

        cutie.add(loginPanel, "LOGIN");
        cutie.add(mainMenuPanel, "MAIN MENU");
        cutie.add(gamePanel, "CHESS GAME");
        cutie.add(endPanel, "GAME END");

        setContentPane(cutie);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        showLogin();
        setVisible(true);
    }

    public void showLogin()
    {
        //loginPanel.clearFields();
        cardLayout.show(cutie, "LOGIN");
    }

    public void showMainMenu()
    {
        mainMenuPanel.refresh();
        cardLayout.show(cutie, "MAIN MENU");
    }

    public void showGame(Game g)
    {
        gamePanel.setGame(g);
        cardLayout.show(cutie, "CHESS GAME");
    }

    public void showGameEnd(String res)
    {
        endPanel.setResult(res);
        cardLayout.show(cutie, "GAME END");
    }
}
