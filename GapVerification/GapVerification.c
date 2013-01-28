#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#define FALSE 0
#define TRUE 1

typedef struct polygon {
    int index;
    int x;
    int y;
    int width;
    int length;
    struct polygon *link;
} Polygon;

typedef struct yLevelList {
    int y;
    int length;
    struct polygon **link;
} YLevelList;

void setYLevelList(YLevelList *yLevelListUp, YLevelList *yLevelListDown, int YLevelCount, Polygon *polygon, int P);
void findVerticalConnect(YLevelList *yLevelListUp, YLevelList *yLevelListDown, int YLevelCount);
void findHorizontalConnect(YLevelList *yLevelListDown, int YLevelCount);
void findViolation(YLevelList *yLevelListDown, int YLevelCount, int S);
void interlockPolygonsHeapSort(Polygon *numbers[], int array_size, int (*compare)(void *a, void *b));
void interlockPolygonsSiftDown(Polygon *numbers[], int root, int bottom, int (*compare)(void *a, void *b));
void heapSort(Polygon numbers[], int array_size, int (*compare)(void *a, void *b));
void siftDown(Polygon numbers[], int root, int bottom, int (*compare)(void *a, void *b));
void swapPolygon(Polygon *p1, Polygon *p2);
int sortByX(void *a, void *b);
int sortByY(void *a, void *b);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Argument invaild\n");
        exit(1);
    }

    FILE *fp = fopen(argv[1], "r");
    if (!fp) {
        fprintf(stderr, "Open file error\n");
        exit(1);
    }

    // Estimate time cost
    clock_t start_time, end_time;
    float total_time = 0.0;
    start_time = clock();

    // Read Test Case Number
    int N, P, S;
    fscanf(fp, "%d", &N);

    int n, i, j;
    for (n = 1; n <= N; n++) {
        // Read P and S of each test case
        fscanf(fp, "%d", &P);
        fscanf(fp, "%d", &S);

        // Initialize polygons
        Polygon *polygon = (Polygon *)malloc(sizeof(Polygon) * P);
        for (i = 0; i < P; i++) {
            polygon[i].link = &polygon[i];
            fscanf(fp, "%d", &(polygon[i].index));
            fscanf(fp, "%d", &(polygon[i].x));
            fscanf(fp, "%d", &(polygon[i].y));
            fscanf(fp, "%d", &(polygon[i].width));
            fscanf(fp, "%d", &(polygon[i].length));
        }

        // Sort polygon arrays by y of polygons
        heapSort(polygon, P, sortByY);

        // Count Y level
        int YLevelCount = 1;
        for (i = 0; i < P - 1; i++) {
            if (polygon[i].y != polygon[i + 1].y) {
                YLevelCount++;
            }
        }

        // Create Level list to record the polygon pass Y axis
        YLevelList *yLevelListUp = (YLevelList *)malloc(sizeof(YLevelList) * YLevelCount);
        YLevelList *yLevelListDown = (YLevelList *)malloc(sizeof(YLevelList) * YLevelCount);

        // Set YLevelList
        setYLevelList(yLevelListUp, yLevelListDown, YLevelCount, polygon, P);

        // Find Vertical Connect
        findVerticalConnect(yLevelListUp, yLevelListDown, YLevelCount);

        // Find Horizontal Connect
        findHorizontalConnect(yLevelListDown, YLevelCount);

        // Find Violation
        findViolation(yLevelListDown, YLevelCount, S);

        // Release
        free(polygon);
    }

    // Print Time Cost
    end_time = clock();
    total_time = (double)(end_time - start_time) / CLOCKS_PER_SEC;
    printf("\n\n- Time : %f sec. -\n", total_time);
}

