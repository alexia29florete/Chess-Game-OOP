public class EqualityScoreStrategy implements EndGameScoreStrategy
{
    @Override
    public int endGamePoints()
    {
        return 150;
    }

    @Override
    public String getMessage()
    {
        return "Equality";
    }
}