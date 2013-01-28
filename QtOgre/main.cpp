#include "qtogre.h"
#include <QtGui/QApplication>

int main(int argc, char *argv[]) 
{
	QApplication app(argc, argv);
	int appResult;

	QtOgre *ogreSceneWidget = new QtOgre();

	ogreSceneWidget->show();
	appResult = app.exec();

	delete ogreSceneWidget;
	return appResult;
}