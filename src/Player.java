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
        Random rnd = new Random();
        List<Move> allMoves = new ArrayList<>();
        List<Move> captureMoves = new ArrayList<>();

        for (ChessPair<Position, Piece> pair : pieseDisponibileOwned)
        {
            Position from = pair.getKey();
            Piece piece = pair.getValue();

            for (Position to : piece.getPossibleMoves(board))
            {
                try
                {
                    // verific daca de la from la to e mutare invalida
                    if (board.isValidMove(from, to))
                    {
                        Piece captured = board.getPieceAt(to);
                        Move m = new Move(culoarePiese, from, to, captured);

                        allMoves.add(m);
                        if (captured != null)
                        {
                            captureMoves.add(m);
                        }
                    }

                }
                catch (InvalidMoveException e)
                {
                    //ignor mutarile invalide
                }
            }
        }
        if (!captureMoves.isEmpty())
        {
            return captureMoves.get(rnd.nextInt(captureMoves.size()));
        }
        if (allMoves.isEmpty())
        {
            return null;
        }

        return allMoves.get(rnd.nextInt(allMoves.size()));
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
