import java.util.*;

public class Board
{
    TreeSet<ChessPair<Position, Piece>> piecePosition;
    public Board()
    {
        piecePosition = new TreeSet<>();
    }

    public void initialize()
    {
        Piece whiteKing = new King(Colors.WHITE, new Position('E', 1));
        piecePosition.add(new ChessPair<>(new Position('E', 1), whiteKing));
        Piece blackKing = new King(Colors.BLACK, new Position('E', 8));
        piecePosition.add(new ChessPair<>(new Position('E', 8), blackKing));

        Piece whiteQueen = new Queen(Colors.WHITE, new Position('D', 1));
        piecePosition.add(new ChessPair<>(new Position('D', 1), whiteQueen));
        Piece blackQueen = new Queen(Colors.BLACK, new Position('D', 8));
        piecePosition.add(new ChessPair<>(new Position('D', 8), blackQueen));

        Piece whiteRook1 = new Rook(Colors.WHITE, new Position('A', 1));
        piecePosition.add(new ChessPair<>(new Position('A', 1), whiteRook1));
        Piece whiteRook2 = new Rook(Colors.WHITE, new Position('H', 1));
        piecePosition.add(new ChessPair<>(new Position('H', 1), whiteRook2));
        Piece blackRook1 = new Rook(Colors.BLACK, new Position('A', 8));
        piecePosition.add(new ChessPair<>(new Position('A', 8), blackRook1));
        Piece blackRook2 = new Rook(Colors.BLACK, new Position('H', 8));
        piecePosition.add(new ChessPair<>(new Position('H', 8), blackRook2));

        Piece whiteKnight1 = new Knight(Colors.WHITE, new Position('B', 1));
        piecePosition.add(new ChessPair<>(new Position('B', 1), whiteKnight1));
        Piece whiteKnight2 = new Knight(Colors.WHITE, new Position('G', 1));
        piecePosition.add(new ChessPair<>(new Position('G', 1), whiteKnight2));
        Piece blackKnight1 = new Knight(Colors.BLACK, new Position('B', 8));
        piecePosition.add(new ChessPair<>(new Position('B', 8), blackKnight1));
        Piece blackKnight2 = new Knight(Colors.BLACK, new Position('G', 8));
        piecePosition.add(new ChessPair<>(new Position('G', 8), blackKnight2));

        Piece whiteBishop1 = new Bishop(Colors.WHITE, new Position('C', 1));
        piecePosition.add(new ChessPair<>(new Position('C', 1), whiteBishop1));
        Piece whiteBishop2 = new Bishop(Colors.WHITE, new Position('F', 1));
        piecePosition.add(new ChessPair<>(new Position('F', 1), whiteBishop2));
        Piece blackBishop1 = new Bishop(Colors.BLACK, new Position('C', 8));
        piecePosition.add(new ChessPair<>(new Position('C', 8), blackBishop1));
        Piece blackBishop2 = new Bishop(Colors.BLACK, new Position('F', 8));
        piecePosition.add(new ChessPair<>(new Position('F', 8), blackBishop2));

        for(char i = 'A'; i <= 'H'; i++)
        {
            piecePosition.add(new ChessPair<>(new Position(i, 2), new Pawn(Colors.WHITE, new Position(i, 2))));
            piecePosition.add(new ChessPair<>(new Position(i, 7), new Pawn(Colors.BLACK, new Position(i, 7))));
        }
    }

    public Piece promotePawn(Piece piece, Position to, char choice) throws InvalidCommandException
    {
        Colors promotionColor = piece.getColor();
        switch (choice)
        {
            case 'Q':
                return new Queen(promotionColor, to);
            case 'R':
                return new Rook(promotionColor, to);
            case 'B':
                return new Bishop(promotionColor, to);
            case 'N':
                return new Knight(promotionColor, to);
            default:
                throw new InvalidCommandException("Not a valid option: " + choice + ". It should be Q, R, B or N.");
        }
    }

    public Piece movePiece(Position from, Position to, char promotionChoice) throws InvalidMoveException, InvalidCommandException {
        if(!this.isValidMove(from, to))
        {
            throw new InvalidMoveException("Mutare nevalida!");
        }
        Piece piesaCurenta = this.getPieceAt(from);
        Piece potentialObstaclePiece = this.getPieceAt(to);

        //elimin piesa pe care am mutat-o
        piecePosition.remove(new ChessPair<>(from, piesaCurenta));

        //verific daca pe pozitia pe care ma duc exista o piesa adversa
        Piece capturedPiece = null;
        if(potentialObstaclePiece != null && potentialObstaclePiece.getColor() != piesaCurenta.getColor())
        {
            //elimin piesa capturata
            piecePosition.remove(new ChessPair<>(to, potentialObstaclePiece));
            capturedPiece = potentialObstaclePiece;
        }
        piesaCurenta.setPosition(to);

        //daca piesa mutata este pion si ajung pe ultima linie, il promovez
        if(piesaCurenta instanceof Pawn)
        {
            //daca sunt pion negru si to este pe linia 1 sau daca sunt pion alb si to este pe linia 8
            if ((to.getY() == 8 && piesaCurenta.getColor() == Colors.WHITE) || (to.getY() == 1 && piesaCurenta.getColor() == Colors.BLACK))
            {
                //pun si eu o alegere oarecare, pt ca oricum o gestionez in game
                piesaCurenta = promotePawn(piesaCurenta, to, promotionChoice);
            }
        }
        //adaug piesa la noua pozitie pe care se gaseste
        piecePosition.add(new ChessPair<>(to, piesaCurenta));
        return capturedPiece;
    }

