public class Brick {

    private int sideLen;
    private color c;
    PVector pos;
    public Brick (int sideLen, color c, PVector pos) {
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
