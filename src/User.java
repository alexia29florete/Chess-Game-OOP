import java.util.ArrayList;
import java.util.List;

public class User
{
    private String email;
    private String password;
    private List<Game> jocuriAsociate;
    private int totalPoints;

    public User(String email, String password, int totalPoints)
    {
        this.email = email;
        this.password = password;
        this.totalPoints = totalPoints;
        jocuriAsociate = new ArrayList<>();
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public void addGame(Game game)
    {
        if (game != null && !jocuriAsociate.contains(game))
        {
            jocuriAsociate.add(game);
        }
    }

    public void removeGame(Game game)
    {
        jocuriAsociate.remove(game);
    }

    public List<Game> getActiveGames()
    {
        return new ArrayList<>(jocuriAsociate);
    }

    public int getPoints()
    {
        return totalPoints;
    }

    public void setPoints(int points)
    {

        this.totalPoints = points;
    }
}
