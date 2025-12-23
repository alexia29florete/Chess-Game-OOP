import java.util.*;

public abstract class Piece implements ChessPiece
{
    private Colors culoare;
    private Position currentPosition;
    private final MoveStrategy moveStrategy;
    public Piece(Colors culoare, Position currentPosition, MoveStrategy moveStrategy)
    {
        this.culoare = culoare;
        this.currentPosition = currentPosition;
        this.moveStrategy = moveStrategy;
    }

    public Colors getColor()
    {
        return culoare;
    }
    public Position getPosition()
    {
        return currentPosition;
    }
    public void setPosition(Position position)
    {
        currentPosition = position;
    }

    public List<Position> getPossibleMoves(Board board)
    {
        if (moveStrategy == null)
        {
            return new ArrayList<>();
        }
        return moveStrategy.getPossibleMoves(board, currentPosition);
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition)
    {
        return getPossibleMoves(board).contains(kingPosition);
    }
    public abstract char type();
}
