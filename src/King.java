import java.util.*;

public class King extends Piece
{
    public King(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition, new KingMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'K';
    }
}
