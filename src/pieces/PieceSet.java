package pieces;

import java.util.*;

public class PieceSet {

    private static Map<Piece.Color, Map<Piece.Type, List<Piece>>> pieceSet = null;
    private static Map<Piece.Color, Stack<Piece>> capturedPieceSet;

    private static char whiteKingFile;
    private static int whiteKingRank;
    private static char blackKingFile;
    private static int blackKingRank;

    private static PieceSet pieceSetInstance = new PieceSet();

    public static PieceSet getInstance() {
        return pieceSetInstance;
    }

    private PieceSet() {
        initialize();
    }

    private static void initialize() {
        initializePieceSet();
        initializeCapturedPieceSet();
        initializeKingsCoordinates();
    }

    public static List<Piece> getPieces(Piece.Color color) {
        List<Piece> piecesSameColor = new ArrayList<Piece>();
        for (Map.Entry<Piece.Type, List<Piece>> piecesEntry : pieceSet.get(color).entrySet()) {
            for (Piece piece : piecesEntry.getValue()) {
                piecesSameColor.add(piece);
            }
        }
        return piecesSameColor;
    }

    public static List<Piece> getPieces(Piece.Color color, Piece.Type type) {
        return pieceSet.get(color).get(type);
    }

    public static void addCapturedPiece(Piece piece) {
        piece.setCapture(true);
        capturedPieceSet.get(piece.getColor()).push(piece);
    }

    public static void minusCapturedPiece(Piece piece) {
        piece.setCapture(false);
        System.out.println(capturedPieceSet.size());
        capturedPieceSet.get(piece.getColor()).remove(piece);
        System.out.println(capturedPieceSet.size());
    }

    public static List<Piece> getCapturedPieces(Piece.Color color) {
        return capturedPieceSet.get(color);
    }

    public static char getOpponentKingFile(Piece.Color color) {
        if (color.equals(Piece.Color.WHITE)) {
            return blackKingFile;
        } else {
            return whiteKingFile;
        }
    }

    public static int getOpponentKingRank(Piece.Color color) {
        if (color.equals(Piece.Color.WHITE)) {
            return blackKingRank;
        } else {
            return whiteKingRank;
        }
    }

