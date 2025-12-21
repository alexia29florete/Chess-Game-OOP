import java.util.*;
import java.util.TreeSet;

public class Player
{
    String name, email;
    Colors culoarePiese;
    private List<Piece> pieseCapturate;
    private TreeSet<ChessPair<Position, Piece>> pieseDisponibileOwned;
    private int puncteAcumulate;

    public Player(String name, String email, Colors culoarePiese)
    {
        this.name = name;
        this.email = email;
        this.culoarePiese = culoarePiese;
        pieseCapturate = new ArrayList<>();
        pieseDisponibileOwned = new TreeSet<>();
        puncteAcumulate = 0;
    }

    public Player(String email, Colors culoarePiese)
    {
        this.email = email;
        this.culoarePiese = culoarePiese;
        pieseCapturate = new ArrayList<>();
        pieseDisponibileOwned = new TreeSet<>();
        puncteAcumulate = 0;
    }

    public Player(String email, String culoarePiese)
    {
        if(culoarePiese.equals("WHITE"))
        {
            this.culoarePiese = Colors.WHITE;
        }
        else
        {
            this.culoarePiese = Colors.BLACK;
        }
        this.email = email;
        pieseCapturate = new ArrayList<>();
        pieseDisponibileOwned = new TreeSet<>();
        puncteAcumulate = 0;
    }

    public String getName()
    {
        if (this.name != null && !this.name.isEmpty())
        {
            return name;
        }
        else
        {
            return email;
        }
    }

    public int getPieceValue(Piece capturedPiece)
    {
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

    public void updateOwnPieces(Board board)
    {
        //nu vreau sa am duplicate
        pieseDisponibileOwned.clear();

        //parcurg toate piesele de pe tabla actuala
        for(ChessPair<Position, Piece> pair : board.piecePosition)
        {
            //daca gasesc piese de culoarea mea
            if(pair.getValue().getColor() == culoarePiese)
            {
                //le adaug ca fiind piesele detinute de mine
                pieseDisponibileOwned.add(pair);
            }
        }
    }

    public void makeMove(Position from, Position to, Board board, Game game) throws InvalidMoveException, InvalidCommandException {

        Piece piesaCurenta = board.getPieceAt(from);
        if(piesaCurenta == null || piesaCurenta.getColor() != culoarePiese)
        {
            throw new InvalidMoveException("Piesa nu va apartine!");
        }
        if(!board.isValidMove(from, to))
        {
            throw new InvalidMoveException("Mutarea nu este valida");
        }

        char promotionChoice = 'Q';

        if(piesaCurenta instanceof Pawn)
        {
            if((to.getY() == 8 && piesaCurenta.getColor() == Colors.WHITE) || (to.getY() == 1 && piesaCurenta.getColor() == Colors.BLACK))
            {
                //daca acest jucator este user
                if(game.getUser() == this)
                {
                    //cer input
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Promoted pawn! Choose: Q/R/N/B");
                    String input = scanner.nextLine().trim().toUpperCase();
                    promotionChoice = input.charAt(0);
                }
            }
        }
        Piece capturedPiece = board.movePiece(from, to, promotionChoice);

        if(capturedPiece != null)
        {
            pieseCapturate.add(capturedPiece);
            puncteAcumulate += getPieceValue(capturedPiece);
        }

        updateOwnPieces(board);

        game.addMove(this, from, to, capturedPiece);
    }

    public Move getComputerMove(Board board)
    {
        updateOwnPieces(board);

        List<Move> checkMoves = new ArrayList<>();
        List<Move> allValidMoves = new ArrayList<>();
        List<Move> captureSafeMoves = new ArrayList<>();

        Colors opponentColor = Colors.WHITE;
        if(culoarePiese == Colors.WHITE)
        {
            opponentColor = Colors.BLACK;
        }

        for (ChessPair<Position, Piece> pair : pieseDisponibileOwned)
        {
            Position from = pair.getKey();
            Piece piece = pair.getValue();

            for (Position to : piece.getPossibleMoves(board))
            {
                try
                {
                    // verific daca de la from la to e mutare invalida
                    if (!board.isValidMove(from, to))
                    {
                        continue;
                    }
                    //fac o simulare
                    Piece captured = board.getPieceAt(to);

                    //elimin piesa de la from
                    board.piecePosition.remove(new ChessPair<>(from, piece));
                    if (captured != null)
                    {
                        board.piecePosition.remove(new ChessPair<>(to, captured));
                    }

                    piece.setPosition(to);
                    board.piecePosition.add(new ChessPair<>(to, piece));

                    //verific daca adversarul e in sah
                    if (board.esteKingInCheck(opponentColor))
                    {
                        checkMoves.add(new Move(culoarePiese, from, to, captured));
                    }
                    else if(captured != null && board.isSquareAttacked(to, opponentColor))
                    {
                        captureSafeMoves.add(new Move(culoarePiese, from, to, captured));
                    }
                    else
                    {
                        allValidMoves.add(new Move(culoarePiese, from, to, captured));
                    }

                    //refac tabla la starea initiala
                    board.piecePosition.remove(new ChessPair<>(to, piece));
                    piece.setPosition(from);
                    board.piecePosition.add(new ChessPair<>(from, piece));

                    if (captured != null)
                    {
                        captured.setPosition(to);
                        board.piecePosition.add(new ChessPair<>(to, captured));
                    }

                }
                catch (InvalidMoveException e)
                {
                    //ignor mutarile invalide
                }
            }
        }

        //prioritizez mutarile care dau sah
        if (!checkMoves.isEmpty())
        {
            Collections.shuffle(checkMoves);
            return checkMoves.get(0);
        }

        //captura
        if (!captureSafeMoves.isEmpty())
        {
            Collections.shuffle(captureSafeMoves);
            return captureSafeMoves.get(0);
        }

        //mutari random valide
        if (!allValidMoves.isEmpty())
        {
            Collections.shuffle(allValidMoves);
            return allValidMoves.get(0);
        }
        return null;
    }

    public List<Piece> getCapturedPieces()
    {
        return pieseCapturate;
    }

    public void setCapturedPieces(List<Piece> pieseCapturate)
    {
        this.pieseCapturate = pieseCapturate;
    }

    public TreeSet<ChessPair<Position, Piece>> getOwnedPieces()
    {
        return pieseDisponibileOwned;
    }

    public void setOwnedPieces(TreeSet<ChessPair<Position, Piece>> pieseDisponibile)
    {
        this.pieseDisponibileOwned = pieseDisponibile;
    }

    public int getPoints()
    {
        return puncteAcumulate;
    }

    public void setPoints(int puncteAcumulate)
    {
        this.puncteAcumulate = puncteAcumulate;
    }

    public Colors getColor()
    {
        return culoarePiese;
    }

    public void setColor(Colors culoarePiese)
    {
        this.culoarePiese = culoarePiese;
    }

    public void addFinalPoints(int finalBonus)
    {
        this.puncteAcumulate += finalBonus;
    }
}
