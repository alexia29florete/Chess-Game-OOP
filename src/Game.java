import java.util.*;

public class Game
{
    long idGame;
    Board board;
    private List<Player> players; //players[0] = WHITE, players[1] = BLACK
    List<Move> mutari;
    int idPlayerCurent;
    private Player user;
    private Player computer;

    private final List<GameObserver> observers = new ArrayList<>();

    public Game()
    {
        board = new Board();
        mutari = new ArrayList<>();
        players = new ArrayList<>();
        idPlayerCurent = 0;
    }

    public Game(long id, Player user, Player computer)
    {
        this.idGame = id;
        this.board = new Board();
        this.mutari = new ArrayList<>();

        this.user = user;
        this.computer = computer;

        if (user.getColor() == Colors.WHITE)
        {
            this.players = Arrays.asList(user, computer);
        }
        else
        {
            this.players = Arrays.asList(computer, user);
        }

        this.idPlayerCurent = 0;
    }

    public long getId()
    {
        return idGame;
    }
    public void setId(long idGame)
    {
        this.idGame = idGame;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(List<Piece> pieces)
    {
        board = new Board();
        for (Piece p : pieces)
        {
            board.piecePosition.add(new ChessPair<>(p.getPosition(), p));
        }
    }

    public Player getPlayer()
    {
        return players.get(idPlayerCurent);
    }

    public Player getUser()
    {
        return user;
    }

    public void setComputer(Player computer)
    {
        this.computer = computer;
    }

    public void setUser(Player user)
    {
        this.user = user;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
    {
        if(players == null || players.size() != 2)
        {
            return;
        }

        if(players.get(0).getColor() == Colors.WHITE)
        {
            this.players = Arrays.asList(players.get(0), players.get(1));
        }
        else
        {
            this.players = Arrays.asList(players.get(1), players.get(0));
        }
    }
    public void setCurrentPlayerColor(String colors)
    {
        if(colors == null || players == null || players.size() != 2)
        {
            return;
        }
        if(colors.equals("WHITE"))
        {
            idPlayerCurent = 0;
        }
        else if(colors.equals("BLACK"))
        {
            idPlayerCurent = 1;
        }
    }

    public Colors getCurrentPlayerColor()
    {
        if(players == null || players.size() != 2)
        {
            return null;
        }
        return players.get(idPlayerCurent).getColor();
    }

    public Player getOpponent()
    {
        return players.get(1 - idPlayerCurent);
    }

    public List<Move> getMoves()
    {
        return mutari;
    }
    public void setMoves(List<Move> mutari)
    {
        this.mutari = mutari;
    }

    public int getPlayerId()
    {
        return idPlayerCurent;
    }
    public void setPlayerId(int idPlayerCurent)
    {
        this.idPlayerCurent = idPlayerCurent;
    }

    public List<Piece> getBoardPieces()
    {
        List<Piece> pieces = new ArrayList<>();
        for (ChessPair<Position, Piece> pair : board.piecePosition)
        {
            pieces.add(pair.getValue());
        }
        return pieces;
    }


    public void start()
    {
        board.initialize();
        mutari.clear();

        //vad ce culoare are jucatorul curent
//        if(players != null && players.size() == 2)
//        {
//            if(players.get(0).getColor() == Colors.WHITE)
//            {
//                idPlayerCurent = 0;
//            }
//            else
//            {
//                idPlayerCurent = 1;
//            }
//        }
//        System.out.println("Is " + getPlayer().getName() + "'s turn");
//        board.printBoard(user.getColor());
    }

    public void resume()
    {
        //sincronizez piesele detinute de jucatori cu jocul si tabla
        for(Player p : players)
        {
            p.updateOwnPieces(board);
        }

//        System.out.println("Is " + getPlayer().getName() + "'s turn");
//        board.printBoard(user.getColor());
//        if(board.esteKingInCheck(getPlayer().getColor()))
//        {
//            System.out.println(getPlayer().getName() + " is in check");
//        }
    }

    public void switchPlayer()
    {
        if(players == null || players.size() != 2)
        {
            return;
        }
        idPlayerCurent = 1 - idPlayerCurent;

        notifyPlayerSwitch(getPlayer());
    }

    public boolean checkForCheckMate()
    {
        for (Player p : players)
        {
            p.updateOwnPieces(board);
        }
        //daca regele jucatorului curent nu este in sah => return false
        if(!board.esteKingInCheck(players.get(idPlayerCurent).getColor()))
        {
            return false;
        }

        //acum regele e in sah si trebuie sa verific daca exista vreo mutare posibila pentru a salva regele
        for(ChessPair<Position, Piece> pair : players.get(idPlayerCurent).getOwnedPieces())
        {
            Piece piesa = pair.getValue();
            Position startingPoint = pair.getKey();

            //iau toate mutarile posibile ale fiecarei piese disponibile pe tabla acum
            for(Position possibleTo : piesa.getPossibleMoves(board))
            {
                try
                {
                    //daca gasesc o mutare valida, nu e sah mat
                    if(board.isValidMove(startingPoint, possibleTo))
                    {
                        return false;
                    }
                }
                catch (InvalidMoveException e)
                {

                }
            }
        }
        //daca parcurg toate piesele si toate miscarile posibile si nu gasesc nimic => sah mat
        return true;
    }

    public void addMove(Player p, Position from, Position to, Piece capturedPiece)
    {
        Move mutare = new Move(p.getColor(), from, to, capturedPiece);
        mutari.add(mutare);

        notifyMoveMade(mutare);
        if (capturedPiece != null)
        {
            notifyPieceCaptured(capturedPiece);
        }
    }

    public void handleGameEnd(Player winner, Player loser, EndGameScoreStrategy strategy)
    {
        winner.addFinalPoints(strategy.endGamePoints());
        loser.addFinalPoints(-strategy.endGamePoints());

        System.out.println("\n=============================================");
        System.out.println("Game ended: " + strategy.getMessage());
        System.out.println("Winner is " + winner.getName());
        System.out.println("Final score for " + winner.getName() + " is " + winner.getPoints() + " points");
        System.out.println("Final score for " + loser.getName() + " is " + loser.getPoints() + " points");
    }


    public void endByCheckmate(Player winner)
    {
        if (players == null || players.size() != 2 || winner == null)
        {
            return;
        }

        if(winner == players.get(0))
        {
            handleGameEnd(players.get(0), players.get(1), new CheckmateScoreStrategy());
        }
        else
        {
            handleGameEnd(players.get(1), players.get(0), new CheckmateScoreStrategy());
        }
    }

    public void resign(Player resigning)
    {
        if (players == null || players.size() != 2 || resigning == null)
        {
            return;
        }

        if(resigning == players.get(0))
        {
            handleGameEnd(players.get(1), players.get(0), new ResignScoreStrategy());
        }
        else
        {
            handleGameEnd(players.get(0), players.get(1), new ResignScoreStrategy());
        }
    }

    public void endByEquality()
    {
        if (players == null || players.size() != 2)
        {
            return;
        }
        handleGameEnd(user, computer, new EqualityScoreStrategy());
    }

    public boolean equality()
    {
        //pentru 3 repetari am nevoie de minim 6 mutari
        if(mutari.size() < 6)
        {
            return false;
        }

        //ma uit la ultimele 6 mutari
        Move m1 = mutari.get(mutari.size() - 6);
        Move m2 = mutari.get(mutari.size() - 5);
        Move m3 = mutari.get(mutari.size() - 4);
        Move m4 = mutari.get(mutari.size() - 3);
        Move m5 = mutari.get(mutari.size() - 2);
        Move m6 = mutari.get(mutari.size() - 1);

        //1: A->B
        //3: B->A
        //5: A->B
        //verific daca piesele albe se muta intre aceleasi directii
        boolean repeats1 = false;
        if(m1.getFrom().equals(m3.getTo()) && m1.getFrom().equals(m5.getFrom()) && m1.getTo().equals(m3.getFrom()) && m1.getTo().equals(m5.getTo()))
        {
            repeats1= true;
        }
        //2: A->B
        //4: B->A
        //6: A->B
        //verifica daca piesele negre se muta intre aceleasi pozitii
        boolean repeats2 = false;
        if(m2.getFrom().equals(m4.getTo()) && m2.getFrom().equals(m6.getFrom()) && m2.getTo().equals(m4.getFrom()) && m2.getTo().equals(m6.getTo()))
        {
            repeats2 = true;
        }

        if(repeats1 && repeats2)
        {
            return true;
        }
        return false;
    }

    public void getScoreByCapturedPieces()
    {
        if(players == null || players.size() != 2 || mutari == null)
        {
            return;
        }

        for(Player player : players)
        {
            player.getCapturedPieces().clear();
            player.setPoints(0);
        }

        CaptureScore score = new CaptureScore();

        for(Move move : mutari)
        {
            if(move.getCapturedPiece() == null)
            {
                continue;
            }

            Player playerThatCaptured;
            //determin care jucator a capturat dupa culoarea piesei
            if(players.get(0).getColor() == move.getPlayerColor())
            {
                playerThatCaptured = players.get(0);
            }
            else
            {
                playerThatCaptured = players.get(1);
            }
            playerThatCaptured.getCapturedPieces().add(move.getCapturedPiece());
            playerThatCaptured.setPoints(playerThatCaptured.getPoints() + score.pointsForCapturedPieces(move.getCapturedPiece()));
        }
    }

    //aduag metode pentru a dat cumva "subscribe"\"unsubscribe" si pentru a notifica "abonatii" (observers)
    public void addObserver(GameObserver observer)
    {
        if(observer != null)
        {
            observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer)
    {
        observers.remove(observer);
    }

    private void notifyMoveMade(Move move)
    {
        for(GameObserver o : observers)
        {
            o.onMoveMade(move);
        }
    }

    private void notifyPieceCaptured(Piece piece)
    {
        for(GameObserver o : observers)
        {
            o.onPieceCaptured(piece);
        }
    }

    private void notifyPlayerSwitch(Player currentPlayer)
    {
        for(GameObserver o : observers)
        {
            o.onPlayerSwitch(currentPlayer);
        }
    }
}