    public static void loadPieceSet(List<Piece> pieces) {
        pieceSet = new LinkedHashMap<Piece.Color, Map<Piece.Type, List<Piece>>>();

        Map<Piece.Type, List<Piece>> whitePieces = new LinkedHashMap<Piece.Type, List<Piece>>();
        Map<Piece.Type, List<Piece>> blackPieces = new LinkedHashMap<Piece.Type, List<Piece>>();

        for(int i=0; i<pieces.size(); i++) {
            switch (pieces.get(i).getColor()) {
                case WHITE:
                    switch (pieces.get(i).getType()) {
                        case KING:
                            List<Piece> whiteKings = new ArrayList<Piece>();
                            whiteKings.add(new King(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.KING, whiteKings);
                            break;
                        case ROOK:
                            List<Piece> whiteRooks = new ArrayList<Piece>();
                            whiteRooks.add(new Rook(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.ROOK, whiteRooks);
                            break;
                        case BISHOP:
                            List<Piece> whiteBishops = new ArrayList<Piece>();
                            whiteBishops.add(new Bishop(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.BISHOP, whiteBishops);
                            break;
                        case QUEEN:
                            List<Piece> whiteQueens = new ArrayList<Piece>();
                            whiteQueens.add(new Queen(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.QUEEN, whiteQueens);
                            break;
                        case KNIGHT:
                            List<Piece> whiteKnights = new ArrayList<Piece>();
                            whiteKnights.add(new Knight(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.KNIGHT, whiteKnights);
                            break;
                        case PAWN:
                            List<Piece> whitePawns = new ArrayList<Piece>();
                            whitePawns.add(new Pawn(Piece.Color.WHITE));
                            whitePieces.put(Piece.Type.PAWN, whitePawns);
                            break;
                    }
                    pieceSet.put(Piece.Color.WHITE, whitePieces);
                    break;
                case BLACK:
                    switch (pieces.get(i).getType()) {
                        case KING:
                            List<Piece> blackKings = new ArrayList<Piece>();
                            blackKings.add(new King(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.KING, blackKings);
                            break;
                        case ROOK:
                            List<Piece> blackRooks = new ArrayList<Piece>();
                            blackRooks.add(new Rook(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.ROOK, blackRooks);
                            break;
                        case BISHOP:
                            List<Piece> blackBishops = new ArrayList<Piece>();
                            blackBishops.add(new Bishop(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.BISHOP, blackBishops);
                            break;
                        case QUEEN:
                            List<Piece> blackQueens = new ArrayList<Piece>();
                            blackQueens.add(new Queen(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.QUEEN, blackQueens);
                            break;
                        case KNIGHT:
                            List<Piece> blackKnights = new ArrayList<Piece>();
                            blackKnights.add(new Knight(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.KNIGHT, blackKnights);
                            break;
                        case PAWN:
                            List<Piece> blackPawns = new ArrayList<Piece>();
                            blackPawns.add(new Pawn(Piece.Color.BLACK));
                            blackPieces.put(Piece.Type.PAWN, blackPawns);
                            break;
                    }
                    pieceSet.put(Piece.Color.BLACK, blackPieces);
                    break;
            }
        }


    }

    private static void initializePieceSet() {
        /*
        TODO-piece
            Initialize pieces and put to pieceSet
            The structure of pieceSet is as following
                pieceSet - collection by color - collection by category - each pieces
            The following code should be good start, but you can fix if you want
         */
        pieceSet = new LinkedHashMap<Piece.Color, Map<Piece.Type, List<Piece>>>();

        Map<Piece.Type, List<Piece>> whitePieces = new LinkedHashMap<Piece.Type, List<Piece>>();
        Map<Piece.Type, List<Piece>> blackPieces = new LinkedHashMap<Piece.Type, List<Piece>>();

        List<Piece> whiteRooks = new ArrayList<Piece>();
        List<Piece> blackRooks = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteRooks.add(new Rook(Piece.Color.WHITE));
            blackRooks.add(new Rook(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.ROOK, whiteRooks);
        blackPieces.put(Piece.Type.ROOK, blackRooks);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);
        //until here is the reference code

        List<Piece> whiteBishops = new ArrayList<Piece>();
        List<Piece> blackBishops = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteBishops.add(new Bishop(Piece.Color.WHITE));
            blackBishops.add(new Bishop(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.BISHOP, whiteBishops);
        blackPieces.put(Piece.Type.BISHOP, blackBishops);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        List<Piece> whiteKings = new ArrayList<Piece>();
        List<Piece> blackKings = new ArrayList<Piece>();
        whiteKings.add(new King(Piece.Color.WHITE));
        blackKings.add(new King(Piece.Color.BLACK));
        whitePieces.put(Piece.Type.KING, whiteKings);
        blackPieces.put(Piece.Type.KING, blackKings);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        List<Piece> whiteQueens = new ArrayList<Piece>();
        List<Piece> blackQueens = new ArrayList<Piece>();
        whiteQueens.add(new Queen(Piece.Color.WHITE));
        blackQueens.add(new Queen(Piece.Color.BLACK));
        whitePieces.put(Piece.Type.QUEEN, whiteQueens);
        blackPieces.put(Piece.Type.QUEEN, blackQueens);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        List<Piece> whiteKnights = new ArrayList<Piece>();
        List<Piece> blackKnights = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteKnights.add(new Knight(Piece.Color.WHITE));
            blackKnights.add(new Knight(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.KNIGHT, whiteKnights);
        blackPieces.put(Piece.Type.KNIGHT, blackKnights);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        List<Piece> whitePawns = new ArrayList<Piece>();
        List<Piece> blackPawns = new ArrayList<Piece>();
        for (int i = 0; i < 8; i++) {
            whitePawns.add(new Pawn(Piece.Color.WHITE));
            blackPawns.add(new Pawn(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.PAWN, whitePawns);
        blackPieces.put(Piece.Type.PAWN, blackPawns);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);
    }

    private static void initializeCapturedPieceSet() {
        capturedPieceSet = new LinkedHashMap<Piece.Color, Stack<Piece>>();
        Stack<Piece> whiteCapturedPieces = new Stack<Piece>();
        Stack<Piece> blackCapturedPieces = new Stack<Piece>();
        capturedPieceSet.put(Piece.Color.WHITE, whiteCapturedPieces);
        capturedPieceSet.put(Piece.Color.BLACK, blackCapturedPieces);
    }

    private static void initializeKingsCoordinates() {
        whiteKingFile = blackKingFile = 'e';
        whiteKingRank = 1;
        blackKingRank = 8;
    }

}
