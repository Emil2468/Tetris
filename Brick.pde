public class Brick {

    private int sideLen;
    private color c;
    PVector pos;
    public IPiece Parrent;
    public Brick (int sideLen, color c, PVector pos, IPiece parrent) {
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
