public abstract class Piece implements ChessPiece
{
    private Colors culoare;
    private Position currentPosition;
    public Piece(Colors culoare, Position currentPosition)
    {
        this.culoare = culoare;
        this.currentPosition = currentPosition;
    }

    public Colors getColor()
    {
        return culoare;
    }
    public Position getPosition()
    {
        return currentPosition;
    }
    public void setPosition(Position position)
    {
        currentPosition = position;
    }
}
