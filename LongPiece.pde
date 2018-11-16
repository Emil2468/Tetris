public class LongPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    boolean upright = true;
    public LongPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,0)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,1)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,2)));
        bricks.add(new Brick(sideLen, color(255,255,0), new PVector(0,3)));
    }

    void Rotate(int dir) {
        if(upright) {
            bricks.get(0).Move(1, 0);
            bricks.get(1).Move(0, -1);
            bricks.get(2).Move(-1, -2);
            bricks.get(3).Move(-2, -3);
            upright = false;
        } else {
            bricks.get(0).Move(-1, 0);
            bricks.get(1).Move(0, 1);
            bricks.get(2).Move(1, 2);
            bricks.get(3).Move(2, 3);
            upright = true;
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
