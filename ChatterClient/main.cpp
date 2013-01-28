#include "ChatterClient.h"
#include <QtGui/QApplication>

int main( int argc, char *argv[] )
{
	QApplication app( argc, argv );
	ChatterClient chatter;
	chatter.show();
	return app.exec();
}
