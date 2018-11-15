public class Board {
    private int sideLen;
    private ArrayList<IPiece> pieces = new ArrayList<IPiece>();
    private int[] cols; //Contains the heigth of each collumn
    int maxY;
    IPiece activePiece;
    public Board (int sideLen) {
        this.sideLen = sideLen;
        maxY = height / sideLen;
        cols = new int[width / sideLen];
        for(int i = 0; i < cols.length; i++) {
            cols[i] = maxY;
        }
    }

    public void DrawGrid() {
        stroke(50);
        strokeWeight(1);
        for(int x = sideLen; x <= width; x+= sideLen) {
            line(x, 0, x, height);
        }
        for(int y = sideLen; y <= height; y+= sideLen) {
            line(0,y, width, y);
        }
    }

    public void Update(){
        if(pieces.size() > 0) {
            activePiece = pieces.get(pieces.size() - 1);
            activePiece.Move(0, 1.0/8); //Move piece down a bit
            for(int i = 0; i < pieces.size(); i++) {
                pieces.get(i).Show();
            }
            for(int i = 0; i < 4; i++) {
                int col = (int)activePiece.GetBricks()[i].pos.x;
                if(activePiece.GetBricks()[i].pos.y + 1 >= cols[col]) { //Add one since coordinate is top of brick, but we want bottom
                    for(int k = 0; k < 4; k++) {
                        col = (int)activePiece.GetBricks()[k].pos.x;
                        if(cols[col] > (int)activePiece.GetBricks()[k].pos.y) {
                            cols[col] = (int)activePiece.GetBricks()[k].pos.y;
                        }
                    }
                    newPiece();
                    break;
                }
            }
        } else {
            newPiece();
        }
    }

    public void newPiece() {
        float rnd = random(1);
        if(rnd < 0.25) {
            pieces.add(new LPiece(sideLen));
        } else if(rnd >= 0.25 && rnd < 0.5) {
            pieces.add(new BoxPiece(sideLen));
        } else if(rnd >= 0.5 && rnd < 0.75) {
            pieces.add(new LongPiece(sideLen));
        } else {
            pieces.add(new RevLPiece(sideLen));
        }
        
    }

    //Left = -1, right = 1
    public void MovePiece(int dir) {
        //TODO: Check if piece can move
        activePiece.Move(dir, 0);
    }
}
