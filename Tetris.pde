Board board;
void setup() {
    size(360, 720);
    background(0);
    int sideLen = width/20;
    board = new Board(sideLen);
    println((int)0.001);
    //frameRate(20);
}

void draw() {
    background(0);
    board.DrawGrid();
    board.Update();
}

void keyPressed() {
    if(keyCode == LEFT) {
        board.MovePiece(-1);
    } else if(keyCode == RIGHT) {
        board.MovePiece(1);
    }
}


