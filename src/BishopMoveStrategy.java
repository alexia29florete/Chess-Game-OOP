import java.util.*;

public class BishopMoveStrategy implements MoveStrategy
{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from)
    {
        // poate merge doar pe diagonala: stanga, dreapta, sus, jos
        //nu am voie sa depasesc limitele matricei
        List<Position> mutariPosibile = new ArrayList<>();
        Piece bishop = board.getPieceAt(from);
        if(bishop == null)
        {
            return mutariPosibile;
        }
        if(bishop.type() != 'B')
        {
            return mutariPosibile;
        }

        char xBishop = from.getX();
        int yBishop = from.getY();

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
                if(obstaclePosition.getColor() != bishop.getColor())
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
                if(obstaclePosition.getColor() != bishop.getColor())
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
                if(obstaclePosition.getColor() != bishop.getColor())
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
                if(obstaclePosition.getColor() != bishop.getColor())
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
}
