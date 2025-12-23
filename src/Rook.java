import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rook extends Piece
{
    //tura
    public Rook(Colors rookColor, Position currentPosition)
    {
        super(rookColor, currentPosition, new RookMoveStrategy());
    }

    @Override
    public char type()
    {
        return 'R';
    }
}
