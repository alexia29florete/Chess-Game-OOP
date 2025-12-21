import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bishop extends Piece
{
    public Bishop(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition);
    }

    @Override
    public char type()
    {
        return 'B';
    }

    @Override
    public List<Position> getPossibleMoves(Board board)
    {
        // poate merge doar pe diagonala: stanga, dreapta, sus, jos
        //nu am voie sa depasesc limitele matricei
        List<Position> mutariPosibile = new ArrayList<>();
        Position pos = this.getPosition();
        char xBishop = pos.getX();
        int yBishop = pos.getY();

        //cand ma duc in dreapta sus => x creste, y creste
        char x = (char)(xBishop + 1);
        int y = yBishop + 1;
        while(x <= 'H' && y <= 8)
        {
            Position newBishopPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newBishopPosition);

            if(obstaclePosition == null)
            {
                mutariPosibile.add(newBishopPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newBishopPosition);
                }
                break;
            }
            x++;
            y++;

        }

        //cand ma duc in stanga sus => x scade, y creste
        x = (char)(xBishop - 1);
        y = yBishop + 1;
        while(x >= 'A' && y <= 8)
        {
            Position newBishopPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newBishopPosition);

            if(obstaclePosition == null)
            {
                mutariPosibile.add(newBishopPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newBishopPosition);
                }
                break;
            }
            x--;
            y++;
        }

        //cand ma duc in stanga jos => x scade, y scade
        x = (char)(xBishop - 1);
        y = yBishop - 1;
        while(x >= 'A' && y >= 1)
        {
            Position newBishopPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newBishopPosition);

            if(obstaclePosition == null)
            {
                mutariPosibile.add(newBishopPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newBishopPosition);
                }
                break;
            }
            x--;
            y--;
        }

        //cand ma duc in dreapta jos -> x creste, y scade
        x = (char)(xBishop + 1);
        y = yBishop - 1;
        while(x <= 'H' && y >= 1)
        {
            Position newBishopPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newBishopPosition);

            if(obstaclePosition == null)
            {
                mutariPosibile.add(newBishopPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newBishopPosition);
                }
                break;
            }
            x++;
            y--;
        }
        return mutariPosibile;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition)
    {
        List<Position> mutariBishop =  this.getPossibleMoves(board);
        Iterator<Position> it = mutariBishop.iterator();
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
