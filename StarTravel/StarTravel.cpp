#include <iostream>
#include <fstream>
#define FALSE 0
#define TRUE 1

using namespace std;

class StarGate {
public:
    int gateNumber;
    int desID;
};

class Planet {
public:
    int id;
    char federation;
    int totalGateCount;
    StarGate *starGateLink;
};

class Traveler {
public:
    int startPlanetID;
    int endPlanetID;
    char federation;
};

void initVisited(int visited[], int N);
void shortestPath(Planet *planetList, int visited[], int current, int end, char federation);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        cerr << "Argument 1 incorrect" << endl;
        exit(1);
    }

    fstream file;
    file.open(argv[1]);

    if (!file) {
        cerr << "Open file fail" << endl;
        exit(1);
    }

    Planet *planetList;
    Traveler *travelerList;
    int T;
    file >> T;

    // For each test case
    for (int i = 0; i < T; i++) {
        int N;
        file >> N;
        planetList = new Planet[N];
        int *visited = new int[N];
        initVisited(visited, N);

        // For each planet
        for (int j = 0; j < N; j++) {
            file >> planetList[j].id;
            file >> planetList[j].federation;
            file >> planetList[j].totalGateCount;

            StarGate *starGateList = new StarGate[planetList[j].totalGateCount];
            planetList[j].starGateLink = starGateList;

            // For each planet's star gate
            for (int gateIndex = 0; gateIndex < planetList[j].totalGateCount; gateIndex++) {
                char ignoreChar;
                file >> ignoreChar;
                file >> starGateList[gateIndex].gateNumber;
                file >> starGateList[gateIndex].desID;
            }
        }

        int P;
        file >> P;

        // For each traveler
        for (int k = 0; k < P; k++) {
            int startPlanetID;
            int endPlanetID;
            char federation;
            file >> startPlanetID;
            file >> endPlanetID;
            file >> federation;

            shortestPath(planetList, visited, startPlanetID, endPlanetID, federation);
        }

        delete visited;
    }

    file.close();

    return 0;
}

void initVisited(int visited[], int N) {
    for (int i = 0; i < N; i++) {
        visited[i] = FALSE;
    }
}

void shortestPath(Planet *planetList, int visited[], int current, int end, char federation) {
    StarGate travelGate = planetList[current].starGateLink;
    visited[current] = TRUE;

    for (int i = 0; i < planetList[current].totalGateCount; i++) {
        if (!visited[travelGate[i].desID]) {
            shortestPath(planetList, visited, travelGate[i].desID, end, federation);
        }
    }
}
