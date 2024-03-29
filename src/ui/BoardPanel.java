package ui;

import pieces.Piece;
import pieces.PieceSet;
import util.Core;
import util.GameModel;
import util.Move;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BoardPanel extends JPanel implements Observer {

    public static final int SQUARE_DIMENSION = 100;

    private GameModel gameModel;
    private boolean boardReversed;
    private boolean usingCustomPieces;
    private JLayeredPane boardLayeredPane;
    private JPanel boardPanel;
    private JPanel[][] squarePanels;

    public BoardPanel(GameModel gameModel) {
        super(new BorderLayout());
        this.gameModel = gameModel;
        this.boardReversed = Core.getPreferences().isBoardReversed();
        this.usingCustomPieces = Core.getPreferences().isUsingCustomPieces();
        initializeBoardLayeredPane();
        initializeSquares();
        initializePieces();
        gameModel.addObserver(this);
    }

    public void submitMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        Component[] tmp = boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER);
        if (getSquarePanel(originFile, originRank).getComponent(0) != null) {
                getSquarePanel(originFile, originRank).getComponent(0).setVisible(true);
                gameModel.onMoveRequest(originFile, originRank, destinationFile, destinationRank);
        }
    }

    public void executeMove(Move move) {
        JPanel originSquarePanel = getSquarePanel(move.getOriginFile(), move.getOriginRank());
        JPanel destinationSquarePanel = getSquarePanel(move.getDestinationFile(), move.getDestinationRank());
        destinationSquarePanel.removeAll();
        destinationSquarePanel.add(originSquarePanel.getComponent(0));
        destinationSquarePanel.repaint();
        originSquarePanel.removeAll();
        originSquarePanel.repaint();
    }

    public void preDrag(char originFile, int originRank, int dragX, int dragY) {
        Piece originPiece = gameModel.queryPiece(originFile, originRank);
        if (originPiece != null) {
            getSquarePanel(originFile, originRank).getComponent(0).setVisible(false);
            JLabel draggedPieceImageLabel = getPieceImageLabel(originPiece);
            draggedPieceImageLabel.setLocation(dragX, dragY);
            draggedPieceImageLabel.setSize(SQUARE_DIMENSION, SQUARE_DIMENSION);
            boardLayeredPane.add(draggedPieceImageLabel, JLayeredPane.DRAG_LAYER);
        }
    }

    public void executeDrag(int dragX, int dragY) {
        JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0]; //[0] 있었음

        if (draggedPieceImageLabel != null) {
            draggedPieceImageLabel.setLocation(dragX, dragY);
        }

    }

    public void postDrag() { //여기 고침 error of submitMoverRequest so Added
        JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0]; //[0] 있었음

        boardLayeredPane.remove(draggedPieceImageLabel);
        boardLayeredPane.repaint();
    }
    //I made it
    public int numDrag() {
        Component[] tmp = boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER);
        return tmp.length;
    }

    public JPanel getSquarePanel(char file, int rank) {
        file = Character.toLowerCase(file);
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8) {
            return null;
        } else {
            return squarePanels[file - 'a'][rank - 1];
        }
    }

    private void initializeSquares() {
        squarePanels = new JPanel[8][8];
        if (boardReversed) {
            for (int r = 0; r < 8; r ++) {
                for (int f = 7; f >= 0; f--) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        } else {
            for (int r = 7; r >= 0; r --) {
                for (int f = 0; f < 8; f++) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        }
    }

    private void initializeSingleSquarePanel(int f, int r) {
        squarePanels[f][r] = new JPanel(new GridLayout(1, 1));
        squarePanels[f][r].setPreferredSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setBackground(f % 2 == r % 2 ? Color.GRAY : Color.WHITE);
        boardPanel.add(squarePanels[f][r]);
    }

    public void loadPieceSet(List<Piece> pieces, List<util.location> locations) {
        initializeSquares();
        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        Iterator<Piece> whiteKingsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
        Iterator<Piece> blackKingsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
        Iterator<Piece> whiteBishopsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
        Iterator<Piece> blackBishopsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
        Iterator<Piece> whiteQueensIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
        Iterator<Piece> blackQueensIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
        Iterator<Piece> whiteKnightsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> blackKnightsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> whitePawnsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
        Iterator<Piece> blackPawnsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();

        for(int i=0; i<pieces.size(); i++) {
            switch (pieces.get(i).getColor()) {
                case WHITE:
                    switch (pieces.get(i).getType()){
                        case KING:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whiteKingsIterator.next()));
                            break;
                        case ROOK:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whiteRooksIterator.next()));
                            break;
                        case BISHOP:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whiteBishopsIterator.next()));
                            break;
                        case QUEEN:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whiteQueensIterator.next()));
                            break;
                        case KNIGHT:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whiteKnightsIterator.next()));
                            break;
                        case PAWN:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(whitePawnsIterator.next()));
                            break;
                    }
                    break;
                case BLACK:
                    switch (pieces.get(i).getType()){
                        case KING:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackKingsIterator.next()));
                            break;
                        case ROOK:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackRooksIterator.next()));
                            break;
                        case BISHOP:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackBishopsIterator.next()));
                            break;
                        case QUEEN:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackQueensIterator.next()));
                            break;
                        case KNIGHT:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackKnightsIterator.next()));
                            break;
                        case PAWN:
                            getSquarePanel(locations.get(i).getx(), locations.get(i).gety()).add(getPieceImageLabel(blackPawnsIterator.next()));
                            break;
                    }
                    break;
            }
        }

    }

    private void initializePieces() {
        /*
        TODO-piece
            Initialize pieces on board
            Check following code to implement other pieces
            Highly recommended to use same template!
         */
        // rooks 내가 여기 추가함 PieceSet.java 도 수정해야 에러 안나더라
        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        Iterator<Piece> whiteKingsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
        Iterator<Piece> blackKingsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
        Iterator<Piece> whiteBishopsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
        Iterator<Piece> blackBishopsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
        Iterator<Piece> whiteQueensIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
        Iterator<Piece> blackQueensIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
        Iterator<Piece> whiteKnightsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> blackKnightsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> whitePawnsIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
        Iterator<Piece> blackPawnsIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();

        getSquarePanel('a', 1).add(getPieceImageLabel(whiteRooksIterator.next())); //Rank 는 세로줄 몇 번 칸인지
        getSquarePanel('h', 1).add(getPieceImageLabel(whiteRooksIterator.next()));
        getSquarePanel('b', 1).add(getPieceImageLabel(whiteKnightsIterator.next()));
        getSquarePanel('g', 1).add(getPieceImageLabel(whiteKnightsIterator.next()));
        getSquarePanel('c', 1).add(getPieceImageLabel(whiteBishopsIterator.next()));
        getSquarePanel('f', 1).add(getPieceImageLabel(whiteBishopsIterator.next()));
        getSquarePanel('e', 1).add(getPieceImageLabel(whiteKingsIterator.next()));
        getSquarePanel('d', 1).add(getPieceImageLabel(whiteQueensIterator.next()));
        getSquarePanel('a', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('b', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('c', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('d', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('e', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('f', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('g', 2).add(getPieceImageLabel(whitePawnsIterator.next()));
        getSquarePanel('h', 2).add(getPieceImageLabel(whitePawnsIterator.next()));

        getSquarePanel('a', 8).add(getPieceImageLabel(blackRooksIterator.next()));
        getSquarePanel('h', 8).add(getPieceImageLabel(blackRooksIterator.next()));
        getSquarePanel('b', 8).add(getPieceImageLabel(blackKnightsIterator.next()));
        getSquarePanel('g', 8).add(getPieceImageLabel(blackKnightsIterator.next()));
        getSquarePanel('c', 8).add(getPieceImageLabel(blackBishopsIterator.next()));
        getSquarePanel('f', 8).add(getPieceImageLabel(blackBishopsIterator.next()));
        getSquarePanel('e', 8).add(getPieceImageLabel(blackKingsIterator.next()));
        getSquarePanel('d', 8).add(getPieceImageLabel(blackQueensIterator.next()));
        getSquarePanel('a', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('b', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('c', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('d', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('e', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('f', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('g', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
        getSquarePanel('h', 7).add(getPieceImageLabel(blackPawnsIterator.next()));
    }

    private void initializeBoardLayeredPane() {
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBounds(0, 0, 800, 800);
        boardLayeredPane = new JLayeredPane();
        boardLayeredPane.setPreferredSize(new Dimension(800, 800));
        boardLayeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        PieceDragAndDropListener pieceDragAndDropListener = new PieceDragAndDropListener(this);
        boardLayeredPane.addMouseListener(pieceDragAndDropListener);
        boardLayeredPane.addMouseMotionListener(pieceDragAndDropListener);
        boardLayeredPane.setVisible(true);
        this.add(boardLayeredPane, BorderLayout.CENTER);

        System.out.println(boardLayeredPane);
    }

    private JLabel getPieceImageLabel(Piece piece) {
        Image pieceImage = new ImageIcon(getClass().getResource(piece.getImageFileName())).getImage();
        pieceImage = pieceImage.getScaledInstance(SQUARE_DIMENSION, SQUARE_DIMENSION, Image.SCALE_SMOOTH);
        JLabel pieceImageLabel = new JLabel(new ImageIcon(pieceImage));
        return pieceImageLabel;
    }

    public boolean isBoardReversed() {
        return boardReversed;
    }

    @Override
    public void update(Observable o, Object arg) {
        executeMove((Move) arg);
    }
}
