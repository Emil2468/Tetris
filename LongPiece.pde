public class LongPiece implements IPiece {

    public Brick[] Bricks = new Brick[4];

    public LongPiece (int sideLen) {
        Bricks[0] = new Brick(sideLen, color(255,255,0), new PVector(0,0));
        Bricks[1] = new Brick(sideLen, color(255,255,0), new PVector(0,1));
        Bricks[2] = new Brick(sideLen, color(255,255,0), new PVector(0,2));
        Bricks[3] = new Brick(sideLen, color(255,255,0), new PVector(0,3));
    }
    void Rotate(int dir) {
        //Box is the same no matter rotation
    }

    void Move(float deltaX, float deltaY) {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Move(deltaX, deltaY);
        }
    }

    void Show() {
        for(int i = 0; i < 4; i++) {
            Bricks[i].Show();
        }
    }

    Brick[] GetBricks() {
        return Bricks;
    }

}
