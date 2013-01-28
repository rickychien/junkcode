#include "ChatterServer.h"
#include <QtCore/QCoreApplication>

int main( int argc, char *argv[] )
{
    QCoreApplication app( argc, argv );

    ChatterServer chatterServer;

    return app.exec();
}
