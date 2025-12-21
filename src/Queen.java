import java.util.*;

public class Queen extends Piece
{
    public Queen(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition);
    }

    @Override
    public char type()
    {
        return 'Q';
    }

    public void parcurgereRegina(Board board, List<Position> mutariPosibile, char xQueen, int yQueen, int dx, int dy)
    {
        char x = xQueen;
        int y = yQueen;
        while(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            if(!(x == xQueen && y == yQueen))
            {
                Position newQueenPosition = new Position(x, y);
                Piece obstaclePosition = board.getPieceAt(newQueenPosition);

                if(obstaclePosition == null)
                {
                    mutariPosibile.add(newQueenPosition);
                }
                else
                {
                    if(obstaclePosition.getColor() != this.getColor())
                    {
                        mutariPosibile.add(newQueenPosition);
                    }
                    break;
                }
            }
            x += dx;
            y += dy;
        }
    }

    @Override
    public List<Position> getPossibleMoves(Board board)
    {
        // regina este o combinatie intre Rook si Bishop pentru ca poate merge
        // fata/spate/stanga/dreapta (deci vertical/orizontal) oricate patratele
        // diagonal oricate patratele

        List<Position> mutariPosibile = new ArrayList<>();
        Position pos = this.getPosition();
        char xQueen = pos.getX();
        int yQueen = pos.getY();

        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 0, 1);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, 0);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 0, -1);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, 0);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, 1);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, -1);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, 1);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, -1);
        return mutariPosibile;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition)
    {
        List<Position> mutariQueen =  this.getPossibleMoves(board);
        Iterator<Position> it = mutariQueen.iterator();
        while(it.hasNext())
        {
            Position p = it.next();
            if(p.equals(kingPosition))
            {
                return true;
            }
        }
        return false;
    }
}