void setYLevelList(YLevelList *yLevelListUp, YLevelList *yLevelListDown, int YLevelCount, Polygon *polygon, int P) {
    // Record Y value in each level
    int i;
    int level = 0;
    for (i = 0; i < P - 1; i++) {
        if (polygon[i].y != polygon[i + 1].y) {
            yLevelListUp[level].y = polygon[i].y;
            yLevelListDown[level++].y = polygon[i].y;
        }
    }
    yLevelListUp[level].y = polygon[i].y;
    yLevelListDown[level].y = polygon[i].y;

    for (level = 0; level < YLevelCount; level++) {
        // Count the list length of level y
        yLevelListUp[level].length = 0;
        yLevelListDown[level].length = 0;
        for (i = 0; i < P; i++) {
            if (yLevelListUp[level].y < polygon[i].y && yLevelListUp[level].y == polygon[i].y - polygon[i].length) {
                yLevelListUp[level].length++;
            }
            if (yLevelListUp[level].y <= polygon[i].y && yLevelListUp[level].y > polygon[i].y - polygon[i].length) {
                yLevelListDown[level].length++;
            }
            if (yLevelListUp[level].y > polygon[i].y) break;
        }

        // Create PolygonPtrList to hold polygon pointer
        Polygon **upPolygonList = (Polygon **)malloc(sizeof(Polygon *) * yLevelListUp[level].length);
        Polygon **downPolygonList = (Polygon **)malloc(sizeof(Polygon *) * yLevelListDown[level].length);
        yLevelListUp[level].link = upPolygonList;
        yLevelListDown[level].link = downPolygonList;

        // Search polygon which should be in level up or level down at current level
        int m, n;
        for (i = 0, m = 0, n = 0; m < yLevelListUp[level].length || n < yLevelListDown[level].length; i++) {
            if (yLevelListUp[level].y < polygon[i].y && yLevelListUp[level].y == polygon[i].y - polygon[i].length) {
                upPolygonList[m++] = &polygon[i];
            }
            if (yLevelListUp[level].y <= polygon[i].y && yLevelListUp[level].y > polygon[i].y - polygon[i].length) {
                downPolygonList[n++] = &polygon[i];
            }
        }

        // Sort polygons in current level
        interlockPolygonsHeapSort(yLevelListUp[level].link, yLevelListUp[level].length, sortByX);
        interlockPolygonsHeapSort(yLevelListDown[level].link, yLevelListDown[level].length, sortByX);
    }
}

void findVerticalConnect(YLevelList *yLevelListUp, YLevelList *yLevelListDown, int YLevelCount) {
    int level;
    for (level = 0; level < YLevelCount; level++) {
        if (yLevelListUp[level].length == 0) continue;

        int m, n;
        for (m = n = 0; m < yLevelListUp[level].length && n < yLevelListDown[level].length;) {
            if (yLevelListUp[level].link[m]->x < yLevelListDown[level].link[n]->x + yLevelListDown[level].link[n]->width &&
                yLevelListUp[level].link[m]->x + yLevelListUp[level].link[m]->width > yLevelListDown[level].link[n]->x) {
                yLevelListDown[level].link[n]->link = yLevelListUp[level].link[m]->link;
            }

            if (yLevelListUp[level].link[m]->x + yLevelListUp[level].link[m]->width <
                yLevelListDown[level].link[n]->x + yLevelListDown[level].link[n]->width) {
                m++;
            } else {
                n++;
            }
        }
    }
}

void findHorizontalConnect(YLevelList *yLevelListDown, int YLevelCount) {
    int level;
    int k;
    for (level = 0; level < YLevelCount; level++) {
        for (k = 0; k < yLevelListDown[level].length - 1; k++) {
            if (yLevelListDown[level].link[k]->x + yLevelListDown[level].link[k]->width == yLevelListDown[level].link[k + 1]->x) {
                yLevelListDown[level].link[k + 1]->link->link = yLevelListDown[level].link[k]->link;
                yLevelListDown[level].link[k + 1]->link = yLevelListDown[level].link[k]->link;
            }
        }
    }
}

