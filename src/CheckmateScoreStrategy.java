public class CheckmateScoreStrategy implements EndGameScoreStrategy
{
    @Override
    public int endGamePoints()
    {
        return 300;
    }

    @Override
    public String getMessage()
    {
        return "Checkmate";
    }
}
