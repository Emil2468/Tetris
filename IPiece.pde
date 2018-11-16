//In hindsigth this would perhaps have been better as an abstract class, since so many implementations are the same
interface IPiece {
    void Show();

    //It is expected that if rotate(dir) and then Rotate(-dir) is called, piece will be same rotation as before
    void Rotate(int dir);
    void Move(float deltaX, float deltaY);
    ArrayList<Brick> GetBricks();
    
    //Removes the given brick for list of bricks, and returns number of bricks left
    int RemoveBrick(Brick brick);

    //Use to move individual bricks that are above a recently cleared row
    void MoveDownIfAbove(int clearedRow);
} 