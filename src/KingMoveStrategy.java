import java.util.ArrayList;
import java.util.List;

public class KingMoveStrategy implements MoveStrategy
{
    public void parcurgereKing(Board board, List<Position> mutariPosibile, char xKing, int yKing, int dx, int dy, Piece king)
    {
        char x = (char)(xKing + dx);
        int y = yKing + dy;
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newKingPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newKingPosition);
            if(obstaclePosition == null)
            {
                mutariPosibile.add(newKingPosition);
            }
            else if(obstaclePosition.getColor() != king.getColor())
            {
                mutariPosibile.add(newKingPosition);
            }
        }
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from)
    {
        List<Position> mutariPosibile = new ArrayList<>();
        Piece king = board.getPieceAt(from);
        if(king == null)
        {
            return mutariPosibile;
        }
        if(king.type() != 'K')
        {
            return mutariPosibile;
        }

        char xKing = from.getX();
        int yKing = from.getY();

        //merge o singura positie in toate directiile
        parcurgereKing(board, mutariPosibile, xKing, yKing, 0, 1, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, 0, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 0, -1, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, 0, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, 1, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, -1, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, 1, king);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, -1, king);

        return mutariPosibile;
    }
}
