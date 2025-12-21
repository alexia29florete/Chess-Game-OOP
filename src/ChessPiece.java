import java.util.List;

public interface ChessPiece
{
    //lista de pozitii valide pt piese
    List<Position> getPossibleMoves(Board board);
    //verific daca pot primi sah
    boolean checkForCheck(Board board, Position kingPosition);
    //returneaza carcterul coresp piesei
    char type();
}
