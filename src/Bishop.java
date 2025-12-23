import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bishop extends Piece
{
    public Bishop(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition, new BishopMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'B';
    }
}
