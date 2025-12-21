import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rook extends Piece
{
    //tura
    public Rook(Colors rookColor, Position currentPosition)
    {
        super(rookColor, currentPosition);
    }

    @Override
    public char type()
    {
        return 'R';
    }

    @Override
    public List<Position> getPossibleMoves(Board board)
    {
        //A1-A8 nu are voie in stanga (prima coloana)
        //H1-H8 nu are voie in dreapta (ultima coloana)
        //A8-H8 nu are voie in fata (W) / nu are voie in spate (B)
        //A1-H1 nu are voie in spate (W) / nu are voie in fata (B)
        //initial: daca pionul din fata/ calul din dreapta sau stanga nu elibereaza pozitie, nu ma pot deplasa
        //Daca intalnesc pe oizontala / verticala o piesa de aceeasi culoare cu tura, ma pot deplasa pana la pozitia piesei - 1 => setPosition(posPiesa - 1)
        //Daca intalnesc pe oizontala / verticala o piesa de culoare diferita cu tura, o pot ataca, si avansez pe pozitia piesei inamice => setPosition(posPiesaAtacata)
        List<Position> mutariPosibile = new ArrayList<>();
        Position pos = this.getPosition();
        char xRook = pos.getX();
        int yRook = pos.getY();
        //ma deplasez in sus => x ramane constant, iar y creste
        for(int i = (yRook + 1); i <= 8; i++)
        {
            Position newRookPosition = new Position(xRook, i);
            Piece obstaclePosition = board.getPieceAt(newRookPosition);
            if(obstaclePosition == null)
            {
                mutariPosibile.add(newRookPosition);
            }
            else
            {
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newRookPosition);
                }
                break;
            }
        }

        //ma deplasez in jos => x ramane constant, iar y scade
        for(int i = (yRook - 1); i >= 1; i--)
        {
            Position newRookPosition = new Position(xRook, i);
            Piece obstaclePosition = board.getPieceAt(newRookPosition);
            //daca nu am nicio piesa in fata, adaug mutarea posibila
            if(obstaclePosition == null)
            {
                mutariPosibile.add(newRookPosition);
            }
            else
            {
                //daca am o piesa de culoare diferita pe verticala, adaug mutarea posibila
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newRookPosition);
                }
                break;
            }
        }

        //ma deplasez in dreapta => x creste, iar y ramane constat
        for(char i = (char)(xRook + 1); i <= 'H'; i++)
        {
            Position newRookPosition = new Position(i, yRook);
            Piece obstaclePosition = board.getPieceAt(newRookPosition);

            //daca nu am nicio piesa in fata/spate/stanga/dreapta, adaug mutarea posibila
            if(obstaclePosition == null)
            {
                mutariPosibile.add(newRookPosition);
            }
            else
            {
                //daca am o piesa de culoare diferita pe orizontala, adaug mutarea posibila
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newRookPosition);
                }
                break;
            }
        }

        //ma deplasez in stanga => x scade, iar y ramane constant
        for(char i = (char)(xRook - 1); i >= 'A'; i--)
        {
            Position newRookPosition = new Position(i, yRook);
            Piece obstaclePosition = board.getPieceAt(newRookPosition);

            //daca nu am nicio piesa pe directia de deplasare, adaug mutarea posibila
            if(obstaclePosition == null)
            {
                mutariPosibile.add(newRookPosition);
            }
            else
            {
                //daca am o piesa de culoare diferita pe orizontala, adaug mutarea posibila
                if(obstaclePosition.getColor() != this.getColor())
                {
                    mutariPosibile.add(newRookPosition);
                }
                break;
            }
        }
        return mutariPosibile;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition)
    {
        List<Position> mutariRook =  this.getPossibleMoves(board);
        Iterator<Position> it = mutariRook.iterator();
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
