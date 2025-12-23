import java.util.*;

public class Pawn extends Piece
{
    public Pawn(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition, new PawnMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'P';
    }

}
