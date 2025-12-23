public class CaptureScore implements CaptureScoreStrategy
{
    public int pointsForCapturedPieces(Piece capturedPiece)
    {
        if (capturedPiece == null)
        {
            return 0;
        }
        if(capturedPiece.type() == 'Q')
        {
            return 90;
        }
        else if(capturedPiece.type() == 'R')
        {
            return 50;
        }
        else if(capturedPiece.type() == 'B')
        {
            return 30;
        }
        else if(capturedPiece.type() == 'N')
        {
            return 30;
        }
        else if(capturedPiece.type() == 'P')
        {
            return 10;
        }
        else
        {
            return 0;
        }
    }
}
