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
    int sideLen = width/20;
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
            activePiece.Move(0, 1.0f/8); //Move piece down a bit
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
        //TODO: Check if piece can move
        activePiece.Move(dir, 0);
    }
}
public class BoxPiece implements IPiece {

    public Brick[] Bricks = new Brick[4];

    public BoxPiece (int sideLen) {
        Bricks[0] = new Brick(sideLen, color(0,0,255), new PVector(0,0));
        Bricks[1] = new Brick(sideLen, color(0,0,255), new PVector(0,1));
        Bricks[2] = new Brick(sideLen, color(0,0,255), new PVector(1,1));
        Bricks[3] = new Brick(sideLen, color(0,0,255), new PVector(1,0));
    }
    public void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Show();
        }
    }

    public Brick[] GetBricks() {
        return Bricks;
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
    Brick[] Bricks = new Brick[4];
    public void Show();
    public void Rotate(int dir);
    public void Move(float deltaX, float deltaY);
    public Brick[] GetBricks();
} 
public class LPiece implements IPiece {

    public Brick[] Bricks = new Brick[4];

    public LPiece (int sideLen) {
        Bricks[0] = new Brick(sideLen, color(255,0,255), new PVector(0,0));
        Bricks[1] = new Brick(sideLen, color(255,0,255), new PVector(0,1));
        Bricks[2] = new Brick(sideLen, color(255,0,255), new PVector(0,2));
        Bricks[3] = new Brick(sideLen, color(255,0,255), new PVector(1,2));
    }
    public void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Show();
        }
    }

    public Brick[] GetBricks() {
        return Bricks;
    }

}
public class LongPiece implements IPiece {

    public Brick[] Bricks = new Brick[4];

    public LongPiece (int sideLen) {
        Bricks[0] = new Brick(sideLen, color(255,255,0), new PVector(0,0));
        Bricks[1] = new Brick(sideLen, color(255,255,0), new PVector(0,1));
        Bricks[2] = new Brick(sideLen, color(255,255,0), new PVector(0,2));
        Bricks[3] = new Brick(sideLen, color(255,255,0), new PVector(0,3));
    }
    public void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Show();
        }
    }

    public Brick[] GetBricks() {
        return Bricks;
    }

}
public class RevLPiece implements IPiece {

    public Brick[] Bricks = new Brick[4];

    public RevLPiece (int sideLen) {
        Bricks[0] = new Brick(sideLen, color(255,0,0), new PVector(1,0));
        Bricks[1] = new Brick(sideLen, color(255,0,0), new PVector(1,1));
        Bricks[2] = new Brick(sideLen, color(255,0,0), new PVector(1,2));
        Bricks[3] = new Brick(sideLen, color(255,0,0), new PVector(0,2));
    }
    public void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    public void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Move(deltaX, deltaY);
        }
    }

    public void Show() {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Show();
        }
    }

    public Brick[] GetBricks() {
        return Bricks;
    }

}
  public void settings() {  size(360, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tetris" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
