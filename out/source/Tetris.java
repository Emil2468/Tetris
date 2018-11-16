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
    }
}


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
            activePiece.Move(0, 1.0f/8); //Move piece down a bit
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
        if(rnd < 0.25f) {
            pieces.add(new LPiece(sideLen));
        } else if(rnd >= 0.25f && rnd < 0.5f) {
            pieces.add(new BoxPiece(sideLen));
        } else if(rnd >= 0.5f && rnd < 0.75f) {
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
}
public class BoxPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public BoxPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,0)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,1)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,1)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,0)));
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
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

}
public class Brick {

    private int sideLen;
    private int c;
    PVector pos;
    public Brick (int sideLen, int c, PVector pos) {
        this.sideLen = sideLen;
        this.c = c;
        this.pos = pos;
    }

    public void Show() {
        stroke(c);
        fill(c);
        rect(pos.x * sideLen + 1, pos.y * sideLen + 1, sideLen - 2, sideLen - 2);
    }

    public void Move(float deltaX, float deltaY) {
        pos.x += deltaX;
        pos.y += deltaY;
    }

}
interface IPiece {
    public void Show();
    public void Rotate(int dir);
    public void Move(float deltaX, float deltaY);
    public ArrayList<Brick> GetBricks();
} 
public class LPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public LPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,0)));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,1)));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(0,2)));
        bricks.add(new Brick(sideLen, color(255,0,255), new PVector(1,2)));
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
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

}
public class LongPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public LongPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,0)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,1)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,2)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,3)));
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
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

}
public class RevLPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public RevLPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,0)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,1)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,2)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(0,2)));
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
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Show();
        }
    }

    public ArrayList<Brick> GetBricks() {
        return bricks;
    }

}
  public void settings() {  size(300, 640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tetris" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
