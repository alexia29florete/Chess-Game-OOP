import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main
{
    private List<Account> accounts; // JSON accounts
    private Map<Long, Game> games;  // existing games
    private User currentUser;       // logged-in user
    private Scanner scanner;

    private static Main instance = null;

    private Main()
    {
        accounts = new ArrayList<>();
        games = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public static Main getInstance()
    {
        if(instance == null)
        {
            instance = new Main();
        }
        return instance;
    }
    // read data from JSON
    public void read()
    {
        try
        {
            accounts = JsonReaderUtil.readAccounts(Paths.get("input/accounts.json"));
        }
        catch (Exception e)
        {
            accounts = new ArrayList<>();
            System.out.println("Error reading accounts: " + e.getMessage());
        }

        try
        {
            games = JsonReaderUtil.readGamesAsMap(Paths.get("input/games.json"));
        }
        catch (Exception e)
        {
            games = new HashMap<>();
            System.out.println("Error reading games: " + e.getMessage());
        }
    }

    public void writeAcc()
    {
        JSONArray accountsArray = new JSONArray();
        for(Account acc : accounts)
        {
            JSONObject accountObj = new JSONObject();
            accountObj.put("email", acc.getEmail());
            accountObj.put("password", acc.getPassword());
            accountObj.put("points", acc.getPoints());

            JSONArray gamesArray = new JSONArray();
            if(acc.getGames() != null)
            {
                for(Integer id : acc.getGames())
                {
                    gamesArray.add(id);
                }
            }
            accountObj.put("games", gamesArray);

            accountsArray.add(accountObj);
        }

        try
        {
            FileWriter file = new FileWriter("input/accounts.json");
            file.write(accountsArray.toJSONString());
            file.flush();
            file.close();
        }
        catch (IOException e)
        {
            System.out.println("Error writing in accounts.json: " + e.getMessage());
        }
    }
    // write data to JSON
    //https://www.digitalocean.com/community/tutorials/json-simple-example
    public void write()
    {
        if(currentUser != null)
        {
            //sincronizez pentru ca altfel as ramane mereu cu punctele initiale si gameId initial
            for(Account acc: accounts)
            {
                //verific daca acest cont apartine utilizatorului logat
                if(acc.getEmail().equals(currentUser.getEmail()))
                {
                    acc.setPoints(currentUser.getPoints());

                    List<Integer> gamesId = new ArrayList<>();
                    for (Game g : currentUser.getActiveGames())
                    {
                        gamesId.add((int) g.getId());
                    }
                    acc.setGames(gamesId);

                }
            }
        }
        writeAcc();
        //games.json
        JSONArray gamesArray = new JSONArray();
        for (Map.Entry<Long, Game> entry : games.entrySet())
        {
            Game game = entry.getValue();
            JSONObject gameObj = new JSONObject();
            gameObj.put("id", game.getId());

            // players
            JSONArray playersArray = new JSONArray();
            for(Player player : game.getPlayers())
            {
                JSONObject playerObject = new JSONObject();
                playerObject.put("email", player.getEmail());
                playerObject.put("color", player.getColor().toString());
                playersArray.add(playerObject);
            }
            gameObj.put("players", playersArray);
            gameObj.put("currentPlayerColor", game.getCurrentPlayerColor().toString());

            //board
            JSONArray boardArray = new JSONArray();
            for(Piece piece : game.getBoardPieces())
            {
                JSONObject pieceObj = new JSONObject();
                pieceObj.put("type", String.valueOf(piece.type()));
                pieceObj.put("color", piece.getColor().toString());
                pieceObj.put("position", piece.getPosition().toString());
                boardArray.add(pieceObj);
            }
            gameObj.put("board", boardArray);

            //moves
            JSONArray movesArray = new JSONArray();
            for(Move move : game.getMoves())
            {
                JSONObject moveObj = new JSONObject();
                moveObj.put("playerColor", move.getPlayerColor().toString());
                moveObj.put("from", move.getFrom().toString());
                moveObj.put("to", move.getTo().toString());

                if(move.getCapturedPiece() != null)
                {
                    JSONObject capturedObj = new JSONObject();
                    capturedObj.put("type", String.valueOf(move.getCapturedPiece().type()));
                    capturedObj.put("color", move.getCapturedPiece().getColor().toString());
                    moveObj.put("captured", capturedObj);
                }
                movesArray.add(moveObj);
            }
            gameObj.put("moves", movesArray);
            gamesArray.add(gameObj);
        }

        try
        {
            FileWriter file = new FileWriter("input/games.json");
            file.write(gamesArray.toJSONString());
            file.flush();
            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Current state of the user and game has been saved");
    }

    // login
    public User login(String email, String password)
    {
        for (Account acc : accounts)
        {
            if (acc.getEmail().equals(email) && acc.getPassword().equals(password))
            {
                User user = new User(acc.getEmail(), acc.getPassword(), acc.getPoints());
                for (Integer gameId : acc.getGames())
                {
                    Game game = games.get(gameId.longValue());
                    if (game != null)
                    {
                        user.addGame(game);
                    }
                }
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void logout()
    {
        currentUser = null;
    }


    // create new account
    public User newAccount(String email, String password)
    {
        for (Account acc : accounts)
        {
            if (acc.getEmail().equals(email))
            {
                System.out.println("Email already in use.");
                return null;
            }
        }
        Account acc = new Account();
        acc.setEmail(email);
        acc.setPassword(password);
        acc.setPoints(0);
        acc.setGames(new ArrayList<>());
        accounts.add(acc);

        writeAcc();

        User user = new User(email, password, 0);
        currentUser = user;
        return user;
    }

    // main menu
    private void mainMenu()
    {
        while (true)
        {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Start a new game");
            System.out.println("2. Visualise or continue an existing game");
            System.out.println("3. Logout");

            System.out.println("Choose a number to execute an action from the list: ");
            String choice = scanner.nextLine();
            switch (choice)
            {
                case "1":
                    startNewGame();
                    break;
                case "2":
                    continueExistingGame();
                    break;
                case "3":
                    currentUser = null;
                    System.out.println("Logged out");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void syncUserPointsFromGame(Game game)
    {
        currentUser.setPoints(currentUser.getPoints() + game.getUser().getPoints());
        game.getUser().setPoints(0);
    }

    // start new game
    private void startNewGame()
    {
        long newId = 1;
        for(Account acc : accounts)
        {
            //caut id-ul maxim in toate conturile
            for(Integer id : acc.getGames())
            {
                if(id >= newId)
                {
                    newId = id + 1;
                }
            }
        }

        System.out.println("Choose which color you would like to be: W (WHITE) / B (BLACK): ");
        String colorChoice = scanner.nextLine();
        Colors userColor, computerColor;

        if (colorChoice.equals("B"))
        {
            userColor = Colors.BLACK;
            computerColor = Colors.WHITE;
        }
        else
        {
            userColor = Colors.WHITE;
            computerColor = Colors.BLACK;
        }

        Player userPlayer = new Player(currentUser.getEmail(), userColor);
        Player computer = new Player("computer", computerColor);
        Game game = new Game(newId, userPlayer, computer);

        // initialize board
        game.start();
        //game.getBoard().initialize();
        games.put(newId, game);
        currentUser.addGame(game);

//        // Daca computerul e alb, muta primul
//        if (computerColor == Colors.WHITE)
//        {
//            game.switchPlayer();
//        }

        playGame(game);
        write();
    }

    // continue existing game
    private void continueExistingGame()
    {
        List<Game> activeGames = currentUser.getActiveGames();
        if (activeGames.isEmpty())
        {
            System.out.println("No games to continue");
            return;
        }

        System.out.println("Select a game by ID to continue or visualize:");
        for (int i = 0; i < activeGames.size(); i++)
        {
            System.out.println((i + 1) + ". Game ID: " + activeGames.get(i).getId());
        }

        //las utilizatprul sa introduca un id din lista de jocuri afisata
        long selectedId;
        try
        {
            System.out.println("Enter Game ID: ");
            selectedId = Long.parseLong(scanner.nextLine());
        }
        catch(Exception e)
        {
            System.out.println("Not a valid ID format");
            return;
        }

        //caut sa vad daca exista un joc cu id-ul selectat
        Game selectedGame = null;
        for(Game game : activeGames)
        {
            if(game.getId() == selectedId)
            {
                //am gasit jocul selectat
                selectedGame = game;
            }
        }

        //daca nu am gasit jocul selectat, informez utilizatorul
        if(selectedGame == null)
        {
            System.out.println("No game found with the provided ID");
            return;
        }

        for (Player p : selectedGame.getPlayers())
        {
            if (p.getName().equals(currentUser.getEmail()))
            {
                selectedGame.setUser(p);
            }
        }

        while(true)
        {
            System.out.println("\nSelected game ID: " + selectedGame.getId());
            System.out.println("1. Visualise game details");
            System.out.println("2. Continue game");
            System.out.println("3. Delete game");
            System.out.println("4. Return to main menu");
            System.out.println("Choose a number to execute an action from the list: ");

            String choice = scanner.nextLine();
            switch(choice)
            {
                case "1":
                    //ma uit la detaliile jocului ales
                    System.out.println("Players:");
                    for (Player p : selectedGame.getPlayers())
                    {
                        System.out.print(p.getName() + ": color " + p.getColor());
                        if (p.getName().equals(currentUser.getEmail()))
                        {
                            System.out.print(", total points = " + currentUser.getPoints());
                        }
                        System.out.println();
                    }

                    System.out.println("Board:");
                    selectedGame.getBoard().printBoard(selectedGame.getUser().getColor());

                    System.out.println("\nMoves history:");
                    if (selectedGame.getMoves().isEmpty())
                    {
                        System.out.println("No moves yet.");
                    }
                    else
                    {
                        for (Move m : selectedGame.getMoves())
                        {
                            if(m.getCapturedPiece() == null)
                            {
                                System.out.println(m.getPlayerColor() + ": " + m.getFrom() + " -> " + m.getTo());
                            }
                            else
                            {
                                System.out.println(m.getPlayerColor() + ": " + m.getFrom() + " -> " + m.getTo() + " captured " + m.getCapturedPiece().type() + "-" + m.getCapturedPiece().getColor());
                            }
                        }
                    }
                    //ma intorc in main menu
                    break;

                case "2":
                    //continui joc
                    selectedGame.resume();
                    playGame(selectedGame);
                    write();
                    return;

                case "3":
                    //sterg joc
                    currentUser.removeGame(selectedGame);
                    games.remove(selectedGame.getId());
                    write();
                    System.out.println("Game deleted successfully");
                    return;

                case "4":
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private boolean isJustAPositionInput(String s)
    {
        if(s == null)
        {
            return false;
        }
        s = s.trim().toUpperCase();
        if(s.length() != 2)
        {
            return false;
        }
        return s.charAt(0) >= 'A' && s.charAt(0) <= 'H' && s.charAt(1) >= '1' && s.charAt(1) <= '8';
    }

    // game loop
    private void playGame(Game game)
    {
        // print board only once at the start
        //game.getBoard().printBoard(game.getUser().getColor());

        while (true)
        {
            Player current = game.getPlayer();
            Player opponent = game.getOpponent();
            for (Player p : game.getPlayers())
            {
                p.updateOwnPieces(game.getBoard());
            }

            if (game.checkForCheckMate())
            {
                // daca jucatorul curent e in sah mat => castiga adversarul
                System.out.println("Checkmate! " + opponent.getName() + " wins");
                game.endByCheckmate(opponent);
                syncUserPointsFromGame(game);
                // scoate jocul terminat din lista de active + din games map
                currentUser.removeGame(game);
                games.remove(game.getId());
                break;
            }

            // verificare sah
            if (game.getBoard().esteKingInCheck(current.getColor()))
            {
                System.out.println(current.getName() + " is in check");
            }

            System.out.println("\nIt's " + current.getName() + "'s turn (" + current.getColor() + ")");

            if (current == game.getUser())
            {
                //mutare user
                System.out.println("Enter your move (e.g: E2-E4) or a position (e.g: B2) to show possible moves");
                System.out.println("You can 'resign' or 'leave'.");
                String input = scanner.nextLine().trim();

                if (input.equals("leave"))
                {
                    System.out.println("Game saved. Returning to main menu...");
                    return;
                }

                if (input.equals("resign"))
                {
                    game.resign(current);
                    syncUserPointsFromGame(game);
                    // scoate jocul terminat din lista de active + din games map
                    currentUser.removeGame(game);
                    games.remove(game.getId());
                    break;
                }

                if(isJustAPositionInput(input))
                {
                    Position pos = new Position(input.toUpperCase());
                    Piece piece = game.getBoard().getPieceAt(pos);
                    if(piece == null)
                    {
                        System.out.println("No piece at " + pos);
                        continue;
                    }
                    if(piece.getColor() != game.getUser().getColor())
                    {
                        System.out.println("That piece is not yours");
                        continue;
                    }

                    System.out.println("Possible moves from " + pos + ":");
                    for(Position to : piece.getPossibleMoves(game.getBoard()))
                    {
                        try
                        {
                            if(game.getBoard().isValidMove(pos, to))
                            {
                                System.out.print(to + " ");
                            }
                        }
                        catch(InvalidMoveException ignored)
                        {

                        }
                    }
                    System.out.println();
                    continue;
                }

                if (!input.contains("-"))
                {
                    System.out.println("Invalid move format");
                    continue;
                }

                String[] parts = input.split("-");
                if (parts.length != 2)
                {
                    System.out.println("Invalid format");
                    continue;
                }

                String from = parts[0];
                String to = parts[1];

                if(from.length() != 2 || to.length() != 2)
                {
                    System.out.println("Invalid format");
                    continue;
                }

                try
                {
                    //current.makeMove(fromPos, toPos, game.getBoard(), game);
                    game.getBoard().printBoard(game.getUser().getColor());
                    //verific daca am 3 mutari egale consecutive
                    if(game.equality())
                    {
                        System.out.println("Equality. Computer quits");
                        game.endByEquality();
                        syncUserPointsFromGame(game);
                        // scoate jocul terminat din lista de active + din games map
                        currentUser.removeGame(game);
                        games.remove(game.getId());
                        break;
                    }
                    game.switchPlayer();
                }
                catch(Exception e)
                {
                    System.out.println("Invalid move: " + e.getMessage());
                }
            }
            else
            {
                //muta computer
                System.out.println("Computer is making its move...");
                try
                {
                    Move computerMove = current.getComputerMove(game.getBoard());

                    if (computerMove == null)
                    {
                        if (game.getBoard().esteKingInCheck(current.getColor()))
                        {
                            System.out.println("Computer is checkmated!");
                            game.endByCheckmate(opponent);
                        }
                        else
                        {
                            System.out.println("Computer cannot make a move. Computer resigns.");
                            game.resign(current);
                        }
                        syncUserPointsFromGame(game);
                        // scoate jocul terminat din lista de active + din games map
                        currentUser.removeGame(game);
                        games.remove(game.getId());
                        break;
                    }

                    Position from = computerMove.getFrom();
                    Position to = computerMove.getTo();

                    //current.makeMove(from, to, game.getBoard(), game);
                    System.out.println(current.getName() + " moves from " + from + " to " + to);
                    game.getBoard().printBoard(game.getUser().getColor());
                    //verific daca am 3 mutari egale consecutive
                    if(game.equality())
                    {
                        System.out.println("Equality. Computer quits");
                        game.endByEquality();
                        syncUserPointsFromGame(game);
                        // scoate jocul terminat din lista de active + din games map
                        currentUser.removeGame(game);
                        games.remove(game.getId());
                        break;
                    }
                    game.switchPlayer();

                }
                catch (Exception e)
                {
                    System.out.println("Computer's move is invalid: " + e.getMessage());
                }
            }
        }
    }

    public void run()
    {
        while (true)
        {
            System.out.println("\n=== LOGIN ===");
            System.out.println("1. Login");
            System.out.println("2. Create new account");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();
            switch (choice)
            {
                case "1":
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    if (login(email, password) != null)
                    {
                        System.out.println("Successfully logged in!");
                        mainMenu();
                    }
                    else
                    {
                        System.out.println("Incorrect email or password.");
                    }
                    break;

                case "2":
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    if (newAccount(email, password) != null)
                    {
                        System.out.println("Account successfully created!");
                        mainMenu();
                    }
                    break;

                case "3":
                    write();
                    System.out.println("Application closed");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    //functii pentru UI
    //start new game in UI
    public Game startNewGameUI(String alias, Colors userColor)
    {
        long newId = 1;
        for(Account acc : accounts)
        {
            //caut id-ul maxim in toate conturile
            for(Integer id : acc.getGames())
            {
                if(id >= newId)
                {
                    newId = id + 1;
                }
            }
        }

//        System.out.println("Choose which color you would like to be: W (WHITE) / B (BLACK): ");
//        String colorChoice = scanner.nextLine();
        Colors computerColor;

        if(userColor == Colors.WHITE)
        {
            computerColor = Colors.BLACK;
        }
        else
        {
            computerColor = Colors.WHITE;
        }

        Player userPlayer = new Player(currentUser.getEmail(), userColor);
        if(alias != null)
        {
            userPlayer = new Player(alias, currentUser.getEmail(), userColor);
        }

        Player computer = new Player("computer", computerColor);
        Game game = new Game(newId, userPlayer, computer);

        // initialize board
        game.start();
        //game.getBoard().initialize();
        games.put(newId, game);
        currentUser.addGame(game);

//        // Daca computerul e alb, muta primul
//        if (computerColor == Colors.WHITE)
//        {
//            game.switchPlayer();
//        }

        return game;
    }

    public boolean deleteGame(long gameId)
    {
        Game game = games.get(gameId);
        if(game == null)
        {
            return false;
        }

        if(currentUser != null)
        {
            currentUser.removeGame(game);
        }
        games.remove(gameId);

        return true;
    }

    public void saveGame(Game game)
    {
        if(game != null)
        {
            games.put(game.getId(), game);
            write();
        }
    }

    public void prepareSelectedGameForCurrentUser(Game selectedGame)
    {
        //verific daca utilizatorul curent face parte din jocul selectat
        if (selectedGame == null || currentUser == null)
        {
            return;
        }

        //activez utilizatorul pentru joc, practic vad cine e user si cine e computer
        for (Player p : selectedGame.getPlayers())
        {
            if (p.getEmail().equals(currentUser.getEmail()))
            {
                selectedGame.setUser(p);
                return;
            }
        }
    }

    private void finishGameAndRemove(Game game)
    {
        if (game == null)
        {
            return;
        }
        syncUserPointsFromGame(game);
        if (currentUser != null)
        {
            currentUser.removeGame(game);
        }
        games.remove(game.getId());
        write();
    }

    public String userMoveUI(Game game, Position from, Position to, char promotion)
    {
        //daca user sau game nu sunt valide
        if (currentUser == null || game == null)
        {
            return "ERR|No user/game";
        }
        prepareSelectedGameForCurrentUser(game);

        //daca incerc sa mut cand nu e randul meu
        if (game.getPlayer() != game.getUser())
        {
            return "ERR|Not your turn.";
        }

        try
        {
            //se face o miscare
            game.getUser().makeMove(from, to, game.getBoard(), game, promotion);

            //equality
            if (game.equality())
            {
                game.endByEquality();
                int gamePoints = game.getUser().getPoints();
                finishGameAndRemove(game);
                return "END|EQUALITY|" + gamePoints + "|Equality";
            }

            //next turn
            game.switchPlayer();

            //verific daca s-a terminat in sah-mat
            if (game.checkForCheckMate())
            {
                game.endByCheckmate(game.getOpponent());
                int gamePoints = game.getUser().getPoints();
                finishGameAndRemove(game);
                String mesaj;
                if(game.getOpponent() == game.getUser())
                {
                    mesaj = "Winner by checkmate!";
                }
                else
                {
                    mesaj = "Defeated!";
                }
                return "END|CHECKMATE|" + gamePoints + "|" + mesaj;
            }

            //verific daca e sah
            if (game.getBoard().esteKingInCheck(game.getPlayer().getColor()))
            {
                return "OK|Computer is in check!";
            }

            return "OK|";

        }
        catch (InvalidMoveException | InvalidCommandException ex)
        {
            return "ERR|Invalid move: " + ex.getMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "ERR|Internal error: " + ex.getClass().getSimpleName();
        }
    }

    public String computerMoveUI(Game game)
    {
        //daca user sau game nu sunt valide
        if (currentUser == null || game == null)
        {
            return "ERR|No user/game.";
        }
        prepareSelectedGameForCurrentUser(game);

        //daca incerc sa mut cand nu e randul computerului
        if (game.getPlayer() == game.getUser())
        {
            return "ERR|Not computer's turn.";
        }

        try
        {
            Player computer = game.getPlayer();
            Move m = computer.getComputerMove(game.getBoard());

            //daca miscarea nu e valida, fie nu mai am ce miscari sa fac, deci sunt in sah
            //fie am renuntat computer
            if (m == null)
            {
                Player opponent = game.getOpponent();
                if (game.getBoard().esteKingInCheck(computer.getColor()))
                {
                    game.endByCheckmate(opponent);
                    int gamePoints = game.getUser().getPoints();
                    finishGameAndRemove(game);
                    return "END|CHECKMATE|" + gamePoints + "|Victory by checkmate";
                }
                else
                {
                    game.resign(computer);
                    int gamePoints = game.getUser().getPoints();
                    finishGameAndRemove(game);
                    return "END|RESIGN|" + gamePoints + "Computer resigns";
                }
            }

            //am miscare valida
            //atunci cand pionul computerului ajunge in capatul opus, il promovez atomat cu regina
            computer.makeMove(m.getFrom(), m.getTo(), game.getBoard(), game, 'Q');

            //daca termin jocul cu egalitate
            if (game.equality())
            {
                game.endByEquality();
                int gamePoints = game.getUser().getPoints();
                finishGameAndRemove(game);
                return "END|EQUALITY|" + gamePoints + "|Equality";
            }

            game.switchPlayer();

            //verific daca s-a terminat in sah-mat
            if (game.checkForCheckMate())
            {
                game.endByCheckmate(game.getOpponent());
                int gamePoints = game.getUser().getPoints();
                finishGameAndRemove(game);
                String mesaj;
                if(game.getOpponent() == game.getUser())
                {
                    mesaj = "Winner by checkmate!";
                }
                else
                {
                    mesaj = "Defeated!";
                }
                return "END|CHECKMATE|" + gamePoints + "|" + mesaj;
            }

            //verific daca e sah
            if (game.getBoard().esteKingInCheck(game.getPlayer().getColor()))
            {
                return "OK|You are in check!";
            }

            return "OK|";

        }
        catch (InvalidMoveException | InvalidCommandException ex)
        {
            return "ERR|Invalid comptuer move: " + ex.getMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "ERR|Internal error: " + ex.getClass().getSimpleName();
        }
    }

    public String resignUI(Game game)
    {
        if (currentUser == null || game == null)
        {
            return "ERR|No user/game.";
        }
        prepareSelectedGameForCurrentUser(game);

        game.resign(game.getUser());
        int gamePoints = game.getUser().getPoints();
        finishGameAndRemove(game);
        return "END|RESIGN|" + gamePoints + "|You resigned";
    }

    public static void main(String[] args)
    {
        Main app = Main.getInstance();
        app.read();
        //app.run();

        //pun codul cand este gata pe un thread corect
        SwingUtilities.invokeLater(() ->
        {
            AppFrame frame = new AppFrame(app);
            frame.setVisible(true);
        });
    }
}
