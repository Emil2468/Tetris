public class SPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();
    boolean upright = true;
    public SPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(2,0), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(1,0), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(0,255,0), new PVector(0,1), this));
    }

    void Rotate(int dir) {
        Brick pivot = bricks.get(1);
        for(int i = 0; i < bricks.size(); i++) {
            //Compute difference from pivot to brick
            float deltaX = bricks.get(i).pos.x - pivot.pos.x;
            float deltaY = bricks.get(i).pos.y - pivot.pos.y; 
            //Subtract and add deltas to rotate 90 degrees
            bricks.get(i).Place(pivot.pos.x - deltaY * dir, pivot.pos.y + deltaX * dir);
        }
    }

    void Move(float deltaX, float deltaY) {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Move(deltaX, deltaY);
        }
    }

    void Show() {
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).Show();
        }
    }

    ArrayList<Brick> GetBricks() {
        return bricks;
    }

    int RemoveBrick(Brick brick) {
        bricks.remove(brick);
        return bricks.size();
    }

    void MoveDownIfAbove(int clearedRow) {
        for(int i = 0; i < bricks.size(); i++) {
            //check if value is less than, since rownumber rise as we go down on the screen
            if(bricks.get(i).pos.y <= clearedRow) {
                bricks.get(i).Move(0, 1);
            }
        }
    }
}
