#include <iostream>
#include <fstream>
#include <string>

using namespace std ;
int main(int argc, char ** argv) {
	int numpolygons ;
	int xincr ;
	int yincr ;
	int kx,ky,kw,kh ;
	int gap ;

	string filename ;
	ofstream myfile;

    cout << "number of polygons:" ;
	cin >> numpolygons ;
	cout << "x increment: " ;
	cin >> xincr ;
	cout << "y increment:" ;
	cin >> yincr ;
	cout << "gap verification:" ;
	cin >> gap ;
	cout << "key polygon (x,y,w,h)" ;
	cin >> kx >> ky >> kw >> kh ;
	cout << "Output file name:" ;
	// getline (cin, filename);
	cin >> filename ;
	myfile.open(filename.c_str());


	myfile << "1" << endl ;
	myfile << numpolygons+1 << " " << gap << endl ;
	myfile << "0 "<< kx << " " << ky << " " << kw << " " << kh << endl ;
	int x = 0 ;
	int y = 0 ;
	for (int i=1;i<= numpolygons; i++ ) {
		myfile << i << " " <<  x << " " << y << " " <<  "10 10 " << endl ;
		x = x + xincr ;
		y = y + yincr ;
	}
	myfile.close();

}
