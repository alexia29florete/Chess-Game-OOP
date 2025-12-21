import java.util.*;

public class King extends Piece
{
    public King(Colors culoare, Position currentPosition)
    {
        super(culoare, currentPosition);
    }

    @Override
    public char type()
    {
        return 'K';
    }

    public void parcurgereKing(Board board, List<Position> mutariPosibile, char xKing, int yKing, int dx, int dy)
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
            else if(obstaclePosition.getColor() != this.getColor())
            {
                mutariPosibile.add(newKingPosition);
            }
        }
    }

    @Override
    public List<Position> getPossibleMoves(Board board)
    {
        List<Position> mutariPosibile = new ArrayList<>();
        Position pos = this.getPosition();
        char xKing = pos.getX();
        int yKing = pos.getY();

        //merge o singura positie in toate directiile
        parcurgereKing(board, mutariPosibile, xKing, yKing, 0, 1);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, 0);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 0, -1);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, 0);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, 1);
        parcurgereKing(board, mutariPosibile, xKing, yKing, 1, -1);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, 1);
        parcurgereKing(board, mutariPosibile, xKing, yKing, -1, -1);

        return mutariPosibile;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition)
    {
        return false;
    }
}
