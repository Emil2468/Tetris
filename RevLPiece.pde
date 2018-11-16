public class RevLPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public RevLPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,0)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,1)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(1,2)));
        bricks.add(new Brick(sideLen, color(255,0,0), new PVector(0,2)));
    }
    void Rotate(int dir) {
        //Box is the same no matter rotation
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
