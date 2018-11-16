public class Board {
    private int sideLen;
    private ArrayList<IPiece> pieces = new ArrayList<IPiece>();
    private int[] cols; //Contains the heigth of each collumn
    int maxY;
    IPiece activePiece;
    private boolean[][] occupied;
    public Board (int sideLen) {
        this.sideLen = sideLen;
        maxY = height / sideLen;
        cols = new int[width / sideLen];
        for(int i = 0; i < cols.length; i++) {
            cols[i] = maxY;
        }
        occupied = new boolean[cols.length][maxY];
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
            if(collision()) {
                updateCells();
                newPiece();
            }
        } else {
            newPiece();
        }
    }

    private boolean collision() {
        for(int i = 0; i < activePiece.GetBricks().size(); i++) {
            if(occupied[(int)activePiece.GetBricks().get(i).pos.x][(int)activePiece.GetBricks().get(i).pos.y]) {
                return true;
            }
            if(activePiece.GetBricks().get(i).pos.y >= maxY - 1) {
                return true;
            }
        }
        
        return false;
    }

    private void updateCells() {
        for(int k = 0; k < activePiece.GetBricks().size(); k++) {
            occupied[(int)activePiece.GetBricks().get(k).pos.x][(int)activePiece.GetBricks().get(k).pos.y - 1] = true;
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
        for(int i = 0; i < activePiece.GetBricks().size(); i++) {
            if(activePiece.GetBricks().get(i).pos.x == 0 && dir == -1) {
                return;
            } 
            if(activePiece.GetBricks().get(i).pos.x == cols.length - 1 && dir == 1) {
                return;
            }
        }
        activePiece.Move(dir, 0);
        if(collision()) {
            activePiece.Move(-dir, 0); //Move back if it now collides
        }
    }

    //Clockwise = -1, Counter clockwise = 1
    public void RotatePiece(int dir) {
        activePiece.Rotate(dir);
        for(int i = 0; i < activePiece.GetBricks().size(); i++) {
            if(activePiece.GetBricks().get(i).pos.x < 0) {
                activePiece.Rotate(-dir);
                return;
            } 
            if(activePiece.GetBricks().get(i).pos.x >= cols.length) {
                activePiece.Rotate(-dir);
                return;
            }
        }
        if(collision()) {
            activePiece.Rotate(-dir);
        }
    }
}
