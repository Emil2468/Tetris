public class BoxPiece implements IPiece {

    private ArrayList<Brick> bricks = new ArrayList<Brick>();

    public BoxPiece (int sideLen) {
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,0), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(0,1), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,1), this));
        bricks.add(new Brick(sideLen, color(0,0,255), new PVector(1,0), this));
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
