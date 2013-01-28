#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <time.h>
#include <process.h>
#include <windows.h>
#define boardwidth 40
#define boardlength 20
#define infowidth 20
#define adjwidth 19
#define adjlength 3
#define UP 72
#define DOWN 80
#define LEFT 75
#define RIGHT 77
#define Lv2 300
#define Lv3 600
#define Lv4 900
#define Lv5 1200
#define FruitStartTime 12
#define FruitCleanTime 5
#define GameLastTime 20
#define NormalSpeed 10

enum ColorTag {
    C_LIGHT    = 0x08,
    C_BLACK    = 0x00, C_BLUE      = 0x01, C_GREEN    = 0x02, C_CYAN   = 0x03,
    C_RED      = 0x04, C_MAGENTA   = 0x05, C_YELLOW   = 0x06, C_GREY   = 0x07,
    // Background
    C_BBLACK   = 0x00, C_BBLUE     = 0x10, C_BGREEN   = 0x20, C_BCYAN  = 0x30,
    C_BRED     = 0x40, C_BMAGENTA  = 0x50, C_BYELLOW  = 0x60, C_BGREY  = 0x70,
    // Light Background
    C_LBBLACK  = 0x80, C_LBBLUE    = 0x90, C_LBGREEN  = 0xA0, C_LBCYAN = 0xB0,
    C_LBRED    = 0xC0, C_LBMAGENTA = 0xD0, C_LBYELLOW = 0xE0, C_LBGREY = 0xF0,
};
enum DirectionTag {space, wall, wall2, right, down, left, up, food, fruit};
typedef struct location {
    int x;
    int y;
} Location;
BOOL threadstart = FALSE;
BOOL isFruit = FALSE;   // 判斷fruit是否在存在
BOOL callCleanFruit = FALSE;    // 判斷是否要call CleanFruit();
BOOL sound = TRUE;  // 音效開關
BOOL restart = TRUE;   // 判斷呼叫ChallengeMode是否restart, if TRUE 從Lv1開始 if FALSE 先ChangeLevel再Call ChallengeMode
BOOL whichMode;   // 0 = ChallengeMode  1 = SpeedMode
BOOL menustop = FALSE;  // 開啟Menu要暫停時間
BOOL checkLv2 = FALSE, checkLv3 = FALSE, checkLv4 = FALSE, checkLv5 = FALSE;
int ChallengeModeTime;
int score = 0;
int speed = NormalSpeed;
int pass_time = GameLastTime;
int board[boardlength][boardwidth];

Location SnakeHead = {boardwidth / 2 + 1, boardlength / 2};
Location SnakeTail = {boardwidth / 2 - 2, boardlength / 2};
Location Food_xy;
Location Fruit_xy;

int DrawGameStart();
void gotoxy(int, int);
void DrawSnake(int, int, int);
void randomfood();
void randomfruit();
void GameOver();
void Change_xy(int*, int*);
void SetColor(int);
void SetLevel_1();
void SetLevel_2();
void SetLevel_3();
void SetLevel_4();
void SetLevel_5();
void ChallengeMode();
void SpeedMode();
void SetSpeedMode();
void menu(int, int, int);
void CleanBoard();
void CleanFruit();
void CountTime();
void ChangeLevel();
void SetCursorVisible(BOOL ,DWORD);

