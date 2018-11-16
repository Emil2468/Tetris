interface IPiece {
    void Show();
    void Rotate(int dir);
    void Move(float deltaX, float deltaY);
    ArrayList<Brick> GetBricks();
} 