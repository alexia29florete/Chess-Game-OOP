import java.util.*;

public class Move
{
    private Colors playerColor;
    private Position from;
    private Position to;
    private Piece capturedPiece;

    public Move(Colors playerColor, Position from, Position to)
    {
        this.playerColor = playerColor;
        this.from = from;
        this.to = to;
        this.capturedPiece = null;
    }

    public Move(String playerColor, String from, String to)
    {
        if(playerColor.equals("WHITE"))
        {
            this.playerColor = Colors.WHITE;
        }
        else
        {
            this.playerColor = Colors.BLACK;
        }
        this.from = new Position(from);
        this.to = new Position(to);
        this.capturedPiece = null;
    }

    public Move(Colors playerColor, Position from, Position to, Piece capturedPiece)
    {
        this.playerColor = playerColor;
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
    }

    public Colors getPlayerColor()
    {
        return playerColor;
    }

    public void setPlayerColor(Colors playerColor)
    {
        this.playerColor = playerColor;
    }

    public Position getFrom()
    {
        return from;
    }

    public void setFrom(Position from)
    {
        this.from = from;
    }

    public Position getTo()
    {
        return to;
    }

    public void setTo(Position to)
    {
        this.to = to;
    }

    public Piece getCapturedPiece()
    {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece)
    {
        this.capturedPiece = capturedPiece;
    }

    @Override
    public String toString()
    {
        String text = playerColor + " moved from " + from + " to " + to;

        if (capturedPiece != null)
        {
            text += " and captured " + capturedPiece;
        }
        else
        {
            text += " no captured pieces";
        }

        return text;
    }
}
