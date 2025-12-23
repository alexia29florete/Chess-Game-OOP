import java.util.*;

public class Knight extends Piece
{
    public Knight(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition, new KnightMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'N';
    }
}
