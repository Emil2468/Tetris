public class RevLPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    private int rotation = 0;
    public RevLPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,0)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,1)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,2)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(0,2)));
    }
    void Rotate(int dir) {
        if(dir == -1 && rotation == 0) {
            bricks.get(0).Move(1,1);
            //brick 1 stays
            bricks.get(2).Move(-1,-1);
            bricks.get(3).Move(0,-2);
            rotation = 1;
        } else if(dir == -1 && rotation == 1) {
            bricks.get(0).Move(-2,1);
            bricks.get(1).Move(-1,0);
            bricks.get(2).Move(0,-1);
            bricks.get(3).Move(1,0);
            rotation = 2;
        } else if(dir == -1 && rotation == 2) {
            bricks.get(0).Move(-1,-1);
            bricks.get(2).Move(1,1);
            bricks.get(3).Move(0,2);
            rotation = 3;
        } else if(dir == -1 && rotation == 3) {
            bricks.get(0).Move(1,-1);
            bricks.get(2).Move(-1,1);
            bricks.get(3).Move(-2,0);
            rotation = 0;
        } 
        else if(dir == 1 && rotation == 0) {
            bricks.get(0).Move(-1,1);
            bricks.get(2).Move(1,-1);
            bricks.get(3).Move(2,0);
            rotation = 3;
        } else if(dir == 1 && rotation == 1) {
            bricks.get(0).Move(-1,-1);
            //brick 1 stays
            bricks.get(2).Move(1,1);
            bricks.get(3).Move(0,2);
            rotation = 0;
        } else if(dir == 1 && rotation == 2) {
            bricks.get(0).Move(2,-1);
            bricks.get(1).Move(1,0);
            bricks.get(2).Move(0,1);
            bricks.get(3).Move(-1,0);
            rotation = 1;
        } else if(dir == 1 && rotation == 3) {
            bricks.get(0).Move(1,1);
            bricks.get(2).Move(-1,-1);
            bricks.get(3).Move(0,-2);
            rotation = 2;
        } 
    }

    void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    void Show() {
        for(int i = 0; i < 4; i++) {
            bricks.get(i).Show();
        }
    }

    ArrayList<Brick> GetBricks() {
        return bricks;
    }

}
