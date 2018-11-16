public class Board {
    private int sideLen;
    private ArrayList<IPiece> pieces = new ArrayList<IPiece>();
    private int[] cols; //Contains the heigth of each collumn
    int maxY;
    IPiece activePiece;
    //Contains the brick occopying some cell, notice that x and y are shifted in order to increase spacial locality in clearRows
    private Brick[][] occupied;
    private float gameSpeed = 1/4.0;
    public Board (int sideLen) {
        this.sideLen = sideLen;
        maxY = height / sideLen;
        cols = new int[width / sideLen];
        for(int i = 0; i < cols.length; i++) {
            cols[i] = maxY;
        }
        occupied = new Brick[maxY][cols.length];
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
            activePiece.Move(0, gameSpeed); //Move piece down a bit
            for(int i = 0; i < pieces.size(); i++) {
                pieces.get(i).Show();
            }
            if(collision()) {
                updateCells();
                clearRows();
                newPiece();
            }
        } else {
            newPiece();
        }
    }

    private boolean collision() {
        for(int i = 0; i < activePiece.GetBricks().size(); i++) {
            if(occupied[(int)activePiece.GetBricks().get(i).pos.y][(int)activePiece.GetBricks().get(i).pos.x] != null) {
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
            occupied[(int)activePiece.GetBricks().get(k).pos.y - 1][(int)activePiece.GetBricks().get(k).pos.x] = activePiece.GetBricks().get(k);
        }
    }
    
    public void newPiece() {
        float rnd = random(1);
        if(rnd < 1/6.0) {
            pieces.add(new SPiece(sideLen));
        } else if(rnd >= 1/6.0 && rnd < 2/6.0) {
            pieces.add(new BoxPiece(sideLen));
        } else if(rnd >= 2/6.0 && rnd < 3/6.0) {
            pieces.add(new LongPiece(sideLen));
        } else if(rnd >= 3/6.0 && rnd < 4/6.0) {
            pieces.add(new RevLPiece(sideLen));
        } else if(rnd >= 4/6.0 && rnd < 5/6.0) {
            pieces.add(new LPiece(sideLen));
        } else {
            pieces.add(new RevSPiece(sideLen));
        }
        activePiece = pieces.get(pieces.size() - 1);
        activePiece.Move((int)cols.length / 2 - 1, 0);
    }

    //Clear any full rows, and returns number of rows cleared
    private int clearRows() {
        //Loop over the cells row after row
        for(int i = 0; i < occupied.length; i++) {
            boolean allTaken = true;
            for(int k = 0; k < occupied[i].length; k++) {
                if(occupied[i][k] == null) {
                    allTaken = false;
                    break;
                }
            }
            if(allTaken) {
                for(int k = 0; k < occupied[i].length; k++) {
                    if(occupied[i][k].Parrent.RemoveBrick(occupied[i][k]) == 0) {
                        pieces.remove(occupied[i][k].Parrent);
                    }
                }
                //Only count down to 1, since game will (in the future) end if a collumn goes above 1
                //Bad spacial locallity here, but less than we would have above if I were to switch x and y back
                for(int k = i; k >= 1; k--) {
                    for(int j = 0; j < occupied[k].length; j++) {
                        occupied[k][j] = occupied[k - 1][j];
                    }
                }

                for(int k = 0; k < pieces.size(); k++) {
                    pieces.get(k).MoveDownIfAbove(i);
                }
            }
        }
        return 1;
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

    //Clockwise = 1, Counter clockwise = -1
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