int main() {
    SetCursorVisible(FALSE,1);
    if (threadstart == FALSE) {
        _beginthread(CountTime, 0, NULL);
        threadstart = TRUE;
    }
    switch (DrawGameStart()) {
    case 0:
        ChallengeMode();
        break;
    case 1:
        SpeedMode();
        break;
    case 3:
        exit(0);
    }
	return 0;
}
int DrawGameStart() {
    int center = 13, high = 4, mode = 0, press;
    CleanBoard();
    SetColor(C_LIGHT|C_CYAN);
    gotoxy(center, high);

    printf("- Greedy Snake -");

    while (1) {
        gotoxy(center-1, high+2);
        SetColor(C_LIGHT|C_GREY);
        printf("[ Challenge Mode ]");
        gotoxy(center+1, high+4);
        printf("[ Speed Mode ]");
        gotoxy(center+1, high+6);
        sound ? printf("[ Sound  ON  ]") : printf("[ Sound  OFF ]");
        gotoxy(center+1, high+8);
        printf("[ Exit  Game ]");
        switch (mode) {
        case 0:
            gotoxy(center-1, high+2);
            SetColor(C_LIGHT|C_MAGENTA);
            printf("[ Challenge Mode ]");
            break;
        case 1:
            gotoxy(center+1, high+4);
            SetColor(C_LIGHT|C_MAGENTA);
            printf("[ Speed Mode ]");
            break;
        case 2:
            gotoxy(center+1, high+6);
            SetColor(C_LIGHT|C_MAGENTA);
            sound ? printf("[ Sound  ON  ]") : printf("[ Sound  OFF ]");
            break;
        case 3:
            gotoxy(center+1, high+8);
            SetColor(C_LIGHT|C_MAGENTA);
            printf("[ Exit  Game ]");
            break;
        }
        press = getch();
        if (press == 224) {
            switch (getch()) {
            case UP  :
                mode--;
                if (mode < 0)mode = 3;
                break;
            case DOWN:
                mode++;
                if (mode > 3)mode = 0;
                break;
            }
        } else if (press == 13) {
            if (mode == 2) {
                sound = !sound;
                gotoxy(center+1, high+6);
                SetColor(C_LIGHT|C_MAGENTA);
                sound ? printf("[ Sound  On  ]") : printf("[ Sound  Off ]");
                continue;
            }
            return mode;
        }
    }
}
void SetColor(int color) {
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), color);
}
void DrawSnake(int a, int b, int dir) {
    int tail_dir;
    // 以下處理蛇頭
    board[SnakeHead.y][SnakeHead.x] = dir;
    if (board[SnakeHead.y += b][SnakeHead.x += a] == wall2) {  //如果碰到'可穿越的'牆壁
        switch (dir) {
        case right :
            SnakeHead.x = 1;
            break;
        case left  :
            SnakeHead.x = boardwidth - 2;
            break;
        case up    :
            SnakeHead.y = boardlength - 2;
            break;
        case down  :
            SnakeHead.y = 1;
            break;
        }
    } else if ((right <= board[SnakeHead.y][SnakeHead.x] && board[SnakeHead.y][SnakeHead.x] <= up) ||
               board[SnakeHead.y][SnakeHead.x] == wall) {  //如果吃到自己 或 碰到'不可穿越'的牆
        GameOver();
    }
    if (board[SnakeHead.y][SnakeHead.x] == food) { //如果吃到食物
        pass_time++;
        SetColor(C_RED|C_LIGHT);
        gotoxy(-16,6);
        printf("Score : %d", score += 10);
        if (sound)Beep(5000,100);
        randomfood();
        SetColor(C_GREEN);
        gotoxy(SnakeHead.x, SnakeHead.y);
        printf("@");
        if (whichMode == 0)ChangeLevel();
        return;
    }
    if (board[SnakeHead.y][SnakeHead.x] == fruit) {     //如果吃到水果
        SetColor(C_RED|C_LIGHT);
        gotoxy(-16,6);
        printf("Score : %d", score += 50);
        if (sound)Beep(3500,100);
        SetColor(C_GREEN);
        gotoxy(SnakeHead.x, SnakeHead.y);
        printf("@");
        if (whichMode == 0) {   // 如果在Challenge Mode下
            ChangeLevel();
            srand(time(NULL));
            (rand() % 2 == 0) ? (speed += 3) : (speed -= 3);  //改變速度的水果
            if (speed < 4 ) speed = 4;
            if (speed > 16) speed = 16;
            SetColor(C_RED|C_LIGHT);
            gotoxy(-16,8);
            printf("Speed : ");
            switch (speed) {
            case 4 :
                printf("Fastest");
                break;
            case 7 :
                printf("Fast   ");
                break;
            case 10 :
                printf("Normal ");
                break;
            case 13:
                printf("Slow   ");
                break;
            case 16:
                printf("Slowest");
                break;
            }
        } else {  // 如果在Speed Mode下
            pass_time += 5;
        }
        return;
    } else
        CleanFruit();
    SetColor(C_GREEN);  //沒碰牆時一般走，如果碰牆上方已改變座標
    gotoxy(SnakeHead.x, SnakeHead.y);
    printf("@");    //先印出蛇頭  下次進來才把 board[SnakeHead.x][SnakeHead.y] = '蛇身'

    //以下處理蛇尾
    tail_dir = board[SnakeTail.y][SnakeTail.x];
    gotoxy(SnakeTail.x, SnakeTail.y);   //刪除 board[SnakeTail.x][SnakeTail.y] = '蛇身'的蛇尾
    printf(" ");
    switch (tail_dir) { //如果刪蛇尾時沒碰牆
    case right :
        board[SnakeTail.y][SnakeTail.x++] = space;
        break;
    case down  :
        board[SnakeTail.y++][SnakeTail.x] = space;
        break;
    case left  :
        board[SnakeTail.y][SnakeTail.x--] = space;
        break;
    case up    :
        board[SnakeTail.y--][SnakeTail.x] = space;
        break;
    }
    if (board[SnakeTail.y][SnakeTail.x] == wall2) {  //如果刪蛇尾時碰到'可穿越'的牆
        switch (tail_dir) {
        case right :
            SnakeTail.x = 1;
            break;
        case left  :
            SnakeTail.x = boardwidth - 2;
            break;
        case up    :
            SnakeTail.y = boardlength - 2;
            break;
        case down  :
            SnakeTail.y = 1;
            break;
        }
    }
}

