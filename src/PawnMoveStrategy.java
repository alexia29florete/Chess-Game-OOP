import java.util.*;

public class PawnMoveStrategy implements MoveStrategy
{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from)
    {
        //se poate muta in fata 2 pozitii la prima mutare si in rest cate una
        //si poate ataca pe diagonala, adica se poate muta pe diagonala daca vad o piesa de culoare diferita
        List<Position> mutariPosibile = new ArrayList<>();
        Piece pawn = board.getPieceAt(from);
        if(pawn == null)
        {
            return mutariPosibile;
        }
        if(pawn.type() != 'P')
        {
            return mutariPosibile;
        }
        char xPawn = from.getX();
        int yPawn = from.getY();
        //ma pot misca doar in fata, deci x ramane constant, doar y se modifica
        //daca sunt alb => y creste, daca sunt negru => y scade
        if(pawn.getColor() == Colors.WHITE)
        {
            //daca sunt piesa alba, atunci y creste, x ramane constant
            //daca sunt piesa alba, cand atac, ma mut cu o pozitie in sus diagonal, stanga sus/ dreapta sus:
            //stanga sus => x scade, y creste
            //dreapta sus => x creste, y creste
            int fowardWhite = yPawn + 1;
            if(fowardWhite >= 1 && fowardWhite <= 8)
            {
                Position newPawnPosition = new Position(xPawn, fowardWhite);
                Piece obstaclePosition = board.getPieceAt(newPawnPosition);
                if(obstaclePosition == null)
                {
                    mutariPosibile.add(newPawnPosition);

                    if(yPawn == 2)
                    {
                        //pot face o mutare de 2 patratele
                        Position newPawnPositionMutari2 = new Position(xPawn, yPawn + 2);
                        Piece obstaclePositionMutari2 = board.getPieceAt(newPawnPositionMutari2);
                        if(obstaclePositionMutari2 == null)
                        {
                            mutariPosibile.add(newPawnPositionMutari2);
                        }
                    }
                }
            }

            //atac diagonal
            //stanga sus => x scade, y creste
            if(xPawn > 'A' && fowardWhite <= 8)
            {
                Position newPawnAttack = new Position((char)(xPawn - 1), fowardWhite);
                Piece obstaclePosition = board.getPieceAt(newPawnAttack);
                if(obstaclePosition != null && pawn.getColor() != obstaclePosition.getColor())
                {
                    mutariPosibile.add(newPawnAttack);
                }
            }

            //dreapta sus => x creste, y creste
            if(xPawn < 'H' && fowardWhite <= 8)
            {
                Position newPawnAttack = new Position((char)(xPawn + 1), fowardWhite);
                Piece obstaclePosition = board.getPieceAt(newPawnAttack);
                if(obstaclePosition != null && pawn.getColor() != obstaclePosition.getColor())
                {
                    mutariPosibile.add(newPawnAttack);
                }
            }

        }
        if(pawn.getColor() == Colors.BLACK)
        {
            //daca sunt piesa neagra, atunci y scade, x ramane constant
            //daca sunt piesa neagra, cand atac, ma mut cu o pozitie in jos diagonal, stanga jos/ dreapta jos:
            //stanga jos => x scade, y scade
            //dreapta jos => x creste, y scade
            int fowardBlack = yPawn - 1;
            if(fowardBlack >= 1)
            {
                Position newPawnPosition = new Position(xPawn, fowardBlack);
                Piece obstaclePosition = board.getPieceAt(newPawnPosition);
                if(obstaclePosition == null)
                {
                    mutariPosibile.add(newPawnPosition);
                    if(yPawn == 7)
                    {
                        //pot face o mutare de 2 patratele
                        Position newPawnPositionMutari2 = new Position(xPawn, yPawn - 2);
                        Piece obstaclePositionMutari2 = board.getPieceAt(newPawnPositionMutari2);
                        if(obstaclePositionMutari2 == null)
                        {
                            mutariPosibile.add(newPawnPositionMutari2);
                        }
                    }
                }
            }
            //atac diagonal
            //stanga jos => x scade, y scade
            if(xPawn > 'A' && fowardBlack >= 1)
            {
                Position newPawnAttack = new Position((char)(xPawn - 1), fowardBlack);
                Piece obstaclePosition = board.getPieceAt(newPawnAttack);
                if(obstaclePosition != null && pawn.getColor() != obstaclePosition.getColor())
                {
                    mutariPosibile.add(newPawnAttack);
                }
            }

            //dreapta jos => x creste, y scade
            if(xPawn < 'H' && fowardBlack >= 1)
            {
                Position newPawnAttack = new Position((char)(xPawn + 1), fowardBlack);
                Piece obstaclePosition = board.getPieceAt(newPawnAttack);
                if(obstaclePosition != null && pawn.getColor() != obstaclePosition.getColor())
                {
                    mutariPosibile.add(newPawnAttack);
                }
            }
        }
        return mutariPosibile;
    }
}
