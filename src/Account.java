import java.util.*;

public class Account
{
    private String email, password;
    private int points;
    private List<Integer> idGames;

    public Account()
    {
        points = 0;
        idGames = new ArrayList<>();
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPoints()
    {
        return points;
    }
    public void setPoints(int points)
    {
        this.points = points;
    }

    public List<Integer> getGames()
    {
        return idGames;
    }

    public void setGames(List<Integer> idGames)
    {
        this.idGames = idGames;
    }

    public void addGame(int idGame)
    {
        if(!idGames.contains(idGame))
        {
            idGames.add(idGame);
        }
    }

    public void removeGame(int idGame)
    {
        if(idGames.contains(idGame))
        {
            idGames.remove(Integer.valueOf(idGame));
        }
    }
}
