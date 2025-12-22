public class PieceFactory
{
    public static Piece createPiece(char type, Colors culoare, Position currentPosition)
    {
        if(type == 'R')
        {
            return new Rook(culoare, currentPosition);
        }
        if(type == 'Q')
        {
            return new Queen(culoare, currentPosition);
        }
        if(type == 'B')
        {
            return new Bishop(culoare, currentPosition);
        }
        if(type == 'N')
        {
            return new Knight(culoare, currentPosition);
        }
        if(type == 'P')
        {
            return new Pawn(culoare, currentPosition);
        }
        if(type == 'K')
        {
            return new King(culoare, currentPosition);
        }
        return null;
    }
}