    public boolean isSquareAttacked(Position pos, Colors byColor)
    {
        for (ChessPair<Position, Piece> pair : piecePosition)
        {
            if (pair.getValue().getColor() == byColor)
            {
                if (pair.getValue().getPossibleMoves(this).contains(pos))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public Piece getPieceAt(Position position)
    {
        for(ChessPair<Position, Piece> pereche : piecePosition)
        {
            if(pereche.getKey().equals(position))
            {
                return pereche.getValue();
            }
        }
        return null;
    }

    public boolean esteKingInCheck(Colors color)
    {
        Position kingPos = null;
        //iau lista tuturor pieselor de pe tabla si vad daca piesa e rege de culoarea cautata
        //ii retin pozitia
        for(ChessPair<Position, Piece> pair : piecePosition)
        {
            if(pair.getValue().type() == 'K' && pair.getValue().getColor() == color)
            {
                kingPos = pair.getValue().getPosition();
            }
        }

        if(kingPos == null)
        {
            return false;
        }

        //iau lista pieselor de pe tabla
        for(ChessPair<Position, Piece> pair : piecePosition)
        {
            //ma uit ce piesa este adversara, de culoare diferita
            if(pair.getValue().getColor() != color)
            {
                //daca printre mutarile posibile ale pieselor adverse se gaseste pozitia pe care e regele => sah
                if(pair.getValue().getPossibleMoves(this).contains(kingPos))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidMove(Position from, Position to) throws InvalidMoveException
    {
        if(from.getX() < 'A' || from.getX() > 'H' || from.getY() < 1 || from.getY() > 8 || to.getX() < 'A' || to.getX() > 'H' || to.getY() < 1 || to.getY() > 8)
        {
            throw new InvalidMoveException("Pozitiile sunt in afara tablei de sah");
        }
        Piece piesaCurenta = getPieceAt(from);
        if(piesaCurenta == null)
        {
            throw new InvalidMoveException("Nu exista piesa la pozitia de start");
        }

        List<Position> mutariPosibile = piesaCurenta.getPossibleMoves(this);
        if(!mutariPosibile.contains(to))
        {
            throw new InvalidMoveException("Mutarea nu este posibila pentru tipul de piesa");
        }

        //simulez mutarea pentru a verifica daca o sa fiu in sah sau nu
        Piece potentialPieceAtTo = this.getPieceAt(to);
        piecePosition.remove(new ChessPair<>(from, piesaCurenta));
        //elimin piesa capturata
        if(potentialPieceAtTo != null && potentialPieceAtTo.getColor() != piesaCurenta.getColor())
        {
            piecePosition.remove(new ChessPair<>(to, potentialPieceAtTo));
        }

        //mut piesa la destinatie
        piesaCurenta.setPosition(to);
        piecePosition.add(new ChessPair<>(to, piesaCurenta));

        //verific daca regele e in singuranta, adica daca nu e in sah
        boolean kingSafe = !esteKingInCheck(piesaCurenta.getColor());

        //restaurez tabla cum era inainte de a verifica sahul
        piecePosition.remove(new ChessPair<>(to, piesaCurenta));

        //pun piesa inapoi in from
        piesaCurenta.setPosition(from);
        piecePosition.add(new ChessPair<>(from, piesaCurenta));

        //pun piesa capturata inapoi
        if(potentialPieceAtTo != null && potentialPieceAtTo.getColor() != piesaCurenta.getColor())
        {
            potentialPieceAtTo.setPosition(to);
            piecePosition.add(new ChessPair<>(to, potentialPieceAtTo));
        }

        if(!kingSafe)
        {
            throw new InvalidMoveException("King would be in check with this move!");
        }
        return true;
    }

    public void printBoard(Colors userColor)
    {
        if(userColor == Colors.WHITE)
        {
            System.out.println("  -------------------------------------------------");

            for(int i = 8; i >= 1; i--)
            {
                System.out.print(i + " |");
                for(char j = 'A'; j <= 'H'; j++)
                {
                    Piece piesa = getPieceAt(new Position(j, i));

                    String displayString;
                    if(piesa == null)
                    {
                        displayString = " ... ";
                    }
                    else
                    {
                        char color = 0;
                        if(piesa.getColor() == Colors.WHITE)
                        {
                            color = 'W';
                        }
                        else if (piesa.getColor() == Colors.BLACK)
                        {
                            color = 'B';
                        }
                        displayString = " " + piesa.type() + "-" + color + " ";
                    }
                    System.out.print(displayString + "|");
                }
                System.out.println();
            }
            System.out.println("  -------------------------------------------------");
            System.out.println("  |  A  |  B  |  C  |  D  |  E  |  F  |  G  |  H  |");
        }
        else
        {
            System.out.println("  -------------------------------------------------");

            for(int i = 1; i <= 8; i++)
            {
                System.out.print(i + " |");
                for(char j = 'H'; j >= 'A'; j--)
                {
                    Piece piesa = getPieceAt(new Position(j, i));

                    String displayString;
                    if(piesa == null)
                    {
                        displayString = " ... ";
                    }
                    else
                    {
                        char color = 0;
                        if(piesa.getColor() == Colors.WHITE)
                        {
                            color = 'W';
                        }
                        else if (piesa.getColor() == Colors.BLACK)
                        {
                            color = 'B';
                        }
                        displayString = " " + piesa.type() + "-" + color + " ";
                    }
                    System.out.print(displayString + "|");
                }
                System.out.println();
            }
            System.out.println("  -------------------------------------------------");
            System.out.println("  |  H  |  G  |  F  |  E  |  D  |  C  |  B  |  A  |");
        }
    }
}
