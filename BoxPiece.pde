public class BoxPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public BoxPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,0)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,1)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,1)));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,0)));
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
