import java.util.*;

public class Queen extends Piece
{
    public Queen(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition, new QueenMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'Q';
    }
}