void findViolation(YLevelList *yLevelListDown, int YLevelCount, int S) {
    int level;
    int k;
    int foundViolation = FALSE;
    for (level = 0; level < YLevelCount; level++) {
        for (k = 0; k < yLevelListDown[level].length - 1; k++) {
            if (yLevelListDown[level].link[k]->link != yLevelListDown[level].link[k + 1]->link &&
                yLevelListDown[level].link[k + 1]->x - yLevelListDown[level].link[k]->x - yLevelListDown[level].link[k]->width < S) {
                foundViolation = TRUE;
                printf("(%d %d)", yLevelListDown[level].link[k]->index, yLevelListDown[level].link[k + 1]->index);
            } else if (foundViolation == TRUE) {
                break;
            }
        }
    }
}

void interlockPolygonsHeapSort(Polygon *numbers[], int array_size, int (*compare)(void *a, void *b)) {
    int i;

    for (i = (array_size / 2); i >= 0; i--) {
        interlockPolygonsSiftDown(numbers, i, array_size - 1, compare);
    }

    for (i = array_size - 1; i >= 1; i--) {
        // Swap
        Polygon *tempPtr = numbers[0];
        numbers[0] = numbers[i];
        numbers[i] = tempPtr;

        interlockPolygonsSiftDown(numbers, 0, i - 1, compare);
    }
}

void interlockPolygonsSiftDown(Polygon *numbers[], int root, int bottom, int (*compare)(void *a, void *b)) {
    int maxChild = root * 2 + 1;

    // Find the biggest child
    if (maxChild < bottom) {
        int otherChild = maxChild + 1;
        // Reversed for stability
        maxChild = compare(&numbers[otherChild], &numbers[maxChild]) ? otherChild : maxChild;
    } else {
        // Don't overflow
        if (maxChild > bottom) return;
    }

    // If we have the correct ordering, we are done.
    if (compare(&numbers[root], &numbers[maxChild])) return;

    // Swap
    Polygon *tempPtr = numbers[root];
    numbers[root] = numbers[maxChild];
    numbers[maxChild] = tempPtr;

    // Tail queue recursion. Will be compiled as a loop with correct compiler switches.
    interlockPolygonsSiftDown(numbers, maxChild, bottom, compare);
}

void heapSort(Polygon numbers[], int array_size, int (*compare)(void *a, void *b)) {
    int i;

    for (i = (array_size / 2); i >= 0; i--) {
        siftDown(numbers, i, array_size - 1, compare);
    }

    for (i = array_size - 1; i >= 1; i--) {
        swapPolygon(&numbers[0], &numbers[i]);
        siftDown(numbers, 0, i - 1, compare);
    }
}

void siftDown(Polygon numbers[], int root, int bottom, int (*compare)(void *a, void *b)) {
    int maxChild = root * 2 + 1;

    // Find the biggest child
    if (maxChild < bottom) {
        int otherChild = maxChild + 1;
        // Reversed for stability
        maxChild = compare(&numbers[otherChild], &numbers[maxChild]) ? otherChild : maxChild;
    } else {
        // Don't overflow
        if (maxChild > bottom) return;
    }

    // If we have the correct ordering, we are done.
    if (compare(&numbers[root], &numbers[maxChild])) return;

    // Swap
    swapPolygon(&numbers[root], &numbers[maxChild]);

    // Tail queue recursion. Will be compiled as a loop with correct compiler switches.
    siftDown(numbers, maxChild, bottom, compare);
}

void swapPolygon(Polygon *p1, Polygon *p2) {
    Polygon temp;
    temp = *p1;
    temp.link = p2->link;
    p2->link = p1->link;
    *p1 = *p2;
    *p2 = temp;
}

int sortByX(void *a, void *b) {
    Polygon **p1 = (Polygon **)a;
    Polygon **p2 = (Polygon **)b;

    return ((*p1)->x > (*p2)->x) ? 1 : 0;
}

int sortByY(void *a, void *b) {
    Polygon *p1 = (Polygon *)a;
    Polygon *p2 = (Polygon *)b;

    return (p1->y < p2->y) ? 1 : 0;
}

