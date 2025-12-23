import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy
{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from)
    {
        //calul este singura piesa care poate sari peste alte piese
        //fie 2 patrate in fata/spate/stanga/dreapta, deci orizontal/vertical si de acolo o pozitie stanga sau dreapta
        //fie o pozitie in fata/spate/stanga/dreapta, deci orizontal/vertical si dupa aceea 2 pozitii la stanga sau dreapta

        //2 pozitii in fata sau spate => 1 pozitie in stanga sau dreapta
        //2 pozitii in dreapta sau stanga => 1 pozitie in sus sau in jos
        //1 pozitie in fata sau spate => 2 pozitii in stanga sau dreapta
        //1 pozitie in dreapta sau stanga => 2 pozitii in sus sau jos
        //ma pot pune pe pozitia respectiva atata timp cat nu e o piesa de aceeasi culoare pe pozitia finala
        //daca intalnesc pe traseu, orice fel de piesa de orice culoare, nu ma deranjeaza
        List<Position> mutariPosibile = new ArrayList<>();
        Piece knight = board.getPieceAt(from);
        if(knight == null)
        {
            return mutariPosibile;
        }
        if(knight.type() != 'N')
        {
            return mutariPosibile;
        }
        char xKnight = from.getX();
        int yKnight = from.getY();

        //2 pozitii in fata si o pozitie in dreapta => x creste cu unu, y creste cu 2
        int y = yKnight + 2;
        char x = (char)(xKnight + 1);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in fata si o pozitie in stanga => x scade cu unu, y creste cu 2
        y = yKnight + 2;
        x = (char)(xKnight - 1);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in spate si o pozitie in dreapta => x creste cu unu, y scade cu 2
        y = yKnight - 2;
        x = (char)(xKnight + 1);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in spate si o pozitie in stanga => x scade cu unu, y scade cu 2
        y = yKnight - 2;
        x = (char)(xKnight - 1);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in dreapta si o pozitie in fata => x creste cu 2, y creste cu unu
        y = yKnight + 1;
        x = (char)(xKnight + 2);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in dreapta si o pozitie in spate => x creste cu 2, y scade cu unu
        y = yKnight - 1;
        x = (char)(xKnight + 2);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in stanga si o pozitie in fata => x scade cu 2, y creste cu unu
        y = yKnight + 1;
        x = (char)(xKnight - 2);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        //2 pozitii in stanga si o pozitie in spate => x scade cu 2, y scade cu unu
        y = yKnight - 1;
        x = (char)(xKnight - 2);
        if(x >= 'A' && x <= 'H' && y >= 1 && y <= 8)
        {
            Position newPositionKnight = new Position(x, y);
            Piece obstaclePiece = board.getPieceAt(newPositionKnight);
            if(obstaclePiece == null || obstaclePiece.getColor() != knight.getColor())
            {
                mutariPosibile.add(newPositionKnight);
            }
        }

        return mutariPosibile;
    }
}
