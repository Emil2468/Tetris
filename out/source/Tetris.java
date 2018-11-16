import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Tetris extends PApplet {

Board board;
public void setup() {
    
    background(0);
    int sideLen = 20;
    board = new Board(sideLen);
    println((int)0.001f);
    //frameRate(20);
}

public void draw() {
    background(0);
    board.DrawGrid();
    board.Update();
}

public void keyPressed() {
    if(keyCode == LEFT) {
        board.MovePiece(-1);
    } else if(keyCode == RIGHT) {
        board.MovePiece(1);
    } else if(keyCode == UP) {
        board.RotatePiece(1);
    } else if(keyCode == DOWN) {
        board.RotatePiece(-1);
    }
}


public class Board {
    private int sideLen;
    private ArrayList<IPiece> pieces = new ArrayList<IPiece>();
    private int[] cols; //Contains the heigth of each collumn
    int maxY;
    IPiece activePiece;
    //Contains the brick occopying some cell, notice that x and y are shifted in order to increase spacial locality in clearRows
    private Brick[][] occupied;
    private float gameSpeed = 1/4.0f;
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
        if(rnd < 1/6.0f) {
            pieces.add(new SPiece(sideLen));
        } else if(rnd >= 1/6.0f && rnd < 2/6.0f) {
            pieces.add(new BoxPiece(sideLen));
        } else if(rnd >= 2/6.0f && rnd < 3/6.0f) {
            pieces.add(new LongPiece(sideLen));
        } else if(rnd >= 3/6.0f && rnd < 4/6.0f) {
            pieces.add(new RevLPiece(sideLen));
        } else if(rnd >= 4/6.0f && rnd < 5/6.0f) {
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
public class BoxPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public BoxPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,0), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,1), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,0), this));
    }
    public void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }


    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }

}
public class Brick {

    private int sideLen;
    private int c;
    PVector pos;
    public IPiece Parrent;
    public Brick (int sideLen, int c, PVector pos, IPiece parrent) {
        this.sideLen = sideLen;
        this.c = c;
        this.pos = pos;
        this.Parrent = parrent;
    }

    public void Show() {
        stroke(c);
        fill(c);
        rect(pos.x * sideLen + 1, pos.y * sideLen + 1, sideLen - 2, sideLen - 2);
    }

    //Moves the brick by deltaX, deltaY
    public void Move(float deltaX, float deltaY) {
        pos.x += deltaX;
        pos.y += deltaY;
    }

    //Places the brick at (x,y)
    public void Place(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

}
//In hindsigth this would perhaps have been better as an abstract class, since so many implementations are the same
interface IPiece {
    public void Show();

    //It is expected that if rotate(dir) and then Rotate(-dir) is called, piece will be same rotation as before
    public void Rotate(int dir);
    public void Move(float deltaX, float deltaY);
    public ArrayList<Brick> GetBricks();
    
    //Removes the given brick for list of bricks, and returns number of bricks left
    public int RemoveBrick(Brick brick);

    //Use to move individual bricks that are above a recently cleared row
    public void MoveDownIfAbove(int clearedRow);
} 
public class LPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public LPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,0), this));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,1), this));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,2), this));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(1,2), this));
    }

    //Rotate by using brick[1] as pivot
    public void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }

}
public class LongPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    boolean upright = true;
    public LongPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,0), this));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,1), this));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,2), this));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,3), this));
    }

    public void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }


    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }
}
public class RevLPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    private int rotation = 0;
    public RevLPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,0), this));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,2), this));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(0,2), this));
    }
    public void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }
}
public class RevSPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    boolean upright = true;
    public RevSPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(100,200,100), new PVector(0,0), this));
        bricks.add(new Brick(sideLen, color(100,200,100), new PVector(1,0), this));
        bricks.add(new Brick(sideLen, color(100,200,100), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(100,200,100), new PVector(2,1), this));
    }

    public void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }

}
public class SPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    boolean upright = true;
    public SPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(2,0), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(1,0), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(0,1), this));
    }

    public void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

    public int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    public void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }
}
  public void settings() {  size(360, 840); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tetris" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
