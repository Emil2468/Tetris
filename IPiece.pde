interface IPiece {
    Brick[] Bricks = new Brick[4];
    void Show();
    void Rotate(int dir);
    void Move(float deltaX, float deltaY);
    Brick[] GetBricks();
} 