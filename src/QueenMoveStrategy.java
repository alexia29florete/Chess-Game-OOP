import java.util.ArrayList;
import java.util.List;

public class QueenMoveStrategy implements MoveStrategy
{
    public void parcurgereRegina(Board board, List<Position> mutariPosibile, char xQueen, int yQueen, int dx, int dy, Piece queen)
    {
        char x = (char)(xQueen + dx);
        int y = yQueen + dy;
        while(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newQueenPosition = new Position(x, y);
            Piece obstaclePosition = board.getPieceAt(newQueenPosition);

            if(obstaclePosition == null)
            {
                mutariPosibile.add(newQueenPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != queen.getColor())
                {
                    mutariPosibile.add(newQueenPosition);
                }
                break;
            }
            x += dx;
            y += dy;
        }
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position from)
    {
        // regina este o combinatie intre Rook si Bishop pentru ca poate merge
        // fata/spate/stanga/dreapta (deci vertical/orizontal) oricate patratele
        // diagonal oricate patratele
        List<Position> mutariPosibile = new ArrayList<>();
        Piece queen = board.getPieceAt(from);
        if(queen == null)
        {
            return mutariPosibile;
        }
        if(queen.type() != 'Q')
        {
            return mutariPosibile;
        }

        char xQueen = from.getX();
        int yQueen = from.getY();

        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 0, 1, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, 0, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 0, -1, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, 0, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, 1, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, 1, -1, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, 1, queen);
        parcurgereRegina(board, mutariPosibile, xQueen, yQueen, -1, -1, queen);
        return mutariPosibile;
    }
}