void gotoxy(int x, int y) {
    COORD point;
    point.X = x + adjwidth;
    point.Y = y + adjlength;
    SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), point);
}
void randomfood() {
    srand(time(NULL));
    do {
        Food_xy.x = rand() % (boardwidth - 2) + 1;
        Food_xy.y = rand() % (boardlength - 2) + 1;
    } while (board[Food_xy.y][Food_xy.x] != space);
    board[Food_xy.y][Food_xy.x] = food;
    SetColor(C_YELLOW|C_LIGHT);
    gotoxy(Food_xy.x, Food_xy.y);
    printf("%c", 6);
}
void randomfruit() {
    if (!isFruit)return;
    isFruit = FALSE;
    do {
        Fruit_xy.x = rand() % (boardwidth - 2) + 1;
        Fruit_xy.y = rand() % (boardlength - 2) + 1;
    } while (board[Fruit_xy.y][Fruit_xy.x] != space);
    board[Fruit_xy.y][Fruit_xy.x] = fruit;
    SetColor(C_CYAN);
    gotoxy(Fruit_xy.x, Fruit_xy.y);
    printf("%c", 2);
}
void GameOver() {
    int i, j, count = 0, list = 0, key;
    restart = TRUE;
    while (count++ < 3) {
        for (i = 0; i < boardlength; i++) {
            for (j = 0; j < boardwidth; j++) {
                if (right <= board[i][j] && board[i][j] <= up) {
                    gotoxy(j, i);
                    printf(" ");
                }
            }
        }
        if (pass_time == 0 && whichMode == 1) {
            SetColor(C_BBLACK);
            gotoxy(SnakeHead.x, SnakeHead.y);
            printf(" ");
        }
        if (sound)Beep(1000,300);
        Sleep(100);
        SetColor(C_BBLACK);
        for (i = 0; i < boardlength; i++) {
            for (j = 0; j < boardwidth; j++) {
                if (right <= board[i][j] && board[i][j] <= up) {
                    SetColor(C_GREEN);
                    gotoxy(j, i);
                    printf("@");
                }
            }
        }
        if (pass_time == 0 && whichMode == 1) {
            SetColor(C_GREEN);
            gotoxy(SnakeHead.x, SnakeHead.y);
            printf("@");
        }
        Sleep(300);
    }
    system("cls");
    SetColor(C_BRED|C_GREY|C_LIGHT);
    gotoxy(13, 7);
    if (pass_time == 0 && whichMode == 1) {
        printf(" Time is up ! ");
    } else {
        printf(" Game Over ! ");
    }
    while (1) {
        SetColor(C_BBLACK|C_GREY);
        gotoxy(4, 10);
        printf("Try again");
        gotoxy(14, 10);
        printf("/");
        gotoxy(16, 10);
        printf("Start Menu");
        gotoxy(27, 10);
        printf("/");
        gotoxy(29, 10);
        printf("Exit");
        switch (list) {
        case 0:
            SetColor(C_BGREY|C_BBLACK);
            gotoxy(4, 10);
            printf("Try again");
            break;
        case 1:
            SetColor(C_BGREY|C_BBLACK);
            gotoxy(16, 10);
            printf("Start Menu");
            break;
        case 2:
            SetColor(C_BGREY|C_BBLACK);
            gotoxy(29, 10);
            printf("Exit");
            break;
        }
        key = getch();
        if (key == 224) {
            switch (getch()) {
            case LEFT  :
                list--;
                if (list < 0)list = 2;
                break;
            case RIGHT :
                list++;
                if (list > 2)list = 0;
                break;
            }
        } else if (key == 13) {
            switch (list) {
            case 0: // Try Again
                if (whichMode == 0) {   // Challenge Mode
                    ChallengeMode();
                } else {                // Speed Mode
                    SpeedMode();
                }
            case 1: // Start Menu
                main();
            case 2: // Exit
                exit(1);
            }
        }
    }
}
void Change_xy(int *x, int *y) {
    switch (board[*y][*x]) {
    case right :
        *x = 1;
        break;
    case left  :
        *x = boardwidth - 2;
        break;
    case up    :
        *y = boardlength - 2;
        break;
    case down  :
        *y = 1;
        break;
    }
}
void ChallengeMode() {
    int a = 1, b = 0, dir = right, key;
    whichMode = 0;
    if (restart == TRUE) {
        checkLv2 = FALSE, checkLv3 = FALSE, checkLv4 = FALSE, checkLv5 = FALSE;
        ChallengeModeTime = 0;
        score = 0;
        SetLevel_1();
        restart = FALSE;
    }
    SetColor(C_BLUE|C_LIGHT);
    gotoxy(45,1);
    printf("Control Key");
    gotoxy(50,3);
    printf("↑");
    gotoxy(48,4);
    printf("←↓→");
    gotoxy(43,7);
    printf("ESE : Pause/Menu");
    gotoxy(45,11);
    printf("%c : + 10 Point", 6);
    gotoxy(45,13);
    printf("%c : + 50 Point", 2);
    gotoxy(49,14);
    printf("/ Speed Up");
    gotoxy(49,15);
    printf("/ Slow Down");
    while (1) {
        SetColor(C_RED|C_LIGHT);
        gotoxy(-16, 10);
        printf("Time : %d s  ", ChallengeModeTime);
        randomfruit();
        gotoxy(boardwidth + 3,6);
        if (!kbhit()) {
            DrawSnake(a, b, dir);
        } else {
            if ((key = getch()) == 27) {
                menu(a, b, dir);
            } else if (key == 224) {
                key = getch();
                if ((key == UP    && dir == down) ||
                        (key == DOWN  && dir == up) ||
                        (key == LEFT  && dir == right) ||
                        (key == RIGHT && dir == left)) {
                    continue;
                }
                switch (key) {
                case UP   :
                    a = 0, b =-1, dir = up;
                    break;
                case DOWN :
                    a = 0, b = 1;
                    dir = down;
                    break;
                case LEFT :
                    a =-1, b = 0;
                    dir = left;
                    break;
                case RIGHT:
                    a = 1, b = 0;
                    dir = right;
                    break;
                }
                DrawSnake(a, b, dir);
            } else
                continue;
        }
        Sleep(speed * 10);
    }
}
void SetLevel_1() {
    int i, j;
    CleanBoard();
    SetColor(C_BYELLOW|C_BGREEN);
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                board[i][j] = wall;
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
        } else {
            board[i][0] = wall;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
    }
    SetColor(C_CYAN|C_LIGHT);
    gotoxy(-18,1);
    printf("-Challenge Mode-");
    gotoxy(-16,3);
    printf("   Level 1");
    gotoxy(-16,13);
    printf("Next Level");
    gotoxy(-16,14);
    printf("Score : %d", Lv2);
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void SetLevel_2() {
    int i, j;
    CleanBoard();
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                board[i][j] = wall2;
                SetColor(C_BGREY);
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
        } else if (6 <= i && i <= 13) {
            SetColor(C_BGREEN|C_BYELLOW);
            board[i][0] = wall;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall;
            gotoxy(boardwidth - 1, i);
            printf("  ");
            if (i == 6 || i == 13) {
                for (j = 11; j <= 30; j++) {
                    board[i][j] = wall;
                    SetColor(C_BGREEN|C_BYELLOW);
                    gotoxy(j, i);
                    printf(" ");
                }
            }
        } else {
            SetColor(C_BGREY);
            board[i][0] = wall2;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall2;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
    }

    SetColor(C_CYAN|C_LIGHT);
    gotoxy(-18,1);
    printf("-Challenge Mode-");
    gotoxy(-16,3);
    printf("   Level 2");
    gotoxy(-16,13);
    printf("Next Level");
    gotoxy(-16,14);
    printf("Score : %d", Lv3);
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void SetLevel_3() {
    int i, j;
    CleanBoard();
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                board[i][j] = wall2;
                SetColor(C_BGREY);
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
        } else {
            if (i == 5 || i == 14) {
                for (j = 11; j <= 28; j++) {
                    board[i][j] = wall;
                    SetColor(C_BGREEN|C_BYELLOW);
                    gotoxy(j, i);
                    printf(" ");

                }
            } else if (7 <= i && i <= 12) {
                SetColor(C_BGREEN|C_BYELLOW);
                board[i][5] = wall;
                board[i][6] = wall;
                gotoxy(5, i);
                printf("  ");
                board[i][boardwidth - 7] = wall;
                board[i][boardwidth - 6] = wall;
                gotoxy(boardwidth - 7, i);
                printf("  ");
            }
            SetColor(C_BGREY);
            board[i][0] = wall2;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall2;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
    }

    SetColor(C_CYAN|C_LIGHT);
    gotoxy(-18,1);
    printf("-Challenge Mode-");
    gotoxy(-16,3);
    printf("   Level 3");
    gotoxy(-16,13);
    printf("Next Level");
    gotoxy(-16,14);
    printf("Score : %d", Lv4);
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void SetLevel_4() {
    int i, j;
    CleanBoard();
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                SetColor(C_BGREY);
                board[i][j] = wall2;
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
        } else {
            if (i >= 10) {
                SetColor(C_BGREEN|C_BYELLOW);
                board[i][14] = wall;
                board[i][15] = wall;
                gotoxy(14, i);
                printf("  ");
            } else {
                SetColor(C_BGREEN|C_BYELLOW);
                board[i][boardwidth - 16] = wall;
                board[i][boardwidth - 15] = wall;
                gotoxy(boardwidth - 16, i);
                printf("  ");
            }
            if (i == 7) {
                for (j = 0; j <= 19; j++) {
                    SetColor(C_BGREEN|C_BYELLOW);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
            }
            if (i == 12) {
                for (j = 20; j < boardwidth; j++) {
                    SetColor(C_BGREEN|C_BYELLOW);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
            }
            SetColor(C_BGREY);
            board[i][0] = wall2;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall2;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
    }
    SetColor(C_CYAN|C_LIGHT);
    gotoxy(-18,1);
    printf("-Challenge Mode-");
    gotoxy(-16,3);
    printf("   Level 4");
    gotoxy(-16,13);
    printf("Next Level");
    gotoxy(-16,14);
    printf("Score : %d", Lv5);
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void SetLevel_5() {
    int i, j;
    CleanBoard();
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                board[i][j] = wall2;
                SetColor(C_BGREY);
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
            if (i == 0)
                for (j = 10; j <= 29; j++) {
                    SetColor(C_BYELLOW|C_BGREEN);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
        } else {
            if (i == 6) {
                for (j = 0; j <= 17; j++) {
                    SetColor(C_BYELLOW|C_BGREEN);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
                for (j = 23; j < boardwidth; j++) {
                    SetColor(C_BYELLOW|C_BGREEN);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
            }
            if (i == 13) {
                for (j = 0; j < boardwidth; j++) {
                    SetColor(C_BYELLOW|C_BGREEN);
                    board[i][j] = wall;
                    gotoxy(j, i);
                    printf(" ");
                }
            }
            SetColor(C_BGREY);
            board[i][0] = wall2;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall2;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
        if (1 <= i && i <= 6) {
            SetColor(C_BYELLOW|C_BGREEN);
            board[i][17] = wall;
            gotoxy(17, i);
            printf(" ");
        }
        if (13 <= i && i < boardlength - 1) {
            SetColor(C_BYELLOW|C_BGREEN);
            board[i][23] = wall;
            gotoxy(23, i);
            printf(" ");
        }
    }
    SetColor(C_CYAN|C_LIGHT);
    gotoxy(-18,1);
    printf("-Challenge Mode-");
    gotoxy(-16,3);
    printf("   Level 5");
    gotoxy(-16,13);
    printf("Next Level");
    gotoxy(-16,14);
    printf("Score : ???");
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void SpeedMode() {
    int a = 1, b = 0, dir = right, key;
    SetSpeedMode();
    if (restart == TRUE) {
        restart = FALSE;
    }
    while (1) {
        SetColor(C_RED|C_LIGHT);
        gotoxy(-16, 10);
        printf("Time : %d s  ", pass_time);
        randomfruit();
        if (pass_time == 0)GameOver();
        gotoxy(boardwidth + 3,6);
        if (!kbhit()) {
            DrawSnake(a, b, dir);
        } else {
            if ((key = getch()) == 27) {
                menu(a, b, dir);
            } else if (key == 224) {
                key = getch();
                if ((key == UP && dir == down) ||
                        (key == DOWN  && dir == up) ||
                        (key == LEFT  && dir == right) ||
                        (key == RIGHT && dir == left)) {
                    continue;
                }
                switch (key) {
                case UP   :
                    a = 0, b =-1, dir = up;
                    break;
                case DOWN :
                    a = 0, b = 1;
                    dir = down;
                    break;
                case LEFT :
                    a =-1, b = 0;
                    dir = left;
                    break;
                case RIGHT:
                    a = 1, b = 0;
                    dir = right;
                    break;
                }
                DrawSnake(a, b, dir);
            } else if (key == 'z' && speed > 4) {
                speed -= 3;
                SetColor(C_RED|C_LIGHT);
                gotoxy(-16,8);
                printf("Speed : ");
                switch (speed) {
                case 4 :
                    printf("Fastest");
                    break;
                case 7 :
                    printf("Fast   ");
                    break;
                case 10 :
                    printf("Normal ");
                    break;
                case 13:
                    printf("Slow   ");
                    break;
                case 16:
                    printf("Slowest");
                    break;
                }
                continue;
            } else if (key == 'x' && speed < 16) {
                speed += 3;
                SetColor(C_RED|C_LIGHT);
                gotoxy(-16,8);
                printf("Speed : ");
                switch (speed) {
                case 4 :
                    printf("Fastest");
                    break;
                case 7 :
                    printf("Fast   ");
                    break;
                case 10 :
                    printf("Normal ");
                    break;
                case 13:
                    printf("Slow   ");
                    break;
                case 16:
                    printf("Slowest");
                    break;
                }
                continue;
            } else
                continue;
        }
        Sleep(speed * 10);
    }
}
void SetSpeedMode() {
    int i, j;
    whichMode = 1;
    score = 0;
    CleanBoard();
    SetColor(C_BGREY);
    for (i = 0; i < boardlength; i++) {
        if (i == 0 || i == boardlength - 1) {
            for (j = 0; j < boardwidth; j++) {
                board[i][j] = wall2;
                gotoxy(j - 1, i);
                printf("  ");
            }
            printf(" ");
        } else {
            board[i][0] = wall2;
            gotoxy(-1, i);
            printf("  ");
            board[i][boardwidth - 1] = wall2;
            gotoxy(boardwidth - 1, i);
            printf("  ");
        }
    }
    SetColor(C_GREEN|C_LIGHT);
    gotoxy(-17,1);
    printf("- Speed Mode -");
    SetColor(C_RED|C_LIGHT);
    gotoxy(-16,6);
    printf("Score : %d", score);
    gotoxy(-16,8);
    printf("Speed : ");
    switch (speed) {
    case 4 :
        printf("Fastest");
        break;
    case 7 :
        printf("Fast   ");
        break;
    case 10 :
        printf("Normal ");
        break;
    case 13:
        printf("Slow   ");
        break;
    case 16:
        printf("Slowest");
        break;
    }
    SetColor(C_BLUE|C_LIGHT);
    gotoxy(45,1);
    printf("Control Key");
    gotoxy(50,3);
    printf("↑");
    gotoxy(48,4);
    printf("←↓→");
    gotoxy(43,7);
    printf("ESE : Pause/Menu");
    gotoxy(45,9);
    printf("Z : Speed Up");
    gotoxy(45,11);
    printf("X : Slow Down");
    gotoxy(45,13);
    printf("%c : + 1 sec", 6);
    gotoxy(45,15);
    printf("%c : + 5 sec", 2);
    SetColor(C_GREEN);
    gotoxy(SnakeTail.x, SnakeTail.y);
    printf("@@@@");
    for (i = 0; i < 4; i++)
        board[SnakeTail.y][SnakeTail.x + i] = right;
    randomfood();
    gotoxy(0, 22);
}
void menu(int a, int b, int dir) {
    int i, j, key, list = 0;
    menustop = TRUE;
    while (1) {
        SetColor(C_BCYAN|C_GREY|C_LIGHT);
        gotoxy(13, 4);
        printf("              ");
        gotoxy(13, 5);
        printf("   - Menu -   ");
        gotoxy(13, 6);
        printf("              ");
        gotoxy(13, 7);
        printf("  Return Game ");
        gotoxy(13, 8);
        printf("              ");
        gotoxy(13, 9);
        printf("  Start Menu  ");
        gotoxy(13, 10);
        printf("              ");
        gotoxy(13, 11);
        sound ? printf("  Sound  ON   ") : printf("  Sound  OFF  ");
        gotoxy(13, 12);
        printf("              ");
        gotoxy(13, 13);
        printf("  Exit  Game  ");
        gotoxy(13, 14);
        printf("              ");
        switch (list) {
        case 0:
            SetColor(C_BCYAN|C_MAGENTA);
            gotoxy(13, 7);
            printf("  Return Game ");
            break;
        case 1:
            SetColor(C_BCYAN|C_MAGENTA);
            gotoxy(13, 9);
            printf("  Start Menu  ");
            break;
        case 2:
            SetColor(C_BCYAN|C_MAGENTA);
            gotoxy(13, 11);
            sound ? printf("  Sound  ON   ") : printf("  Sound  OFF  ");
            break;
        case 3:
            SetColor(C_BCYAN|C_MAGENTA);
            gotoxy(13, 13);
            printf("  Exit  Game  ");
            break;
        }
        key = getch();
        if (key == 224) {
            switch (getch()) {
            case UP  :
                list--;
                if (list < 0)list = 3;
                break;
            case DOWN:
                list++;
                if (list > 3)list = 0;
                break;
            }
        } else if (key == 13 || key == 27) {
            menustop = FALSE;
            if (key == 27)list = 0;
            switch (list) {
            case 0:   //Return Game
                for (i = 4; i < 15; i++) {
                    for (j = 13; j < 27; j++) {
                        if (right <= board[i][j] && board[i][j] <= up) {
                            gotoxy(j, i);
                            SetColor(C_GREEN);
                            printf("@");

                        } else if (board[i][j] == wall) {
                            gotoxy(j, i);
                            SetColor(C_BGREEN|C_BYELLOW);
                            printf(" ");
                        } else {
                            gotoxy(j, i);
                            SetColor(C_BBLACK);
                            printf(" ");
                        }
                    }
                }
                gotoxy(SnakeHead.x, SnakeHead.y);
                SetColor(C_GREEN);
                printf("@");
                gotoxy(Food_xy.x, Food_xy.y);
                SetColor(C_YELLOW|C_LIGHT);
                printf("%c", 6);
                return;
            case 1:  // Start Menu
                restart = TRUE;
                main();
            case 2:  // Sound ON / OFF
                sound = !sound;
                SetColor(C_BCYAN|C_MAGENTA);
                gotoxy(13, 11);
                sound ? printf("  Sound  ON   ") : printf("  Sound  OFF  ");
                break;
            case 3:  // EXIT
                exit(1);
            }
        }
    }
}
void CleanBoard() {
    int i, j;
    for (i = 0; i < boardlength; i++)
        for (j = 0; j < boardwidth; j++)
            board[i][j] = 0;
    speed = NormalSpeed;
    pass_time = GameLastTime;
    isFruit = FALSE;
    callCleanFruit = FALSE;
    SnakeHead.x = boardwidth / 2 + 1;
    SnakeHead.y = boardlength / 2;
    SnakeTail.x = boardwidth / 2 - 2;
    SnakeTail.y = boardlength / 2;
    SetColor(C_LIGHT|C_GREY|C_BBLACK);
    system("cls");
}
void CleanFruit() {
    if (!callCleanFruit)return;
    callCleanFruit = FALSE;
    isFruit = FALSE;
    if (board[Fruit_xy.y][Fruit_xy.x] == fruit) {
        board[Fruit_xy.y][Fruit_xy.x] = space;
        SetColor(C_BBLACK);
        gotoxy(Fruit_xy.x, Fruit_xy.y);
        printf(" ");
    }
}
void CountTime() {
    int fruit_start = 0, fruit_clean_time = FruitCleanTime;
    BOOL start_clean = FALSE;   // if == TRUE 才開始減 fruit_clean_time 的時間
    while (1) {
        while (menustop);   //開啟menu時暫停計時器
        if (start_clean == TRUE)   //設定水果出現後多久消失
            fruit_clean_time--;
        if (fruit_start % FruitStartTime == 0) {  //設定多久水果出現
            isFruit = TRUE;
            start_clean = TRUE;
            fruit_clean_time = FruitCleanTime;
        }
        if (fruit_clean_time == 0) {
            callCleanFruit = TRUE;
            isFruit = FALSE;
            start_clean = FALSE;
        }
        if (pass_time > 0 && whichMode == 1)pass_time--;
        ChallengeModeTime++;
        fruit_start++;
        Sleep(1000);
    }
}
void ChangeLevel() { //判斷分數是否足夠接下一關
    restart = FALSE;
    if ((score >= Lv2 && checkLv2 == FALSE) ||
            (score >= Lv3 && checkLv3 == FALSE) ||
            (score >= Lv4 && checkLv4 == FALSE) ||
            (score >= Lv5 && checkLv5 == FALSE)) {
        do {
            SetColor(C_BRED|C_GREY|C_LIGHT);
            gotoxy(11, 7);
            printf(" Congratulations ! ");
            SetColor(C_BBLACK|C_GREY);
            gotoxy(7, 10);
            printf(" Press ""ENTER"" to next level");
        } while (getch() != 13);
    }
    if (score >= Lv2 && checkLv2 == FALSE) {
        checkLv2 = TRUE;
        SetLevel_2();
        ChallengeMode();
    } else if (score >= Lv3 && checkLv3 == FALSE) {
        checkLv3 = TRUE;
        SetLevel_3();
        ChallengeMode();
    } else if (score >= Lv4 && checkLv4 == FALSE) {
        checkLv4 = TRUE;
        SetLevel_4();
        ChallengeMode();
    } else if (score >= Lv5 && checkLv5 == FALSE) {
        checkLv5 = TRUE;
        SetLevel_5();
        ChallengeMode();
    }
}
void SetCursorVisible(BOOL _bVisible,DWORD _dwSize) {
    CONSOLE_CURSOR_INFO CCI;
    CCI.bVisible = _bVisible;
    CCI.dwSize = _dwSize;
    SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE),&CCI);
}
